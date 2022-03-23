package com.example.foundyapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
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

        public void bind(Post post) {
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