package com.example.foundyapp.ui.MyDetails;

import android.media.Image;
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

        final ImageView textView = binding.imageView2;
        //myDetailsViewModel.getText().observe(getViewLifecycleOwner(), (textView::textView));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}