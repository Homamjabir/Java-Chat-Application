/**
 * The following class handles all incoming data from the server
 */

package client.communication;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Receiver implements Runnable {

    private final SSLSocket sslSocket;
    private final BufferedReader indata;

    /**
     * Constructor
     * @param socket Client socket.
     * @throws IOException Thrown if their is problem with the incoming data
     */
    public Receiver(SSLSocket socket) throws IOException
    {
        this.sslSocket = socket;
        InputStream input = sslSocket.getInputStream();
        this.indata = new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Gets called when the thread starts and listens for incoming input from server and prints it to the client.
     */
    public void run() {
        try {
            String message;

            while (true) {
                message = indata.readLine();

                if(null == message)
                    break;
                else if(message.split(" ")[0].equals("-f123123"))
                    receiveFile(message.split(" ")[1]);
                else
                    System.out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String fileSize) throws IOException
    {
        int size = Integer.parseInt(fileSize);
        byte[] fileContent = new byte[size];

        InputStream inputStream = this.sslSocket.getInputStream();
        inputStream.read(fileContent, 0, fileContent.length);

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Homam\\Desktop\\new_file.txt");
        fileOutputStream.write(fileContent);
        fileOutputStream.flush();
        System.out.println("The file was downloaded successfully!");

    }

}