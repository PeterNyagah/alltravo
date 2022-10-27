package hotels.activities;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import e.wolfsoft1.liberty_ui_kit.R;
import hotels.fragments.Hotel_Fragment;
import utils.Utils;

public class HotelActivity extends AppCompatActivity {


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
                .replace(R.id.hotelactivity, new Hotel_Fragment(),
                        Utils.Hotel_Fragment).addToBackStack(Utils.Hotel_Fragment).commit();
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
}
