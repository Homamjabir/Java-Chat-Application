package server;

/*
 * Created by: Homam Jabir
 *
 * The following class creates a server and handles all incoming connections.
 */

import java.io.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Server {

    private final SSLServerSocketFactory socketFactory;
    private final MessageHandler messageHandler;
    private final ClientData clientData;
    private final Lobby lobby;

    /**
     * Constructor.
     */
    Server() throws IOException, NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        char[] password = "rootroot".toCharArray();
        this.lobby = new Lobby();
        this.clientData = new ClientData();
        this.messageHandler = new MessageHandler(this.lobby, this.clientData);

        InputStream inputStream = new FileInputStream("src/server/.keystore");
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        keyStore.load(inputStream, password);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        this.socketFactory = context.getServerSocketFactory();
    }

    /**
     * Creates a server that handles all the incoming connections and adding them to a client list.
     */
    private void createServer() {
        SSLServerSocket sslServerSocket;
        SSLSocket sslSocket;

        try {
            sslServerSocket = (SSLServerSocket) this.socketFactory.createServerSocket(4444);
            System.out.println("The server has now been created");

            while (true) {
                sslSocket = (SSLSocket) sslServerSocket.accept();
                ChatClient chatClient = new ChatClient(sslSocket, this.messageHandler);
                Thread thread = new Thread(chatClient);
                thread.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, NoSuchProviderException
    {
        Server server = new Server();
        server.createServer();
    }
}
