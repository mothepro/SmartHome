package com.client.api;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Voltage information read from the board
 *
 * @author Mo
 */
public class Voltage {
    /**
     * All data from the board
     */
    private List<Integer> data = new ArrayList<>();

    /**
     * All listeners
     */
    private List<VoltageListener> listeners = new ArrayList<>();

    /**
     * Amount voltage must change to take effect
     */
    private final static int THRESHOLD = 200;

    /**
     * Number of values to keep saved
     * Must be at least 2
     */
    private final static int SAVE = 3;

    /**
     * Add a voltage to stack
     * @param volt
     */
    public boolean push(int volt) {
        boolean ret = data.add(volt);
        update();
        return ret;
    }

    /**
     * Check values for a change
     */
    private void update() {
        int l = data.size(),
            curr, prev;

        if(l >= 2) {
            curr = data.get(l - 1);
            prev = data.get(l - 2);

            // big change
            if(abs(curr - prev) >= THRESHOLD) {
                for(VoltageListener v : listeners)
                    v.onChange(curr < prev);

                if(curr > prev) // increase = unblock
                    for(VoltageListener v : listeners)
                        v.onUnblock();

                else // decrease = block
                    for(VoltageListener v : listeners)
                        v.onBlock();
            }

            trim();
        }
    }

    /**
     * Remove from front of list
     */
    private void trim() {
        while(SAVE >= 2 && data.size() > SAVE)
            data.remove(0);
    }

    /**
     * Attach a new listener
     * @param v
     */
    public void addListener(VoltageListener v) {
        listeners.add(v);
    }

    /**
     * Convert then push
     * @param volt
     */
    public boolean push(String volt) {
        return push(Integer.parseInt(volt));
    }

}