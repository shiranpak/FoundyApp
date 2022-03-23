package com.example.foundyapp.ui.MyDetails;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentMydetailsBinding;
import com.example.foundyapp.model.Model;
import com.squareup.picasso.Picasso;

public class MyDetailsFragment extends Fragment {

    private FragmentMydetailsBinding binding;
    Button edit_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyDetailsViewModel myDetailsViewModel =
                new ViewModelProvider(this).get(MyDetailsViewModel.class);

        binding = FragmentMydetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView fullName = binding.etFullName;
        final TextView email = binding.etEmail;
        final ImageView avatar = binding.uiDetails;
        final String url;

        myDetailsViewModel.getFullName().observe(getViewLifecycleOwner(), (fullName::setText));
        myDetailsViewModel.getEmail().observe(getViewLifecycleOwner(), (email::setText));
        myDetailsViewModel.getImage().observe(getViewLifecycleOwner(), imageUrl ->
        Picasso.get().load(imageUrl).error(R.drawable.icon_user).into(avatar));
        edit_btn = binding.editBtn;
        edit_btn.setOnClickListener((v)-> Navigation.findNavController(v).navigate(MyDetailsFragmentDirections.actionMyDetailsFragmentToEditUserFragment()));

        return root;
    }



    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}