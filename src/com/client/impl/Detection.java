package com.client.impl;

import com.client.api.Serial;
import com.client.api.SerialListener;
import com.client.test.VirtualSerialPort;

import java.util.ArrayList;
import java.util.List;

/**
 * Read data being streamed from Arduino
 * Then decide if a light is blocking path
 * and which room the bug is in
 *
 * @author Mo
 */
public class Detection implements SerialListener, SensorListener {
    /**
     * Sensors connected to board
     */
    private List<Sensor> sensors = new ArrayList<>();

    /**
     * Connection to board
     */
    private Serial serial;

    /**
     * Start detection
     * @param s device to connect to
     * @param n number of sensors in device
     */
    Detection(String s, Integer n) throws Exception {
        this(new Serial(s), n);
    }

    /**
     * Start detection
     * @param s device to connect to
     * @param n number of sensors in device
     */
    Detection(Serial s, Integer n) {
        try {
            // add device with me as the listener
            serial = s;
            serial.addListener(this);

            // add sensors with me as the listener
            for(int i=0; i<n; i++)
                sensors.add(new Sensor(this));

            if(s instanceof VirtualSerialPort)
                ((VirtualSerialPort) s).go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * New voltage data?
     * Add to volt objects
     *
     * @param data
     */
    @Override
    public void onData(String data) {
        int i = 0;
        for(String v : data.split("\t"))
            sensors.get(i++).push(v);
    }

    public static void main(String[] args) throws Exception {
        String s = args.length > 0 ? args[0] : "";
        Serial serial;

        if(s.isEmpty()) // virtual if empty
            serial = new VirtualSerialPort("src/com/client/test/res/scenario.raw");
        else
            serial = new Serial(s);

        Detection d = new Detection(serial, 3);
    }

    @Override
    public void onBlock(int id) {
        System.out.println("Sensor "+ (id+1) +" is blocked");
    }

    @Override
    public void onUnblock(int id) {
        System.out.println("Sensor "+ (id+1) +" is no longer blocked");
    }

    @Override
    public void onChange(int id, boolean b) {

    }
}
