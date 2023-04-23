package sd2223.trab1.server.resources;

import jakarta.jws.WebService;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.soap.FeedsException;
import sd2223.trab1.api.soap.FeedsService;
import sd2223.trab1.server.javas.JavaFeeds;

import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@WebService(serviceName = FeedsService.NAME, targetNamespace = FeedsService.NAMESPACE, endpointInterface = FeedsService.INTERFACE)
public class SoapFeedsServiceWeb extends SoapResource<FeedsException> implements FeedsService {

    private static Logger Log = Logger.getLogger(FeedsResource.class.getName());
    final Feeds impl;

    public SoapFeedsServiceWeb() {
        super((result) -> new FeedsException(result.error().toString()));
        this.impl = new JavaFeeds();
    }

    @Override
    public long postMessage(String user, String pwd, Message msg) throws FeedsException, URISyntaxException {
       return super.fromJavaResult(impl.postMessage(user, pwd, msg));

    }

    @Override
    public Void removeFromPersonalFeed(String user, long mid, String pwd) throws FeedsException, URISyntaxException {
        return  super.fromJavaResult(impl.removeFromPersonalFeed(user, mid, pwd));

    }

    @Override
    public Message getMessage(String user, long mid) throws FeedsException, URISyntaxException {
        return super.fromJavaResult(impl.getMessage(user, mid));
    }

    @Override
    public List<Message> getMessages(String user, long time) throws FeedsException, URISyntaxException {
        return super.fromJavaResult(impl.getMessages(user, time));
    }

    @Override
    public List<Message> getInternalMessages(String user, long time) throws FeedsException, URISyntaxException {
        return super.fromJavaResult(impl.getInternalMessages(user, time));

    }

    @Override
    public Void subUser(String user, String userSub, String pwd) throws FeedsException, URISyntaxException {
      return super.fromJavaResult(impl.subUser(user, userSub, pwd));
    }

    @Override
    public Void unsubscribeUser(String user, String userSub, String pwd) throws FeedsException, URISyntaxException {
       return super.fromJavaResult(impl.unsubscribeUser(user, userSub, pwd));
    }

    @Override
    public List<String> listSubs(String user) throws FeedsException, URISyntaxException {
        return super.fromJavaResult(impl.listSubs(user));
    }
}
