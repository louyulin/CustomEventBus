package com.example.louyulin.customeventbusdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.louyulin.customeventbusdemo.bean.EventBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefult().register(MainActivity.this);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscrible(threadMode = ThreadMode.MAIN)
    public void getMessage(EventBean bean){
        Log.d("bean", "bean:" + bean);
    }

    public void test() {

    }
}
