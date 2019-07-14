package com.example.louyulin.customeventbusdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.louyulin.customeventbusdemo.bean.EventBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefult().register(this);
    }

    @Subscrible(threadMode = ThreadMode.MAIN)
    public void getMessage(EventBean bean){
        Log.d("bean", "bean:" + bean);
    }

    public void test() {

    }
}
