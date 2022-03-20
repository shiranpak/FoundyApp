package com.example.foundyapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentGalleryBinding;
import com.example.foundyapp.ui.home.HomeViewModel;
import com.example.foundyapp.MyRecyclerViewAdapter;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ListView postsList;

    public GalleryFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gallery,container,false);
        RecyclerView rv = view.findViewById(R.id.my_posts_rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(new MyRecyclerViewAdapter());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}