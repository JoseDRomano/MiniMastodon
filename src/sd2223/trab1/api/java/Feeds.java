package sd2223.trab1.api.java;

import sd2223.trab1.api.Message;
import sd2223.trab1.api.User;

import java.net.URISyntaxException;
import java.util.List;

public interface Feeds {
    Result<Long> postMessage(String user, String pwd, Message msg) throws URISyntaxException;

    Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) throws URISyntaxException;

    Result<Message> getMessage(String user, long mid) throws URISyntaxException;
    Result<List<Message>> getMessages(String user, long time) throws URISyntaxException;
    Result<List<Message>> getInternalMessages(String user, long time) throws URISyntaxException;

    Result<Void> subUser(String user, String userSub, String pwd) throws URISyntaxException;

    Result<Void> unsubscribeUser(String user, String userSub, String pwd) throws URISyntaxException;

    Result<List<String>> listSubs(String user) throws URISyntaxException;


}
