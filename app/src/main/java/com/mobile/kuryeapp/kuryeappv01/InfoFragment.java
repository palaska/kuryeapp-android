package com.mobile.kuryeapp.kuryeappv01;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = (LinearLayout) inflater.inflate(R.layout.tab_info, container, false);
        TextView username = (TextView) view.findViewById(R.id.username);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("xmlFile", Context.MODE_PRIVATE);
        String user_name = preferences.getString("saved_username","--");
        username.setText(user_name);

        return view;
    }
}