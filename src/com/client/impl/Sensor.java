package com.client.impl;

import com.client.api.Voltage;
import com.client.api.VoltageListener;

/**
 * @author Mo
 */
public class Sensor extends Voltage implements VoltageListener {
    private final int id;
    private static int counter = 0;
    SensorListener listener;

    Sensor(SensorListener l) {
        id = Sensor.counter++;
        listener = l;
    }

    public void addListener(VoltageListener v) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public void onBlock() {
        listener.onBlock(id);
    }

    @Override
    public void onUnblock() {
        listener.onUnblock(id);
    }

    @Override
    public void onChange(boolean blocked) {
        listener.onChange(id, blocked);
    }
}
