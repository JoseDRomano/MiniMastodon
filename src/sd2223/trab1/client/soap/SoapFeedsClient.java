package sd2223.trab1.client.soap;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.soap.FeedsService;

import javax.xml.namespace.QName;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SoapFeedsClient extends SoapClient implements Feeds {

    public SoapFeedsClient(URI serverURI) {
        super(serverURI);
    }


    private FeedsService stub;

    synchronized private FeedsService stub() {
        if (stub == null) {
            QName QNAME = new QName(FeedsService.NAMESPACE, FeedsService.NAME);
            Service service = Service.create(toURL(super.uri + WSDL), QNAME);
            this.stub = service.getPort(FeedsService.class);
            super.setTimeouts((BindingProvider) stub);
        }
        return stub;
    }

    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.postMessage(user, pwd, msg)));
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.removeFromPersonalFeed(user, mid, pwd)));
    }

    @Override
    public Result<Message> getMessage(String user, long mid) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.getMessage(user, mid)));
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.getMessages(user, time)));
    }

    @Override
    public Result<List<Message>> getInternalMessages(String user, long time) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.getInternalMessages(user, time)));
    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.subUser(user, userSub, pwd)));
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.unsubscribeUser(user, userSub, pwd)));
    }

    @Override
    public Result<List<String>> listSubs(String user) throws URISyntaxException {
        return super.reTry(() ->
                super.toJavaResult(() -> stub.listSubs(user)));
    }
}

