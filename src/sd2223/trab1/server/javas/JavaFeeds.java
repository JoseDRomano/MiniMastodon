package sd2223.trab1.server.javas;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.client.ClientFactory;
import sd2223.trab1.client.rest.RestFeedsClient;
import sd2223.trab1.client.rest.RestUsersClient;

import sd2223.trab1.server.RestFeedsServer;
import sd2223.trab1.server.util.Discovery;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class JavaFeeds implements Feeds {
    private static ConcurrentHashMap<String, List<Message>> userMessages = new ConcurrentHashMap<>();//Key - idUSer , value - Mapa com id e conteudo da mensagem.
    private static ConcurrentHashMap<String, List<String>> userSubscribers = new ConcurrentHashMap<>(); //Key - idUSer , value - Lista de usar que ele subscreveu.


    private static Logger Log = Logger.getLogger(JavaFeeds.class.getName());


    public static int counter;


    public JavaFeeds() {
        counter = 0;
    }


    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) throws URISyntaxException {
        String[] currUser = user.split("@");
        String name = currUser[0];
        String userDomain = currUser[1];
        Long msgID = counter * (256L + RestFeedsServer.idServer);
        counter++;
        Result checkUser = ClientFactory.getClient(userDomain).getUser(name, pwd);


        if (!userDomain.equals(RestFeedsServer.myDomain)) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        if (msg == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        if (!checkUser.isOK()) {
            return Result.error(Result.ErrorCode.valueOf(checkUser.error().toString()));
        }

        List<Message> messages = userMessages.get(name);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        msg.setId(msgID);
        messages.add(msg);
        userMessages.put(name, messages);
        return Result.ok(msgID);
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) throws URISyntaxException {
        String[] currUser = user.split("@");
        String name = currUser[0];
        String userDomain = currUser[1];


        Result checkUser = ClientFactory.getClient(userDomain).getUser(name, pwd);

        if (!checkUser.isOK()) {
            throw new WebApplicationException(Response.Status.valueOf(checkUser.error().toString()));
        }
        List<Message> messages = userMessages.get(name);

        if (!checkMessage(messages, mid)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            messages.remove(getCurrMessage(messages, mid));

        }
        return Result.ok();
    }

    @Override
    public Result<Message> getMessage(String user, long mid) throws URISyntaxException {

        List<Message> userMessages = getMessages(user, 0).value();

        if (getCurrMessage(userMessages, mid) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // return the message with the given mid
        return Result.ok(getCurrMessage(userMessages, mid));

    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) throws URISyntaxException {
        List<Message> userMessages ;
        String[] us = user.split("@");

        if (!us[1].equals(RestFeedsServer.myDomain)) {
            userMessages = ClientFactory.getFeeds(us[1]).getMessages(user, time).value();
        } else {
            userMessages = getInternalMessages(user, time).value();
        }
        if ((userSubscribers != null) && userSubscribers.containsKey(user)) {
            for (String u : userSubscribers.get(user)) {

                userMessages.addAll(getInternalMessages(u, time).value());
            }
        }
        return Result.ok(userMessages);
    }


    @Override
    public Result<List<Message>> getInternalMessages(String user, long time) throws URISyntaxException {
        String[] currUser = user.split("@");
        String name = currUser[0];
        String userDomain = currUser[1];
        List<Message> messages;


        if (!RestFeedsServer.myDomain.equals(userDomain)) {
            Result farUser = ClientFactory.getFeeds(userDomain).getInternalMessages(user, time);
            messages = (List<Message>) farUser.value();
        } else {
            Result checkUser = ClientFactory.getClient(userDomain).hasUserNoPwd(name);
            if (!checkUser.isOK()) {
                throw new WebApplicationException(Response.Status.valueOf(checkUser.error().toString()));
            }
            if (checkUser.value().equals(false)) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            messages = userMessages.get(name);
        }
        List<Message> result = new ArrayList<>();
        if (messages == null || messages.isEmpty()) {
        } else {
            for (Message message : messages) {
                if (message.getCreationTime() > time && message.getUser().equals(name)) {
                    result.add(message);
                }
            }
        }
        return Result.ok(result);
    }


    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) throws URISyntaxException {
        String[] currUser = user.split("@");
        String[] userToSub = userSub.split("@");
        String currName = currUser[0];
        String currDomain = currUser[1];
        String subName = userToSub[0];
        String subDomain = userToSub[1];
        List<String> subs = new ArrayList<>();
        List<Message> msg = new ArrayList<>();

        //fazer chamada ao server de users de URI tal
        Result checkUser = ClientFactory.getClient(currDomain).getUser(currName, pwd);
        Result subUser = ClientFactory.getClient(subDomain).getUser(subDomain, null);


        if (!checkUser.isOK() || subUser.error().equals(Result.ErrorCode.NOT_FOUND)) {
            throw new WebApplicationException(Response.Status.valueOf(checkUser.error().toString()));
        }

        subs.add(userSub);

        if (userSubscribers.get(user) == null) {
            userSubscribers.put(user, subs);

        } else if (!userSubscribers.get(user).contains(userSub)) {
            userSubscribers.get(user).add(userSub);
        }

        if (!userMessages.containsKey(currName)) {
            userMessages.put(currName, msg);
        }
        return Result.ok();
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) throws URISyntaxException {
        String[] currUser = user.split("@");
        String[] userToSub = userSub.split("@");
        String currName = currUser[0];
        String currDomain = currUser[1];
        String subName = userToSub[0];
        String subDomain = userToSub[1];

        //fazer chamada ao server de users de URI tal
        Result checkUser = ClientFactory.getClient(currDomain).getUser(currName, pwd);
        Result subUser = ClientFactory.getClient(subDomain).getUser(subDomain, null);


        if (!checkUser.isOK() || subUser.error().equals(Result.ErrorCode.NOT_FOUND)) {
            throw new WebApplicationException(Response.Status.valueOf(checkUser.error().toString()));
        }

        List<String> subs = userSubscribers.get(user);

        if (subs.contains(userSub)) {
            subs.remove(userSub);
        }
        return Result.ok();
    }

    @Override
    public Result<List<String>> listSubs(String user) throws URISyntaxException {
        String[] currUser = user.split("@");
        String name = currUser[0];
        String userDomain = currUser[1];
        Result checkUser = ClientFactory.getClient(userDomain).hasUserNoPwd(name);
        if (!checkUser.isOK()) {
            throw new WebApplicationException(Response.Status.valueOf(checkUser.error().toString()));
        }
        if (checkUser.value().equals(false)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (userSubscribers.get(user) == null) {
            List<String> subs = new ArrayList<>();
            userSubscribers.put(user, subs);
        }
        return Result.ok(userSubscribers.get(user));
    }


    private boolean checkMessage(List<Message> message, Long mid) {
        if (message != null) {
            for (Message msg : message) {
                if (msg.getId() == mid) {
                    return true;
                }
            }
        }
        return false;
    }


    private Message getCurrMessage(List<Message> message, Long mid) {
        Message thisMsg = null;
        for (Message msg : message) {
            if (msg.getId() == mid) {
                thisMsg = msg;
                break;
            }
        }
        return thisMsg;
    }


}
