package com.example.foundyapp.ui.home;

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

import com.example.foundyapp.MyRecyclerViewAdapter;
import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    ListView postsList;
    FragmentHomeBinding binding;

    public HomeFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);
        RecyclerView rv = view.findViewById(R.id.home_posts_rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(new MyRecyclerViewAdapter());
        return view;
    }
}