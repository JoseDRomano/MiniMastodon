package sd2223.trab1.server.util;

import java.net.*;
import java.util.*;
import java.util.logging.Logger;


/**
 * Implementation of the multicast discovery service
 */
class DiscoveryImpl implements Discovery {

    private static Logger Log = Logger.getLogger(Discovery.class.getName());

    // The pre-aggreed multicast endpoint assigned to perform discovery.

    static final int DISCOVERY_RETRY_TIMEOUT = 5000;
    static final int DISCOVERY_ANNOUNCE_PERIOD = 1000;

    // Replace with appropriate values...
    static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);

    // Used separate the two fields that make up a service announcement.
    private static final String DELIMITER = "\t";

    private static final int MAX_DATAGRAM_SIZE = 65536;


    private static Discovery singleton;

    public int serverMSG;
    private HashMap<String, HashMap<String, List<URI>>> URIS;

    synchronized static Discovery getInstance() {
        if (singleton == null) {
            singleton = new DiscoveryImpl();
        }
        return singleton;
    }

    private DiscoveryImpl() {
        //this.startListener();
        this.URIS = new HashMap<>();
        serverMSG = 0;
    }

    @Override
    public void announce(String serviceName, String serviceURI, String domain) {
        Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s\n", DISCOVERY_ADDR, serviceName,
                serviceURI));

        var pktBytes = String.format("%s%s%s%s%s", domain, ":", serviceName, DELIMITER, serviceURI).getBytes();
        Log.info("AAAAAAAAAAAAAAAAA " + Arrays.toString(pktBytes));
        var pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);

        // start thread to send periodic announcements
        new Thread(() -> {
            try (var ds = new DatagramSocket()) {
                while (true) {
                    try {
                        ds.send(pkt);
                        Thread.sleep(DISCOVERY_ANNOUNCE_PERIOD);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public URI[] knownUrisOf(String serviceName, String domain) throws URISyntaxException {

        if (!URIS.containsKey(serviceName)) {

            return new URI[0];
        } else {
            HashMap<String, List<URI>> help = URIS.get(serviceName); // key - dominio values - Lista URIS

            if (!help.containsKey(domain)) {

                return new URI[0];
            }

            if (help == null) {

                return new URI[0];
            }


            URI[] aux = new URI[help.size()];
            for (int i = 0; i < help.size(); i++) {

                aux[i] = new URI(help.get(domain).get(i).toString());

            }
            return aux;
        }
    }

    public void startListener() {
        Log.info(String.format("Starting discovery on multicast group: %s, port: %d\n", DISCOVERY_ADDR.getAddress(),
                DISCOVERY_ADDR.getPort()));

        new Thread(() -> {
            try (var ms = new MulticastSocket(DISCOVERY_ADDR.getPort())) {
                ms.joinGroup(DISCOVERY_ADDR, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
                for (; ; ) {
                    try {
                        var pkt = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);
                        ms.receive(pkt);

                        var msg = new String(pkt.getData(), 0, pkt.getLength());


                        var parts = msg.split(DELIMITER);
                        var firstPart = parts[0].split(":");
                        String domain = firstPart[0];
                        String serviceName = firstPart[1];


                        if (firstPart.length == 2) {


                            if (URIS.containsKey(serviceName)) {

                                HashMap<String, List<URI>> tmp = URIS.get(serviceName);
                                if (tmp.containsKey(domain)) {
                                    List<URI> uris = tmp.get(domain);
                                    uris.add(new URI(parts[1]));

                                } else {

                                    List<URI> uris = new ArrayList<>();
                                    uris.add(new URI(parts[1]));
                                    tmp.put(domain, uris);

                                }

                            } else {

                                List<URI> uris = new ArrayList<URI>();

                                uris.add(new URI(parts[1]));

                                HashMap<String, List<URI>> tmp = new HashMap<>();

                                tmp.put(serviceName, uris);

                                URIS.put(serviceName, tmp);

                            }

                        }

                    } catch (Exception x) {
                        Log.info("EXCECAO1");
                        x.printStackTrace();
                    }
                }
            } catch (Exception x) {
                Log.info("EXCECAO2");
                x.printStackTrace();
            }
        }).start();
    }


}