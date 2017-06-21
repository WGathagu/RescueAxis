package com.example.user.rescueaxis.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.rescueaxis.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class About extends Fragment {

    public About() {
    }
    TextView TxtRescuecall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        TxtRescuecall = (TextView) view.findViewById(R.id.textRescuecall);

        TxtRescuecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String number = TxtNumber.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String number = TxtRescuecall.getText().toString();
                callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(callIntent);
            }
        });

        return view;
    }
}
