package startup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cars.activities.CarHireActivity;
import flights.activities.FlightsActivity;
import hotels.activities.HotelActivity;
import packages.activities.PackagesActivity;
import payments.activities.Payments_List_Activity;
import startup.adapter.HomeTabs;
import e.wolfsoft1.liberty_ui_kit.R;
import startup.adapter.SlidingImage_Adapter;
import trips.activities.MyTrips_List_Activity;
import user.LoginActivity;
import user.ProfileActivity;
import user.model.User;
import utils.AppConstants;
import utils.SessionManager;

import static user.SignActivity.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class Home extends AppCompatActivity {

    //implements NavigationView.OnNavigationItemSelectedListener
    ViewPager viewPager1;
    TabLayout tabLayout1;
    HomeTabs adapter;
    ImageView img_menu, imgProfile;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private String[] urls = new String[]{
            "https://admin.alltravo.com/sliderImages/1.jpg",
            "https://admin.alltravo.com/sliderImages/2.jpg",
            "https://admin.alltravo.com/sliderImages/3.jpg",
            "https://admin.alltravo.com/sliderImages/4.jpg",
            "https://admin.alltravo.com/sliderImages/5.jpg",
    };


    private User user = new User();
    private SessionManager sessionManager;
    private DrawerLayout mDrawerLayout;
    private Typeface mTypeface;
    private Typeface mTypefaceBold;

    private TextView profileName, profileEmail, user_name;
    private CardView btn_flights, btn_myprofile, btn_packages,
            btn_hotels, btn_mybookins, btn_payments, btn_livechat, btn_contact, btn_notification;


    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("825873749004-lu6nitp3pl2fnsek55d0vhd65u6qac6m.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Home.this);

        sessionManager = new SessionManager(Home.this);

        if (!sessionManager.isLoggedIn()) {
            finish();
            Intent homeIntent = new Intent(Home.this, LoginActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(homeIntent);
            return;
        }

        user = sessionManager.getUserDetails();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        img_menu = findViewById(R.id.img_menu);
        user_name = findViewById(R.id.user_name);
        btn_flights = findViewById(R.id.btn_flights);
        btn_hotels = findViewById(R.id.btn_hotels);
        //   btn_myprofile = findViewById(R.id.btn_myprofile);
        btn_mybookins = findViewById(R.id.btn_mytrips);
        btn_packages = findViewById(R.id.btn_packages);
//        btn_payments = findViewById(R.id.btn_payments);
       /* btn_livechat = findViewById(R.id.btn_livechat);
        btn_contact = findViewById(R.id.btn_contact);
        btn_notification = findViewById(R.id.btn_notifications);*/

        btn_hotels.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, HotelActivity.class);
                startActivity(intent);
            }
        });
        btn_flights.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, FlightsActivity.class);
                startActivity(intent);
            }
        });
        btn_packages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, PackagesActivity.class);
                startActivity(intent);
            }
        });
        /*
        btn_myprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ProfileActivity.class);
                startActivity(intent);
            }
        });*/
       /* btn_mybookins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyTrips_List_Activity.class);
                startActivity(intent);
            }
        });*/

        btn_mybookins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, CarHireActivity.class);
                startActivity(i);
            }
        });


       /* btn_payments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Payments_List_Activity.class);
                startActivity(intent);
            }
        });*/
       /*  btn_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + AppConstants.CONTACT_PHONE_NUMBER));
                if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    startActivity(intent);
                else {

                    List<String> listPermissionsNeeded = new ArrayList<>();
                    listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions(Home.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }
                }
            }
        });
       btn_livechat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(Home.this, "Feature under development", Toast.LENGTH_SHORT).show();

                String url = "https://wa.me/254712362577";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        btn_notification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Home.this, "You have no New Notifications", Toast.LENGTH_SHORT).show();
            }
        });*/

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        final NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        profileEmail = header.findViewById(R.id.profileEmail);
        imgProfile = header.findViewById(R.id.imgProfile);

        profileName = header.findViewById(R.id.profileName);

        profileEmail.setText(user.email);

        if (account != null) {
            Picasso.with(getApplicationContext()).load(account.getPhotoUrl()).into(imgProfile);
        } else {
//          {  Picasso.with(getApplicationContext()).load(user.profileimg).into(imgProfile);
            Glide.with(this)
                    .load(user.profileimg)
                    .placeholder(R.drawable.imgloading)
                    .into(imgProfile);


        }


        profileName.setText(user.fname + " " + user.lname);
        user_name.setText("Welcome " + user.fname.toUpperCase());

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        unCheckAllMenuItems(navigationView.getMenu());
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_myTrips:
                                menuItem.setChecked(true);
                                Intent mytripIntent = new Intent(Home.this, MyTrips_List_Activity.class);
                                startActivity(mytripIntent);
                                break;
                            case R.id.nav_profile:
                                menuItem.setChecked(true);
                                Intent profileIntent = new Intent(Home.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                break;
                            case R.id.nav_payments:
                                menuItem.setChecked(true);
                                Intent paymentsIntent = new Intent(Home.this, Payments_List_Activity.class);
                                startActivity(paymentsIntent);
                                break;
                            case R.id.nav_notifications:
                                menuItem.setChecked(true);
                                String url = "https://wa.me/254740234002";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                            case R.id.nav_about:
                                Intent yes = new Intent(Home.this, AboutActivity.class);
                                startActivity(yes);
                                break;

                            case R.id.nav_call:
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + AppConstants.CONTACT_PHONE_NUMBER));
                                if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                                    startActivity(intent);
                                else {

                                    List<String> listPermissionsNeeded = new ArrayList<>();
                                    listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
                                    if (!listPermissionsNeeded.isEmpty()) {
                                        ActivityCompat.requestPermissions(Home.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                                    }
                                }
                                break;
                            case R.id.nav_logout:
                                signout();
                                break;
                        }

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


    }

    private void signout() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sessionManager.logoutUser();
                        finish();
                        Intent loginIntent = new Intent(Home.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
    }

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(Home.this, urls));

       /* CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);*/

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        // indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        /*indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/

    }

}
