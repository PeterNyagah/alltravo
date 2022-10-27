package trips.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import e.wolfsoft1.liberty_ui_kit.R;
import trips.activities.MyTripsViewDetailsActivity;
import trips.models.MyTripsClass;
import utils.customfonts.MyTextView_Roboto_Regular;


/**
 * Created by kuldeep on 31/01/18.
 */

public class MyTripsListAdapters extends RecyclerView.Adapter<MyTripsListAdapters.ViewHolder> {

    private Context context;
    private ArrayList<MyTripsClass> myTripsClassArrayList;

    public MyTripsListAdapters(Context context, ArrayList<MyTripsClass> myTripsClassArrayList) {
        this.context = context;
        this.myTripsClassArrayList = myTripsClassArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytrips_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final int id = myTripsClassArrayList.get(position).getId();
        final String type = myTripsClassArrayList.get(position).getType();
        final String name = myTripsClassArrayList.get(position).getName();
        final String city = myTripsClassArrayList.get(position).getCity();
        final String country = myTripsClassArrayList.get(position).getCountry();
        final String roomtype = myTripsClassArrayList.get(position).getRoomtype();
        final String persons = myTripsClassArrayList.get(position).getPersons();
        final String status = myTripsClassArrayList.get(position).getStatus();
        final String from = myTripsClassArrayList.get(position).getFrom();
        final String to = myTripsClassArrayList.get(position).getTo();
        final String duration = myTripsClassArrayList.get(position).getDuration();
        final String date = myTripsClassArrayList.get(position).getText_timeschedule();
        final String date2 = myTripsClassArrayList.get(position).getDate2();
        final Double price = myTripsClassArrayList.get(position).getPrice();

        holder.text_airline.setText(myTripsClassArrayList.get(position).getText_airline());
        holder.text_timeschedule.setText(myTripsClassArrayList.get(position).getText_timeschedule());
        holder.text_time.setText(myTripsClassArrayList.get(position).getStatus());
        holder.type.setText(myTripsClassArrayList.get(position).getType());
        holder.id.setText(Integer.toString(myTripsClassArrayList.get(position).getId()));
        holder.text_amount.setText("Ksh. "+Double.toString(myTripsClassArrayList.get(position).getPrice()));
        if (myTripsClassArrayList.get(position).getStatus().equals("paid")){
            holder.text_time.setTextColor(Color.parseColor("#009900"));
        }
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mytripsviewIntent = new Intent(context, MyTripsViewDetailsActivity.class);
                mytripsviewIntent.putExtra("ID",id);
                mytripsviewIntent.putExtra("TYPE",type);
                mytripsviewIntent.putExtra("NAME",name);
                mytripsviewIntent.putExtra("CITY",city);
                mytripsviewIntent.putExtra("COUNTRY",country);
                mytripsviewIntent.putExtra("ROOMTYPE",roomtype);
                mytripsviewIntent.putExtra("PERSONS",persons);
                mytripsviewIntent.putExtra("STATUS",status);
                mytripsviewIntent.putExtra("FROM",from);
                mytripsviewIntent.putExtra("TO",to);
                mytripsviewIntent.putExtra("DURATION",duration);
                mytripsviewIntent.putExtra("DATE",date);
                mytripsviewIntent.putExtra("DATE2",date2);
                mytripsviewIntent.putExtra("PRICE",price);
                context.startActivity(mytripsviewIntent);
            }
        });

    }

    @Override
    public int getItemCount()  {
        return myTripsClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_airline,text_timeschedule,text_time,text_amount,id,type;
        MyTextView_Roboto_Regular btn_view;
//        ImageView logo;


        public ViewHolder(View view) {
            super(view);

            text_airline =  view.findViewById(R.id.text_airline);
            type =  view.findViewById(R.id.type);
            text_amount =  view.findViewById(R.id.text_amount);
            id =  view.findViewById(R.id.id);
            text_timeschedule =  view.findViewById(R.id.text_timeschedule);
            text_time =  view.findViewById(R.id.text_time);
            btn_view = view.findViewById(R.id.btn_view);
//            logo = view.findViewById(R.id.logo);
        }
    }
}
