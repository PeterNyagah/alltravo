package flights.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.NumberFormat;
import java.util.ArrayList;

import e.wolfsoft1.liberty_ui_kit.R;
import flights.activities.FlightViewDetailsActivity;
import flights.models.PojoClass;
import utils.customfonts.MyTextView_Roboto_Regular;


/**
 * Created by kuldeep on 31/01/18.
 */

public class ListAdapters extends RecyclerView.Adapter<ListAdapters.ViewHolder> {

    private Context context;
    private ArrayList<PojoClass> pojoClassArrayList;
    private PojoClass pojclass;

    public ListAdapters(Context context, ArrayList<PojoClass> pojoClassArrayList,PojoClass pojclass ) {
        this.context = context;
        this.pojoClassArrayList = pojoClassArrayList;
        this.pojclass = pojclass;
    }
    @Override
    public ListAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListAdapters.ViewHolder holder, int position) {

        final String id = pojoClassArrayList.get(position).getId();
        final String name = pojoClassArrayList.get(position).getText_airline();
        final String price = pojoClassArrayList.get(position).getText_time();
        final String duration = pojoClassArrayList.get(position).getText_timeschedule();

        NumberFormat nf = NumberFormat.getInstance();

        holder.text_airline.setText(pojoClassArrayList.get(position).getText_airline());
        holder.text_timeschedule.setText(pojoClassArrayList.get(position).getText_timeschedule()+","+pojoClassArrayList.get(position).getStops());
        holder.text_time.setText(nf.format(Double.valueOf(pojoClassArrayList.get(position).getText_time())));
        holder.id.setText(pojoClassArrayList.get(position).getId());
        holder.logo.setImageResource(pojoClassArrayList.get(position).getLogo());
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent flightviewIntent = new Intent(context, FlightViewDetailsActivity.class);
                flightviewIntent.putExtra("ID",id);
                flightviewIntent.putExtra("NAME",name);
                flightviewIntent.putExtra("PRICE",price);
                flightviewIntent.putExtra("DURATION",duration);
                flightviewIntent.putExtra("FROM",pojclass.getFrom());
                flightviewIntent.putExtra("TO",pojclass.getTo());
                flightviewIntent.putExtra("WAY",pojclass.getWay());
                flightviewIntent.putExtra("CLASS",pojclass.getLevel());
                flightviewIntent.putExtra("PASSANGERS",pojclass.getPassangers());
                flightviewIntent.putExtra("DEPARTURE_DATE",pojclass.getDeparture_date());
                flightviewIntent.putExtra("RETURN_DATE",pojclass.getReturn_date());
                context.startActivity(flightviewIntent);
            }
        });

    }

    @Override
    public int getItemCount()  {
        return pojoClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_airline,text_timeschedule,text_time,id;
        ImageView logo;
        MyTextView_Roboto_Regular btn_view;


        public ViewHolder(View view) {
            super(view);

            id =  view.findViewById(R.id.id);
            text_airline =  view.findViewById(R.id.text_airline);
            text_timeschedule =  view.findViewById(R.id.text_timeschedule);
            text_time =  view.findViewById(R.id.text_time);
            logo = view.findViewById(R.id.logo);
            btn_view = view.findViewById(R.id.btn_view);
        }
    }
}
