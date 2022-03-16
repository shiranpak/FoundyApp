package com.example.foundyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foundyapp.model.ModelFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth ;
    private EditText password;
    private EditText email;
    ModelFirebase db;
    private EditText reenterpass;
    private EditText fullname;
    private Button btn;
    private ProgressBar progressBar;

    boolean isEmail(String text) {

        return (!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //firebase instance
        mAuth = FirebaseAuth.getInstance();
        db=new ModelFirebase();

        password=findViewById(R.id.et_password);
        reenterpass=findViewById(R.id.et_repassword);
        fullname = findViewById(R.id.et_name);
        email=findViewById(R.id.et_email);
        btn=findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressbar);

        btn.setOnClickListener(v -> registernewuser());
    }

    public void registernewuser () {
        String str_name= fullname.getText().toString();
        String str_email = email.getText().toString();
        String str_pass= password.getText().toString();
        String str_repass= reenterpass.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        if(str_name.isEmpty()) {
            this.fullname.setError("Please enter your full name");
        }

        if(str_email.isEmpty()) {
            this.email.setError("Please enter email");
        }
        if(!isEmail(str_email))
        {
            email.setError("Invalid email");
        }

        if(str_pass.length()<6) {
            this.password.setError("Password must be at least 6 characters ");
        }
        if(str_repass.isEmpty() || str_pass.compareTo(str_repass)!=0) {
            this.reenterpass.setError("Please re type your password");
        }
        else {
            db.userRegistration(str_email,str_pass,getApplicationContext());

            progressBar.setVisibility(View.GONE);
        }

        
        
        
    }

//  public void toLoginActivity(){
//        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//        startActivity(intent);
//        finish();
//  }



    }



