package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView intro;
    private ListView m_oListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Thread(new Runnable() {
            public void run() {
                try {
                    intro = (ImageView) findViewById(R.id.ex1);
                    Animation alphaAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                    intro.startAnimation(alphaAnim);
                    Thread.sleep(2000);
                    isIntro();

                } catch (Exception e) {

                }
            }

        }).start();
    }

    private void isIntro() {

        Intent intent = new Intent(this, MainListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();


    }

}


