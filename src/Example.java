import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Simple Serial Example
 */
public class Example implements SerialListener {
    Serial serial;

    /**
     * Connect to a device by name
     * @param names
     */
    public Example(String[] names) {
        for (String s: names) {
            System.out.println("Attempting to connect to device: " + s);

            try {
                serial = new Serial(s);
                System.out.println("Success!");

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
            List<String> devs = Serial.getPortNames();

            System.out.println("We found "+ devs.size() +" devices.");
            for (String s : devs)
                System.out.println("- "+ s);

            System.out.println();
            System.out.println("Enter a Device Port Name to connect to");

            Scanner reader = new Scanner(System.in);

            args = new String[] { reader.nextLine() };
        }

        new Example(args);

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
