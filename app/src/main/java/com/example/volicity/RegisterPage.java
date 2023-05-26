package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class RegisterPage extends AppCompatActivity {

    EditText etFullName, etUsername, etEmail, etPass;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        Register();

    }

    public void Register(){
        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = etFullName.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();

                if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    AndroidNetworking.post("https://mediadwi.com/api/latihan/register-user")
                            .addBodyParameter("full_name", fullName)
                            .addBodyParameter("username", username)
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", pass)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", "onResponse: " + response);
                                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                }
                else {
                    Toast.makeText(RegisterPage.this, "Please Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}