package com.cmos.framework.agera.bus;


import android.support.annotation.NonNull;

interface IEventReceiver {
    void onReceive(@NonNull Event event);
}
