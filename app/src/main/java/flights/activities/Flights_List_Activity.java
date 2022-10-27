package flights.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import flights.adapters.ListAdapters;
import flights.models.PojoClass;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.customfonts.MyTextView_Roboto_Regular;

public class Flights_List_Activity extends AppCompatActivity {

    private ListAdapters listAdapter;
    private RecyclerView rv;
    private ArrayList<PojoClass> pojoClassArrayList;

    private String from = "", to = "", level = "", passangers = "", departure_date = "", return_date = "";
    private boolean way = false;

    private static String url_flights = AppConstants.URL_FLIGHTS;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();

    private int counter=0;

    private final String TAG_STATUS = "status";
    private String msg = "";


    private static ProgressDialog progressDialog;

    TextView txt_from, txt_to,no_data;
    private ImageView img_back, img_oneway, img_twoway;
    private MyTextView_Roboto_Regular title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.flights_list_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(Flights_List_Activity.this);
        progressDialog.setCancelable(false);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        from = extras.getString("FROM");
        to = extras.getString("TO");
        way = extras.getBoolean("WAY");
        level = extras.getString("CLASS");
        passangers = extras.getString("PASSANGERS");
        departure_date = extras.getString("DEPARTURE_DATE");
        return_date = extras.getString("RETURN_DATE");


        title = findViewById(R.id.title);
        img_back = findViewById(R.id.img_back);
        img_twoway = findViewById(R.id.img_twoway);
        img_oneway = findViewById(R.id.img_oneway);
        txt_to = findViewById(R.id.txt_to);
        txt_from = findViewById(R.id.txt_from);
        no_data = findViewById(R.id.no_data);


        txt_to.setText(to);
        txt_from.setText(from);
        title.setText("Available Flights");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flights_List_Activity.super.onBackPressed();
            }
        });

        if (way) {
            img_oneway.setVisibility(View.VISIBLE);
            img_twoway.setVisibility(View.GONE);
        } else {

            img_twoway.setVisibility(View.VISIBLE);
            img_oneway.setVisibility(View.GONE);
        }

        pojoClassArrayList = new ArrayList<>();


        new ListFlights().execute(from, to);


    }

    public class ListFlights extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Getting Flights...");
            showDialog();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "list");
            params.put("from", args[0]);
            params.put("to", args[1]);
            // getting JSON string from URL

            online = DUtils.isOnline(Flights_List_Activity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_flights, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }
                if (json != null)
                    if (json.length() > 0) {
                        status = true;

                        try {
                            jsonArray = json.getJSONArray("flights");
                            Log.d("ERROR task Details", jsonArray.toString());
                            NumberFormat nf = NumberFormat.getInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                counter=counter+1;
                                JSONObject c = jsonArray.getJSONObject(i);
                                PojoClass pojoClass = new PojoClass();
                                pojoClass.setId(c.getString("id"));
                                pojoClass.setText_airline(c.getString("name"));
                                pojoClass.setText_timeschedule(c.getString("duration"));
                                pojoClass.setStops(c.getString("stops"));
                                pojoClass.setText_time(c.getString("price"));
                                pojoClass.setLogo(R.drawable.flight);
                                pojoClassArrayList.add(pojoClass);
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

            if (counter>0) {
                PojoClass pojClass = new PojoClass();
                pojClass.setFrom(from);
                pojClass.setTo(to);
                pojClass.setWay(way);
                pojClass.setLevel(level);
                pojClass.setPassangers(passangers);
                pojClass.setDeparture_date(departure_date);
                pojClass.setReturn_date(return_date);

                rv = findViewById(R.id.rv);

                listAdapter = new ListAdapters(Flights_List_Activity.this, pojoClassArrayList, pojClass);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Flights_List_Activity.this);
                rv.setLayoutManager(mLayoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(listAdapter);

            } else {
                if (online) {
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(Flights_List_Activity.this, "Sorry, we do not have flights of specified filters!.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Flights_List_Activity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
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
