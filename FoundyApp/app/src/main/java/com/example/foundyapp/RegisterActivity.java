package com.example.foundyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foundyapp.model.ModelFirebase;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName = findViewById(R.id.et_name);
    EditText email = findViewById(R.id.et_email);
    EditText password = findViewById(R.id.et_password);
    EditText reTypePass = findViewById(R.id.et_repassword);
    Button register=findViewById(R.id.btn_register);

    ModelFirebase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = fullName.getText().toString();
                String str_email=email.getText().toString();
                String str_password = password.getText().toString();


            }
        });

    }
}