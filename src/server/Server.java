/**
 * The following class creates a server and handles all incoming connections.
 */

package server;

import server.lobbys.Lobby;

import java.io.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Server {

    private final SSLServerSocketFactory socketFactory;
    private final Lobby lobby;

    /**
     * Creates a encrypted socket for communication with the clients
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    Server() throws IOException, NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        char[] password = "rootroot".toCharArray();
        this.lobby = new Lobby();

        InputStream inputStream = new FileInputStream("src/server/.keystore");
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        keyStore.load(inputStream, password);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        this.socketFactory = context.getServerSocketFactory();
    }

    private void createServer() {
        SSLServerSocket sslServerSocket;
        SSLSocket sslSocket;

        try {
            sslServerSocket = (SSLServerSocket) this.socketFactory.createServerSocket(4444);
            System.out.println("The server has now been created");

            while (true) {
                sslSocket = (SSLSocket) sslServerSocket.accept();
                this.lobby.addClient(sslSocket);
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
