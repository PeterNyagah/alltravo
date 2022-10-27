package trips.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MyTripsViewDetailsActivity extends AppCompatActivity {

    private Button btnPay, btnCancel;

    private ImageView img_back;
    private TextView txtFName, txtClientPhone, txtTimeStamp, txtAirlineDetail, flightNoDetail, txtFlightNumberDetail, originDetail, txtOriginDetail, destinationDetail, txtDestinationDetail, departureDateDetail, txtDepartureDateDetail, arrivalDateDetail, txtArrivalDateDetail, departureTimeDetail, txtDepartureTimeDetail, arrivalTimeDetail, txtArrivalTimeDetail, durationDetail, txtFlightDurationDetail, flightClassDetail, txtFlightClassDetail, travellerDetail, txtTravellerDetail, fareDetail, txtFareDetail, totalfareDetail, txtTotalFareDetail;

    private MyTextView_Roboto_Regular title;


    private static String url_booking = AppConstants.URL_MY_TRIPS;
    JSONArray jsonArray = null;
    private boolean test = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();


    private final String TAG_STATUS = "status";
    private String msg = "";


    private SweetAlertDialog pDialog;


    private User user = new User();
    private SessionManager sessionManager;
    private int id;
    private String date2, type, name, city, country, roomtype, persons, status, from, to, duration, date;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mytrips_view_details_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sessionManager = new SessionManager(MyTripsViewDetailsActivity.this);

        user = sessionManager.getUserDetails();

        initComponents();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getInt("ID");
        type = extras.getString("TYPE");
        name = extras.getString("NAME");
        city = extras.getString("CITY");
        country = extras.getString("COUNTRY");
        roomtype = extras.getString("ROOMTYPE");
        persons = extras.getString("PERSONS");
        status = extras.getString("STATUS");
        from = extras.getString("FROM");
        to = extras.getString("TO");
        duration = extras.getString("DURATION");
        date = extras.getString("DATE");
        date2 = extras.getString("DATE2");
        price = extras.getDouble("PRICE");

        txtClientPhone.setText(user.phone);
        txtFName.setText(user.fname + " " + user.lname);
        txtTimeStamp.setText(date);
        txtAirlineDetail.setText(name);
        txtFlightNumberDetail.setText(Integer.toString(id));
        txtTravellerDetail.setText(persons);
        txtFareDetail.setText(String.valueOf(price / Double.valueOf(persons)));
        txtTotalFareDetail.setText(String.valueOf(price));

        switch (type) {
            case "packageBookings":
                flightNoDetail.setText("Package No:");
                departureDateDetail.setText("Checkin Date:");
                txtDepartureDateDetail.setText(date2);

                txtFlightDurationDetail.setVisibility(View.GONE);
                durationDetail.setVisibility(View.GONE);
                txtFlightClassDetail.setVisibility(View.GONE);
                flightClassDetail.setVisibility(View.GONE);
                txtOriginDetail.setVisibility(View.GONE);
                originDetail.setVisibility(View.GONE);
                txtDestinationDetail.setVisibility(View.GONE);
                destinationDetail.setVisibility(View.GONE);
                break;
            case "flightBooking":
                txtOriginDetail.setText(from);
                txtDestinationDetail.setText(to);
                txtDepartureDateDetail.setText(date);
                txtFlightDurationDetail.setText(duration);
                txtFlightClassDetail.setText(roomtype);
                break;
            case "hotelBookings":
                flightNoDetail.setText("Hotel No:");
                departureDateDetail.setText("Checkin Date:");
                txtDepartureDateDetail.setText(date2);
                flightClassDetail.setText("Room Type:");
                txtFlightClassDetail.setText(roomtype);


                txtFlightDurationDetail.setVisibility(View.GONE);
                durationDetail.setVisibility(View.GONE);
                txtOriginDetail.setVisibility(View.GONE);
                originDetail.setVisibility(View.GONE);
                txtDestinationDetail.setVisibility(View.GONE);
                destinationDetail.setVisibility(View.GONE);
                break;
        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pmethodIntent = new Intent(MyTripsViewDetailsActivity.this, Payment_method.class);
                pmethodIntent.putExtra("ID", id);
                pmethodIntent.putExtra("TYPE", type);
                pmethodIntent.putExtra("PRICE", price);
                startActivity(pmethodIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MyTripsViewDetailsActivity.this)
                        .setTitle("Warning!")
                        .setMessage("Do you really want to Cancel this Booking? This cannot be undone!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new CancelBooking().execute(type,String.valueOf(id));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        if (status.equals("paid")||status.equals("canceled")) {
            btnPay.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    private void initComponents() {
        txtTimeStamp = findViewById(R.id.txtTimeStamp);
        txtClientPhone = findViewById(R.id.txtClientPhone);
        txtFName = findViewById(R.id.txtFName);
        txtAirlineDetail = findViewById(R.id.txtAirlineDetail);
        btnCancel = findViewById(R.id.btnCancel);
        flightNoDetail = findViewById(R.id.flightNoDetail);
        txtFlightNumberDetail = findViewById(R.id.txtFlightNumberDetail);
        originDetail = findViewById(R.id.originDetail);
        txtOriginDetail = findViewById(R.id.txtOriginDetail);
        destinationDetail = findViewById(R.id.destinationDetail);
        txtDestinationDetail = findViewById(R.id.txtDestinationDetail);
        departureDateDetail = findViewById(R.id.departureDateDetail);
        txtDepartureDateDetail = findViewById(R.id.txtDepartureDateDetail);
        arrivalDateDetail = findViewById(R.id.arrivalDateDetail);
        txtArrivalDateDetail = findViewById(R.id.txtArrivalDateDetail);
        departureTimeDetail = findViewById(R.id.departureTimeDetail);
        txtDepartureTimeDetail = findViewById(R.id.txtDepartureTimeDetail);
        arrivalTimeDetail = findViewById(R.id.arrivalTimeDetail);
        txtArrivalTimeDetail = findViewById(R.id.txtArrivalTimeDetail);
        durationDetail = findViewById(R.id.durationDetail);
        txtFlightDurationDetail = findViewById(R.id.txtFlightDurationDetail);
        flightClassDetail = findViewById(R.id.flightClassDetail);
        txtFlightClassDetail = findViewById(R.id.txtFlightClassDetail);
        travellerDetail = findViewById(R.id.travellerDetail);
        txtTravellerDetail = findViewById(R.id.txtTravellerDetail);
        fareDetail = findViewById(R.id.fareDetail);
        txtFareDetail = findViewById(R.id.txtFareDetail);
        totalfareDetail = findViewById(R.id.totalfareDetail);
        txtTotalFareDetail = findViewById(R.id.txtTotalFareDetail);

        btnPay = findViewById(R.id.btnPay);

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);

        title.setText("Booking Details");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTripsViewDetailsActivity.super.onBackPressed();
            }
        });
    }

    public class CancelBooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(MyTripsViewDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            params.put("action", "cancelBooking");
            params.put("type", args[0]);
            params.put("id", args[1]);
            // getting JSON string from URL

            online = DUtils.isOnline(MyTripsViewDetailsActivity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_booking, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }

                try {
                    test = json.getBoolean(TAG_STATUS);
                    msg = json.getString("msg");

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

            if (test) {
                pDialog.setTitleText("Success!")
                        .setContentText(msg)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms

                        pDialog.hide();
                        Intent pmethodIntent = new Intent(MyTripsViewDetailsActivity.this, MyTrips_List_Activity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(pmethodIntent);
                    }
                }, 2000);
            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(MyTripsViewDetailsActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
                } else {

                    pDialog.hide();
                    Toast.makeText(MyTripsViewDetailsActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }
}