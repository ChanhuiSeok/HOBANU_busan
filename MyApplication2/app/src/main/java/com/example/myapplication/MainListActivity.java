package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



import static java.sql.DriverManager.println;

public class MainListActivity extends AppCompatActivity {
    static String device_token;
    static int work_status = -1;
    static int flag = 0;
    private ListView m_oListView = null;
    private Intent serviceIntent;
    private ImageButton button = null;
    ArrayList<String> UserKeyList = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> Number = new ArrayList<>();
    ArrayList<String> CarNum = new ArrayList<>();
    ArrayList<String> Drink_Status = new ArrayList<>();
    ArrayList<String> Motion_Status = new ArrayList<>();

    ArrayList<String> Latitude = new ArrayList<>();
    ArrayList<String> Longitude = new ArrayList<>();
    ArrayList<String> Picture = new ArrayList<>();
    String Response = "";
    int count;

    String pass_name ="";
    String pass_number ="";
    String pass_status ="";
    String pass_latitude = "";
    String pass_longitude ="";

    ToggleButton toggleButton;

    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                device_token = newToken;
                Log.d("nowToken",newToken);
            }
        });


        startService(new Intent(getApplicationContext(), UserService.class));


        setContentView(R.layout.activity_main);



        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
                //datamanage();
            }
        });


        if(AppHelper.requestQueue == null){
            //리퀘스트큐 생성 (MainActivit가 메모리에서 만들어질 때 같이 생성이 될것이다.
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // 서버에서 받아온 데이터를 나타낸다.

        // (임의) 데이터 1000개 생성--------------------------------.

        button.performClick();

        toggleButton = (ToggleButton)findViewById(R.id.go_to_work_button);
        if(work_status != -1){
            if(this.work_status == 1){// 출근한 경우
                toggleButton.setChecked(true);
            }
            else if(this.work_status == 0){ // 퇴근한 경우
                toggleButton.setChecked(false);
            }
        }
        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 5 seconds
                    if(work_status == 1){// 출근한 경우
                        toggleButton.setChecked(true);
                    }
                    else if(work_status == 0){ // 퇴근한 경우
                        toggleButton.setChecked(false);
                    }
                }
            }, 5000);

        }




    }

    public void sendRequest(){
        String url = "http://13.124.36.143/API/User/fetchAll.php";

        //StringRequest를 만듬 (파라미터구분을 쉽게하기위해 엔터를 쳐서 구분하면 좋다)
        //StringRequest는 요청객체중 하나이며 가장 많이 쓰인다고한다.
        //요청객체는 다음고 같이 보내는방식(GET,POST), URL, 응답성공리스너, 응답실패리스너 이렇게 4개의 파라미터를 전달할 수 있다.(리퀘스트큐에 ㅇㅇ)
        //화면에 결과를 표시할때 핸들러를 사용하지 않아도되는 장점이있다.
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {

                        boolean flag = true;

                        Log.d("RRRRRR ", response);

                        JsonParser Parser = new JsonParser();
                        JsonObject jsonObj = (JsonObject) Parser.parse(response);
                        JsonArray memberArray = (JsonArray) jsonObj.get("Users");

                        count = memberArray.size();
                        //System.out.print("count : " + count);

                        //TextView countText = (TextView) findViewById(R.id.count_text);
                        //countText.setText(Integer.toString(count));


                        for (int i = 0; i < memberArray.size(); i++) {
                            JsonObject object = (JsonObject) memberArray.get(i);
                            System.out.println("User Number  : " + object.get("User Number"));
                            Number.add(""+object.get("User Number"));

                            // System.out.println(Number);

                            System.out.println("User Name : " + object.get("User Name"));
                            Name.add(""+object.get("User Name"));
                            //Name.get(i).replaceAll("\"","");
                            Name.set(i,Name.get(i).substring(1,Name.get(i).length()-1));


                            System.out.println("User Car Number : " + object.get("User Car Number"));
                            CarNum.add(""+object.get("User Car Number"));
                            CarNum.set(i,CarNum.get(i).substring(1,CarNum.get(i).length()-1));


                            System.out.println("User Drink Status : " + object.get("User Drink Status"));
                            Drink_Status.add(""+object.get("User Drink Status"));
                            Drink_Status.set(i,Drink_Status.get(i).substring(1,Drink_Status.get(i).length()-1));


                            System.out.println("User Latitude : " + object.get("User Latitude"));
                            Latitude.add(""+object.get("User Latitude")); // 위도


                            System.out.println("User Longitude : " + object.get("User Longitude"));
                            Longitude.add(""+object.get("User Longitude"));



                            System.out.println("User Motion Status : " + object.get("User Motion Status"));
                            Motion_Status.add(""+object.get("User Motion Status"));
                            Motion_Status.set(i,Motion_Status.get(i).substring(1,Motion_Status.get(i).length()-1));

                            System.out.println("User Picture : " + object.get("User Picture"));
                            Picture.add(""+object.get("User Picture"));
                            Picture.set(i,Picture.get(i).substring(1,Picture.get(i).length()-1));


                            if(flag){
                                pass_name = Name.get(0);
                                pass_number = CarNum.get(0);
                                pass_status = Drink_Status.get(0);
                                pass_latitude = Latitude.get(0);
                                pass_longitude = Longitude.get(0);
                                flag = false;
                            }



                            System.out.println("------------------------");
                        }

                        datamanage();
                    }
                },
                new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 => "+ error.getMessage());
                    }
                }
        ){
            //만약 POST 방식에서 전달할 요청 파라미터가 있다면 getParams 메소드에서 반환하는 HashMap 객체에 넣어줍니다.
            //이렇게 만든 요청 객체는 요청 큐에 넣어주는 것만 해주면 됩니다.
            //POST방식으로 안할거면 없어도 되는거같다.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        //아래 add코드처럼 넣어줄때 Volley라고하는게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        println("요청 보냄!!");
    }


    public void datamanage(){

        int nDatCnt=0;
        // 총 몇 대입니다. db로 값을 받아야함
        ArrayList<ItemData> oData = new ArrayList<>();
        for (int i=0; i<count; i++)
        {
            if(Drink_Status.get(i).equals("0"))
                continue;

            String[] new_name = Name.toArray(new String[Name.size()]);
            System.out.println(new_name);

            ItemData oItem = new ItemData();
            oItem.strTitle = "차주 : " + Name.get(i);
            oItem.strDate = "차량 번호 : " + CarNum.get(i);
            oData.add(oItem);

            nDatCnt++;
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        m_oListView = (ListView)findViewById(R.id.listView);
        ListAdapter oAdapter = new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);

        TextView countText = (TextView) findViewById(R.id.count_text);
        countText.setText(Integer.toString(nDatCnt));

    }


    public void println(String data){

        //textView.append(data + "\n");
    }


    public void onButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("name", pass_name);
        intent.putExtra("number", pass_number);
        intent.putExtra("status", pass_status);
        intent.putExtra("latitude", pass_latitude);
        intent.putExtra("longitude", pass_longitude);

        startActivity(intent);
        finish();
    }

    public void onToggleClicked(View v){

        if(work_status == 1){
            work_status = 0;
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://13.124.36.143/API/Police/workupdate.php";
            url = url.concat("?token=");
            url = url.concat(MainListActivity.device_token);
            url = url.concat("&work=0");

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                        @Override
                        public void onResponse(String response) {
                            Log.d("starttest",response);


                        }
                    },
                    new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("errormessage",error.toString());
                        }
                    }
            );
            queue.add(request);

        }
        else if(work_status == 0){
            work_status = 1;
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://13.124.36.143/API/Police/workupdate.php";
            url = url.concat("?token=");
            url = url.concat(MainListActivity.device_token);
            url = url.concat("&work=1");

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                        @Override
                        public void onResponse(String response) {
                            Log.d("starttest",response);

                        }
                    },
                    new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("errormessage",error.toString());
                        }
                    }
            );
            queue.add(request);
            {
                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainListActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
                RequestQueue queue2 = Volley.newRequestQueue(this);
                String url2 ="http://13.124.36.143/API/Police/gpsupdate.php";
                url2 = url2.concat("?token=");
                url2 = url2.concat(MainListActivity.device_token);
                url2 = url2.concat("&latitude=");
                url2 = url2.concat(Double.toString(latitude));
                url2 = url2.concat("&longitude=");
                url2 = url2.concat(Double.toString(longitude));

                StringRequest request2 = new StringRequest(
                        Request.Method.GET,
                        url2,
                        new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                            @Override
                            public void onResponse(String response) {
                                Log.d("starttest",response);


                            }
                        },
                        new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("errormessage",error.toString());
                            }
                        }
                );

                queue2.add(request2);
            }
        }


    }

}