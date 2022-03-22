package com.example.foundyapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foundyapp.DrawerActivity;
import com.example.foundyapp.R;
import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.ModelFirebase;
import com.example.foundyapp.model.UserSession;


public class LoginActivity extends Activity {
    private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;
    private ProgressBar progressbar;
    UserSession session;

    public int checkData (String email, String password)
    {
        if(email.isEmpty() )
        {
            eEmail.setError("Please enter your email");
            progressbar.setVisibility(View.GONE);
            return 0;
        }
        if(password.isEmpty() )
        {
            ePassword.setError("Please enter your password");
            progressbar.setVisibility(View.GONE);

            return 0;
        }
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session= new UserSession(getApplicationContext());

        // initialising all views through id defined above
        eEmail = findViewById(R.id.et_email);
        ePassword = findViewById(R.id.et_password);
        eLogin = findViewById(R.id.btn_login);
       TextView regis = findViewById(R.id.text_register);
       progressbar=findViewById(R.id.progressBar);


       // Set on Click Listener on Sign-in button
        eLogin.setOnClickListener(v -> loginUserAccount());

        //Set on click Listener on registration text
        regis.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


    private void loginUserAccount (){
        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = eEmail.getText().toString();
        password = ePassword.getText().toString();
        // validations for input email and password
        if (checkData(email,password)!=0)
        {
            Model.instance.login(email,password,getApplicationContext());
            if(Model.instance.logincCheck()) {
                session.createUserLoginSession(password,email);
                toFeedActivity();
            }
        }

        progressbar.setVisibility(View.GONE);
    }

    public void toFeedActivity(){
        Intent intent=new Intent(getApplicationContext(), DrawerActivity.class);
        startActivity(intent);
        finish();
    }






}
