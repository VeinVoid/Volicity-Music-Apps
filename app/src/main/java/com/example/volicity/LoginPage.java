package com.example.volicity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginPage extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private int RC_SIGN_IN = 214;
    TextView tvSignUp;
    EditText etEmail, etPass;
    Button btnLogin, btnGoogle, btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.hide();
        }

        LoginLocalApps();
        LoginGoogle();
        LoginFacebook();
        Register();
    }

    private void LoginLocalApps() {
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail.setText("");
        etEmail.setText("");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    AndroidNetworking.post("https://mediadwi.com/api/latihan/login")
                            .addBodyParameter("username", email)
                            .addBodyParameter("password", pass)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject respons) {
                                    Log.d("GG Gan", "onResponse: " + respons);
                                    try {
                                        boolean status = respons.getBoolean("status");

                                        if (status){
                                            Intent intent = new Intent(LoginPage.this, SplashScreen.class);
                                            intent.putExtra("username", email);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(LoginPage.this, "Wrong Pass", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
                else {
                    Toast.makeText(LoginPage.this, "Please Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("221209691536-r6751bem8avln0heh5cl8lq1p5gr4hqo.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            UserData userData = new UserData();
            userData.setUsername(account.getDisplayName());
            userData.setEmail(account.getEmail());
            userData.setImageProfile(account.getPhotoUrl());

            Intent intent = new Intent(this, SplashScreen.class);
            intent.putExtra("userData", userData);
            EventBus.getDefault().postSticky(userData);
            startActivity(intent);
            finish();

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleGoogleSignInResult(task);
    }

    private void LoginFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginPage.this, Arrays.asList("email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle successful login
                    }

                    @Override
                    public void onCancel() {
                        // Handle login cancelation
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // Handle login error
                    }
                });
            }
        });
    }

    private void Register(){
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), RegisterPage.class);
                startActivity(signUpIntent);
            }
        });
    }
}