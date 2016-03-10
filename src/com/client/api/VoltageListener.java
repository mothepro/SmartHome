package com.client.api;

/**
 * Event callable interface
 */
public interface VoltageListener {
    void onBlock();
    void onUnblock();
    void onChange(boolean blocked); // true if blocked
}
