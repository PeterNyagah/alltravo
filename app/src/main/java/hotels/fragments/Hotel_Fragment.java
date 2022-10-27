package hotels.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import hotels.adapters.HotelAdapter;

import e.wolfsoft1.liberty_ui_kit.R;
import hotels.models.HotelModel;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;

public class  Hotel_Fragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    private static String url_hotels = AppConstants.URL_HOTELS;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";


    private User user = new User();
    private SessionManager sessionManager;

    private static ProgressDialog progressDialog;

    private int counter = 0;

    private HotelAdapter homepageAdapter;
    private RecyclerView recyclerview;
    private ArrayList<HotelModel> hotelModelArrayList;


    private Button btn_filter;

    private Dialog slideDialog;

    private ImageView img_back;
    private MyTextView_Roboto_Regular title;
    private TextView no_data;


    boolean check_ScrollingUp = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String name="",location="",country="";

    @Override
    public void onRefresh(){
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }
    public void refreshList(){

        new ListHotels().execute();
        swipeRefreshLayout.setRefreshing(false);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nyrecycler, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);


        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        img_back = view.findViewById(R.id.img_back);
        no_data = view.findViewById(R.id.no_data);
        title = view.findViewById(R.id.title);

        recyclerview = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        hotelModelArrayList = new ArrayList<>();

        title.setText("Hotels & Apartments");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        initComponents(view);

        new ListHotels().execute();

        return view;
    }

    private void initComponents(View view) {
        btn_filter = view.findViewById(R.id.btn_filter);


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                slideDialog = new Dialog(getActivity(), R.style.CustomDialogAnimation1);
                Objects.requireNonNull(slideDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Setting dialogview
                Window window = slideDialog.getWindow();
                window.setGravity(Gravity.TOP);


                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                slideDialog.setContentView(R.layout.layout_searchitem);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                slideDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
                layoutParams.copyFrom(slideDialog.getWindow().getAttributes());

                //int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.480);

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = height;
                layoutParams.gravity = Gravity.TOP;

                slideDialog.getWindow().setAttributes(layoutParams);
                slideDialog.setCancelable(true);
                slideDialog.setCanceledOnTouchOutside(true);
                slideDialog.show();

                final EditText edtxt_search = slideDialog.findViewById(R.id.edtxt_search);
                final EditText edtxt_location = slideDialog.findViewById(R.id.edtxt_location);
                final EditText edtxt_country = slideDialog.findViewById(R.id.edtxt_country);

                edtxt_search.setText(name);
                edtxt_location.setText(location);
                edtxt_country.setText(country);

                final MyTextView_Roboto_Regular btn_clear = slideDialog.findViewById(R.id.btn_clear);
                final MyTextView_Roboto_Regular btn_done = slideDialog.findViewById(R.id.btn_done);

                ImageView img_clear = slideDialog.findViewById(R.id.img_clear);
                ImageView img_clear2 = slideDialog.findViewById(R.id.img_clear2);
                ImageView img_clear3 = slideDialog.findViewById(R.id.img_clear3);
                img_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtxt_search.setText("");
                        name = "";
                    }
                });img_clear2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            edtxt_location.setText("");
                            location = "";
                    }
                });img_clear3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtxt_country.setText("");
                        country = "";
                    }
                });


                edtxt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(String.valueOf(charSequence).isEmpty())
                            return;
                        // filter recycler view when query submitted
                        name = charSequence.toString();
                        homepageAdapter.getFilter().filter(name+"%"+location+"%"+country);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                edtxt_location.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (String.valueOf(charSequence).isEmpty())
                            return;
                        // filter recycler view when query submitted
                        location = charSequence.toString();
                        homepageAdapter.getFilter().filter(name+"%"+location+"%"+country);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                edtxt_country.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (String.valueOf(charSequence).isEmpty())
                            return;
                        // filter recycler view when query submitted
                        country = charSequence.toString();
                        homepageAdapter.getFilter().filter(name+" % "+location+"% "+country);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homepageAdapter.getFilter().filter("");
                        slideDialog.hide();
                        name = "";
                        location = "";
                        country = "";
                    }
                });

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slideDialog.hide();
                    }
                });
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    if(check_ScrollingUp)
                    {

                        btn_filter.startAnimation(AnimationUtils
                                .loadAnimation(getActivity(),R.anim.trans_upwards));
                        check_ScrollingUp = true;
                    }

                } else {
                    // User scrolls down
                    if(!check_ScrollingUp )
                    {

                        btn_filter.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.trans_downwards));
                        check_ScrollingUp = false;

                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
    }

    public class ListHotels extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hotelModelArrayList.clear();
            progressDialog.setMessage("Please Wait...");
            showDialog();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "listHotels");
            // getting JSON string from URL

            online = DUtils.isOnline(getActivity());
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_hotels, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }
                if (json != null)
                    if (json.length() > 0) {
                        status = true;

                        try {
                            jsonArray = json.getJSONArray("hotels");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                counter = counter + 1;
                                JSONObject c = jsonArray.getJSONObject(i);

                                HotelModel view1 = new HotelModel(
                                        "" + c.getString("id")
                                        , "" + c.getString("name")
                                        , "" + c.getString("country")
                                        , "" + c.getString("owner")
                                        , "https://admin.alltravo.com/ticketing/controls/hotels/" + c.getString("image")
                                        , "" + c.getString("description")
                                        , "" + c.getString("city"));

                                //  Toast.makeText(getContext(),String.valueOf(view1),Toast.LENGTH_LONG).show();

                                hotelModelArrayList.add(view1);
                            }

                        } catch (JSONException e) {
                            Log.d("ERROR LOG", "ueufhuwew");
                        }

                    }

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all items


            progressDialog.dismiss();

            if (counter > 0) {
                homepageAdapter = new HotelAdapter(getActivity(), hotelModelArrayList);
                recyclerview.setAdapter(homepageAdapter);
            } else {
                if (online) {
                    no_data.setText("Sorry, we do not have Hotels at the moment!");
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Sorry, we do not have Hotels at the moment!.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }


    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();

        }

    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
