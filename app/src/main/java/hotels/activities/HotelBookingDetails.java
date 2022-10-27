package hotels.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
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
import utils.customfonts.EditText_Roboto_Regular;
import utils.customfonts.MyTextView_Montserrat_Light;
import utils.customfonts.MyTextView_Roboto_Medium;
import utils.customfonts.MyTextView_Roboto_Regular;

public class HotelBookingDetails extends AppCompatActivity {

    private ImageView img_back;
    private MyTextView_Roboto_Regular title, btn_cancel;
    private EditText_Roboto_Regular no_of_persons, no_of_days;
    private TextView txt_price, txt_roomType, txt_hotelName, tprice;
    private MyTextView_Montserrat_Light txt_header;
    private String bookingfor;
    private MyTextView_Roboto_Medium btn_next;
    private CheckBox cbx_terms;
    private TextView checkBoxTCs;

    private String id, price, h_id, h_name, name;
    private Double tot;

    //    Check In Start
    DatePickerDialog picker;
    EditText edCheckIn;
    TextInputEditText edNoOfDays;
    TextView txtCheckOut;
    public int year1 = 0;
    public int month1 = 0;
    public int day1 = 0;
    public Calendar cldr;
//    Check In Stop

    private static String url_booking = AppConstants.URL_MY_TRIPS;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "", bookingid = "";


    private SweetAlertDialog pDialog;


    private int mYear, mMonth, mDay;

    private String date;
    private String checkOutDate;

    private String checkIn, checkOut;

    private User user = new User();
    private SessionManager sessionManager;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hotel_booking_details);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sessionManager = new SessionManager(HotelBookingDetails.this);

        user = sessionManager.getUserDetails();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("ID");
        price = extras.getString("PRICE");
        h_id = extras.getString("H_ID");
        bookingfor = extras.getString("BOOKING_FOR");
        h_name = extras.getString("H_NAME");
        name = extras.getString("NAME");

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);
        txt_header = findViewById(R.id.txt_header);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_next = findViewById(R.id.btn_next);


        cbx_terms = findViewById(R.id.cbx_terms);
        checkBoxTCs = findViewById(R.id.checkBoxTCs);
        txt_price = findViewById(R.id.txt_price);
        tprice = findViewById(R.id.tprice);
        txt_hotelName = findViewById(R.id.txt_hotelName);
        txt_roomType = findViewById(R.id.txt_roomType);
        no_of_persons = findViewById(R.id.no_of_persons);
//        no_of_days = findViewById(R.id.no_of_days);

        //Start Check In

        edCheckIn = (EditText) findViewById(R.id.edCheckIn);
        edNoOfDays = findViewById(R.id.edNoOfDays);
        txtCheckOut = findViewById(R.id.txtCheckOut);
        //Stop Check In


        no_of_persons.setText("1");
//        no_of_days.setText("1");
        tot = Double.valueOf(price);

        edCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cldr = Calendar.getInstance();
                final int[] day = {cldr.get(Calendar.DAY_OF_MONTH)};
                final int[] month = {cldr.get(Calendar.MONTH)};
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(HotelBookingDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                year1 = year;
                                month1 = monthOfYear;
                                day1 = dayOfMonth;

//                                edCheckIn.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                edCheckIn.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month[0], day[0]);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });

        edNoOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtCheckOut.setText(day1 + "/" + (month1 + 1) + "/" + year1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals(null) || s.toString().equals("")) {

                    edNoOfDays.setError("Enter No Of Days");

                } else {
                    int noofdays = Integer.valueOf(edNoOfDays.getText().toString());
                    int nopersons = Integer.valueOf(no_of_persons.getText().toString());
                    checkIn = edCheckIn.getText().toString();
                    checkOut = txtCheckOut.getText().toString();

                    tot = Double.valueOf(String.valueOf(noofdays)) * Double.valueOf(price) * Double.valueOf(nopersons);
                    tprice.setText("Total Cost: Ksh." + String.valueOf(tot));

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    int month = month1 + 1;
                    String selectedDate = year1 + "-" + month + "-" + day1;

                    try {
                        //Setting the date to the given date
                        c.setTime(sdf.parse(selectedDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Number of Days to add
                    c.add(Calendar.DAY_OF_MONTH, noofdays);
                    //Date after adding the days to the given date
                    checkOutDate = sdf.format(c.getTime());
                    //Displaying the new Date after addition of Days
//                txtCheckOut.setText(day1 + "/" + (month1 + 1) + "/" + year1);
                    txtCheckOut.setText("Check Out Date: " + checkOutDate);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no_of_persons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals(null) || charSequence.toString().equals("")) {

                } else {
                    int noofdays = Integer.valueOf(edNoOfDays.getText().toString());
                    tot = Double.valueOf(String.valueOf(charSequence)) * Double.valueOf(price) * Double.valueOf(noofdays);
                    tprice.setText("Total Cost: Ksh." + String.valueOf(tot));
                    checkIn = edCheckIn.getText().toString();
                    checkOut = txtCheckOut.getText().toString();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        no_of_days.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                tot = Double.valueOf(String.valueOf(charSequence)) * Double.valueOf(price);
//                tprice.setText("Total Cost: Ksh." + String.valueOf(tot));
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        txt_price.setText("Price: Ksh." + price);
        tprice.setText("Total Cost: Ksh." + price);
        txt_roomType.setText("Room Type:" + name);
        txt_hotelName.setText("Hotel Name:" + h_name);

        switch (bookingfor) {
            case "self":
                cbx_terms.setChecked(true);
//                edtxt_fname.setText(user.fname);
//                edtxt_lname.setText(user.lname);
//                edtxt_email.setText(user.email);
//                edtxt_phone_number.setText(user.phone);
                title.setText("Self Booking");
                txt_header.setText("Your Booking Details");
                break;
//            case "other":
//                title.setText("Other Person's Booking");
//                txt_header.setText("Other Person's Booking Details");
//                break;
            default:
                break;
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelBookingDetails.super.onBackPressed();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelBookingDetails.super.onBackPressed();
            }
        });

        date = dateBuilder();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbx_terms.isChecked()) {

                    new PostMyHotelBooking().execute();
                } else {
                    agree();
                }
            }
        });

        checkBoxTCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://admin.alltravo.com/ticketing/terms_and_conditions.php"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void agree() {
        Toast.makeText(this, "Agree To the Terms and Conditions", Toast.LENGTH_LONG).show();
    }


    public class PostMyHotelBooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(HotelBookingDetails.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

//            hello(checkIn);

        }


        /**
         *
         * getting All articles from url
         */
        protected String doInBackground(String... args) {


            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "appaddHotelBooking");
            params.put("hname", h_id);
            params.put("rtype", id);
            params.put("huser", String.valueOf(user.id));
            params.put("hnumber", no_of_persons.getText().toString());
//            params.put("hcheckin", String.valueOf(date));
            params.put("hcheckin", checkIn);
            params.put("hcheckout", checkOutDate);
            params.put("hnumberofdays", edNoOfDays.getText().toString());
            params.put("cost", String.valueOf(tot));


            // getting JSON string from URL
            online = DUtils.isOnline(HotelBookingDetails.this);
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
//            checkIn = edCheckIn.getText().toString();
            hello(checkIn);

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
                        Intent pmethodIntent = new Intent(HotelBookingDetails.this, Payment_method.class);
                        pmethodIntent.putExtra("TYPE", "hotelBookings");
                        pmethodIntent.putExtra("ID", Integer.valueOf(bookingid));
                        pmethodIntent.putExtra("PRICE", tot);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(pmethodIntent);
                    }
                }, 2000);
            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(HotelBookingDetails.this, msg.toString(), Toast.LENGTH_LONG).show();
                } else {

                    pDialog.hide();
                    Toast.makeText(HotelBookingDetails.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }

    private void hello(String checkIn) {

        Toast.makeText(this, "Check In " + checkIn, Toast.LENGTH_LONG).show();

    }

    public static String dateBuilder() {
        /*DateFormat format = new SimpleDateFormat("dd/MM/yyyy ");
        Date date = new Date();
        String dot = format.format(date);
        return dot;*/

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String dot = format.format(date);
        return dot;


    }


}
