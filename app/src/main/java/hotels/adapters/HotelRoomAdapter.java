package hotels.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import e.wolfsoft1.liberty_ui_kit.R;
import hotels.activities.HotelBookingDetails;
import hotels.models.HotelRoomModel;
import utils.customfonts.MyTextView_Roboto_Regular;

public class HotelRoomAdapter extends RecyclerView.Adapter<HotelRoomAdapter.MyViewHolder>  implements PopupMenu.OnMenuItemClickListener{
    Context context;


    public HotelRoomAdapter(Context context, ArrayList<HotelRoomModel> hotelRoomModelArrayList) {
        this.context = context;
        this.hotelRoomModelArrayList = hotelRoomModelArrayList;
    }

    private ArrayList<HotelRoomModel> hotelRoomModelArrayList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotels_rooms_list_recyclerview, parent, false);

        return new HotelRoomAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        HotelRoomModel hotelRoomModel = hotelRoomModelArrayList.get(position);
        final String id = hotelRoomModel.getId();
        final String price = hotelRoomModel.getPrice();
        final String h_id = hotelRoomModel.getH_id();
        final String h_name = hotelRoomModel.getH_name();
        final String name = hotelRoomModel.getName();

        Picasso.with(context).load(hotelRoomModel.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.nyimg, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });
        holder.nytext1.setText(hotelRoomModel.getName());
        holder.nytext2.setText("Ksh. "+hotelRoomModel.getPrice());

        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String test = holder.nytext2.getText().toString();
//                Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
//                PopupMenu popup = new PopupMenu(context, view);
//                popup.setOnMenuItemClickListener(HotelRoomAdapter.this);
//                popup.inflate(R.menu.popup_menu);
//                popup.show();
                Intent selfintent = new Intent(context, HotelBookingDetails.class);
                selfintent.putExtra("BOOKING_FOR","self");
                selfintent.putExtra("ID",id);
                selfintent.putExtra("PRICE",price);
                selfintent.putExtra("H_ID",h_id);
                selfintent.putExtra("H_NAME",h_name);
                selfintent.putExtra("NAME",name);
                context.startActivity(selfintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelRoomModelArrayList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_self:
//                Intent selfintent = new Intent(context, HotelBookingDetails.class);
//                selfintent.putExtra("BOOKING_FOR","self");
//                selfintent.putExtra("ID",id);
//                selfintent.putExtra("PRICE",price);
//                selfintent.putExtra("H_ID",h_id);
//                selfintent.putExtra("H_NAME",h_name);
//                selfintent.putExtra("NAME",name);
//                context.startActivity(selfintent);
                return true;
//            case R.id.menu_other:
//                Intent otherintent = new Intent(context, HotelBookingDetails.class);
//                otherintent.putExtra("BOOKING_FOR","other");
//                context.startActivity(otherintent);
//                return true;
            default:
                return false;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView nyimg,nyimg2;
        TextView nytext1,nytext2;
        MyTextView_Roboto_Regular btn_book;
        public MyViewHolder(View itemView) {
            super(itemView);

            nyimg = itemView.findViewById(R.id.nyimg);
            btn_book = itemView.findViewById(R.id.btn_book);
            nytext1 = itemView.findViewById(R.id.nytext1);
            nytext2 = itemView.findViewById(R.id.nytext2);
        }
    }
}
