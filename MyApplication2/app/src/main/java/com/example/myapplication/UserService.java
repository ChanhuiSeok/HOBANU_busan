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
    int count;

    private ListView m_oListView = null;
    ArrayList<String> UserKeyList = new ArrayList<>();
    static ArrayList<String> Name = new ArrayList<>();
    static ArrayList<String> Number = new ArrayList<>();
    static ArrayList<String> CarNum = new ArrayList<>();
    static ArrayList<String> Drink_Status = new ArrayList<>();
    static ArrayList<String> Motion_Status = new ArrayList<>();

    static ArrayList<String> Latitude = new ArrayList<>();
    static ArrayList<String> Longitude = new ArrayList<>();
    static ArrayList<String> Picture = new ArrayList<>();
    String Response = "";

    String pass_name ="";
    String pass_number ="";
    String pass_status ="";
    String pass_latitude = "";
    String pass_longitude ="";

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
                            Drink_Status.set(i,Drink_Status.get(i));
//

                            System.out.println("User Latitude : " + object.get("User Latitude"));
                            Latitude.add(""+object.get("User Latitude")); // 위도


                            System.out.println("User Longitude : " + object.get("User Longitude"));
                            Longitude.add(""+object.get("User Longitude"));



                            System.out.println("User Motion Status : " + object.get("User Motion Status"));
                            Motion_Status.add(""+object.get("User Motion Status"));
                            Motion_Status.set(i,Motion_Status.get(i));

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

                        int nDatCnt=0;
                        // 총 몇 대입니다. db로 값을 받아야함
                        ArrayList<ItemData> oData = new ArrayList<>();
                        for (int i=0; i<count; i++)
                        {
                            System.out.print("test + " + Drink_Status.get(i));

                            if(Drink_Status.get(i).equals("\"0\"")) {
                                System.out.print("test success");
                                continue;
                            }

                            String[] new_name = Name.toArray(new String[Name.size()]);
                            System.out.println(new_name);

                            ItemData oItem = new ItemData();
                            oItem.strTitle = "차주 : " + Name.get(i);
                            oItem.strDate = "차량 번호 : " + CarNum.get(i);
                            oData.add(oItem);

                            nDatCnt++;
                        }

                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout1 = inflater.inflate(R.layout.activity_main, null);

                        // ListView, Adapter 생성 및 연결 ------------------------
                        m_oListView = (ListView) layout1.findViewById(R.id.listView);
                        ListAdapter oAdapter = new ListAdapter(oData);
                        m_oListView.setAdapter(oAdapter);

                        TextView countText = (TextView) layout1.findViewById(R.id.count_text);
                        countText.setText(Integer.toString(nDatCnt));

                    }
                },
                new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.print("에러 => "+ error.getMessage());
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
        queue.add(request);
        System.err.print("요청 보냄!!");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void datamanage(){

        int nDatCnt=0;
        // 총 몇 대입니다. db로 값을 받아야함
        ArrayList<ItemData> oData = new ArrayList<>();
        for (int i=0; i<count; i++)
        {
            System.out.print("test + " + UserService.Drink_Status.get(i));

            if(UserService.Drink_Status.get(i).equals("\"0\"")) {
                System.out.print("test success");
                continue;
            }

            String[] new_name = UserService.Name.toArray(new String[UserService.Name.size()]);
            System.out.println(new_name);

            ItemData oItem = new ItemData();
            oItem.strTitle = "차주 : " + UserService.Name.get(i);
            oItem.strDate = "차량 번호 : " + UserService.CarNum.get(i);
            oData.add(oItem);

            nDatCnt++;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout1 = inflater.inflate(R.layout.activity_main, null);

        // ListView, Adapter 생성 및 연결 ------------------------
        m_oListView = (ListView) layout1.findViewById(R.id.listView);
        ListAdapter oAdapter = new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);

        TextView countText = (TextView) layout1.findViewById(R.id.count_text);
        countText.setText(Integer.toString(nDatCnt));

    }
}
