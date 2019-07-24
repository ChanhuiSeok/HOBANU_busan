package com.example.myapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "Test TAG";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Called when a message is received.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
    }

    @Override
    public void onMessageSent(String msgId) {
        //Called when an upstream message has been successfully sent to the GCM connection server.
    }

    @Override
    public void onNewToken(String token){
//        Called when a new token for the default Firebase project is generated.

        super.onNewToken(token);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://13.124.36.143/API/Police/register.php";
        url = url.concat("?token=");
        url = url.concat(token);
        String latitude = "latitude=";
        String longitude = "longitude=";
        latitude = latitude.concat("34.1245");
        longitude = longitude.concat("128.123");
        url = url.concat("&");
        url = url.concat(latitude);
        url = url.concat("&");
        url = url.concat(longitude);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {
                        Log.d("starttest","test success");

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

        String Devicetoken = token;
        Log.d("test_device", Devicetoken);

    }

    @Override
    public void onSendError(String msgId, Exception exception){
//        Called when there was an error sending an upstream message.
    }

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
////        if (remoteMessage.getNotification() != null) {
////            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }


}
