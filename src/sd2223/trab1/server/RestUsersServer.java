package sd2223.trab1.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab1.server.resources.UsersResource;
import sd2223.trab1.server.util.Discovery;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

public class RestUsersServer {

    private static Logger Log = Logger.getLogger(RestUsersServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "UsersService";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    private static String myDomain;


    public static void main(String[] args) {
        try {

            ResourceConfig config = new ResourceConfig();
            config.register(UsersResource.class);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
            JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);
            myDomain = args[0];


            Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

            Discovery.getInstance().announce("users",serverURI, myDomain);
            Discovery.getInstance().startListener();

            //More code can be executed here...
        } catch( Exception e) {
            Log.severe(e.getMessage());
        }

    }
}
