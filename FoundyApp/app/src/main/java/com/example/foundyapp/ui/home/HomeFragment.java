package com.example.foundyapp.ui.home;

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

import com.example.foundyapp.R;
import com.example.foundyapp.databinding.FragmentHomeBinding;
import com.example.foundyapp.model.Post;

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


    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

        public MyRecyclerViewAdapter(){
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            /*Post post = viewModel.getValue().get(position);
            holder.bind(student);*/
        }


        @Override
        public int getItemCount() {
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView date;
            TextView location;
            TextView category;
            TextView description;
            TextView userName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.post_date_tv);
                location = itemView.findViewById(R.id.post_location_tv);
                category = itemView.findViewById(R.id.post_category_tv);
                description = itemView.findViewById(R.id.post_description_tv);
                userName = itemView.findViewById(R.id.post_username_textview);

            }

            void bind(Post post){
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
}