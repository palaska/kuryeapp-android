package com.mobile.kuryeapp.kuryeappv01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONException;
import org.json.JSONObject;


public class AddressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = (LinearLayout) inflater.inflate(R.layout.tab_address, container, false);
        BootstrapButton gotomapbtn = (BootstrapButton) view.findViewById(R.id.gotomapbtn);
        BootstrapButton callbtn = (BootstrapButton) view.findViewById(R.id.callbtn);

        gotomapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressPaymentActivity.getData() != null) {
                    try {
                        JSONObject dest = AddressPaymentActivity.getData().getJSONObject("destination");
                        String name = dest.getString("name");
                        String description = dest.getString("description");
                        Double x = dest.getDouble("lat");
                        Double y = dest.getDouble("lng");
                        if (x == null) {
                            Toast.makeText(getActivity(), "Harita konumu bilgisi yok!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
                            mapIntent.putExtra("x", x);
                            mapIntent.putExtra("y", y);
                            mapIntent.putExtra("name", name);
                            mapIntent.putExtra("description", description);
                            startActivity(mapIntent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else Toast.makeText(getActivity(),"Aktif görev yok.",Toast.LENGTH_SHORT).show();
            }
        });


        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressPaymentActivity.getData() != null) {
                    String phone = null;
                    try {
                        phone = AddressPaymentActivity.getData().getString("phone");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (phone == null) {
                        Toast.makeText(getActivity(), "Telefon bilgisi yok!", Toast.LENGTH_SHORT).show();
                    } else {
                        makeCall(phone);
                    }
                } else Toast.makeText(getActivity(),"Aktif görev yok.",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    protected void makeCall(String tel) {
        Log.i("Make call", "");

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + tel));
        //phoneIntent.setData(Uri.parse("tel:05355963550"));

        try {
            startActivity(phoneIntent);
            //this.getActivity().finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Log.i("make call catch","");
        }
    }

}