package flights.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import payments.activities.Payment_method;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;

public class FlightViewDetailsActivity extends AppCompatActivity {

    private Button btnPay;


    private User user = new User();
    private SessionManager sessionManager;

    private TextView txtFName, txtClientPhone, txtTimeStamp, txtAirlineDetail,
            txtFlightNumberDetail, txtOriginDetail, txtDestinationDetail, txtDepartureDateDetail, txtArrivalDateDetail,
            txtDepartureTimeDetail, txtArrivalTimeDetail, txtFlightDurationDetail, txtFlightClassDetail, txtTravellerDetail, txtFareDetail, txtTotalFareDetail;
    private ImageView img_back;
    private MyTextView_Roboto_Regular title;

    private static String url_booking = AppConstants.URL_MY_TRIPS;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";


    private SweetAlertDialog pDialog;

    private String from = "", to = "", level = "", passangers = "",
            departure_date = "", return_date = "", id = "", name = "", price = "", duration = "",way_desc="one way",bookingid="";
    private boolean way = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.flights_view_details_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sessionManager = new SessionManager(FlightViewDetailsActivity.this);

        user = sessionManager.getUserDetails();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("ID");
        name = extras.getString("NAME");
        price = extras.getString("PRICE");
        duration = extras.getString("DURATION");
        from = extras.getString("FROM");
        to = extras.getString("TO");
        way = extras.getBoolean("WAY");
        level = extras.getString("CLASS");
        passangers = extras.getString("PASSANGERS");
        departure_date = extras.getString("DEPARTURE_DATE");
        return_date = extras.getString("RETURN_DATE");

        price = String.valueOf(Double.valueOf(price)*Double.valueOf(passangers));

        if(!way)
            way_desc="two way";

        initComponents();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostMyFlightBooking().execute();
            }
        });
    }

    private void initComponents() {
        txtFName = findViewById(R.id.txtFName);
        txtClientPhone = findViewById(R.id.txtClientPhone);
        txtTimeStamp = findViewById(R.id.txtTimeStamp);

        txtAirlineDetail = findViewById(R.id.txtAirlineDetail);
        txtFlightNumberDetail = findViewById(R.id.txtFlightNumberDetail);
        txtOriginDetail = findViewById(R.id.txtOriginDetail);
        txtDestinationDetail = findViewById(R.id.txtDestinationDetail);
        txtDepartureDateDetail = findViewById(R.id.txtDepartureDateDetail);
        txtArrivalDateDetail = findViewById(R.id.txtArrivalDateDetail);
        txtDepartureTimeDetail = findViewById(R.id.txtDepartureTimeDetail);
        txtArrivalTimeDetail = findViewById(R.id.txtArrivalTimeDetail);
        txtFlightDurationDetail = findViewById(R.id.txtFlightDurationDetail);
        txtFlightClassDetail = findViewById(R.id.txtFlightClassDetail);
        txtTravellerDetail = findViewById(R.id.txtTravellerDetail);
        txtFareDetail = findViewById(R.id.txtFareDetail);
        txtTotalFareDetail = findViewById(R.id.txtTotalFareDetail);

        btnPay = findViewById(R.id.btnPay);

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);

        txtAirlineDetail.setText(name+" "+way_desc);
        txtFlightNumberDetail.setText(id);
        txtOriginDetail.setText(from);
        txtDestinationDetail.setText(to);
        txtDepartureDateDetail.setText(departure_date);
        txtArrivalDateDetail.setText("-");
        txtDepartureTimeDetail.setText("00:00");
        txtArrivalTimeDetail.setText("00:00");
        txtFlightDurationDetail.setText(duration);
        txtFlightClassDetail.setText(level);
        txtTravellerDetail.setText(passangers);
        txtFareDetail.setText("Ksh. "+price);
        txtTotalFareDetail.setText("Ksh. "+price);
        txtFName.setText(user.fname + " " + user.lname);
        txtClientPhone.setText(user.phone);
        txtTimeStamp.setText(dateBuilder() + " " + timeBuilder());
        title.setText("Booking Details");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlightViewDetailsActivity.super.onBackPressed();
            }
        });
    }

    public class PostMyFlightBooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(FlightViewDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "addFlightBook");
            params.put("fname", String.valueOf(id));
            params.put("NoPass", passangers);
            params.put("android", passangers);
            params.put("fclass", String.valueOf(level));
            params.put("fuser", String.valueOf(user.id));
            params.put("totalCost", String.valueOf(price));
            // getting JSON string from URL

            online = DUtils.isOnline(FlightViewDetailsActivity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_booking, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }

                try {
                    status = json.getBoolean(TAG_STATUS);
                    msg = json.getString("msg");
                    bookingid = json.getString("bookingid");

                } catch (JSONException e) {
                    Log.d("ERROR LOG", "ueufhuwew");
                }

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all items

            if (status) {
                pDialog.setTitleText("Success!")
                        .setContentText(msg)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms

                        pDialog.hide();
                        Intent pmethodIntent = new Intent(FlightViewDetailsActivity.this, Payment_method.class);
                        pmethodIntent.putExtra("TYPE","flightBooking");
                        pmethodIntent.putExtra("ID",Integer.valueOf(bookingid));
                        pmethodIntent.putExtra("PRICE",Double.valueOf(price));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(pmethodIntent);
                    }
                }, 2000);
            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(FlightViewDetailsActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
                } else {

                    pDialog.hide();
                    Toast.makeText(FlightViewDetailsActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }

    public static String dateBuilder() {
        /*DateFormat format = new SimpleDateFormat("dd/MM/yyyy ");
        Date date = new Date();
        String dot = format.format(date);
        return dot;*/

        DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        String dot = format.format(date);
        return dot;


    }

    public static String timeBuilder() {

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}