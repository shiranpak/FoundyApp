package com.example.foundyapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.example.foundyapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchPostsFragment extends Fragment {
    Post[] posts;
    public SearchPostsFragment() {
        // Required empty public constructor
    }
    MyRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_posts, container, false);
        setHasOptionsMenu(true);
        posts = SearchPostsFragmentArgs.fromBundle(getArguments()).getPostsArg();

        RecyclerView rv = view.findViewById(R.id.found_posts_rv);
        rv.setHasFixedSize(true);

        adapter = new MyRecyclerViewAdapter(posts);
        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(adapter);

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context context;
        private Post[] myPostslist;

        public MyRecyclerViewAdapter(Post[] list) {
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
            Post post = posts[position];
            holder.bind(post);
        }


        @Override
        public int getItemCount() {
            if(posts == null){
                return 0;
            }
            return posts.length;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        TextView location;
        TextView category;
        TextView description;
        TextView userName;
        ImageView userProfileImage;
        ImageView postImage;
        ImageButton contactBtn;
        TextView contactInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.post_title_input_tv);
            date = itemView.findViewById(R.id.post_date_input_tv);
            location = itemView.findViewById(R.id.post_location_input_tv);
            category = itemView.findViewById(R.id.post_category_input_tv);
            description = itemView.findViewById(R.id.post_description_input_tv);
            userProfileImage = itemView.findViewById(R.id.post_userprofile_imageview);
            userName = itemView.findViewById(R.id.post_username_textview);
            postImage=itemView.findViewById(R.id.post_imageview);
            contactBtn = itemView.findViewById(R.id.post_contact_imageButton);
            contactBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    contactInfo.setVisibility(View.VISIBLE);
                }
            } );
            contactInfo = itemView.findViewById(R.id.post_contact_info_tv);

        }

        public void bind(Post post){
            title.setText(post.getTitle());
            date.setText(post.getFormattedDate());
            location.setText(post.getAddress());
            category.setText(post.getCategory());
            description.setText(post.getDescription());
            LiveData<User> user = Model.instance.getUser(post.getUserId());
            user.observe(getViewLifecycleOwner(),liveDataUser -> {
                userName.setText(liveDataUser.getFullName());
                contactInfo.setText(liveDataUser.getEmail());
                Picasso.get()
                        .load(user.getValue().getImage())
                        .into(userProfileImage);
            });

            if (post.getImageUrl() != null) {
                Picasso.get()
                        .load(post.getImageUrl())
                        .into(postImage);
            }
        }
    }
}