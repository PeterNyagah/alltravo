package flights.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import e.wolfsoft1.liberty_ui_kit.R;
import flights.activities.Flights_List_Activity;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import startup.adapter.Spinner_ClassAdapter;
import startup.adapter.Spinner_Cusine_DataAdapter;
import startup.model.ItemData1;
import utils.AppConstants;
import utils.DUtils;
import utils.JSONParser;
import utils.customfonts.MyTextView_Roboto_Regular;


public class Flight_Fragment extends Fragment {

    private TextView textView_round, textView_oneway, Edittext_dep, Edittext_return;
    private EditText Edittext_to, Edittext_from;
    private LinearLayout trip_select, linear1, linear2, layout_return;

    public LinearLayout continue_booking;
    private Spinner spinner, spinner1;
    private int mYear, mMonth, mDay;


    private boolean way = false;
    private SpinnerDialog fromDialog;
    private SpinnerDialog toDialog;
    private ArrayList<String> places = new ArrayList<>();

    private String passangers = "", level = "";

    private ImageView img_back;
    private MyTextView_Roboto_Regular title;
    private MyTextView_Roboto_Regular details_preview;

    private TextInputLayout Edittext_from_layout, Edittext_to_layout;

    private static String url_places = AppConstants.URL_PLACES;
    JSONArray jsonArray = null;
    private boolean status = false;
    private boolean online = false;
    private JSONParser jParser = new JSONParser();


    private static ProgressDialog progressDialog;

    Date date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flight_fragment_layout, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        init(view);
        setListeners();

        new GetPlaces().execute();

        img_back = view.findViewById(R.id.img_back);
        title = view.findViewById(R.id.title);
        spinner = (Spinner) view.findViewById(R.id.spinner_dep);
        spinner1 = (Spinner) view.findViewById(R.id.spinner_class);

        final ArrayList<ItemData1> list = new ArrayList<>();

        title.setText("Flights");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        list.add(new ItemData1("1"));
        list.add(new ItemData1("2"));
        list.add(new ItemData1("3"));
        list.add(new ItemData1("4"));
        Spinner sp = (Spinner) view.findViewById(R.id.spinner_dep);
        Spinner_Cusine_DataAdapter adapter = new Spinner_Cusine_DataAdapter(getActivity(), R.layout.itemlist_dep, R.id.textView_dep, list);
        sp.setAdapter(adapter);
        spinner.setAdapter(adapter);


        final ArrayList<ItemData1> list1 = new ArrayList<>();

        list1.add(new ItemData1("Economy"));
        list1.add(new ItemData1("Premium Economy"));
        list1.add(new ItemData1("Business"));
        list1.add(new ItemData1("First Class"));
        Spinner sp1 = (Spinner) view.findViewById(R.id.spinner_class);
        Spinner_ClassAdapter adapter1 = new Spinner_ClassAdapter(getActivity(), R.layout.itemlist_class, R.id.textview_class, list1);
        sp1.setAdapter(adapter1);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                level = list1.get(position).getText();
                details_preview.setText(passangers + " Passanger(s) : " + level);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                return;
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                passangers = list.get(position).getText().trim();
                details_preview.setText(passangers + " Passanger(s) : " + level);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                return;
            }

        });

        return view;
    }

    private void setListeners() {
        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear1.setBackgroundResource(R.drawable.top_leftcorner);
                textView_round.setTextColor(Color.parseColor("#ffffff"));
                way = false;

                linear2.setBackgroundResource(R.drawable.top_rightcorner_white);
                textView_oneway.setTextColor(Color.parseColor("#837a7a"));

                layout_return.setVisibility(View.VISIBLE);


            }
        });
        linear2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        linear2.setBackgroundResource(R.drawable.top_rightcorner);
                        textView_oneway.setTextColor(Color.parseColor("#ffffff"));
                        way = true;

                        linear1.setBackgroundResource(R.drawable.top_leftcorner_white);
                        textView_round.setTextColor(Color.parseColor("#837a7a"));

                        layout_return.setVisibility(View.GONE);

                    }
                }
        );


        Edittext_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                monthOfYear = monthOfYear + 1;

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateInString = dayOfMonth + "-" + monthOfYear + "-" + year;

                                try {

                                    date = formatter.parse(dateInString);
                                    System.out.println(date);
                                    System.out.println(formatter.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                                String day = (String) DateFormat.format("dd", date); // 20
                                String monthString = (String) DateFormat.format("MMM", date); // Jun

                                Edittext_dep.setText(dateInString);


                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        Edittext_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                monthOfYear = monthOfYear + 1;

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateInString = dayOfMonth + "-" + monthOfYear + "-" + year;

                                try {

                                    date = formatter.parse(dateInString);
                                    System.out.println(date);
                                    System.out.println(formatter.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                                String day = (String) DateFormat.format("dd", date); // 20
                                String monthString = (String) DateFormat.format("MMM", date); // Jun


                                Edittext_return.setText(dateInString);


                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        continue_booking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (validation()) {

                    Intent flightlist = new Intent(getActivity(), Flights_List_Activity.class);
                    flightlist.putExtra("FROM", Edittext_from.getText().toString());
                    flightlist.putExtra("TO", Edittext_to.getText().toString());
                    flightlist.putExtra("WAY", way);
                    flightlist.putExtra("CLASS", level);
                    flightlist.putExtra("PASSANGERS", passangers);
                    flightlist.putExtra("DEPARTURE_DATE", Edittext_dep.getText().toString());
                    flightlist.putExtra("RETURN_DATE", Edittext_return.getText().toString());
                    startActivity(flightlist);
                }

            }
        });

        Edittext_from.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fromDialog.showSpinerDialog();
                }
            }
        });

        Edittext_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDialog.showSpinerDialog();
            }
        });

        Edittext_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDialog.showSpinerDialog();
            }
        });

        Edittext_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    toDialog.showSpinerDialog();
                }
            }
        });


        fromDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Edittext_from.setText(item);
            }
        });

        toDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Edittext_to.setText(item);
            }
        });
    }

    public void init(View view) {


        fromDialog = new SpinnerDialog(getActivity(), places, "Select where From", R.style.DialogAnimations_SmileWindow);// With 	Animation
        toDialog = new SpinnerDialog(getActivity(), places, "Select where To", R.style.DialogAnimations_SmileWindow);// With 	Animation


        textView_round = view.findViewById(R.id.textView_round);
        details_preview = view.findViewById(R.id.details_preview);
        textView_oneway = view.findViewById(R.id.textView_oneway);
        Edittext_dep = view.findViewById(R.id.Edittext_dep);
        Edittext_return = view.findViewById(R.id.Edittext_return);
        Edittext_from = view.findViewById(R.id.Edittext_from);
        Edittext_to = view.findViewById(R.id.Edittext_to);
        trip_select = view.findViewById(R.id.trip_select);
        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        layout_return = view.findViewById(R.id.layout_return);


        Edittext_from_layout = view.findViewById(R.id.Edittext_from_layout);
        Edittext_to_layout = view.findViewById(R.id.Edittext_to_layout);

        continue_booking = view.findViewById(R.id.continue_booking);


        layout_return.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Edittext_dep.setText(mDay + "-" + mMonth + "-" + mYear);
        Edittext_return.setText(mDay + "-" + mMonth + "-" + mYear);

        linear2.setBackgroundResource(R.drawable.top_rightcorner);
        textView_oneway.setTextColor(Color.parseColor("#ffffff"));
        way = true;

        linear1.setBackgroundResource(R.drawable.top_leftcorner_white);
        textView_round.setTextColor(Color.parseColor("#837a7a"));


    }

    public class GetPlaces extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            places.clear();
            progressDialog.setMessage("Getting Ready...");
            showDialog();
        }


        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "list");
            // getting JSON string from URL

            online = DUtils.isOnline(getActivity());
            if (online) {

                JSONObject json = new JSONObject();
                try {
                    json = jParser.makeHttpRequest(url_places, "POST", params);
                } catch (Exception e) {
                    Log.d("ERROR LOG", "hi therererererre");
//                    Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
                }

                try {
                    jsonArray = json.getJSONArray("places");
                    Log.d("ERROR task Details", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        status = true;

                        JSONObject c = jsonArray.getJSONObject(i);
                        places.add(c.getString("name"));
                    }

                } catch (JSONException e) {
                    Log.d("ERROR LOG", "ueufhuwew");
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

            if (status) {


            } else {
                if (online) {
                    Toast.makeText(getActivity(), "Your internet is slow, Try again later!.", Toast.LENGTH_LONG).show();
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

    public boolean validation() {

        boolean result = false;

        if (Edittext_to.getText().toString().equals("") || Edittext_from.getText().toString().equals("")) {
            if (Edittext_from.getText().toString().equals("")) {
                Edittext_from_layout.setErrorEnabled(true);
                Edittext_from_layout.setError("Field is required");
            }

            if (Edittext_to.getText().toString().equals("")) {
                Edittext_to_layout.setErrorEnabled(true);
                Edittext_to_layout.setError("Field is required");
            }
        } else if (Edittext_to.getText().toString().equals(Edittext_from.getText().toString())) {

            Edittext_to_layout.setErrorEnabled(true);
            Edittext_to_layout.setError("Invalid route");
            Edittext_from_layout.setErrorEnabled(true);
            Edittext_from_layout.setError("Invalid route");
            Toast.makeText(getActivity(), "Departure and destination cannot be the same!", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }
}
