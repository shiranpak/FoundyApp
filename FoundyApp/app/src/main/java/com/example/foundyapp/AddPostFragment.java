package com.example.foundyapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.foundyapp.model.Category;
import com.example.foundyapp.model.City;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Category> categoriesList;
    ProgressBar progressBar;
    AutoCompleteTextView categoriesTextView;
    TextInputLayout dateTextLayout;
    TextInputEditText dateTextView, nameTextView, descriptionTextView;
    private Switch useMyLocation;
    private ImageView itemImage;
    private PlacesClient placesClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Button saveBtn;
    private LatLng selectedLocation;
    private Long selectedDate;
    private AutocompleteSupportFragment autocompleteFragment;
    private boolean postType;
    Bitmap imageBitmap = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        progressBar = view.findViewById(R.id.add_progress);
        progressBar.setVisibility(View.VISIBLE);

        saveBtn = view.findViewById(R.id.add_submitBtn);
        saveBtn.setOnClickListener(v -> savePost());

        nameTextView = view.findViewById(R.id.add_item_title_tf);
        descriptionTextView = view.findViewById(R.id.add_desc_text);

        postType = AddPostFragmentArgs.fromBundle(getArguments()).getPostType();

        Model.instance.getCategories(list -> {
            if (!list.isEmpty()) {
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
            progressBar.setVisibility(View.GONE);
        });

        dateTextLayout = view.findViewById(R.id.add_date_select);
        dateTextView = (TextInputEditText) dateTextLayout.getEditText();
        dateTextView.setShowSoftInputOnFocus(false);

        CalendarConstraints.Builder calendarCons = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.
                Builder.datePicker().setCalendarConstraints(calendarCons.build());

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        dateTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<Long>)
                        selection -> {
                            selectedDate = selection;
                            Date sDate = new Date(selectedDate);
                            String startDateString = DateFormat.format("dd/MM/yyyy", sDate).toString();
                            dateTextView.setText(startDateString);

                            Timestamp selectedDate = new Timestamp(sDate);

                        });
        Context mContext = getActivity();

        if (!Places.isInitialized()) {
            Places.initialize(mContext, getString(R.string.api_key));
        }
        placesClient = Places.createClient(getActivity());

        useMyLocation = (Switch) view.getRootView().findViewById(R.id.add_my_location_switch);

        useMyLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked == true) {
                showCurrentPlace();
            }
        });
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.add_select_place_autocomplete_fragment);


        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS).setCountries("ISR");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        // its not working rn because we need to add billing details
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                selectedLocation = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("Tag", "An error occurred: " + status);
            }
        });
        itemImage = view.findViewById(R.id.add_image_upload);
        itemImage.setOnClickListener(v -> {
            selectImage();
        });


        return view;
    }

    private void showCurrentPlace() {
        // Use fields to define the data types to return.
        progressBar.setVisibility(View.VISIBLE);
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ADDRESS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(MyApplication.context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        autocompleteFragment.setText(
                                placeLikelihood.getPlace().getAddress());

                        selectedLocation = placeLikelihood.getPlace().getLatLng();
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Toast.makeText(MyApplication.context, "Place not found, Try Again", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            getLocationPermission();
            progressBar.setVisibility(View.GONE);
        }
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {
                "Take Photo", "Choose from Library",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    openGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private static final int PICK_IMAGE = 100;
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri imageUri;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                try {
                    ParcelFileDescriptor parcelFileDescriptor =
                            MyApplication.getContext().getContentResolver().openFileDescriptor(imageUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    imageBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                    parcelFileDescriptor.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemImage.setImageURI(imageUri);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                itemImage.setImageBitmap(imageBitmap);
            }
        }
    }

    private void savePost() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        itemImage.setEnabled(false);
        Post post = new Post();

        post.setTitle(nameTextView.getText().toString());
        post.setDescription(descriptionTextView.getText().toString());
        post.setCategory(categoriesTextView.getText().toString());
        post.setLocation(selectedLocation);
        post.setDate(selectedDate);
        post.setType(postType);
        post.setIsDeleted(false);
        post.setUserId("todo");

        if (imageBitmap == null) {
            Model.instance.addPost(post, () -> {
                new DialogFragment().show(
                        getChildFragmentManager(), "");
            });
        }
        else
        {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Model.instance.saveImage(imageBitmap, post.getTitle() + currentTime + ".jpg", url -> {
                post.setImageUrl(url);
                Model.instance.addPost(post,()->{
                    new DialogFragment().show(
                            getChildFragmentManager(), "");
                });
            });
        }
    }
}