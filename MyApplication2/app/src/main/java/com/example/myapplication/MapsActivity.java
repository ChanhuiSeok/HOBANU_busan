package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double new_latitude = 35.890323;
    double new_longitude = 128.611346;
    double trans_latitude = 35.889419;
    double trans_longitude = 128.610459;
    private GoogleMap mMap;
    private Button button1;
    private TextView txtResult;
    MarkerOptions markerOptions1;
    MarkerOptions markerOptions2;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // 1. 마커 옵션 설정 (만드는 과정)
        markerOptions1 = new MarkerOptions();
        markerOptions1 // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(new LatLng(new_latitude, new_longitude))
                .title("현재 위치"); // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker_police_600_600);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);
        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        mMap.addMarker(markerOptions1);

        markerOptions2 = new MarkerOptions();
        markerOptions2 // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(new LatLng(trans_latitude, trans_longitude))
                .title("차량 위치"); // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)
        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.marker_car_526_210);
        Bitmap b2=bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, 140, 70, false);
        markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(smallMarker2));

        mMap.addMarker(markerOptions2);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(new_latitude, new_longitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(new_latitude, new_longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("전화 걸기"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        MarkerOptions new_marker = new MarkerOptions();
        new_marker
                .position(new LatLng(trans_latitude, trans_longitude))
                .title("해당차량");

        mMap.addMarker(new_marker);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:00000000000"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button1 = (Button) findViewById(R.id.button1);

        TextView tx1 = (TextView)findViewById(R.id.tx1);
        TextView tx2 = (TextView)findViewById(R.id.tx2);
        TextView tx3 = (TextView)findViewById(R.id.tx3);
        txtResult = (TextView)findViewById(R.id.txtResult);
        Intent intent = getIntent();

        /* 상세 화면에서 차주, 번호, 음주 정보를 프린트하는 부분 */
        String name = intent.getExtras().getString("name");
        tx1.setText("차주 : "+name);

        String number = intent.getExtras().getString("number");
        tx2.setText("번호 : "+number);

        String status = intent.getExtras().getString("status");

        // gps 받아오기
        /*
        final String t_latitude = intent.getExtras().getString("latitude");
        final String t_longitude = intent.getExtras().getString("longitude");

        trans_latitude = Double.parseDouble(t_latitude);
        trans_longitude = Double.parseDouble(t_longitude);*/


        if(status.equals("\"0\""))
            tx3.setText("해당 차량에서 음주가 감지되었습니다!");

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    //txtResult.setText("위치정보 : " + provider + "\n"+
                    //        "위도 : " + longitude + "\n" +
                    //        "경도 : " + latitude
                    //);


                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);

                    new_latitude = latitude;
                    new_longitude = longitude;

                }
            }
        });

        // button1.performClick();
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            String strNumber = String.format("%.3f", longitude);
            double latitude = location.getLatitude();
            String strNumber2 = String.format("%.3f", latitude);
            double altitude = location.getAltitude();
            String strNumber3 = String.format("%.3f", altitude);

            txtResult.setText("\n [실시간 위치정보] : " + provider + "\n" +
                    " 위도 : " + strNumber + " / " +
                    "경도 : " + strNumber2 + " / " +
                    "고도  : " + strNumber3);


            new_longitude = longitude;
            new_latitude = latitude;

            mMap.clear();

            onMapReady(mMap);

        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };

    public void backtohome(View v) {
        Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLastLocationButtonClicked(View view) {
    }
}