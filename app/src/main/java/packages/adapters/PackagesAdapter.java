package packages.adapters;

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
import packages.activities.PackagesViewDetailsActivity;
import packages.models.PackagesModel;
import utils.customfonts.MyTextView_Roboto_Regular;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.MyViewHolder>  implements Filterable {
    private Context context;

    public PackagesAdapter(Context context, ArrayList<PackagesModel> packagesModelArrayList) {
        this.context = context;
        this.packagesModelArrayList = packagesModelArrayList;
        this.packagesModelArrayListFiltered = packagesModelArrayList;
    }

    private ArrayList<PackagesModel> packagesModelArrayList;
    private ArrayList<PackagesModel> packagesModelArrayListFiltered;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.packages_list_recyclerview, parent, false);

        return new PackagesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PackagesModel modelfoodrecycler = packagesModelArrayListFiltered.get(position);


//        holder.nyimg.setImageResource(modelfoodrecycler.getNyimg());
        Picasso.with(context).load(modelfoodrecycler.getImage1()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.nyimg, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });

        final String id = String.valueOf(modelfoodrecycler.getId());
        final String name = modelfoodrecycler.getName();
        final String validity = modelfoodrecycler.getValidity();
        final String price = modelfoodrecycler.getPrice();
        final String image1 = modelfoodrecycler.getImage1();
        final String image2 = modelfoodrecycler.getImage2();
        final String image3 = modelfoodrecycler.getImage3();
        final String image4 = modelfoodrecycler.getImage4();
        final String description = modelfoodrecycler.getDescription();
        final String pkginclusion = modelfoodrecycler.getPkginclusion();

        holder.nytext1.setText(modelfoodrecycler.getName());
        holder.nytext2.setText("Ksh."+modelfoodrecycler.getPrice()+"/=");
        holder.txt_validity.setText("Validity: "+modelfoodrecycler.getValidity());

        holder.btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent packageIntent = new Intent(context, PackagesViewDetailsActivity.class);
                packageIntent.putExtra("ID",id);
                packageIntent.putExtra("NAME",name);
                packageIntent.putExtra("VALIDITY",validity);
                packageIntent.putExtra("PRICE",price);
                packageIntent.putExtra("IMAGE1",image1);
                packageIntent.putExtra("IMAGE2",image2);
                packageIntent.putExtra("IMAGE3",image3);
                packageIntent.putExtra("IMAGE4",image4);
                packageIntent.putExtra("DESCRIPTION",description);
                packageIntent.putExtra("PKGINCLUSION",pkginclusion);
                context.startActivity(packageIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return packagesModelArrayListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView nyimg;
        TextView nytext1,nytext2,txt_validity;
        MyTextView_Roboto_Regular btn_view_more;
        public MyViewHolder(View itemView) {
            super(itemView);

            nyimg = itemView.findViewById(R.id.nyimg);
            txt_validity = itemView.findViewById(R.id.txt_validity);
            btn_view_more = itemView.findViewById(R.id.btn_view_more);
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
                    packagesModelArrayListFiltered = packagesModelArrayList;
                } else {
                    ArrayList<PackagesModel> filteredList = new ArrayList<>();
                    for (PackagesModel row : packagesModelArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (splitFilterValue.length == 1) {
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else if (splitFilterValue.length == 2) {
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase()) && row.getValidity().toLowerCase().contains(splitFilterValue[1].toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else if (splitFilterValue.length == 3) {
                            Toast.makeText(context, splitFilterValue[2], Toast.LENGTH_SHORT).show();
                            if (row.getName().toLowerCase().contains(splitFilterValue[0].toLowerCase()) && row.getValidity().toLowerCase().contains(splitFilterValue[1].toLowerCase()) && Double.valueOf(row.getPrice())<=Double.valueOf(splitFilterValue[2])) {
                                filteredList.add(row);
                            }
                        }


                    }

                    packagesModelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = packagesModelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                packagesModelArrayListFiltered = (ArrayList<PackagesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
