package sd2223.trab1.server.resources;

import java.net.URISyntaxException;
import java.util.*;

import jakarta.inject.Singleton;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;

import sd2223.trab1.api.rest.FeedsService;

import sd2223.trab1.server.javas.JavaFeeds;

@Singleton
public class FeedsResource extends RestResource implements FeedsService {

    final Feeds impl;

    public FeedsResource() {
        this.impl = new JavaFeeds();
    }


    @Override
    public Long postMessage(String user, String pwd, Message msg) throws URISyntaxException {
        return super.reTry(impl.postMessage(user, pwd, msg));
    }

    @Override
    public void removeFromPersonalFeed(String user, long mid, String pwd) throws URISyntaxException {
        super.reTry(impl.removeFromPersonalFeed(user, mid, pwd));
    }

    @Override
    public Message getMessage(String user, long mid) throws URISyntaxException {
        return super.reTry(impl.getMessage(user, mid));
    }

    @Override
    public List<Message> getMessages(String user, long time) throws URISyntaxException {
        return super.reTry(impl.getMessages(user, time));
    }


    public List<Message> getInternalMessages(String user,long time) throws URISyntaxException {
        return super.reTry(impl.getInternalMessages(user,time));
    }

    @Override
    public void subUser(String user, String userSub, String pwd) throws URISyntaxException {
        super.reTry(impl.subUser(user, userSub, pwd));
    }

    @Override
    public void unsubscribeUser(String user, String userSub, String pwd) throws URISyntaxException {
        super.reTry(impl.unsubscribeUser(user, userSub, pwd));
    }

    @Override
    public List<String> listSubs(String user) throws URISyntaxException {
        return super.reTry(impl.listSubs(user));
    }

}
