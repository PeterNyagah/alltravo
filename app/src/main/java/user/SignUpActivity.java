package user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.Utils;

import static user.LoginActivity.isValidEmail;

public class SignUpActivity extends AppCompatActivity {

    private TextView signup, login;
    private EditText edtxt_email, edtxt_password, edtxt_confrirmPass, edtxt_fname, edtxt_lname, edtxt_phone;
    private String email, password, fname, lname, phone, username = "", country = "Kenya", city = "Nairobi";
    private static String url_signup = AppConstants.URL_USER_LOGIN_SIGNUP;
    private JSONArray tasksPerson = null;
    private boolean status = false;
    private boolean online = false;
    private CheckBox checkBoxTCs;
    private TextView readTCs;


    private User user = new User();
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";


    private SweetAlertDialog pDialog;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);

        initComponents();

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                phone = (ccp.getSelectedCountryCodeWithPlus() + edtxt_phone.getText().toString());
            }
        });

        readTCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://admin.alltravo.com/ticketing/terms_and_conditions.php"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsVerified()) {
                    email = edtxt_email.getText().toString();
                    password = edtxt_password.getText().toString();
                    fname = edtxt_fname.getText().toString();
                    lname = edtxt_lname.getText().toString();
                    phone = ccp.getSelectedCountryCodeWithPlus() + edtxt_phone.getText().toString();
                    username = edtxt_fname.getText().toString();

                    if (checkBoxTCs.isChecked()) {
                        new SignUp().execute();
                    } else {
                        agree();
                    }
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.super.onBackPressed();
            }
        });
    }

    private void agree() {
        Toast.makeText(this, "Agree To the Terms and Conditions", Toast.LENGTH_LONG).show();
    }


    private void initComponents() {
        ccp = findViewById(R.id.ccp);
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
        if (!edtxt_confrirmPass.getText().toString().equals(edtxt_password.getText().toString()) ||
                !isValidEmail(edtxt_email.getText().toString()) ||
                TextUtils.isEmpty(edtxt_password.getText().toString()) ||
                TextUtils.isEmpty(edtxt_fname.getText().toString()) ||
                TextUtils.isEmpty(edtxt_lname.getText().toString()) ||
                TextUtils.isEmpty(edtxt_phone.getText().toString()) ||
                TextUtils.isEmpty(edtxt_confrirmPass.getText().toString()) ||
                edtxt_phone.getText().toString().length() < 8 ||
                edtxt_password.getText().toString().length() < 4) {
            if (!isValidEmail(edtxt_email.getText().toString()))
                edtxt_email.setError("Valid Email address is Required!");
            if (TextUtils.isEmpty(edtxt_password.getText().toString()))
                edtxt_password.setError("Password is Required!");
            else if (edtxt_password.getText().toString().length() < 4)
                edtxt_password.setError("Password should be atleast 4 characters!");
            if (TextUtils.isEmpty(edtxt_confrirmPass.getText().toString()))
                edtxt_confrirmPass.setError("Confirm Password is Required!");
            else if (!edtxt_confrirmPass.getText().toString().equals(edtxt_password.getText().toString()))
                edtxt_confrirmPass.setError("Password do not match!");
            if (TextUtils.isEmpty(edtxt_phone.getText().toString()))
                edtxt_phone.setError("Phone number is Required!");
            else if (edtxt_phone.getText().toString().length() < 8)
                edtxt_phone.setError("Phone number should be atleast 10 characters!");
            if (TextUtils.isEmpty(edtxt_fname.getText().toString()))
                edtxt_fname.setError("First name is Required!");
            if (TextUtils.isEmpty(edtxt_lname.getText().toString()))
                edtxt_lname.setError("Last name is Required!!");

        } else
            verified = true;


        return verified;
    }

    public class SignUp extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Saving Your Details...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "addUser");
            params.put("email", email);
            params.put("password", password);
            params.put("password2", password);
            params.put("fname", fname);
            params.put("lname", lname);
            params.put("email", email);
            params.put("phone", phone);
            params.put("android", username);
            params.put("country", country);
            params.put("city", city);
            // getting JSON string from URL

            online = DUtils.isOnline(SignUpActivity.this);
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
                    try {
                        user.id = Integer.valueOf(json.getString("userid"));
                        user.fname = Utils.sanitizeText(json.getString("first_name"));
                        user.lname = Utils.sanitizeText(json.getString("last_name"));
                        user.phone = Utils.sanitizeText(json.getString("phone"));
                        user.phone2 = Utils.sanitizeText(json.getString("phone2"));
                        user.email = Utils.sanitizeText(json.getString("email"));
                        user.email2 = Utils.sanitizeText(json.getString("email2"));
                        user.country = Utils.sanitizeText(json.getString("country"));
                        user.city = Utils.sanitizeText(json.getString("city"));
                        user.type = Utils.sanitizeText(json.getString("type"));

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

            if (status) {
                pDialog.setTitleText("Success!")
                        .setContentText(msg)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms

                        SessionManager sessionManager = new SessionManager(SignUpActivity.this);
                        sessionManager.logoutUser();
                        sessionManager.createLoginSession(user);

                        finish();
                        Intent homeIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(homeIntent);
                    }
                }, 2000);


            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(SignUpActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

                } else {
                    pDialog.hide();
                    Toast.makeText(SignUpActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }


}