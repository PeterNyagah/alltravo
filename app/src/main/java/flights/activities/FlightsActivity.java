package flights.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import e.wolfsoft1.liberty_ui_kit.R;
import flights.fragments.Flight_Fragment;
import utils.Utils;

public class FlightsActivity extends AppCompatActivity {


    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hotel);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.hotelactivity, new Flight_Fragment(),
                        Utils.Flight_Fragment).addToBackStack(Utils.Flight_Fragment).commit();
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
}
