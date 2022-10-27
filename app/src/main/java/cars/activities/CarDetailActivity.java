package cars.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import packages.activities.PackagesViewDetailsActivity;
import payments.activities.Payment_method;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.EditText_Roboto_Regular;
import utils.customfonts.MyTextView_Roboto_Regular;

public class CarDetailActivity extends AppCompatActivity {

    String id, name, plate, capacity, fee, city, country, image, description;
    Toolbar toolbar;
    ImageView carimage;
    private EditText_Roboto_Regular no_of_days;
    TextView txtcarname, txtcarplate, txtcarcapacity, txtcarcity, txtcarfee, txtcarcountry, total_price, txtTCs;
    private Double total_cost;
    private MyTextView_Roboto_Regular btn_car_view_pay;
    CheckBox cbx_terms,checkBoxTCs;
    private String msg = "", bookingid = "";

    private User user = new User();
    private SessionManager sessionManager;

    private SweetAlertDialog pDialog;
    private boolean online = false;
    private boolean status = false;
    private JSONParser jParser = new JSONParser();
    private static String url_booking = AppConstants.URL_MY_TRIPS;
    private final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        btn_car_view_pay = findViewById(R.id.btn_car_view_pay);

        sessionManager = new SessionManager(CarDetailActivity.this);

        user = sessionManager.getUserDetails();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("ID");
        name = extras.getString("NAME");
        plate = extras.getString("PLATE");
        fee = extras.getString("FEE");
        capacity = extras.getString("CAPACITY");
        city = extras.getString("CITY");
        image = extras.getString("IMAGE");
        description = extras.getString("DESCRIPTION");
        country = extras.getString("COUNTRY");

        txtcarname = findViewById(R.id.txtcarname);
        txtcarplate = findViewById(R.id.txtcarplate);
        txtcarcapacity = findViewById(R.id.txtcarcapacity);
        txtcarcity = findViewById(R.id.txtcarcity);
        txtcarfee = findViewById(R.id.txtcarfee);
        txtcarcountry = findViewById(R.id.txtcarcountry);
        no_of_days = findViewById(R.id.no_of_days);
        total_price = findViewById(R.id.total_price);
        carimage = findViewById(R.id.carimage);
        cbx_terms = findViewById(R.id.cbx_terms);
        checkBoxTCs = findViewById(R.id.checkBoxTCs);

        txtcarname.setText(name);
        txtcarplate.setText(plate);
        txtcarcapacity.setText(capacity + " Adults");
        txtcarcity.setText(city);
        txtcarfee.setText("Ksh " + fee);
        txtcarcountry.setText(country);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.imgloading)
                .into(carimage);

        no_of_days.setText("1");

        total_cost = Double.valueOf(fee);

        no_of_days.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(null) || charSequence.toString().equals("")) {

                } else {
                    total_cost = Double.valueOf(String.valueOf(charSequence)) * Double.valueOf(fee);
                    total_price.setText("Total Cost: Ksh." + String.valueOf(total_cost));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        total_price.setText("Total Cost: Ksh" + fee);

        checkBoxTCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://admin.alltravo.com/ticketing/terms_and_conditions.php"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        btn_car_view_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbx_terms.isChecked()){
                String user_id = String.valueOf(user.id);
                new PostMyCarBooking().execute(user_id);

                }else {

                    agree();
                }
            }
        });




        //   Toast.makeText(this, "Name: " + name + "Fee: " + fee, Toast.LENGTH_LONG).show();
    }

    private void agree() {
        Toast.makeText(this,"Agree to The Terms & Condtions", Toast.LENGTH_LONG).show();

    }

    public class PostMyCarBooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(CarDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            params.put("action", "appAddCarBook");
            params.put("car_id", id);
            params.put("car_cat_id", String.valueOf(1));
            params.put("car_name", String.valueOf(name));
            params.put("client_id", String.valueOf(user.id));
            params.put("no_of_days", no_of_days.getText().toString());
            params.put("check_in_date", String.valueOf(new Date()));
            params.put("total_cost", String.valueOf(total_cost));
            params.put("android", "1");


            /*params.put("action", "appAddCarBook");
            params.put("car_id", id);
            params.put("car_cat_id", String.valueOf(1));
            params.put("car_name", String.valueOf(name));
            params.put("client_id", String.valueOf(user.id));
            params.put("no_of_days", "2");
            params.put("check_in_date", "01012020");
            params.put("total_cost", "1");
            params.put("android", "1");*/

            // getting JSON string from URL


            online = DUtils.isOnline(CarDetailActivity.this);
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
                        Intent pmethodIntent = new Intent(CarDetailActivity.this, Payment_method.class);
                        pmethodIntent.putExtra("TYPE", "carBooking");
                        pmethodIntent.putExtra("ID", Integer.valueOf(bookingid));
                        pmethodIntent.putExtra("PRICE", Double.valueOf(total_cost));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(pmethodIntent);
                    }
                }, 2000);
            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(String.valueOf(user.id) + "-" + msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(CarDetailActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
                } else {

                    pDialog.hide();
                    Toast.makeText(CarDetailActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }
}
