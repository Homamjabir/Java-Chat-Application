package client.communication;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Receiver implements Runnable {

    private final SSLSocket sslSocket;
    private final BufferedReader indata;

    public Receiver(SSLSocket socket) throws IOException
    {
        this.sslSocket = socket;
        InputStream input = sslSocket.getInputStream();
        this.indata = new BufferedReader(new InputStreamReader(input));
    }

    public void run() {

        try {

            String message;

            while (true) {
                message = indata.readLine();

                if(null == message)
                    break;
                else if(message.split(" ")[0].equals("-f"))
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

        System.out.println("Enter path to save file including the filename");
        String path = this.indata.readLine();

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Homam\\Desktop\\new_file.txt");
        fileOutputStream.write(fileContent);
        fileOutputStream.flush();
        System.out.println("The file was downloaded successfully!");


    }

}