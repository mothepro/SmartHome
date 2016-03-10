import java.io.IOException;
import java.util.Scanner;

/**
 * Simple Serial Example
 */
public class Test implements SerialListener {
    Serial serial;

    /**
     * Connect to a device by name
     * @param names
     */
    public Test(String[] names) {
        for (String s: names) {
            System.out.println("Attempting to connect to device: " + s);

            try {
                serial = new Serial(s);
                serial.addListener(this);
                send("Hello");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test out a device
     * @param args
     */
    public static void main(String[] args) {
        // without arguments
        if(args.length == 0) {
            System.out.println("Enter a Device Port Name to connect to:");

            Scanner reader = new Scanner(System.in);
            args[0] = reader.nextLine();
        }

        new Test(args);

        //the following lines will keep this app alive for 1000 seconds,
        new Thread() {
            @Override
            public void run() { }
        };
    }

    /**
     * Send data to device
     */
    public void send (String data) {
        try {
            serial.send(data);
            System.out.println(serial.getPortName() + " << "+ data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * When we recieve data from device
     */
    public void onData(String data) {
        System.out.println(serial.getPortName() + " >> "+ data);
    }
}
