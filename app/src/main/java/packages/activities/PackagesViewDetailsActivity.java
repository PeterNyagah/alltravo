package packages.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cars.activities.CarDetailActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import flights.activities.FlightViewDetailsActivity;
import hotels.adapters.ViewPagerAdapter;
import payments.activities.Payment_method;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;
import utils.customfonts.MyTextView_Varela;

import static user.SignActivity.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class PackagesViewDetailsActivity extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    private Button btnPay;
    private ImageView img_back;
    private MyTextView_Roboto_Regular title;
    private MyTextView_Varela txt_name, txt_abouthotel;
    private TextView txt_inclide;
    private boolean online = false;
    private boolean status = false;

    private final String TAG_STATUS = "status";

    private String msg = "",bookingid="";
    private User user = new User();
    private SessionManager sessionManager;

    private String id ,name, validity, price, image1, image2, image3, image4, description, pkginclusion;

    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewpager;

    private JSONParser jParser = new JSONParser();
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    private static String url_booking = AppConstants.URL_MY_TRIPS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.packages_view_details_activity_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sessionManager = new SessionManager(PackagesViewDetailsActivity.this);

        user = sessionManager.getUserDetails();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("ID");
        name = extras.getString("NAME");
        validity = extras.getString("VALIDITY");
        price = extras.getString("PRICE");
        image1 = extras.getString("IMAGE1");
        image2 = extras.getString("IMAGE2");
        image3 = extras.getString("IMAGE3");
        image4 = extras.getString("IMAGE4");
        description = extras.getString("DESCRIPTION");
        pkginclusion = extras.getString("PKGINCLUSION");

        viewpager = (ViewPager) findViewById(R.id.viewpager1);
        // CircleIndicator indicator =(CircleIndicator) findViewById(R.id.indicator);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), image1, image2, image3, image4);
        viewpager.setAdapter(pagerAdapter);


        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int tab = viewpager.getCurrentItem();
                if (tab == 3) {
                    tab = 0;
                    viewpager.setCurrentItem(tab);
                } else {
                    tab++;
                    viewpager.setCurrentItem(tab);
                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        btnPay = findViewById(R.id.btnPay);
        img_back = findViewById(R.id.img_back);
        txt_inclide = findViewById(R.id.txt_inclide);
        title = findViewById(R.id.title);
        txt_abouthotel = findViewById(R.id.txt_abouthotel);
        txt_name = findViewById(R.id.txt_name);

        txt_name.setText(name);
        txt_abouthotel.setText(description);
        txt_inclide.setText(pkginclusion);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackagesViewDetailsActivity.super.onBackPressed();
            }
        });
        title.setText("Package details");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = String.valueOf(user.id);
                new PostMyPackageBooking().execute(user_id);
            }
        });


    }


    public class PostMyPackageBooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(PackagesViewDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            params.put("action", "addPackageBook");
            params.put("pname", String.valueOf(name));
            params.put("pkg_id", id);
            params.put("puser", args[0]);
            params.put("noPersons", String.valueOf(1));
            params.put("pchkdate", String.valueOf(new Date()));
            params.put("totalCost", String.valueOf(price));
            params.put("android", "1");
            // getting JSON string from URL

            online = DUtils.isOnline(PackagesViewDetailsActivity.this);
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
                        Intent pmethodIntent = new Intent(PackagesViewDetailsActivity.this, Payment_method.class);
                        pmethodIntent.putExtra("TYPE", "packageBooking");
                        pmethodIntent.putExtra("ID", Integer.valueOf(bookingid));
                        pmethodIntent.putExtra("PRICE", Double.valueOf(price));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(pmethodIntent);
                    }
                }, 2000);
            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(String.valueOf(user.id)+"-"+msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(PackagesViewDetailsActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
                } else {

                    pDialog.hide();
                    Toast.makeText(PackagesViewDetailsActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }
}
