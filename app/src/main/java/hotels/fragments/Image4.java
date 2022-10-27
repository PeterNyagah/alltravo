package hotels.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import e.wolfsoft1.liberty_ui_kit.R;

public class Image4 extends Fragment {
    ImageView image4;
    String url_img="";

    public Image4  newInstance(String imgurl){
        Image4 image4 = new Image4();
        image4.url_img = imgurl;

        return image4;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotel_image4_fragment,container,false);
        image4 = view.findViewById(R.id.img_image4);

        Picasso.with(getActivity()).load(url_img).into(image4, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });
        return view;
    }
}
