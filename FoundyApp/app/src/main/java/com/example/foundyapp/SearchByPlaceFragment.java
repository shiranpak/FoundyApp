package com.example.foundyapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchByPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchByPlaceFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private MapFragment mapFragment;

    public SearchByPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchByPlaceFragment.
     */
    public static SearchByPlaceFragment newInstance(String param1, String param2) {
        SearchByPlaceFragment fragment = new SearchByPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_search_by_place, container, false);

        setHasOptionsMenu(true);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);

        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.findings_losts_toggle_group);
        if (toggleGroup != null) {
            toggleGroup.addOnButtonCheckedListener(
                    (group, checkedId, isChecked) -> {
                        if (checkedId == R.id.findings_toggle) {
                            mapFragment.ShowFindings();
                        }else if(checkedId == R.id.losts_toggle){
                            mapFragment.ShowLosts();
                        }
                    });
        }
        return view;
    }
}