package com.example.foundyapp.ui.myposts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.foundyapp.R;
import com.example.foundyapp.model.Category;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    List<Category> categoriesList;
    ProgressBar progressBar;
    Bitmap imageBitmap = null;
    private Button saveBtn;
    private Button cancelBtn;
    private Long selectedDate;
    private ImageView itemImage;
    private Switch useMyLocation;
    TextInputLayout dateTextLayout;
    TextInputEditText dateTextView, nameTextView, descriptionTextView;
    public EditPostFragment() {// Required empty public constructor
    }
    public static EditPostFragment newInstance(String param1, String param2) {
        EditPostFragment fragment = new EditPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        String pid = EditPostFragmentArgs.fromBundle(getArguments()).getPostid();

        //dateTextView=view.findViewById(R.id.add_date_select);
        nameTextView=view.findViewById(R.id.edit_item_title_tf);
        itemImage=view.findViewById(R.id.edit_image_upload);
        descriptionTextView=view.findViewById(R.id.edit_desc_text);
        progressBar = view.findViewById(R.id.edit_progress);
        saveBtn = view.findViewById(R.id.edit_submitBtn);
        cancelBtn = view.findViewById(R.id.edit_cancelBtn);


        Model.instance.getCategories(list -> {
            if (!list.isEmpty()) {
                //Categories
                categoriesList = (List<Category>) list;

                String[] categoriesListArr = new String[categoriesList.size()];
                for (int i = 0; i < categoriesList.size(); i++)
                    categoriesListArr[i] = categoriesList.get(i).getName();

                AutoCompleteTextView categoriesTextView = (AutoCompleteTextView) view.findViewById(R.id.category_selection_dp);

                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, categoriesListArr);

                categoriesTextView.setAdapter(categoriesAdapter);
            }
            progressBar.setVisibility(View.GONE);
        });

        Model.instance.getPostById(pid, new Model.getPostByIdListener() {
            @Override
            public void onComplete(Post post) {
                nameTextView.setText(post.getTitle());
                descriptionTextView.setText(post.getDescription());
                Picasso.get().load(post.getImageUrl()).
                        error(R.drawable.icons8_edit_image_127px_3).
                        into(itemImage);
                //dateTextView.setText(post.getFormattedDate());

            }
        });

       //default post object
        Post p = null;
        cancelBtn.setOnClickListener(v->
                Navigation.findNavController(v).navigate(EditPostFragmentDirections.actionEditPostFragmentToNavGallery()));
        saveBtn.setOnClickListener(v -> savePost(p));


        return  view;

    }


    private void savePost(Post post) {





    }
}