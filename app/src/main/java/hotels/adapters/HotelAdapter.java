package hotels.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import e.wolfsoft1.liberty_ui_kit.R;
import hotels.activities.HotelViewDetailsActivity;
import hotels.models.HotelModel;
import utils.customfonts.MyTextView_Roboto_Regular;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> implements Filterable {
    Context context;

    public HotelAdapter(Context context, ArrayList<HotelModel> hotelModelArrayList) {
        this.context = context;
        this.hotelModelArrayList = hotelModelArrayList;
        this.hotelModelArrayListFiltered = hotelModelArrayList;
    }

    private ArrayList<HotelModel> hotelModelArrayList;
    private ArrayList<HotelModel> hotelModelArrayListFiltered;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotels_list_recyclerview, parent, false);

        return new HotelAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HotelModel modelfoodrecycler = hotelModelArrayListFiltered.get(position);


        final String id = modelfoodrecycler.getId();
        final String name = modelfoodrecycler.getName();
        final String country = modelfoodrecycler.getCountry();
        final String owner = modelfoodrecycler.getOwner();
        final String image = modelfoodrecycler.getImage();
        final String description = modelfoodrecycler.getDescription();
        final String city = modelfoodrecycler.getCity();

        Picasso.with(context).load(modelfoodrecycler.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.nyimg, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });
        holder.nytext1.setText(modelfoodrecycler.getName());
        holder.nytext2.setText(modelfoodrecycler.getCity() + "," + modelfoodrecycler.getCountry());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hotelDetailsIntent = new Intent(context, HotelViewDetailsActivity.class);
                hotelDetailsIntent.putExtra("ID", id);
                hotelDetailsIntent.putExtra("NAME", name);
                hotelDetailsIntent.putExtra("COUNTRY", country);
                hotelDetailsIntent.putExtra("OWNER", owner);
                hotelDetailsIntent.putExtra("IMAGE", image);
                hotelDetailsIntent.putExtra("DESCRIPTION", description);
                hotelDetailsIntent.putExtra("CITY", city);
                context.startActivity(hotelDetailsIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelModelArrayListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView nyimg;
        TextView nytext1, nytext2;
        MyTextView_Roboto_Regular btn_view;

        public MyViewHolder(View itemView) {
            super(itemView);

            nyimg = itemView.findViewById(R.id.nyimg);
            btn_view = itemView.findViewById(R.id.btn_view);
            nytext1 = itemView.findViewById(R.id.nytext1);
            nytext2 = itemView.findViewById(R.id.nytext2);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();


                String[] splitFilterValue = charString.split("%");


                if (charString.isEmpty()) {
                    Toast.makeText(context, "Filter Cleared", Toast.LENGTH_SHORT).show();
                    hotelModelArrayListFiltered = hotelModelArrayList;
                } else {
                    ArrayList<HotelModel> filteredList = new ArrayList<>();
                    for (HotelModel row : hotelModelArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (splitFilterValue.length == 1) {
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else if (splitFilterValue.length == 2) {
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase()) && row.getCity().toLowerCase().contains(splitFilterValue[1].toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else if (splitFilterValue.length == 3) {
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase()) && row.getCity().toLowerCase().contains(splitFilterValue[1].toLowerCase()) && row.getCountry().toLowerCase().contains(splitFilterValue[2].toLowerCase())) {
                                filteredList.add(row);
                            }
                        }


                    }

                    hotelModelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = hotelModelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hotelModelArrayListFiltered = (ArrayList<HotelModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
