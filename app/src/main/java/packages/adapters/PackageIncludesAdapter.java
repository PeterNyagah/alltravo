package packages.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import e.wolfsoft1.liberty_ui_kit.R;
import packages.models.PackageIncludesModel;

public class PackageIncludesAdapter extends RecyclerView.Adapter<PackageIncludesAdapter.MyViewHolder> {
    Context context;

    public PackageIncludesAdapter(Context context, ArrayList<PackageIncludesModel> packageIncludesModelArrayList) {
        this.context = context;
        this.packageIncludesModelArrayList = packageIncludesModelArrayList;
    }

    private ArrayList<PackageIncludesModel> packageIncludesModelArrayList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_includes_list, parent, false);

        return new PackageIncludesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        PackageIncludesModel packageIncludesModel = packageIncludesModelArrayList.get(position);

        holder.txt_inclide.setText(packageIncludesModel.getTxt_inclide());



    }

    @Override
    public int getItemCount() {
        return packageIncludesModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_inclide;
        public MyViewHolder(View itemView) {
            super(itemView);

            txt_inclide = itemView.findViewById(R.id.txt_inclide);
        }
    }
}
