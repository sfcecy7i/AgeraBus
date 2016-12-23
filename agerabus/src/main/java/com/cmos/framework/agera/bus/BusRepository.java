package com.cmos.framework.agera.bus;


import android.support.annotation.NonNull;

import com.google.android.agera.ActivationHandler;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;
import com.google.android.agera.UpdateDispatcher;

import static com.google.android.agera.Observables.updateDispatcher;

class BusRepository extends EventReceiver implements ActivationHandler, Repository<Event> {
    @NonNull
    private final UpdateDispatcher updateDispatcher;
    @NonNull
    private final Class[] events;
    private Event event;


    BusRepository(@NonNull final Class... events) {
        this.updateDispatcher = updateDispatcher(this);
        this.events = events;
    }

    @Override
    public void observableActivated(@NonNull final UpdateDispatcher caller) {
        AgeraBus.register(this, events);
    }

    @Override
    public void observableDeactivated(@NonNull final UpdateDispatcher caller) {
        AgeraBus.unregister(this);
    }

    @Override
    public void onReceive(@NonNull Event event) {
        this.event = event;
        updateDispatcher.update();
    }

    @Override
    public void addUpdatable(@NonNull final Updatable updatable) {
        updateDispatcher.addUpdatable(updatable);
    }

    @Override
    public void removeUpdatable(@NonNull final Updatable updatable) {
        updateDispatcher.removeUpdatable(updatable);
    }

    @NonNull
    @Override
    public Event get() {
        return event;
    }
}
