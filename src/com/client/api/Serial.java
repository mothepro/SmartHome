package com.client.api;

import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * This is an abstracted interface between Java
 * And serial ports used to communicate to the
 * Arduino
 *
 * @author Mo
 */
public class Serial implements SerialPortEventListener {
    private SerialPort serialPort;

    protected BufferedReader tx;
    protected OutputStream rx;

    protected List<SerialListener> listeners = new ArrayList<>();

    protected static final int TIME_OUT = 2000;
    protected static final int DATA_RATE = 9600;

    /**
     * Connect to a port name
     * @param name
     */
    public Serial(String name) throws Exception {
        this(name2id(name));
    }

    /**
     * Connect to a port id
     * @param portID
     */
    public Serial(CommPortIdentifier portID) throws PortInUseException, TooManyListenersException, UnsupportedCommOperationException, IOException {
        this(id2port(portID));
    }

    /**
     * Connect to a serial port
     * @param port
     */
    public Serial(SerialPort port) throws UnsupportedCommOperationException, IOException, TooManyListenersException {
        serialPort = port;

        serialPort.setSerialPortParams(
                DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );

        // open the streams
        tx = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        rx = serialPort.getOutputStream();

        // this class will be an event emitter
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    }

    /**
     * Converts a port name to an id
     * @param name
     * @return CommPortIdentifier
     */
    private static CommPortIdentifier name2id(String name) throws Exception {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            if (currPortId.getName().equals(name))
                return currPortId;
        }

        throw new Exception("Serial Port "+ name +" not found");
    }

    /**
     * Converts a port id to an actual port
     * @param portID
     * @return SerialPort
     */
    private static SerialPort id2port(CommPortIdentifier portID) throws PortInUseException {
        return (SerialPort) portID.open(Serial.class.getName(), TIME_OUT);
    }

    /**
     * A List of all the ports found
     * @return
     */
    public static List getPortNames() {
        final Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        List<String> ret = new ArrayList<>();

        while (portIdentifiers.hasMoreElements())
            ret.add(((CommPortIdentifier) portIdentifiers.nextElement()).getName());

        return ret;
    }

    /**
     * Close connection with my port
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * The name of the current port
     */
    public String getPortName() {
        return serialPort.getName();
    }

    /**
     * Sends some data to the serial port
     * @param data
     * @throws IOException
     */
    public synchronized void send(String data) throws IOException {
        rx.write(data.getBytes());
    }

    /**
     * Recieves a line of data at this instance
     * @return
     * @throws IOException
     */
    public synchronized String recieve() throws IOException {
        if(tx.ready())
            return tx.readLine();
        return "";
    }

    /**
     * Add a new listener
     * @param s
     */
    public void addListener(SerialListener s) {
        listeners.add(s);
    }

    /**
     * Event Emitter
     * @param oEvent
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        // only catch new data
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                if (tx.ready())
                    for(SerialListener s : listeners)
                        s.onData(tx.readLine());
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

}
