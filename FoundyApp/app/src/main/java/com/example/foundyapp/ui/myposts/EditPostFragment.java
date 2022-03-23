package com.example.foundyapp.ui.myposts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.foundyapp.R;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    Bitmap imageBitmap = null;
    private Button saveBtn;
    private Button cancelBtn;
    private LatLng selectedLocation;
    private Long selectedDate;
    private PlacesClient placesClient;
    private ImageView itemImage;
    private Switch useMyLocation;
    AutocompleteSupportFragment autocompleteFragment;
    TextInputLayout dateTextLayout;
    TextInputEditText dateTextView, nameTextView, descriptionTextView;
    public EditPostFragment() {// Required empty public constructor
    }
    public static EditPostFragment newInstance(String param1, String param2) {
        EditPostFragment fragment = new EditPostFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        String pid = EditPostFragmentArgs.fromBundle(getArguments()).getPostid();
        progressBar = view.findViewById(R.id.edit_progress);
        saveBtn = view.findViewById(R.id.edit_submitBtn);
        cancelBtn = view.findViewById(R.id.edit_cancelBtn);
        cancelBtn.setOnClickListener(v->
               Navigation.findNavController(v).navigate(EditPostFragmentDirections.actionEditPostFragmentToNavGallery()));
        saveBtn.setOnClickListener(v -> savePost());


        Context mContext = getActivity();
        if (!Places.isInitialized()) {
            Places.initialize(mContext, getString(R.string.api_key));
        }
        placesClient = Places.createClient(getActivity());
        useMyLocation = (Switch) view.getRootView().findViewById(R.id.add_my_location_switch);
        useMyLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked == true) {

            }
        });

     autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.add_select_place_autocomplete_fragment);


        Model.instance.getPostById(pid, new Model.getPostByIdListener() {
            @Override
            public void onComplete(Post post) {

            }
        });




        return  view;

    }


    private void savePost() {



    }
}