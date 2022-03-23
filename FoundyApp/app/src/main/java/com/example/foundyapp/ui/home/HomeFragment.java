package com.example.foundyapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentHomeBinding;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    SwipeRefreshLayout swipeRefresh;
    HomeViewModel homeViewModel;
    List<Post> allPostList;
    MyRecyclerViewAdapter adapter;
    public HomeFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);
        RecyclerView rv = view.findViewById(R.id.home_posts_rv);
        swipeRefresh = view.findViewById(R.id.home_posts_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostsList());

        allPostList = homeViewModel.getData().getValue();
        adapter = new MyRecyclerViewAdapter(allPostList);
        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(adapter);

        homeViewModel.getData().observe(getViewLifecycleOwner(), list1 -> refreshPostList());
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.ListLoadingState.loading);
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {
            if (postListLoadingState == Model.ListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }


    private void refreshPostList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context context;
        private List<Post> myPostslist;

        public MyRecyclerViewAdapter(List<Post> list) {
            this.myPostslist = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = homeViewModel.getData().getValue().get(position);
            holder.bind(post);
        }


        @Override
        public int getItemCount() {
            if(homeViewModel.getData().getValue() == null){
                return 0;
            }
            return homeViewModel.getData().getValue().size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView location;
        TextView category;
        TextView description;
        ImageView userProfileImage;
        TextView userName;
        ImageView postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.post_date_input_tv);
            location = itemView.findViewById(R.id.post_location_input_tv);
            category = itemView.findViewById(R.id.post_category_input_tv);
            description = itemView.findViewById(R.id.post_description_input_tv);
            userProfileImage = itemView.findViewById(R.id.post_userprofile_imageview);
            userName = itemView.findViewById(R.id.post_username_textview);
            postImage=itemView.findViewById(R.id.post_imageview);

        }

        public void bind(Post post){
            date.setText(post.getFormattedDate());
            location.setText(post.getAddress());
            category.setText(post.getCategory());
            description.setText(post.getDescription());
            userName.setText(post.getUserId());

            if (post.getImageUrl() != null) {
                Picasso.get()
                        .load(post.getImageUrl())
                        .into(postImage);
            }

        }
    }


}