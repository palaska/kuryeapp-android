package com.mobile.kuryeapp.kuryeappv01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.kuryeapp.kuryeappv01.Classes.customJob;
import com.mobile.kuryeapp.kuryeappv01.Classes.customLocation;

public class AddressFragment extends Fragment {
    public customJob job;
    public customLocation loc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = (LinearLayout) inflater.inflate(R.layout.tab_address, container, false);
        Button gotomapbtn = (Button) view.findViewById(R.id.gotomapbtn);
        Button callbtn = (Button) view.findViewById(R.id.callbtn);
        TextView address = (TextView) view.findViewById(R.id.addressText);
        TextView addressdesc = (TextView) view.findViewById(R.id.addressdesc);

//////////////////////////////////////////////////////////////////////////////////////////////
        // incoming job object
        loc = new customLocation(41.085050,29.046506);
        job = new customJob("id132fdaslj13",
                1,
                "Kahve Diyari",
                "Akaygen Sokak No:2 Küçükbebek Sarıyer / Hisarüstü",
                "Boğaziçi Üniversitesi Güney Kampüsü Girişi Yanı",
                loc,
                0,
                "pickup",
                "05355963550");
///////////////////////////////////////////////////////////////////////////////////////////////
        gotomapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (job.getLocation().getX() == null){
                        Toast.makeText(getActivity(),"Harita konumu bilgisi yok!",Toast.LENGTH_LONG).show();
                    } else {
                        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
                        mapIntent.putExtra("x",job.getLocation().getX());
                        mapIntent.putExtra("y",job.getLocation().getY());
                        mapIntent.putExtra("address",job.getAddress());
                        startActivity(mapIntent);
                    }
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String phone = job.getPhone();
                    if (phone == null){
                        Toast.makeText(getActivity(),"Telefon bilgisi yok!",Toast.LENGTH_LONG).show();
                    } else {
                        makeCall(phone);
                    }
            }
        });

        return view;
    }
    protected void makeCall(String tel) {
        Log.i("Make call", "");

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + tel));

        try {
            startActivity(phoneIntent);
            //this.getActivity().finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Log.i("make call catch","");
        }
    }


}
