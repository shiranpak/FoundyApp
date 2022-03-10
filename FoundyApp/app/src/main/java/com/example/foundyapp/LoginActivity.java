package com.example.foundyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

   private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;

    boolean isEmail(String text) {

        return (!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

    public void checkData (String email, String password)
    {
        if(email.isEmpty() )
        {
            Toast.makeText(LoginActivity.this,"please enter  email",Toast.LENGTH_SHORT).show();
        }
        else if(!isEmail(email) )
        {
            Toast.makeText(LoginActivity.this,"your email is  invalid",Toast.LENGTH_SHORT).show();
        }
        if(password.isEmpty() )
        {
            Toast.makeText(LoginActivity.this,"please enter  password",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eEmail=findViewById(R.id.et_email);
        ePassword = findViewById(R.id.et_password);
        eLogin=findViewById(R.id.btn_login);
        TextView regis=findViewById(R.id.text_register);

        eLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String inputEmail = eEmail.getText().toString();
                String inputPassword = ePassword.getText().toString();
                checkData(inputEmail,inputPassword);
            }
        });

        regis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}