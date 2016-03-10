package com.client.impl;

/**
 * Event callable interface
 */
public interface SensorListener {
    void onBlock(int id);
    void onUnblock(int id);
    void onChange(int id, boolean b); // true if blocked
}
