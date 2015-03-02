package com.mobile.kuryeapp.kuryeappv01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = (LinearLayout) inflater.inflate(R.layout.tab_address, container, false);
        Button button = (Button) view.findViewById(R.id.haritayagitbtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mapIntent = new Intent(getActivity(),MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        return view;
    }
}
