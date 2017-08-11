package com.cmos.agerabus.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.cmos.agera.bus.ABus;
import com.cmos.agerabus.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ABus.INSTANCE.whileCreate(this, new Function1<Object, Unit>() {
            @Override
            public Unit invoke(Object o) {
                Toast.makeText(MainActivity.this, "我收到消息了" + o, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
