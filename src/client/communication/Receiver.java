package client.communication;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Receiver implements Runnable {

    private final SSLSocket socket;

    public Receiver(SSLSocket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            InputStream input = socket.getInputStream();
            BufferedReader indata = new BufferedReader(new InputStreamReader(input));
            String message;

            while (true) {
                message = indata.readLine();
                
                if(null == message)
                    break;
                else
                    System.out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}