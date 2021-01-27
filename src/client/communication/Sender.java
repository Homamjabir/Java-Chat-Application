package client.communication;

import client.Client;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Sender implements Runnable {

    private final SSLSocket socket;
    private final Client client;

    public Sender(Client client, SSLSocket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run()  {
        try {
            PrintStream outdata = new PrintStream(this.socket.getOutputStream());
            BufferedReader indata = new BufferedReader(new InputStreamReader((System.in)));
            String message;

            outdata.println(indata.readLine());

            while(true) {
                message = indata.readLine();
                if(message.equals("-q"))
                    exit();
                outdata.println(message);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void exit() throws InterruptedException, IOException {

        this.client.setLoggedIn(false);
        Thread.sleep(500);
        this.socket.close();
        System.exit(0);

    }




}
