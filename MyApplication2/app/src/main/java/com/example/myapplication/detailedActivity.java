package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class detailedActivity extends AppCompatActivity {
    //Frag1 frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


    }


    public void backtohome(View v) {
        Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
        startActivity(intent);
        finish();
    }

}
