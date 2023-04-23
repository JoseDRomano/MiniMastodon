package sd2223.trab1.client.rest;

import java.net.URI;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import sd2223.trab1.api.User;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.api.rest.UsersService;


@Singleton
public class RestUsersClient extends RestClient implements Users {

    final WebTarget target;

    public RestUsersClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(UsersService.PATH);
    }

    private Result<String> clt_createUser(User user) {

        Response r = target.request()
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));

        return super.toJavaResult(r, String.class);

    }

    private Result<User> clt_getUser(String name, String pwd) {

        Response r = target.path(name)
                .queryParam(UsersService.PWD, pwd).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return super.toJavaResult(r, User.class);

    }

    private Result<User> clt_UpdateUser(String name, String pwd, User user) {
        Response r = target.path(name)
                .queryParam(UsersService.PWD, pwd).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(user, MediaType.APPLICATION_JSON));
        return super.toJavaResult(r, User.class);
    }

    private Result<Void> clt_verifyPassword(String name, String pwd) {
        Response r = target.path(name).path(UsersService.PWD)
                .queryParam(UsersService.PWD, pwd).request()
                .get();

        return super.toJavaResult(r, Void.class);
    }


    private Result<Boolean> clt_hasUserNoPwd(String name) {
        Response r = target.path(name).path("nopwd")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return super.toJavaResult(r, Boolean.class);
    }

    private Result<List<User>> clt_SearchUsers(String pattern) {
        Response r = target.queryParam(UsersService.QUERY, pattern).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        GenericType<List<User>> entityType = new GenericType<>() {
        };
        return super.toJavaResult(r, entityType);
    }

    private Result<User> clt_DeleteUser(String name, String pwd) {
        Response r = target.path(name)
                .queryParam(UsersService.PWD, pwd).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();
        return super.toJavaResult(r, User.class);


    }


    @Override
    public Result<String> createUser(User user) {
        return super.reTry(() -> clt_createUser(user));
    }

    @Override
    public Result<User> getUser(String name, String pwd) {
        return super.reTry(() -> clt_getUser(name, pwd));
    }


    @Override
    public Result<User> updateUser(String name, String pwd, User user) {
        return super.reTry(() -> clt_UpdateUser(name, pwd, user));
    }

    public Result<Void> verifyPassword(String name, String pwd) {
        return super.reTry(() -> clt_verifyPassword(name, pwd));
    }


    public Result<Boolean> hasUserNoPwd(String name) {
        return super.reTry(() -> clt_hasUserNoPwd(name));
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return super.reTry(() -> clt_SearchUsers(pattern));
    }

    @Override
    public Result<User> deleteUser(String name, String pwd) {
        return super.reTry(() -> clt_DeleteUser(name, pwd));
    }


    private Result.ErrorCode errorCode(int status) {
        switch (status) {
            case 403:
                return Result.ErrorCode.FORBIDDEN;
            case 404:
                return Result.ErrorCode.NOT_FOUND;
            case 409:
                return Result.ErrorCode.CONFLICT;
            default:
                return Result.ErrorCode.INTERNAL_ERROR;

        }
    }

}