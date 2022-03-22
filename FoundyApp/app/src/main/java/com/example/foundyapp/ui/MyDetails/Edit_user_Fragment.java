package com.example.foundyapp.ui.MyDetails;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foundyapp.MyApplication;
import com.example.foundyapp.R;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.User;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;


public class Edit_user_Fragment extends Fragment {
    private static final int PICK_IMAGE =2 ;
    private String userid;
 Button cancelBtn;
 Button saveBtn;
 EditText nameEt;
 ImageView avatar;
 Bitmap imageBitmap;
 TextView email;
 ProgressBar progressBar;
 ImageButton imageButton;
 static final int REQUEST_IMAGE_CAPTURE = 1;
 static final int RESULT_OK=-1;


    public Edit_user_Fragment() {
        // Required empty public constructor
    }

    public static Edit_user_Fragment newInstance(String param1, String param2) {
        Edit_user_Fragment fragment = new Edit_user_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        progressBar=view.findViewById(R.id.progressBarEdit);
        imageButton=view.findViewById(R.id.btn_image);
        avatar=view.findViewById(R.id.user_image);
        nameEt = view.findViewById(R.id.edit_fullName);
        email=view.findViewById(R.id.edit_mail);
        Model.instance.getUser(new Model.GetUserById() {
            @Override
            public void onComplete(User user) {
                nameEt.setText(user.getFullName());
                email.setText(user.getEmail());
                if(user.getImage()!=null){
                    Picasso.get().load(user.getImage()).error(R.drawable.icon_user).into(avatar);
                }
            }
        });
        //creates a default user
        imageButton.setOnClickListener((v)->
        {
            setPicture();
        });

        cancelBtn = (Button) view.findViewById(R.id.edit_cancel_button);
        cancelBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });

        saveBtn=(Button) view.findViewById(R.id.edit_save_btn);
        saveBtn.setOnClickListener((v)->{
           User u = new User(nameEt.getText().toString(),email.getText().toString(),"1");
            progressBar.setVisibility(View.VISIBLE);
            if(imageBitmap!=null) {
                Model.instance.saveUserImage(imageBitmap, u.getEmail() + ".jpg", url -> {
                    u.setImage(url);
                    Model.instance.editUser(u, new Model.EditUserListener() {@Override public void onComplete() { }});
                });
                Navigation.findNavController(v).navigate(Edit_user_FragmentDirections.actionEditUserFragmentToMyDetailsFragment());
            }
            else {
                Model.instance.editUser(u, new Model.EditUserListener() {
                    @Override
                    public void onComplete() {
                    }
                });
                //progressBar.setVisibility(View.GONE);
                Navigation.findNavController(v).navigate(Edit_user_FragmentDirections.actionEditUserFragmentToMyDetailsFragment());
            }
        });



        return view;
    }

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
    public void onActivityResult (int requestCode , int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            if(resultCode==RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitmap);
            }
        }
      else {
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
                    avatar.setImageURI(imageUri);
                }
            }
        }
    }


}