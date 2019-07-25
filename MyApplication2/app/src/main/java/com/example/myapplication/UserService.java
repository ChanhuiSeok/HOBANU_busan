package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://13.124.36.143/API/Police/fetchwork.php";
        url = url.concat("?token=");
        url = url.concat(MainListActivity.device_token);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {
                        Log.d("starttest",response);

                        JsonParser Parser = new JsonParser();
                        JsonObject jsonObj = (JsonObject) Parser.parse(response);
                        JsonArray memberArray = (JsonArray) jsonObj.get("Polices");
                        JsonObject object = (JsonObject) memberArray.get(0);
                        String tmp = object.get("police Work Status").toString();
                        MainListActivity.work_status = Integer.parseInt(tmp.substring(1,tmp.length()-1));
                        Log.d("work_status",String.format("%d",MainListActivity.work_status));
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
