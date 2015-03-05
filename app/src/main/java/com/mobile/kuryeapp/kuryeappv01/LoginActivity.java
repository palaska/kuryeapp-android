package com.mobile.kuryeapp.kuryeappv01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserGot;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserSent;
import com.mobile.kuryeapp.kuryeappv01.CustomAPIs.RestAPI;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {
    EditText email,password;
    Button login,forpass;
    String emailtxt,passwordtxt;
    UserGot courier;
    //public static final String ENDPOINT = "http://localhost:3000";
    public static final String ENDPOINT = "https://cryptic-ridge-2951.herokuapp.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginbtn);
        forpass = (Button)findViewById(R.id.forgotpass);

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();

                final String hashedpass = Hashing.sha256()
                        .hashString(passwordtxt, Charsets.UTF_8)
                        .toString();

                UserSent userSent = new UserSent(emailtxt, hashedpass);

                RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .build();
                RestAPI api = adapter.create(RestAPI.class);
                api.getUserModel(userSent, new Callback<UserGot>() {
                    @Override
                    public void success(UserGot user, Response response) {
                        if(user.get_id() != null && user.getRole().equals("admin")) {
                            //TODO change admin to courier
                            Log.d("fsad","success");
                            courier = user;
                            courier.setDeliverycnt(5);
                            goToNext();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //TODO do something here
                        Log.d("fsad","failed");
                        Toast.makeText(getApplicationContext(), "Yanlış kullanıcı adı veya şifre.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void goToNext(){
        Intent intent = new Intent(this, AddressPaymentActivity.class);
        intent.putExtra("access_token", courier.getAccess_token());
        intent.putExtra("username",courier.getUsername());
        startActivity(intent);
    }

}