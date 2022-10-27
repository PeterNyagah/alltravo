package user;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForgotPasswordActivity extends AppCompatActivity {


    private TextView login,submit;
    private EditText edtxt_email;
    private static String url_login  = AppConstants.URL_USER_LOGIN_SIGNUP;
    JSONArray tasksPerson = null;
    private boolean status = false;
    private boolean online = false;


    private User user = new User();
    private SessionManager sessionManager;
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";


    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass_activity_layout);

        initComponents();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsVerified()) {
                    new Login().execute(edtxt_email.getText().toString());
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordActivity.super.onBackPressed();
            }
        });
    }

    private void initComponents() {
        edtxt_email = findViewById(R.id.edtxt_email);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
    }

    public boolean fieldsVerified() {
        boolean verified = false;
        if (TextUtils.isEmpty(edtxt_email.getText().toString())) {
            if (TextUtils.isEmpty(edtxt_email.getText().toString()))
                edtxt_email.setError("Valid Email or Phone is Required!");

        } else
            verified = true;


        return verified;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public class Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "forgot_pass");
            params.put("email", args[0]);
            // getting JSON string from URL

            online = DUtils.isOnline(ForgotPasswordActivity.this);
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_login, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi hehehhehehe");
                    e.printStackTrace();
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }


                try {

                    status = json.getBoolean(TAG_STATUS);
                    msg = json.getString("msg");
                } catch (Exception e) {
                    Log.d("ERROR LOG", "pieError");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();


            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(ForgotPasswordActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

                } else {
                    pDialog.hide();
                    Toast.makeText(ForgotPasswordActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }
}