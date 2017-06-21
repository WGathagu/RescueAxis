package com.example.user.rescueaxis.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.example.user.rescueaxis.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.getIntent;
import static com.google.android.gms.internal.zzir.runOnUiThread;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class Home extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String LOG_TAG = "Ambulance";
    private static final String SERVICE_URL = "http://www.codeninja.co.ke/Rach/Android/Rescue/ambulance1.php";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_NUMBER = "phoneno";
    private static final String LOG_TAG1 = "Police";
    private static final String SERVICE_URL1 = "http://www.codeninja.co.ke/Rach/Android/Rescue/police.php";
    private static final String TAG_LOCATION1 = "location1";
    private static final String TAG_NUMBER1 = "phoneno1";
    private static final String LOG_TAG2 = "Fire";
    private static final String SERVICE_URL2 = "http://www.codeninja.co.ke/Rach/Android/Rescue/firebrigade.php";
    private static final String TAG_LOCATION2 = "location2";
    private static final String TAG_NUMBER2 = "phoneno2";
    protected GoogleMap Mmap;
    GoogleMapOptions options = new GoogleMapOptions();
    public JSONArray jsonArray;

    SeekBar skRadius;
    TextView txtRadius;
    Location currentPosition;
    CardView cardView;
    CircleOptions circle;
    Circle mapCircle;

    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setUpMapIfNeeded();
        Mmap.setOnMapLongClickListener(myOnMapLongClickListener);
        skRadius = (SeekBar) view.findViewById(R.id.seekBar_home);
        txtRadius = (TextView) view.findViewById(R.id.radius);
        cardView = (CardView) view.findViewById(R.id.card_seek_home);
        skRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtRadius.setText(String.valueOf(i));
                if (currentPosition != null) {
                    drawCircle(currentPosition, i * 1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void setUpMapIfNeeded() {
        if (Mmap == null) {
            Mmap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (Mmap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Retrieve the city data from the web service
        // In a worker thread since it's a network operation.
        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveAndAddCities();
                    retrieveAndAddCities1();
                    retrieveAndAddCities2();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrieve ambulances, police and firebrigades", e);
                    //return;
                }
            }
        }).start();


        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true)
                .zOrderOnTop(true);



        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Mmap.setMyLocationEnabled(true);
        Mmap.setOnInfoWindowClickListener(this);
        Mmap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                currentPosition=location;
                cardView.setVisibility(View.VISIBLE);
                LatLng point = new LatLng(lat, lng);

                Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                Mmap.getUiSettings().setZoomControlsEnabled(true);
                Mmap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("You Are Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_home)));

            }
        });
    }


    protected void retrieveAndAddCities() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson(json.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONObject jsonObject = new JSONObject(json);
        jsonArray = jsonObject.getJSONArray("medical");
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Mmap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                    //.snippet(jsonObj.getString("phoneno"))
                            //.snippet(Integer.toString(jsonObj.getInt("population")))
                    .position(new LatLng(
                            jsonObj.getDouble("latitude"),
                            jsonObj.getDouble("longitude")
                    ))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ambulance)));
//            Mmap.addCircle(new CircleOptions()
//                    .center(point)
//                    .radius(radius)
//                    .strokeColor(Color.parseColor("#f4a460"))
//                    .strokeWidth(2)
//                    .fillColor(Color.TRANSPARENT));

            //Toast.makeText(getContext(), json, Toast.LENGTH_LONG);
        }
        Log.e(LOG_TAG, json);
    }
    protected void retrieveAndAddCities1() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL1);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG1, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson1(json.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG1, "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson1(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONObject jsonObject = new JSONObject(json);
        jsonArray = jsonObject.getJSONArray("police");
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Mmap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                            //.snippet(Integer.toString(jsonObj.getInt("population")))
                    .position(new LatLng(
                            jsonObj.getDouble("latitude"),
                            jsonObj.getDouble("longitude")
                    ))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_police)));
//            Mmap.addCircle(new CircleOptions()
//                    .center(point)
//                    .radius(radius)
//                    .strokeColor(Color.parseColor("#f4a460"))
//                    .strokeWidth(2)
//                    .fillColor(Color.TRANSPARENT));

            //Toast.makeText(getContext(), json, Toast.LENGTH_LONG);
        }
        Log.e(LOG_TAG1, json);
    }

    protected void retrieveAndAddCities2() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL2);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG2, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson2(json.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG2, "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson2(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONObject jsonObject = new JSONObject(json);
        jsonArray = jsonObject.getJSONArray("fire");
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Mmap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                            //.snippet(Integer.toString(jsonObj.getInt("population")))
                    .position(new LatLng(
                            jsonObj.getDouble("latitude"),
                            jsonObj.getDouble("longitude")
                    ))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fire)));
//            Mmap.addCircle(new CircleOptions()
//                    .center(point)
//                    .radius(radius)
//                    .strokeColor(Color.parseColor("#f4a460"))
//                    .strokeWidth(2)
//                    .fillColor(Color.TRANSPARENT));

            //Toast.makeText(getContext(), json, Toast.LENGTH_LONG);
        }
        Log.e(LOG_TAG2, json);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(marker.getTitle());
        TextView tv_place = (TextView) dialog.findViewById(R.id.location);
        TextView tv_phone = (TextView) dialog.findViewById(R.id.phone);
        ImageView image = (ImageView) dialog.findViewById(R.id.iv_logo);

        String mID = marker.getId();
        int id = Integer.parseInt(mID.substring(1));


        String phone = "";
        try {
            JSONObject m = jsonArray.getJSONObject(id);
            tv_place.setText(m.getString(TAG_LOCATION));
            tv_phone.setText(m.getString(TAG_NUMBER));
            phone = m.getString(TAG_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        image.setImageResource(R.drawable.logo1);
        Button btnCall = (Button) dialog.findViewById(R.id.btn_call);
        Button btnSms = (Button) dialog.findViewById(R.id.btn_sms);

        final String finalPhone = phone;
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("smsto:"));
                sendIntent.putExtra("sms_body", "");
                sendIntent.putExtra("address", finalPhone);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
                dialog.dismiss();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String uri = "tel:" + finalPhone;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        dialog.show();
    }

    OnMapLongClickListener myOnMapLongClickListener =
            new OnMapLongClickListener(){

                @Override
                public void onMapLongClick(LatLng point) {
                    Mmap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(point.toString()));

                    Location myLocation = Mmap.getMyLocation();
                    if(myLocation == null){
                        Toast.makeText(getContext(),
                                "My location not available",
                                Toast.LENGTH_LONG).show();
                    }else{
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.add(point);
                        polylineOptions.add(
                                new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                        Mmap.addPolyline(polylineOptions);
                    }
                }

            };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap = googleMap;
        Mmap.setOnInfoWindowClickListener(this);
    }
    public void drawCircle(Location loc, double rad){
        if(mapCircle!=null){
            mapCircle.remove();
        }
        circle = new CircleOptions()
                .center(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .radius(rad)
                .strokeColor(R.color.circleStroke)
                .strokeWidth(3)
                .fillColor(R.color.circleFill);
        mapCircle = Mmap.addCircle(circle);
    }
}

