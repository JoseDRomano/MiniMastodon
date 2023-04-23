package sd2223.trab1.client.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sd2223.trab1.api.Message;
import sd2223.trab1.api.java.Feeds;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.rest.FeedsService;
import sd2223.trab1.client.rest.RestClient;

import java.net.URI;
import java.util.List;

public class RestFeedsClient extends RestClient implements Feeds {
    final WebTarget target;

    public RestFeedsClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(FeedsService.PATH);
    }

    private Result<Long> clt_PostMessage(String user, String pwd, Message msg) {
        Response r = target.path(user).queryParam(FeedsService.PWD, pwd)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(msg, MediaType.APPLICATION_JSON));

        return super.toJavaResult(r, Long.class);

    }

    private Result<Message> clt_GetMessage(String user, long mid) {
        Response r = target.path(user)
                .path(Long.toString(mid))
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return super.toJavaResult(r, Message.class);
    }


    private Result<List<Message>> clt_GetMessages(String user, long time) {
        Response r = target.path(user)
                .queryParam(FeedsService.TIME, time)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        GenericType<List<Message>> entityType = new GenericType<>() {
        };
        return super.toJavaResult(r, entityType);
    }


    private Result<List<Message>> clt_getInternalMessages(String user, long time) {
        Response r = target.path(user)
                .path("gim")
                .queryParam(FeedsService.TIME, time)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        GenericType<List<Message>> entityType = new GenericType<>() {
        };
        return super.toJavaResult(r, entityType);
    }

    private Result<Void> clt_subUser(String user, String userSub, String pwd) {
        Response r = target.path("sub")
                .path(user)
                .path(userSub)
                .queryParam(FeedsService.PWD, pwd)
                .request()
                .post(null);

        return super.toJavaResult(r, Void.class);
    }


    private Result<Void> clt_UnsubscribeUser(String user, String userSub, String pwd) {
        Response r = target.path("sub")
                .path(user)
                .path(userSub)
                .queryParam(FeedsService.PWD, pwd)
                .request()
                .delete();

        return super.toJavaResult(r, Void.class);
    }


    private Result<Void> clt_removeFromPersonalFeed(String user, long mid, String pwd) {
        Response r = target.path(user)
                .path(Long.toString(mid))
                .queryParam(FeedsService.PWD, pwd)
                .request()
                .delete();


        return super.toJavaResult(r, Void.class);
    }

    private Result<List<String>> clt_ListSubs(String user) {
        Response r = target.path("sub").path("list").path(user)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        GenericType<List<String>> entityType = new GenericType<>() {
        };
        return super.toJavaResult(r, entityType);
    }


    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {
        return super.reTry(() -> clt_PostMessage(user, pwd, msg));
    }

    @Override
    public Result<Message> getMessage(String user, long mid) {
        return super.reTry(() -> clt_GetMessage(user, mid));
    }

    @Override
    public Result<List<Message>> getInternalMessages(String user, long time) {
        return super.reTry(() -> clt_getInternalMessages(user, time));
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) {
        return super.reTry(() -> clt_GetMessages(user, time));

    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) {
        return super.reTry(() -> clt_subUser(user, userSub, pwd));
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
        return super.reTry(() -> clt_UnsubscribeUser(user, userSub, pwd));
    }

    @Override
    public Result<List<String>> listSubs(String user) {
        return super.reTry(() -> clt_ListSubs(user));
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
        return super.reTry(() -> clt_removeFromPersonalFeed(user, mid, pwd));
    }


}
