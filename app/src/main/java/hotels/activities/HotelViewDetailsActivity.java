package hotels.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import e.wolfsoft1.liberty_ui_kit.R;
import hotels.adapters.HotelRoomAdapter;
import hotels.adapters.ViewPagerAdapter;
import hotels.models.HotelRoomModel;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;
import utils.customfonts.MyTextView_Varela;

public class HotelViewDetailsActivity extends AppCompatActivity {

    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewpager;
    ImageView img_cover;
    MyTextView_Varela txt_abouthotel;

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

    private HotelRoomAdapter hotelRoomAdapter;
    private RecyclerView recyclerview;
    private ArrayList<HotelRoomModel> hotelRoomModelArrayList;

    private ImageView img_back;
    private MyTextView_Roboto_Regular title,txt_hotelName;

    private String id,name,country,owner,image,description,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hotel_view_details_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sessionManager = new SessionManager(HotelViewDetailsActivity.this);

        user = sessionManager.getUserDetails();


        progressDialog = new ProgressDialog(HotelViewDetailsActivity.this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("ID");
        name = extras.getString("NAME");
        country = extras.getString("COUNTRY");
        owner = extras.getString("OWNER");
        image = extras.getString("IMAGE");
        description = extras.getString("DESCRIPTION");
        city = extras.getString("CITY");

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);
        title.setText("Hotel/Apartment Details");
        txt_hotelName = findViewById(R.id.txt_hotelName);
        txt_abouthotel = findViewById(R.id.txt_abouthotel);

        txt_abouthotel.setText(description);
        txt_hotelName.setText(name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelViewDetailsActivity.super.onBackPressed();
            }
        });
        img_cover = findViewById(R.id.img_cover);
        Picasso.with(HotelViewDetailsActivity.this).load(image).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_cover, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });

        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( HotelViewDetailsActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        hotelRoomModelArrayList = new ArrayList<>();

        new ListHotelooms().execute();
    }

    public class ListHotelooms extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Please Wait...");
            showDialog();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "listHotelRooms");
            params.put("h_id", id);
            // getting JSON string from URL

            online = DUtils.isOnline(HotelViewDetailsActivity.this);
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
                            jsonArray = json.getJSONArray("hotelRooms");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                counter = counter + 1;
                                JSONObject c = jsonArray.getJSONObject(i);

                                HotelRoomModel view1 = new HotelRoomModel(
                                        ""+c.getString("id")
                                        ,""+c.getString("name")
                                        ,""+c.getString("price")
                                        ,"https://admin.alltravo.com/ticketing/controls/hotels/"+c.getString("image")
                                        ,id,name
                                );
                                hotelRoomModelArrayList.add(view1);
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
                hotelRoomAdapter = new HotelRoomAdapter(HotelViewDetailsActivity.this, hotelRoomModelArrayList);
                recyclerview.setAdapter(hotelRoomAdapter);
            } else {
                if (online) {
//                    no_data.setText("Sorry, we do not have Hotels at the moment!");
//                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(HotelViewDetailsActivity.this, "Sorry, we do not have Hotel Rooms at the moment!.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HotelViewDetailsActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
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
