package com.example.user.rescueaxis.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import com.example.user.rescueaxis.R;
import com.example.user.rescueaxis.Tabs.ExpandableListViewAdapterOthers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Intent.ACTION_CALL;

/**
 * A placeholder fragment containing a simple view.
 */
public class Others extends Fragment {
    private ExpandableListView expandableListView;
    private List<String> parentHeaderInformation;
    Button BtnCall, BtnText;
    TextView TxtNumber;

    public Others() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Other Emergency Services");

        final View mCustomView;
        mCustomView = inflater.inflate(R.layout.fragment_others, null);

        TxtNumber = (TextView) mCustomView.findViewById(R.id.greetchild_layout);
        BtnCall = (Button) mCustomView.findViewById(R.id.btn_callin);
        BtnText = (Button) mCustomView.findViewById(R.id.btn_textin);
        //Popup

        parentHeaderInformation = new ArrayList<String>();
        parentHeaderInformation.add("Child Helpline");
        parentHeaderInformation.add("Domestic Violence Helpline");
        parentHeaderInformation.add("Sexual and Gender Based Violence Helpline");
        parentHeaderInformation.add("Female Genital Mutilation Hotline");

        final HashMap<String, List<String>> allChildItems = returnGroupedChildItems();
        expandableListView = (ExpandableListView) mCustomView.findViewById(R.id.expandableListView);
        ExpandableListViewAdapterOthers expandableListViewAdapter = new ExpandableListViewAdapterOthers(getActivity().getApplicationContext(), parentHeaderInformation, allChildItems);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String form = parentHeaderInformation.get(groupPosition);
                String term = allChildItems.get(form).get(childPosition);

                if (groupPosition == 0 && childPosition == 0) {

                    BtnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String number = "116";
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(callIntent);
                        }
                    });
                    BtnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            String number = "116";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                    + number)));
                        }
                    });
                } else if (groupPosition == 1 && childPosition == 0) {
                    BtnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String number = "1195";
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(callIntent);
                        }
                    });
                    BtnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            String number = "1195";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                    + number)));
                        }
                    });
                }
                else if (groupPosition == 2 && childPosition == 0) {
                    BtnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String number = "1195";
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(callIntent);
                        }
                    });
                    BtnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            String number = "1195";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                    + number)));
                        }
                    });
                }else if (groupPosition == 3 && childPosition == 0) {
                    BtnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String number = "0770610505";
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(callIntent);
                        }
                    });
                    BtnText = (Button) v.findViewById(R.id.btn_sms);
                    BtnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String number = TxtNumber.getText().toString();
                            String number = "0770610505";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                    + number)));
                        }
                    });
               }
                Toast.makeText(getActivity(), "You clicked: " + form + "-" + term, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return mCustomView;
    }
    private HashMap<String, List<String>> returnGroupedChildItems() {
        HashMap<String, List<String>> childContent = new HashMap<String, List<String>>();
        List<String> Child = new ArrayList<String>();
        Child.add("116");

        List<String> Domestic = new ArrayList<String>();
        Domestic.add("1195");

        List<String> Gender = new ArrayList<String>();
        Gender.add("1195");

        List<String> Female = new ArrayList<String>();
        Female.add("0770610505");

        childContent.put(parentHeaderInformation.get(0), Child);
        childContent.put(parentHeaderInformation.get(1), Domestic);
        childContent.put(parentHeaderInformation.get(2), Gender);
        childContent.put(parentHeaderInformation.get(3), Female);

        return childContent;
    }

    private void call() {
        String number = TxtNumber.getText().toString();
        Intent in=new Intent(Intent.ACTION_CALL,Uri.parse("TEL:" +number));
        try{
            startActivity(in);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getContext(),"You cannot make the call",Toast.LENGTH_SHORT).show();
        }
    }


//    protected void sendSMSMessage() {
//        Log.i("Send SMS", "");
//
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            String phoneNo = 0723727372;
//            smsManager.sendTextMessage(phoneNo, null, null);
//            Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
//        }
//
//        catch (Exception e) {
//            Toast.makeText(getContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//    }
}
