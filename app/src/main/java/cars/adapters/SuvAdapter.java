package cars.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cars.ItemClickListener;
import cars.activities.CarDetailActivity;
import cars.models.CarModel;
import e.wolfsoft1.liberty_ui_kit.R;
import utils.customfonts.MyTextView_Roboto_Regular;

public class SuvAdapter extends RecyclerView.Adapter<SuvAdapter.SuvHolder> {

    private Context context;
    private ArrayList<CarModel> cars;
    private ArrayList<CarModel> carModelArrayListFiltered;

    MyTextView_Roboto_Regular btn_car_view;

    public SuvAdapter(Context context, ArrayList<CarModel> cars) {
        this.context = context;
        this.cars = cars;
    }

    @NonNull
    @Override
    public SuvAdapter.SuvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_list_recyclerview, parent, false);
        return new SuvHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SuvAdapter.SuvHolder holder, int position) {
       /* CarModel car = cars.get(position);
        holder.setDetails(car);*/

//       CarModel carModel = carModelArrayListFiltered.get(position);
        CarModel carModel = cars.get(position);

        final String id = String.valueOf(carModel.getId());
        final String name = carModel.getName();
        final String plate = carModel.getPlate();
        final String capacity = carModel.getCapacity();
        final String city = carModel.getCity();
        final String image = carModel.getImage();
        final String description = carModel.getDescription();
        final String fee = carModel.getFee();
        final String country = carModel.getCountry();


        Glide.with(context)
                .load(image)
                .placeholder(R.drawable.imgloading)
                .into(holder.carimage);


        //holder.carimage.setImageResource(carModel.getImage());
        holder.txtcarname.setText(carModel.getName());
        holder.txtcarplate.setText(carModel.getPlate());
        holder.txtcarcapacity.setText(carModel.getCapacity() + " Adults");
        holder.txtcarcity.setText(carModel.getCity());
        holder.txtcarfee.setText("Ksh " + carModel.getFee());




       /* holder.btn_car_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent carDetails = new Intent(context, CarDetailActivity.class);
                carDetails.putExtra("ID", id);
                carDetails.putExtra("NAME", name);
                carDetails.putExtra("PLATE", plate);
                carDetails.putExtra("CAPACITY", capacity);
                carDetails.putExtra("CITY", city);
                carDetails.putExtra("IMAGE", image);
                carDetails.putExtra("DESCRIPTION", description);
                carDetails.putExtra("FEE", city);
                carDetails.putExtra("COUNTRY", country);
                context.startActivity(carDetails);
            }
        });*/

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent carDetails = new Intent(context, CarDetailActivity.class);
                carDetails.putExtra("ID", id);
                carDetails.putExtra("NAME", name);
                carDetails.putExtra("PLATE", plate);
                carDetails.putExtra("CAPACITY", capacity);
                carDetails.putExtra("CITY", city);
                carDetails.putExtra("IMAGE", image);
                carDetails.putExtra("DESCRIPTION", description);
                carDetails.putExtra("FEE", fee);
                carDetails.putExtra("COUNTRY", country);
                context.startActivity(carDetails);
            }
        });

        //BIND DATA
      /*  holder.txtPlate.setText(bus);
        holder.txtTillNo.setText((CharSequence) buses.get(position));
        holder.txtRoute.setText((CharSequence) buses.get(position));*/

       /* holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.i(TAG, "Item Clicked: " + pos);
                Toast.makeText(context,"Clicked: "+pos,Toast.LENGTH_LONG).show();
//
                // DetailFragment optionsFrag = new DetailFragment ();
                //((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"OptionsFragment").addToBackStack(null).commit();

                // Create new fragment and transaction
                Fragment newFragment = new DetailFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

//
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return cars.size();
    }

    class SuvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtcarname, txtcarplate, txtcarcapacity, txtcarcity, txtcarfee;
        private ImageView carimage;
        private ItemClickListener itemClickListener;
        private MyTextView_Roboto_Regular btn_car_view;

        SuvHolder(View itemView) {
            super(itemView);
            txtcarname = itemView.findViewById(R.id.txtcarname);
            txtcarplate = itemView.findViewById(R.id.txtcarplate);
            txtcarcapacity = itemView.findViewById(R.id.txtcarcapacity);
            txtcarcity = itemView.findViewById(R.id.txtcarcity);
            txtcarfee = itemView.findViewById(R.id.txtcarfee);
            carimage = itemView.findViewById(R.id.carimage);
            btn_car_view = itemView.findViewById(R.id.btn_car_view);


            itemView.setOnClickListener(this);
        }

      /*  void setDetails(CarModel carModel) {

            txtcarname.setText(carModel.getName());
            txtcarplate.setText(carModel.getPlate());
            txtcarcapacity.setText(carModel.getCapacity()+" Adults");
            txtcarcity.setText(carModel.getCity());
            txtcarfee.setText("Ksh "+carModel.getFee());


        }*/


        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;

        }
    }
}
