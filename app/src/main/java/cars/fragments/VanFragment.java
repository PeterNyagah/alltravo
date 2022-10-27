package cars.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cars.adapters.CarAdapter;
import cars.models.CarModel;
import e.wolfsoft1.liberty_ui_kit.R;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;


public class VanFragment extends Fragment {


    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private ArrayList<CarModel> carArrayList;
    private static ProgressDialog progressDialog;
    private int counter = 0;

    private TextView no_data;

    private static String url_cars = AppConstants.URL_CARS;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_car, container, false);


        no_data = root.findViewById(R.id.no_data);
        recyclerView = (RecyclerView) root.findViewById(R.id.carRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carArrayList = new ArrayList<>();
        carAdapter = new CarAdapter(getContext(), carArrayList);
        recyclerView.setAdapter(carAdapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //createListData();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        new ListCars().execute();

        return root;
    }

    private void createListData() {

        CarModel route = new CarModel( "Mercedes Benz", "KCC 343T", "5 Adults", "7500", "Nairobi", "Kenya", "Avatar", "Mercedes Benz luxury beyond experience",1);
        carArrayList.add(route);

        CarModel route1 = new CarModel("Mazda Demio", "KCZ 343T", "5 Adults", "5500", "Nairobi", "Kenya", "Avatar", "Mercedes Benz luxury beyond experience",2);
        carArrayList.add(route1);

        CarModel route2 = new CarModel( "RunX", "KCQ 343T", "5 Adults", "4500", "Nairobi", "Kenya", "Avatar", "Mercedes Benz luxury beyond experience",3);
        carArrayList.add(route2);

        CarModel route3 = new CarModel( "Voxy", "KCE 343T", "5 Adults", "3500", "Nairobi", "Kenya", "Avatar", "Mercedes Benz luxury beyond experience",4);
        carArrayList.add(route3);

        CarModel route4 = new CarModel( "Subaru Impreza", "KCE 343T", "5 Adults", "500", "Nairobi", "Kenya", "Avatar", "Mercedes Benz luxury beyond experience", 5);
        carArrayList.add(route4);

        carAdapter.notifyDataSetChanged();
    }


    public class ListCars extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carArrayList.clear();
            progressDialog.setMessage("Please Wait...");
            showDialog();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "listCarsVan");
            // getting JSON string from URL

            online = DUtils.isOnline(getActivity());
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_cars, "POST", params);
//                    msg = json.toString();
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(getContext(), "A connection could not be established", Toast.LENGTH_LONG).show();
                }
                if (json != null)
                    if (json.length() > 0) {
                        status = true;

                        try {
                            jsonArray = json.getJSONArray("cars");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                counter = counter + 1;
                                JSONObject c = jsonArray.getJSONObject(i);
//                                String  name, plate, capacity, fee, city, country, image, description;
                                CarModel view1 = new CarModel(
                                        c.getString("name"),
                                        c.getString("plate"),
                                        c.getString("capacity"),
                                        c.getString("fee"),
                                        c.getString("city"),
                                        c.getString("country"),
                                        "https://admin.alltravo.com/ticketing/controls/cars/" + c.getString("image"),
                                        c.getString("description"),
                                        c.getInt("id")

                                );

                                carArrayList.add(view1);
                            }

                        } catch (JSONException e) {
                            Log.d("ERROR LOG", "ueufhuwew");
                        }

                    }

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all items

            hideDialog();

//            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            if (counter > 0) {
                carAdapter = new CarAdapter(getActivity(), carArrayList);
                recyclerView.setAdapter(carAdapter);
            } else {
                if (online) {
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Sorry, we do not have cars at the moment!.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Internet connection found.", Toast.LENGTH_LONG).show();
                }

                return;
            }

        }

    }


    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();

        }

    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
