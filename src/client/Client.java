/**
 *
 * The following class handles incoming and outgoing messages for the client
 * through two threads.
 */

package client;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import client.communication.*;

public class Client {

    private final SSLSocket sslSocket;

    /**
     * Creates a encrypted socket for communication with the server
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private Client() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
    {
        String hostName = "localhost";
        int portNumber = 4444;

        File cert = new File("src/client/server.crt");
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(cert));

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", certificate);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

        this.sslSocket = (SSLSocket) socketFactory.createSocket(hostName, portNumber);
    }

    private void createClient() throws IOException
    {

        Thread sender = new Thread(new Sender(this, sslSocket));
        Thread receiver = new Thread(new Receiver(sslSocket));
        sender.start();
        receiver.start();
    }

    public static void main( String[] args) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        Client client = new Client();
        client.createClient();
    }
}