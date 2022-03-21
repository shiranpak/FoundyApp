package com.example.foundyapp.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentGalleryBinding;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.example.foundyapp.ui.home.HomeViewModel;
import com.firebase.ui.auth.data.model.User;

import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    SwipeRefreshLayout swipeRefresh;
    MyRecyclerViewAdapter adapter;
    GalleryViewModel galleryViewModel;
    List<Post> myPosts;

    public GalleryFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gallery,container,false);
        RecyclerView rv = view.findViewById(R.id.my_posts_rv);
        swipeRefresh = view.findViewById(R.id.myposts_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostsList());

        myPosts = galleryViewModel.getData().getValue();
        adapter = new MyRecyclerViewAdapter(myPosts);
        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(adapter);

        galleryViewModel.getData().observe(getViewLifecycleOwner(), list1 -> refreshPostList());
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
            Post post = galleryViewModel.getData().getValue().get(position);
            holder.bind(post);
        }


        @Override
        public int getItemCount() {
            if(galleryViewModel.getData().getValue() == null){
                return 0;
            }
            return galleryViewModel.getData().getValue().size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView location;
        TextView category;
        TextView description;
        TextView userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.post_date_input_tv);
            location = itemView.findViewById(R.id.post_location_input_tv);
            category = itemView.findViewById(R.id.post_category_input_tv);
            description = itemView.findViewById(R.id.post_description_input_tv);
            userName = itemView.findViewById(R.id.post_username_textview);

        }

        public void bind(Post post){
            date.setText(post.getDate().toString());
            //location.setText(post.getLocation());
            category.setText(post.getCategory());
            description.setText(post.getDescription());
            userName.setText(post.getUserId());

            /*if (student.getAvatarUrl() != null) {
                Picasso.get()
                        .load(student.getAvatarUrl())
                        .into(avatarImv);
            }*/
        }
    }


}