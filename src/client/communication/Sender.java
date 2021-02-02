package client.communication;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.io.File;
import java.nio.file.Files;

public class Sender implements Runnable {

    private final SSLSocket sslSocket;
    private BufferedReader indata;
    private PrintWriter outdata;

    public Sender(SSLSocket socket) {
        this.sslSocket = socket;
    }

    @Override
    public void run()  {
        try {
            this.outdata = new PrintWriter(this.sslSocket.getOutputStream(), true);
            this.indata = new BufferedReader(new InputStreamReader((System.in)));

            String message;

            outdata.println(indata.readLine());

            while(true) {
                message = indata.readLine();
                if(message.split(" ")[0].equals("-f"))
                    sendFile();
                else
                    outdata.println(message);
            }
        } catch (IOException e) {
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






}
