package com.example.foundyapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundyapp.model.Category;
import com.example.foundyapp.model.City;
import com.example.foundyapp.model.Model;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AdvancedSearchFragment extends Fragment {

    private TextInputLayout fromDateTextLayout,toDateTextLayout;
    private TextInputEditText fromDateTextView,toDateTextView;
    private AutoCompleteTextView categoriesTextView,citiesTextView;
    List<Category> categoriesList;
    List<City> citiesList;
    ProgressBar progressBar;

    public AdvancedSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        progressBar = view.findViewById(R.id.adv_progress);
        progressBar.setVisibility(View.VISIBLE);

        Model.instance.getAllData(list -> {
            if (!list.isEmpty())
            {
                if (list.get(0) instanceof Category) {
                    //Categories
                    categoriesList = (List<Category>) list;

                    String[] categoriesListArr = new String[categoriesList.size()];
                    for (int i = 0; i < categoriesList.size(); i++)
                        categoriesListArr[i] = categoriesList.get(i).getName();

                    categoriesTextView = (AutoCompleteTextView) view.findViewById(R.id.category_selection_dp);

                    ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, categoriesListArr);

                    categoriesTextView.setAdapter(categoriesAdapter);
                }
                else {
                    //Cities
                    citiesList = (List<City>) list;

                    String[] citiesListArr = new String[citiesList.size()];
                    for (int i = 0; i < citiesList.size(); i++)
                        citiesListArr[i] = citiesList.get(i).getName();

                    citiesTextView = (AutoCompleteTextView) view.findViewById(R.id.city_selection_dp);

                    ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, citiesListArr);

                    citiesTextView.setAdapter(citiesAdapter);
                }
            }
            progressBar.setVisibility(View.GONE);
        });

        fromDateTextLayout = view.findViewById(R.id.from_date_tf);
        fromDateTextView = (TextInputEditText) fromDateTextLayout.getEditText();
        fromDateTextView.setShowSoftInputOnFocus(false); //disable keyboard

        toDateTextLayout = view.findViewById(R.id.to_date_tf);
        toDateTextView = (TextInputEditText) toDateTextLayout.getEditText();
        toDateTextView.setShowSoftInputOnFocus(false); //disable keyboard


        // now create instance of the material date picker
        // builder make sure to add the "dateRangePicker"
        // which is material date range picker which is the
        // second type of the date picker in material design
        // date picker we need to pass the pair of Long
        // Long, because the start date and end date is
        // store as "Long" type value
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        // now define the properties of the
        // materialDateBuilder
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        fromDateTextView.setOnClickListener(
                v -> {
                    // getSupportFragmentManager() to
                    // interact with the fragments
                    // associated with the material design
                    // date picker tag is to get any error
                    // in logcat
                    materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                });
        toDateTextView.setOnClickListener(
                v -> {
                    materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>)
                        selection -> {

                    Long startDate = selection.first;
                    Long endDate = selection.second;
                    Date sDate = new Date(startDate);
                    Date eDate = new Date(endDate);
                    String startDateString = DateFormat.format("dd/MM/yyyy", sDate).toString();
                    String endDateString = DateFormat.format("dd/MM/yyyy", eDate).toString();
                    fromDateTextView.setText(startDateString);
                    toDateTextView.setText(endDateString);


                    //TODO: for firebase
                    Timestamp startDateUtc =new Timestamp(sDate);
                });


        return view;
    }


}