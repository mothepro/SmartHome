
// SENDING DATA OR COMMANDS 
//------------------------------------------------------------

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Send_data {
    public static BufferedReader input;
    public static OutputStream output;

    public static synchronized void writeData(String data) {
        System.out.println("Sent: " + data);
        try {
            output.write(data.getBytes());
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public static void main(String[] args) {

        try {
            SerialTest obj = new SerialTest();
            int c = 0;
            obj.initialize();
            input = SerialTest.input;
            output = SerialTest.output;
            InputStreamReader Ir = new InputStreamReader(System.in);
            BufferedReader Br = new BufferedReader(Ir);
            while (c != 3) {
                System.out.println("LED ON / OFF MENU.");
                System.out.println("1.LED ON");
                System.out.println("2.LED OFF");
                System.out.print("Enter your choice :");
                c = Integer.parseInt(Br.readLine());

                switch (c) {
                    case 1:
                        writeData("1");
                        break;

                    case 2:
                        writeData("0");
                        break;

                    case 3:
                        System.exit(0);
                }
            }

            String inputLine = input.readLine();
            System.out.println(inputLine);

            obj.close();

        } catch (Exception e) {
        }

    }
}
