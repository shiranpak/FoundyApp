package com.example.foundyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foundyapp.model.UserSession;
import com.example.foundyapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends Activity {
    private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    UserSession session;




    public int checkData (String email, String password)
    {
        if(email.isEmpty() )
        {
            eEmail.setError("Please enter your email");
            return 0;
        }
        if(password.isEmpty() )
        {
            ePassword.setError("Please enter your password");
            return 0;
        }
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // taking instance of FirebaseAuth
        mAuth=FirebaseAuth.getInstance();
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

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Hello!",
                            Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressbar.setVisibility(View.GONE);
                    session.createUserLoginSession(email,password);
                    // if sign-in is successful
                    // intent to home activity
                    Intent intent
                            = new Intent(LoginActivity.this,
                            DrawerActivity
                                    .class);
                    startActivity(intent);
                    finish();


                } else {
                    // sign-in failed
                    try
                    {
                        throw task.getException();
                    }
                    // if user enters wrong email.
                    catch (FirebaseAuthInvalidUserException invalidEmail)
                    {
                      eEmail.setError("Email is not exist");
                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                    {
                        ePassword.setError("Wrong password");
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Failed to login",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    // hide the progress bar
                    progressbar.setVisibility(View.GONE);
                }
            });
            }

    }




}
