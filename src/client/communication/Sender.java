/**
 * The following class handles all outgoing data to the server
 */

package client.communication;

import client.Client;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.io.File;
import java.nio.file.Files;

public class Sender implements Runnable {

    private final SSLSocket sslSocket;
    private final Client client;
    private BufferedReader indata;
    private PrintWriter outdata;

    /**
     * Constructor
     * @param socket Client socket.
     * @throws IOException Thrown if their is problem with the incoming data
     */
    public Sender(Client client, SSLSocket socket) {
        this.client = client;
        this.sslSocket = socket;
    }

    /**
     * Gets called when the thread starts and listens for incoming input from client and sends it to the server
     */
    public void run()  {
        try {
            this.outdata = new PrintWriter(this.sslSocket.getOutputStream(), true);
            this.indata = new BufferedReader(new InputStreamReader((System.in)));

            String message;

            outdata.println(indata.readLine());

            while(true) {
                message = indata.readLine();
                String[] msg = message.split(" ");
                if(msg[0].equals("-f"))
                    sendFile();
                else if(msg[0].equals("-q"))
                    ending();
                else
                    outdata.println(message);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendFile() throws IOException {

        System.out.println("Enter full path of file");
        String filePath = this.indata.readLine();


        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        OutputStream outputStream = this.sslSocket.getOutputStream();
        this.outdata.println("-f " + fileContent.length);
        outputStream.write(fileContent, 0, fileContent.length);
        outputStream.flush();
        System.out.println("size: " + fileContent.length);


//C:\Users\Homam\Desktop\test.txt
    }

    private void ending() throws InterruptedException, IOException {

        Thread.sleep(500);
        this.sslSocket.close();
        System.exit(0);

    }
}
