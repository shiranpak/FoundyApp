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

public class SearchByPlaceFragment extends Fragment {

    private MapFragment mapFragment;

    public SearchByPlaceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_by_place, container, false);

        setHasOptionsMenu(true);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);

        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.findings_losts_toggle_group);
        if (toggleGroup != null) {
            toggleGroup.addOnButtonCheckedListener(
                    (group, checkedId, isChecked) -> {
                        if (isChecked) {
                            if (group.getCheckedButtonId() == R.id.findings_toggle) {
                                mapFragment.ShowFindings();
                            } else {
                                mapFragment.ShowLosts();
                            }
                        }
                    });
        }
        return view;
    }
}