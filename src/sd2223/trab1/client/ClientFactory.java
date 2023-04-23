package sd2223.trab1.client;

import jakarta.inject.Singleton;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.client.rest.RestFeedsClient;
import sd2223.trab1.client.rest.RestUsersClient;
import sd2223.trab1.client.soap.SoapFeedsClient;
import sd2223.trab1.client.soap.SoapUsersClient;
import sd2223.trab1.server.util.Discovery;

import java.net.URI;
import java.net.URISyntaxException;

@Singleton
public class ClientFactory {

    public static Users getClient(String domain) throws URISyntaxException {
        URI serverURI = Discovery.getInstance().knownUrisOf("users", domain)[0];
        if (serverURI.toString().endsWith("rest"))
            return new RestUsersClient(serverURI);
       else return new SoapUsersClient(serverURI);
    }

    public static Feeds getFeeds(String domain) throws  URISyntaxException{
        URI serverURI = Discovery.getInstance().knownUrisOf("feeds", domain)[0];
        if (serverURI.toString().endsWith("rest"))
            return new RestFeedsClient(serverURI);
        else return new SoapFeedsClient(serverURI);
    }
}
