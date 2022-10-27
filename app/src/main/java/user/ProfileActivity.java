package user;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import e.wolfsoft1.liberty_ui_kit.R;
import id.zelory.compressor.Compressor;
import user.model.User;
import utils.AppConstants;
import utils.DUtils;
import utils.FilePath;
import utils.JSONParser;
import utils.SessionManager;
import utils.customfonts.MyTextView_Roboto_Regular;

public class ProfileActivity extends AppCompatActivity {


    private ImageView img_back;
    private MyTextView_Roboto_Regular title;
    private ImageButton uploadImage, uploadLicenseImage, uploadIdImage;
    private ImageView profileImage, licenseImage, idImage;
    private TextView clientFirstname;
    private TextView clientLastName;
    private TextView clientEmail;
    private TextView clientPhone;
    private TextView fullName;
    private TextView clientCreditCard;
    private ImageButton editProfile;


    private User user = new User();
    private SessionManager sessionManager;


    private static final int STORAGE_PERMISSION_CODE = 2342;
    private Bitmap bitmap, bitmap1, bitmap2;
    ProgressDialog dialog;
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int PICK_FILE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST2 = 2;
    private static final int PICK_FILE_REQUEST3 = 3;
    private String selectedFilePath;


    private final String TAG_STATUS = "status";
    private String msg = "";
    private String profileimg = "";
    private String licenseimg = "";
    private String idimg = "";


    private JSONParser jParser = new JSONParser();
    private boolean online = true;
    private boolean status = true;


    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initComponents();
        requestStoragePermission();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("825873749004-lu6nitp3pl2fnsek55d0vhd65u6qac6m.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);

        sessionManager = new SessionManager(ProfileActivity.this);

        user = sessionManager.getUserDetails();

        fullName.setText(user.fname + " " + user.lname);
        clientFirstname.setText("First Name: " + user.fname);
        clientLastName.setText("Last Name: " + user.lname);
        clientEmail.setText("Email: " + user.email);
        clientPhone.setText("Phone: " + user.phone);

        if (account != null) {
            Picasso.with(getApplicationContext()).load(account.getPhotoUrl()).into(profileImage);
            Picasso.with(getApplicationContext()).load(user.licenseimg).into(licenseImage);
            Picasso.with(getApplicationContext()).load(user.idimg).into(idImage);
          /*  Picasso.with(getApplicationContext()).load(account.getPhotoUrl()).into(licenseImage);
            Picasso.with(getApplicationContext()).load(account.getPhotoUrl()).into(idImage);*/
        } else {
            Picasso.with(getApplicationContext()).load(user.profileimg).into(profileImage);
            Picasso.with(getApplicationContext()).load(user.licenseimg).into(licenseImage);
            Picasso.with(getApplicationContext()).load(user.idimg).into(idImage);
        }
        title.setText("My Profile");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //sets the select file to all types of files
                intent.setType("*/*");
                //allows to select data and return it
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

            }
        });

        uploadLicenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                //sets the select file to all types of files
                i.setType("*/*");
                //allows to select data and return it
                i.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(i, "Choose File to Upload.."), PICK_FILE_REQUEST2);

            }
        });

        uploadIdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent();
                //sets the select file to all types of files
                o.setType("*/*");
                //allows to select data and return it
                o.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(o, "Choose File to Upload.."), PICK_FILE_REQUEST3);

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ProfileActivity.this, "Feature under development", Toast.LENGTH_SHORT).show();
                Intent updateProfile = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(updateProfile);
            }
        });


    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permision granted ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permision denied ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //no data present
                return;
            }
            if (requestCode == PICK_FILE_REQUEST) {

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);

                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profileImage.setImageBitmap(bitmap);
                    dialog = ProgressDialog.show(ProfileActivity.this, "", "Uploading File...*", true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            uploadFile(selectedFilePath, PICK_FILE_REQUEST);
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_FILE_REQUEST2) {

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);

                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {

                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    licenseImage.setImageBitmap(bitmap1);
                    dialog = ProgressDialog.show(ProfileActivity.this, "", "Uploading File...*", true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            uploadFile(selectedFilePath, PICK_FILE_REQUEST2);
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_FILE_REQUEST3) {

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);

                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {

                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    idImage.setImageBitmap(bitmap2);
                    dialog = ProgressDialog.show(ProfileActivity.this, "", "Uploading File...*", true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            uploadFile(selectedFilePath, PICK_FILE_REQUEST3);
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public int uploadFile(String selectedFilePath, final int FILE_TYPE) {//Uploads File

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        final String[] parts = selectedFilePath.split("/");


        final String fileName = parts[parts.length - 1];
        if (!selectedFile.isFile()) {
            dialog.dismiss();

            final String finalSelectedFilePath = selectedFilePath;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProfileActivity.this, "Source File Doesn't Exist: " + finalSelectedFilePath, Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        } else {
            try {
                try {
                    selectedFile = new Compressor(this).compressToFile(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedFilePath = selectedFilePath
                        .replaceAll("-", "").trim()
                        .replaceAll("_", "").trim();
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL("https://admin.alltravo.com/ticketing/controls/users/uploadfile.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("filename", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"filename\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "https://infleg.com/infleg/images/" + fileName);
                            final String fname = "https://admin.alltravo.com/ticketing/controls/users/img/" + fileName

                                    .replaceAll("-", "").trim()
                                    .replaceAll("_", "").trim();

                            profileimg = "https://admin.alltravo.com/ticketing/controls/users/img/" + fileName.replaceAll("-", "").trim().replaceAll("_", "").trim();

                            String action = "";

                            if (FILE_TYPE == 1) {
                                action = "updateUserimg";
                                user.profileimg = profileimg;
                            } else if (FILE_TYPE == 2) {
                                action = "updateLinsenceimg";
                                user.licenseimg = profileimg;
                            } else if (FILE_TYPE == 3) {
                                action = "updateIdimg";
                                user.idimg = profileimg;
                            }

                            new updatephotourl().execute(fname, action);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }

    public class updatephotourl extends AsyncTask<String, String, String> {//updates url on database

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait...");
            dialog.show();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", Integer.toString(user.id));
            params.put("action", args[1]);
            params.put("profileimg", args[0]);
            // getting JSON string from URL

            JSONObject postjson = new JSONObject();
            online = DUtils.isOnline(ProfileActivity.this);
            if (online) {
                try {
                    postjson = jParser.makeHttpRequest(AppConstants.URL_USER_LOGIN_SIGNUP, "POST", params);


                    Log.d("ERROR task Details", "Categories available " + postjson.toString());
                } catch (Exception e) {
                    Log.d("ERROR LOG", "printStackTraceD");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }
                try {
                    status = postjson.getBoolean(TAG_STATUS);
                    msg = postjson.getString("msg");
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
            dialog.dismiss();
            if (status) {
                SessionManager sessionManager = new SessionManager(ProfileActivity.this);
                sessionManager.logoutUser();
                sessionManager.createLoginSession(user);
                Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else

                Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();


        }

    }


    private void initComponents() {
        title = findViewById(R.id.title);
        img_back = findViewById(R.id.img_back);
        profileImage = findViewById(R.id.profileImage);
        licenseImage = findViewById(R.id.licenseImage);
        idImage = findViewById(R.id.idImage);

        uploadImage = findViewById(R.id.btnEditProfilePicture);
        uploadLicenseImage = findViewById(R.id.btnEditLicensePicture);
        uploadIdImage = findViewById(R.id.btnEditIdPicture);
        editProfile = findViewById(R.id.btnEditProfile);

        clientFirstname = findViewById(R.id.txtClientFirstName);
        clientLastName = findViewById(R.id.txtClientLastName);
        clientEmail = findViewById(R.id.txtClientEmail);
        clientPhone = findViewById(R.id.txtClientPhone);
        fullName = findViewById(R.id.txtFullName);
    }
}
