package sd2223.trab1.server;

import jakarta.xml.ws.Endpoint;
import sd2223.trab1.server.resources.SoapUsersServiceWeb;
import sd2223.trab1.server.util.Discovery;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoapUsersServer {

    public static final int PORT = 8081;
    public static final String SERVICE_NAME = "users";
    public static String SERVER_BASE_URI = "http://%s:%s/soap";

    public static String myDomain;

    private static Logger Log = Logger.getLogger(SoapUsersServer.class.getName());



    public static void main(String[] args) throws Exception {


            System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

            Log.setLevel(Level.INFO);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_BASE_URI, ip, PORT);
            myDomain = args[0];


            Endpoint.publish(serverURI.replace(ip, "0.0.0.0"), new SoapUsersServiceWeb());
            Log.info(String.format("%s Soap Server ready @ %s\n", SERVICE_NAME, serverURI));
            Discovery.getInstance().announce("users", serverURI, myDomain);
            Discovery.getInstance().startListener();

    }
}
