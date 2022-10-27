package hotels.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import hotels.fragments.Image4;


/**
 * Created by wolfsoft3 on 25/7/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String url_img1="";
    String url_img2="";
    String url_img3="";
    String url_img4="";
    public ViewPagerAdapter(FragmentManager fm,String img_url1,String img_url2,String img_url3,String img_url4) {
        super(fm);
        this.url_img1 = img_url1;
        this.url_img2 = img_url2;
        this.url_img3 = img_url3;
        this.url_img4 = img_url4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               Image4 tab1 = new Image4().newInstance(url_img1);

                return tab1;

            case 1:
                Image4 tab2 = new Image4().newInstance(url_img2);
                return tab2;

            case 2:
                Image4 tab3 = new Image4().newInstance(url_img3);
                return tab3;

            case 3:
                Image4 tab4 = new Image4().newInstance(url_img4);
                return tab4;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
