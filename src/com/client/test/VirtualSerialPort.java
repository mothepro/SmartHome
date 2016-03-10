package com.client.test;

import com.client.api.Serial;
import com.client.api.SerialListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VirtualSerialPort extends Serial {
    /**
     * Data
     */
    private List<String> data = new ArrayList<>();

    /**
     * Time to wait between TX
     */
    private static final int DELAY = 200;

    /**
     * Loop number
     */
    private int iter = 0;

    /**
     * Fake default port and get data
     * @param file
     * @throws Exception
     */
    public VirtualSerialPort(String file) throws Exception {
        super("COM1");
        getData(file);
    }

    /**
     * Send fake data
     * @throws InterruptedException
     */
    private void loop(final SerialListener s) throws InterruptedException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                s.onData(data.get(iter++ % data.size()));
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0, DELAY); //call the run() method at DELAY-millisecond intervals
    }

    public void addListener(SerialListener s) {
        super.addListener(s);
        try {
            loop(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get fake data
     * @throws InterruptedException
     */
    private void getData(String f) {
        try {
            String line;

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));

            while((line = bufferedReader.readLine()) != null)
                data.add(line);

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + f + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}