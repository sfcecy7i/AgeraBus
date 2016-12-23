package com.cmos.agerabus.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cmos.agerabus.R;
import com.cmos.framework.agera.bus.AgeraBus;
import com.cmos.framework.agera.bus.Event;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable {
    private Repository<Event> mEventRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventRepo = AgeraBus.repository(MyEvent.class);
        mEventRepo.addUpdatable(this);
    }

    @Override
    protected void onDestroy() {
        mEventRepo.removeUpdatable(this);
        super.onDestroy();
    }

    @Override
    public void update() {
        Event event = mEventRepo.get();
        if (event instanceof MyEvent) {
            Toast.makeText(this, "我收到消息了", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

    public static class MyEvent implements Event {

    }
}
