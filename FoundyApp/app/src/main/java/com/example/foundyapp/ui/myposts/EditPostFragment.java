package com.example.foundyapp.ui.myposts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.foundyapp.DialogFragment;
import com.example.foundyapp.MyApplication;
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

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EditPostFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    AutoCompleteTextView categoriesTextView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_OK=-1;
    private static final int PICK_IMAGE =2 ;

    List<Category> categoriesList;
    ProgressBar progressBar;
    Bitmap imageBitmap = null;
    private Button saveBtn;
    private Button cancelBtn;
    private ImageView itemImage;
    private Switch useMyLocation;
    TextInputEditText  nameTextView, descriptionTextView;
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

        nameTextView=view.findViewById(R.id.edit_item_title_tf);
        itemImage=view.findViewById(R.id.edit_image_upload);
        descriptionTextView=view.findViewById(R.id.edit_desc_text);
        progressBar = view.findViewById(R.id.edit_progress);
        saveBtn = view.findViewById(R.id.edit_submitBtn);
        categoriesTextView = (AutoCompleteTextView) view.findViewById(R.id.category_selection_dp);
        cancelBtn = view.findViewById(R.id.edit_cancelBtn);
        progressBar.setVisibility(View.VISIBLE);

        Model.instance.getCategories(list -> {
            if (!list.isEmpty()) {
                //Categories
                categoriesList = (List<Category>) list;

                String[] categoriesListArr = new String[categoriesList.size()];
                for (int i = 0; i < categoriesList.size(); i++)
                    categoriesListArr[i] = categoriesList.get(i).getName();


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
                if(post.getImageUrl()!=null){
                Picasso.get().load(post.getImageUrl()).
                        error(R.drawable.icons8_edit_image_127px_3).
                        into(itemImage);}
                else
                    Picasso.get().load(R.drawable.icons8_edit_image_127px_3).into(itemImage);
                //int i = categoriesList.indexOf(post.getCategory());

            }
        });
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicture();
            }
        });
        Post p = new Post();

        p.setPostId(pid);
        p.setDescription(descriptionTextView.getText().toString());
        p.setTitle(nameTextView.getText().toString());


        cancelBtn.setOnClickListener(v->
                Navigation.findNavController(v).navigate(EditPostFragmentDirections.actionEditPostFragmentToNavGallery()));
        saveBtn.setOnClickListener(v -> {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            //post.setLastUpdated(currentTime);
            if(imageBitmap==null){
                Model.instance.editMyPost(p);}
            else
            {
                Model.instance.saveImage(imageBitmap, p.getTitle() + currentTime + ".jpg", url -> {
                    p.setImageUrl(url);
                    Model.instance.editMyPost(p);
                });
            }
            Navigation.findNavController(v).navigate(EditPostFragmentDirections.actionEditPostFragmentToNavGallery());
        });


        return  view;

    }




    /*Picture*/
    public void setPicture(){
        final CharSequence[] items = {
                "Take Photo", "Choose from Library",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    openCamera();

                } else if (items[item].equals("Choose from Library")) {
                    openGallery();

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void openCamera() {
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    Uri imageUri;
    public void onActivityResult (int requestCode , int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                itemImage.setImageBitmap(imageBitmap);
            }
        } else {
            if (requestCode == PICK_IMAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data.getData();
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                MyApplication.getContext().getContentResolver().openFileDescriptor(imageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        imageBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                        parcelFileDescriptor.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    itemImage.setImageURI(imageUri);
                }
            }
        }
    }
}