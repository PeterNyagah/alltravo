package user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import e.wolfsoft1.liberty_ui_kit.R;
import startup.Home;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.SessionManager;
import utils.Utils;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    private TextView signup, login, forgot_pass;
    private EditText edtxt_email, edtxt_pass;
    private static String url_login = AppConstants.URL_USER_LOGIN_SIGNUP;
    JSONArray tasksPerson = null;
    private boolean status = false;
    private boolean online = false;

    //private Button signInButton;
    private SignInButton signInButton;

    private User user = new User();
    private SessionManager sessionManager;
    private JSONParser jParser = new JSONParser();

    private final String TAG_STATUS = "status";
    private String msg = "";

    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        initComponents();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("17907441452-sdmdqngj08lthf8eqpo83qvgmb1uh89c.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsVerified()) {
                    String id_token = "";
                    String fname = "";
                    String lname = "";
                    new Login().execute(edtxt_email.getText().toString(), edtxt_pass.getText().toString(), id_token, fname, lname);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(signupIntent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotpassIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(forgotpassIntent);
            }
        });
    }

    private void initComponents() {
        edtxt_email = findViewById(R.id.edtxt_email);
        edtxt_pass = findViewById(R.id.edtxt_pass);
        signup = findViewById(R.id.signup);
        forgot_pass = findViewById(R.id.forgot_pass);
        login = findViewById(R.id.login);
        signInButton = findViewById(R.id.signInButton);
    }

    public boolean fieldsVerified() {
        boolean verified = false;
        if (TextUtils.isEmpty(edtxt_email.getText().toString()) || TextUtils.isEmpty(edtxt_pass.getText().toString()) || edtxt_pass.getText().toString().length() < 4) {
            if (TextUtils.isEmpty(edtxt_email.getText().toString()))
                edtxt_email.setError("Valid Email or Phone is Required!");
            if (TextUtils.isEmpty(edtxt_pass.getText().toString()))
                edtxt_pass.setError("Password is Required!");
            else if (edtxt_pass.getText().toString().length() < 4)
                edtxt_pass.setError("Password should be atleast 4 characters!");

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

            pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Authenticating...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "user_sign_in");
            params.put("email", args[0]);
            params.put("password", args[1]);
            params.put("id_token", args[2]);
            params.put("fname", args[3]);
            params.put("lname", args[4]);
            // getting JSON string from URL

            online = DUtils.isOnline(LoginActivity.this);
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
                        user.profileimg = Utils.sanitizeText(json.getString("profileimg"));
                        user.licenseimg = Utils.sanitizeText(json.getString("licenseimg"));
                        user.idimg = Utils.sanitizeText(json.getString("idimg"));

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

                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.logoutUser();
                        sessionManager.createLoginSession(user);

                        finish();
                        Intent homeIntent = new Intent(LoginActivity.this, Home.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(homeIntent);
                    }
                }, 2000);


            } else {
                if (online) {
                    pDialog.setTitleText("Error!")
                            .setContentText(msg)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    Toast.makeText(LoginActivity.this, msg.toString(), Toast.LENGTH_LONG).show();

                } else {
                    pDialog.hide();
                    Toast.makeText(LoginActivity.this, "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        } else if (resultCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();
        else if (resultCode == Activity.CONTEXT_RESTRICTED)
            Toast.makeText(this, "Context Restricted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Failed! Something went wrong.", Toast.LENGTH_SHORT).show();
    }
    // [END onactivityresult]

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            // Google Sign In was successful, authenticate with php Backend
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {

                Toast.makeText(this, "sign in success", Toast.LENGTH_SHORT).show();
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();
                String idToken = account.getIdToken();
//                String idToken = "off";

                new Login().execute(personEmail, personId, idToken, personGivenName, personFamilyName);
            }
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Google sign in failed", e);
            // [START_EXCLUDE]
            // updateUI(null);
            // [END_EXCLUDE]
        }
    }

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // updateUI(null);
                    }
                });
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}