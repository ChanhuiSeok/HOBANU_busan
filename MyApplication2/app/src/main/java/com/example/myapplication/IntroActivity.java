package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class IntroActivity extends Activity {
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        h = new Handler();
        h.postDelayed(run, 2300); //3초후에 헨들러전송

    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, MainListActivity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    };

        @Override
        public void onBackPressed() {
            // TODO Auto-generated method stub
            super.onBackPressed();
            h.removeCallbacks(run); //백버튼누르면 헨들러전송취소 }

        }

    }
