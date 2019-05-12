import java.io.FileInputStream;
import javax.net.ssl.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class TLSClient
{
    private static final String tlsServerAddress = "192.168.1.81";
    private static final int port = 4443;

    public static void main(String[] args)
    {
        //setting system properties
        System.setProperty("javax.net.ssl.trustStore", "clientTrustore.ts");//defines the trustore file
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "false");
        System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
        System.setProperty("https.protocols", "SSLv2");
        if (true)
        {
            System.setProperty("javax.net.debug", "ssl");
        }

        try
        {
            SSLSocketFactory sslSocketFactory = getFactory(); //SSLSocketFactory creation
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(tlsServerAddress, port); //creates and initializes server socket

            new FileSharing(sslSocket).run(); //Creates a new FileSharing object with the ssl socket that was created and calls the run function

        } catch (IOException e1) {
            System.out.println("Exception: "+e1.getMessage());
            e1.printStackTrace();
        }
    }

    private static SSLSocketFactory getFactory()
    {
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            char [] pKeyPassword= "evg2018".toCharArray();
            InputStream keyInput = new FileInputStream("D:/MSC/1ο Εξάμηνο/Ασφάλεια/Εργασία Β1/TLSClient/evgKeystore.jks"); //keystore file
            keyStore.load(keyInput, pKeyPassword);
            keyInput.close();

            keyManagerFactory.init(keyStore, pKeyPassword);

            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();
    }
}