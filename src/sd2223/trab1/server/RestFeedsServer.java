package sd2223.trab1.server;


import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab1.server.resources.FeedsResource;
import sd2223.trab1.server.util.Discovery;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

public class RestFeedsServer {

    private static Logger Log = Logger.getLogger(RestFeedsServer.class.getName());
    public static String myDomain;
    public static int idServer;

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static final int PORT = 8090;
    static String SERVICE = "FeedsService";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {

            ResourceConfig config = new ResourceConfig();
            config.register(FeedsResource.class);
            myDomain = args[0];
            idServer = Integer.parseInt(args[1]);


            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

            JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

            Log.info(String.format("%s Server ready @ %s\n", SERVICE, serverURI));
            Log.info("WHASS DISSS " + args[0] + " " + args[1]);


            Discovery.getInstance().announce("feeds", serverURI, myDomain);
            Discovery.getInstance().startListener();

            // More code can be executed here...
        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }

}
