package com.example.foundyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundyapp.model.Category;
import com.example.foundyapp.model.City;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.example.foundyapp.model.PostViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedSearchFragment extends Fragment {

    private TextInputLayout fromDateTextLayout,toDateTextLayout;
    private TextInputEditText fromDateTextView,toDateTextView;
    private AutoCompleteTextView categoriesTextView,citiesTextView;
    List<Category> categoriesList;
    List<City> citiesList;
    ProgressBar progressBar;
    Button searchBtn;
    Geocoder gcd;
    boolean currentType = true; //true = findings
    private City selectedCity = null;
    private Category selectedCategory = null;
    PostViewModel postViewModel;
    Long startDate;
    Long endDate;
    public AdvancedSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_advanced_search, container, false);
        gcd = new Geocoder(MyApplication.context, Locale.getDefault());

        progressBar = view.findViewById(R.id.adv_progress);
        progressBar.setVisibility(View.VISIBLE);
        searchBtn = view.findViewById(R.id.adv_search_btn);
        searchBtn.setOnClickListener(v -> searchForPosts(v));
        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.adv_findings_losts_toggle_group);
        if (toggleGroup != null) {
            toggleGroup.addOnButtonCheckedListener(
                    (group, checkedId, isChecked) -> {
                        if(isChecked) {
                            if (checkedId == R.id.adv_findings_toggle) {
                                currentType = true;
                            } else if (checkedId == R.id.adv_losts_toggle) {
                                currentType = false;
                            }
                        }
                    });
        }
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
                    categoriesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                            selectedCategory = categoriesList.get(position);
                        }
                    });
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
                    citiesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedCity = citiesList.get(position);
                        }
                    });
                }
            }
            progressBar.setVisibility(View.GONE);
        });
        postViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
            /*    progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            */}
        });
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), studentListLoadingState -> {
            if (studentListLoadingState == Model.ListLoadingState.loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "");
            }
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
        Calendar now = Calendar.getInstance();
        CalendarConstraints.Builder calendarCons = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setCalendarConstraints(calendarCons.build());

        materialDateBuilder.setSelection(new Pair(now.getTimeInMillis(), now.getTimeInMillis()));

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

        //default dates
        startDate = endDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        fromDateTextView.setText(DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString());
        toDateTextView.setText(DateFormat.format("dd/MM/yyyy",  new Date(endDate)).toString());

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>)
                        selection -> {

                    startDate = selection.first;
                    endDate = selection.second;
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
    public void searchForPosts(View view) {
        if (selectedCity == null || selectedCategory == null || startDate == null || endDate == null) {
            Toast.makeText(MyApplication.context, "Some details are missing", Toast.LENGTH_LONG).show();
            return;
        } else {
            String cityName = getSelectedCity();
            if (TextUtils.isEmpty(cityName)) {
                Toast.makeText(MyApplication.context, "Unexpected Problem :( (select different city)", Toast.LENGTH_LONG).show();
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                Model.instance.searchForPosts(cityName, selectedCategory.getName(), currentType, startDate, endDate, new Model.searchForPostsListener() {
                    @Override
                    public void onComplete(List<Post> posts) {
                        if(posts == null || posts.isEmpty())
                        {
                            Toast.makeText(MyApplication.context, "0 posts found", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Navigation.findNavController(view).navigate(SearchPostsFragmentDirections.actionGlobalSearchPostsFragment(posts.toArray(new Post[0])));
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
    public String getSelectedCity(){
        if (selectedCity != null) {
            double lat = selectedCity.getLocation().latitude;
            double lng = selectedCity.getLocation().longitude;
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lat, lng, 1);

                if (addresses.size() > 0) {
                     return addresses.get(0).getLocality();
                }
                else {
                     return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}