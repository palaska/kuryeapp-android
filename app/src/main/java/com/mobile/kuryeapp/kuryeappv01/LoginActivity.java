package com.mobile.kuryeapp.kuryeappv01;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {
    EditText email,password,res_email,code,newpass;
    Button login,cont,cont_code,cancel,cancel1,register,forpass;
    String emailtxt,passwordtxt,email_res_txt,code_txt,npass_txt;
    List<NameValuePair> params;
    SharedPreferences pref;
    Dialog reset;
    ServerRequest sr;
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sr = new ServerRequest();

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginbtn);
        forpass = (Button)findViewById(R.id.forgotpass);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);


        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                android_id = Secure.getString(getApplicationContext().getContentResolver(),
                        Secure.ANDROID_ID);
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                final String hashedpass = Hashing.sha256()
                        .hashString(passwordtxt, Charsets.UTF_8)
                        .toString();
                params.add(new BasicNameValuePair("password", hashedpass));
                params.add(new BasicNameValuePair("android_id",android_id));
                //maybe additional info

                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON("http://kuryeapp.com/login",params);

                //temporarily filling json
                try {
                    json.put("response", "Response of json");
                    json.put("res",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //end filling

                if(json != null){
                try{
                String jsonstr = json.getString("response");
                    if(json.getBoolean("res")){
                        //String token = json.getString("token");
                        //String grav = json.getString("grav");
                        //SharedPreferences.Editor edit = pref.edit();
                        //Storing Data using SharedPreferences
                        //edit.putString("token", token);
                        //edit.putString("grav", grav);
                        //edit.commit();
                        Intent addressactivity = new Intent(LoginActivity.this,AddressPaymentActivity.class);

                        startActivity(addressactivity);
                        finish();
                    }

                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            }
        });
    }
}