package startup.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import flights.fragments.Flight_Fragment;
import hotels.fragments.Hotel_Fragment;
import packages.fragments.Package_Fragment;


/**
 * Created by wolfsoft3 on 24/7/18.
 */

public class HomeTabs extends FragmentStatePagerAdapter {
    int numoftabs;

    public HomeTabs(FragmentManager fm, int  mnumoftabs ) {
        super(fm);
        this.numoftabs = mnumoftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Flight_Fragment tab1 = new Flight_Fragment();
                return tab1;

            case 1:
                Hotel_Fragment tab2 = new Hotel_Fragment();
                return tab2;

            case 2:
                Package_Fragment tab3 = new Package_Fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
            return numoftabs;
    }
}
