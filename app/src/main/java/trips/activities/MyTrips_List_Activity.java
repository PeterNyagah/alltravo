package trips.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import e.wolfsoft1.liberty_ui_kit.R;
import trips.adapters.MyTripsListAdapters;
import trips.models.MyTripsClass;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;

public class MyTrips_List_Activity extends AppCompatActivity {

    private MyTripsListAdapters myTripsListAdapters;
    private RecyclerView rv;
    private ArrayList<MyTripsClass> myTripsClassArrayList;

    private ImageView img_back;
    private MyTextView_Roboto_Regular title;
    TextView no_data;


    private static String url_mytrips = AppConstants.URL_MY_TRIPS;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mytrips_list_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(MyTrips_List_Activity.this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(MyTrips_List_Activity.this);

        user = sessionManager.getUserDetails();

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);

        no_data = findViewById(R.id.no_data);

        title.setText("My Bookings");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTrips_List_Activity.super.onBackPressed();
            }
        });

        myTripsClassArrayList = new ArrayList<>();

        new ListMyTrips().execute();

    }

    public class ListMyTrips extends AsyncTask<String, String, String> {

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
            params.put("action", "listMyBookings");
            params.put("userid", String.valueOf(user.id));
            // getting JSON string from URL

            online = DUtils.isOnline(MyTrips_List_Activity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_mytrips, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }
                if (json != null)
                    if (json.length() > 0) {
                        status = true;

                        try {
                            jsonArray = json.getJSONArray("bookings");
                            Log.d("ERROR task Details", jsonArray.toString());
                            NumberFormat nf = NumberFormat.getInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                counter = counter + 1;
                                JSONObject c = jsonArray.getJSONObject(i);
                                MyTripsClass myTripsClass = new MyTripsClass();

                                myTripsClass.setText_airline(c.getString("description"));
                                myTripsClass.setText_timeschedule(c.getString("date"));
                                myTripsClass.setStatus(c.getString("status"));
                                myTripsClass.setType(c.getString("type"));

                                myTripsClass.setName(c.getString("name"));
                                myTripsClass.setCity(c.getString("city"));
                                myTripsClass.setCountry(c.getString("country"));
                                myTripsClass.setRoomtype(c.getString("roomtype"));
                                myTripsClass.setPersons(c.getString("persons"));
                                myTripsClass.setFrom(c.getString("from"));
                                myTripsClass.setTo(c.getString("to"));
                                myTripsClass.setDuration(c.getString("duration"));
                                myTripsClass.setDate2(c.getString("date2"));

                                myTripsClass.setId(Integer.valueOf(c.getString("id")));
                                myTripsClass.setPrice(Double.valueOf(c.getString("cost")));


                                myTripsClassArrayList.add(myTripsClass);
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

            hideDialog();

            if (counter > 0) {
                rv = (RecyclerView) findViewById(R.id.rv);

                myTripsListAdapters = new MyTripsListAdapters(MyTrips_List_Activity.this, myTripsClassArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyTrips_List_Activity.this);
                rv.setLayoutManager(mLayoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(myTripsListAdapters);

            } else {
                if (online) {
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(MyTrips_List_Activity.this, "Sorry, we do not have flights of specified filters!.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyTrips_List_Activity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
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
