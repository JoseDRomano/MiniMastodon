package sd2223.trab1.server.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>A class interface to perform service discovery based on periodic
 * announcements over multicast communication.</p>
 *
 */

public interface Discovery {

    /**
     * Used to announce the URI of the given service name.
     *
     * @param serviceName - the name of the service
     * @param serviceURI  - the uri of the service
     */
    public void announce(String serviceName, String serviceURI,String domain);

    /**
     * Get discovered URIs for a given service name
     *
     * @param serviceName - name of the service
     * @return array with the discovered URIs for the given service name.
     */
    public URI[] knownUrisOf(String serviceName,String domain) throws URISyntaxException;

    /**
     * Get the instance of the Discovery service
     *
     * @return the singleton instance of the Discovery service
     */
    public static Discovery getInstance() {
        return DiscoveryImpl.getInstance();
    }

    public void startListener();
}