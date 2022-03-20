package com.example.foundyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.example.foundyapp.model.PostViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment  {
    FirebaseFirestore db;

    private AutocompleteSupportFragment autocompleteFragment;
    private final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // The entry point to the Places API.
    private PlacesClient placesClient;
    PostViewModel postViewModel;

    private Switch useMyLocation;

    // A default location (Israel, Jerusalem) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(31.771959, 35.217018);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    ProgressBar progressBar;

    private List<Marker> lostMarkers = new ArrayList<>(), foundMarkers=new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Nullable
                @Override
                public View getInfoContents(@NonNull Marker marker) {
                    // Inflate the layouts for the info window, title and snippet.
                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                            (FrameLayout) getView().findViewById(R.id.map), false);
                             TextView title = infoWindow.findViewById(R.id.title);
                    title.setText(marker.getTitle());

                    TextView snippet = infoWindow.findViewById(R.id.snippet);
                    snippet.setText(marker.getSnippet());

                    /*View infoWindow = getLayoutInflater().inflate(R.layout.custom_infowindow, null);

                    TextView title = infoWindow.findViewById(R.id.post_title_iw);
                    title.setText(marker.getTitle());

                    TextView snippet = infoWindow.findViewById(R.id.post_desc_iw);
                    snippet.setText(marker.getSnippet());

                    TextView user = infoWindow.findViewById(R.id.post_userInfo_iw);
                    user.setText(marker.get());*/

                    return infoWindow;
                }

                @Nullable
                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    return null;
                }
            });

            Context mContext = getActivity();

            if (!Places.isInitialized()) {
                Places.initialize(mContext, getString(R.string.api_key));
            }

            map.setOnInfoWindowClickListener(marker -> {

            });
            placesClient = Places.createClient(getActivity());
            autocompleteFragment = (AutocompleteSupportFragment)
                    getParentFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


            autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS).setCountries("ISR");
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the response.
            // its not working rn because we need to add billing details
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    if(map != null)
                    {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                (place.getLatLng()), DEFAULT_ZOOM));
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.i("Tag", "An error occurred: " + status);
                }
            });
            postViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
                @Override
                public void onChanged(List<Post> posts) {
                    progressBar.setVisibility(View.VISIBLE);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Post post :posts){
                        if (post != null && post.getLocation() != null){

                            LatLng location = post.getLocation();
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(post.getTitle())
                                    .snippet(post.getDescription())
                                    .draggable(false));

                            builder.include(marker.getPosition());
                            if(post.isType()) {
                                foundMarkers.add(marker);
                                marker.setVisible(true);
                                marker.showInfoWindow();
                            }
                            else
                            {
                                lostMarkers.add(marker);
                                marker.setVisible(false);
                                marker.hideInfoWindow();
                            }
                        }
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                        map.animateCamera(cameraUpdate);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
            Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), studentListLoadingState -> {
                if (studentListLoadingState == Model.ListLoadingState.loading){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG","");
                }

            });
            updateLocationUI();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        db = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.search_place_progress);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        useMyLocation = (Switch) view.getRootView().findViewById(R.id.use_my_location_toggle);

        useMyLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked == true){
                // Prompt the user for permission.
                getLocationPermission();

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                showCurrentPlace();
            }
        });
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mapFragment.getActivity());

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission")
            final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();

                    // Set the count, handling cases where less than 5 entries are returned.
                    int count;
                    if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                        count = likelyPlaces.getPlaceLikelihoods().size();
                    } else {
                        count = M_MAX_ENTRIES;
                    }

                    int i = 0;
                    likelyPlaceNames = new String[count];
                    likelyPlaceAddresses = new String[count];
                    likelyPlaceAttributions = new List[count];
                    likelyPlaceLatLngs = new LatLng[count];

                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                        likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                        likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                .getAttributions();
                        likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                        i++;
                        if (i > (count - 1)) {
                            break;
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    MapFragment.this.openPlacesDialog();
                }
                else {
                    Log.e(TAG, "Exception: %s", task.getException());
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // The "which" argument contains the position of the selected item.
            LatLng markerLatLng = likelyPlaceLatLngs[which];
            String markerSnippet = likelyPlaceAddresses[which];
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map.addMarker(new MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet));

            // Position the map's camera at the location of the marker.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM));
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
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

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public void ShowLosts(){
        for (Marker marker:foundMarkers)
        {
            marker.setVisible((false));
            marker.hideInfoWindow();
        }
        for (Marker marker:lostMarkers)
        {
            marker.setVisible((true));
            marker.showInfoWindow();
        }
    }

    public void ShowFindings(){
        for (Marker marker:foundMarkers)
        {
            marker.setVisible((true));
            marker.showInfoWindow();
        }
        for (Marker marker:lostMarkers)
        {
            marker.setVisible((false));
            marker.hideInfoWindow();
        }
    }
}