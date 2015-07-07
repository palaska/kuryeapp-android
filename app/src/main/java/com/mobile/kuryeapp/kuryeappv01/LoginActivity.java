package com.mobile.kuryeapp.kuryeappv01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserGot;
import com.mobile.kuryeapp.kuryeappv01.Classes.UserSent;
import com.mobile.kuryeapp.kuryeappv01.api.ApiClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {
    String emailtxt,passwordtxt;
    EditText email,password;
    UserGot courier;
    //public static final String ENDPOINT = "http://localhost:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        BootstrapButton login = (BootstrapButton)findViewById(R.id.loginbtn);
        BootstrapButton forpass = (BootstrapButton)findViewById(R.id.forgotpass);
        final LinearLayout loadingwheel = (LinearLayout) findViewById(R.id.loadingwheel);

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                loadingwheel.setVisibility(View.VISIBLE);
                email.setEnabled(false);
                password.setEnabled(false);
                final String hashedpass = Hashing.sha256()
                        .hashString(passwordtxt, Charsets.UTF_8)
                        .toString();

                UserSent userSent = new UserSent(emailtxt, hashedpass);

                ApiClient.getLoginApiClient().getUserModel(userSent, new Callback<UserGot>() {
                    @Override
                    public void success(UserGot user , Response response) {
                        if(user.get_id() != null ){
                            //&& user.getRole().equals("admin")
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
                        Log.d("fsad", "failed");
                        SuperToast superToast = new SuperToast(getApplicationContext(),
                                Style.getStyle(Style.RED, SuperToast.Animations.SCALE));
                        superToast.setDuration(SuperToast.Duration.LONG);
                        superToast.setText("Yanlış kullanıcı adı-şifre kombinasyonu!");
                        superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                        superToast.show();
                        loadingwheel.setVisibility(View.INVISIBLE);
                        email.setEnabled(true);
                        password.setEnabled(true);
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