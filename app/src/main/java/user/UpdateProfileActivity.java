package user;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;

import static user.LoginActivity.isValidEmail;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextView signup, login;
    private EditText edtxt_email, edtxt_password, edtxt_confrirmPass, edtxt_fname, edtxt_lname, edtxt_phone;
    private LinearLayout lyout_confpass, lyout_pass;
    private MyTextView_Roboto_Regular txt_title;
    CheckBox checkBoxTCs;
    TextView readTCs;
    private String email, password, fname, lname, phone = "", username = "", country = "Kenya", city = "Nairobi";
    private static String url_signup = AppConstants.URL_USER_LOGIN_SIGNUP;
    private JSONArray tasksPerson = null;
    private boolean status = false;
    private boolean online = false;


    private User user = new User();
    private SessionManager sessionManager;

    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";


    private SweetAlertDialog pDialog;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);

        sessionManager = new SessionManager(UpdateProfileActivity.this);

        user = sessionManager.getUserDetails();
        ccp = findViewById(R.id.ccp);

        initComponents();
        signup.setText("Update");
        txt_title.setText("UPDATE PROFILE");
        edtxt_lname.setText(user.lname);
        edtxt_phone.setText(user.phone);
        edtxt_fname.setText(user.fname);
        edtxt_email.setText(user.email);
        edtxt_email.setFocusable(false);
        lyout_confpass.setVisibility(View.GONE);
        lyout_pass.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        checkBoxTCs.setVisibility(View.GONE);
        readTCs.setVisibility(View.GONE);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                phone = (ccp.getSelectedCountryCodeWithPlus() + edtxt_phone.getText().toString());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsVerified()) {
                    email = edtxt_email.getText().toString();
                    fname = edtxt_fname.getText().toString();
                    lname = edtxt_lname.getText().toString();
                    phone = ccp.getSelectedCountryCodeWithPlus() + edtxt_phone.getText().toString();
                    username = edtxt_fname.getText().toString();

//                    Toast.makeText(UpdateProfileActivity.this, phone, Toast.LENGTH_SHORT).show();

                    new UpdateUser().execute();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfileActivity.super.onBackPressed();
            }
        });
    }

    private void initComponents() {
        txt_title = findViewById(R.id.txt_title);
        lyout_confpass = findViewById(R.id.lyout_confpass);
        lyout_pass = findViewById(R.id.lyout_pass);

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        edtxt_email = findViewById(R.id.edtxt_email);
        edtxt_password = findViewById(R.id.edtxt_password);
        edtxt_confrirmPass = findViewById(R.id.edtxt_confrirmPass);
        edtxt_fname = findViewById(R.id.edtxt_fname);
        edtxt_lname = findViewById(R.id.edtxt_lname);
        edtxt_phone = findViewById(R.id.edtxt_phone);
        checkBoxTCs = findViewById(R.id.checkBoxTCs);
        readTCs = findViewById(R.id.readTCs);
    }

    public boolean fieldsVerified() {
        boolean verified = false;
        if (!isValidEmail(edtxt_email.getText().toString()) ||
                TextUtils.isEmpty(edtxt_fname.getText().toString()) ||
                TextUtils.isEmpty(edtxt_lname.getText().toString()) ||
                TextUtils.isEmpty(edtxt_phone.getText().toString()) ||
                edtxt_phone.getText().toString().length() < 9) {
            if (TextUtils.isEmpty(edtxt_phone.getText().toString())) {
                edtxt_phone.setError("Phone number is Required!");
                edtxt_phone.requestFocus();
            } else if (edtxt_phone.getText().toString().length() < 9) {
                edtxt_phone.setError("Phone number should be at least 10 characters!");
                edtxt_phone.requestFocus();
            }
            if (TextUtils.isEmpty(edtxt_fname.getText().toString())) {
                edtxt_fname.setError("First name is Required!");
                edtxt_fname.requestFocus();
            }
            if (TextUtils.isEmpty(edtxt_lname.getText().toString())) {
                edtxt_lname.setError("Last name is Required!!");
                edtxt_lname.requestFocus();
            }

        } else
            verified = true;


        return verified;
    }

    public class UpdateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(UpdateProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Updating Your Details...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "updateUser");
            params.put("email", email);
            params.put("fname", fname);
            params.put("lname", lname);
            params.put("phone", phone);
            params.put("userid", String.valueOf(user.id));
            params.put("android", username);
            params.put("country", "Kenya");
            params.put("city", "Nairobi");
            params.put("type", "user");
            // getting JSON string from URL

            online = DUtils.isOnline(UpdateProfileActivity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_signup, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }


                try {

                    status = json.getBoolean(TAG_STATUS);
                    msg = json.getString("msg");
                } catch (Exception e) {
                    Log.d("ERROR LOG", "pieError");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }

                if (status) {
                    user.fname = fname;
                    user.lname = lname;
                    user.phone = phone;
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

                        SessionManager sessionManager = new SessionManager(UpdateProfileActivity.this);
                        sessionManager.logoutUser();
                        sessionManager.createLoginSession(user);
                        UpdateProfileActivity.super.onBackPressed();

                    }
                }, 2000);


            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(UpdateProfileActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

                } else {
                    pDialog.hide();
                    Toast.makeText(UpdateProfileActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }


}