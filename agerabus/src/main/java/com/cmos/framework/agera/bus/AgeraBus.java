package com.cmos.framework.agera.bus;


import android.support.annotation.NonNull;

import com.google.android.agera.Repository;

import java.util.Arrays;

import static com.google.android.agera.Preconditions.checkNotNull;

public class AgeraBus {
    private AgeraBus() {
        //no instance
    }

    @NonNull
    public static Repository<Event> repository(@NonNull final Class... events) {
        checkNotNull(events);
        for (Class clazz : events) {
            if (!Event.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Your event class must implement " + Event.class.getName());
            }
        }
        return new BusRepository(events);
    }

    public static void post(@NonNull Event event) {
        Bus.DEFAULT.post(event);
    }

    static void register(@NonNull IEventReceiver receiver, @NonNull Class... events) {
        Bus.DEFAULT.register(receiver, events);
    }

    static void unregister(@NonNull IEventReceiver receiver) {
        Bus.DEFAULT.unregister(receiver);
    }

    private enum Bus {
        DEFAULT;
        @NonNull
        private Object[] receivers = new Object[0];

        @SuppressWarnings({"unchecked", "UnusedDeclaration"})
        public void post(@NonNull Event event) {
            checkNotNull(event);
            for (int index = 0; index < receivers.length; index += 2) {
                if (null != receivers[index]) {
                    Class<? extends Event>[] events = (Class<? extends Event>[]) receivers[index + 1];
                    for (Class<? extends Event> eClass : events) {
                        if (eClass.isAssignableFrom(event.getClass())) {
                            IEventReceiver receiver = (IEventReceiver) receivers[index];
                            receiver.onReceive(event);
                        }
                    }
                }
            }
        }

        public void register(@NonNull IEventReceiver receiver, @NonNull Class... events) {
            checkNotNull(receiver);
            checkNotNull(events);
            addKeyValuePair(receiver, events);
        }

        public void unregister(@NonNull IEventReceiver receiver) {
            checkNotNull(receiver);
            removeKey(receiver);
        }

        private synchronized boolean addKeyValuePair(@NonNull final IEventReceiver key, @NonNull final Class... value) {
            int size = 0;
            int indexToAdd = -1;
            boolean hasValue = false;
            for (int index = 0; index < receivers.length; index += 2) {
                final Object keysValue = receivers[index];
                if (keysValue == null) {
                    indexToAdd = index;
                }
                if (keysValue == key) {
                    size++;
                    if (receivers[index + 1] == value) {
                        indexToAdd = index;
                        hasValue = true;
                    }
                }
            }
            if (indexToAdd == -1) {
                indexToAdd = receivers.length;
                receivers = Arrays.copyOf(receivers, indexToAdd < 2 ? 2 : indexToAdd * 2);
            }
            if (!hasValue) {
                receivers[indexToAdd] = key;
                receivers[indexToAdd + 1] = value;
            }
            return size == 0;
        }

        private synchronized boolean removeKey(@NonNull final IEventReceiver key) {
            boolean removed = false;
            for (int index = 0; index < receivers.length; index += 2) {
                if (receivers[index] == key) {
                    receivers[index] = null;
                    receivers[index + 1] = null;
                    removed = true;
                }
            }
            return removed;
        }
    }
}
