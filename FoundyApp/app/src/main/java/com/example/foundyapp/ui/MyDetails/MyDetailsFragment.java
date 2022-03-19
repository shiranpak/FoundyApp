package com.example.foundyapp.ui.MyDetails;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.foundyapp.databinding.FragmentMydetailsBinding;

public class MyDetailsFragment extends Fragment {

    private FragmentMydetailsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyDetailsViewModel myDetailsViewModel =
                new ViewModelProvider(this).get(MyDetailsViewModel.class);

        binding = FragmentMydetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView fullName = binding.etFullName;
        final TextView email = binding.etEmail;
        myDetailsViewModel.getFullName().observe(getViewLifecycleOwner(), (fullName::setText));
        myDetailsViewModel.getEmail().observe(getViewLifecycleOwner(), (email::setText));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}