package sd2223.trab1.server.resources;

import jakarta.jws.WebService;
import sd2223.trab1.api.User;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.api.soap.UsersException;
import sd2223.trab1.api.soap.UsersService;
import sd2223.trab1.server.javas.JavaUsers;

import java.util.List;
import java.util.logging.Logger;


@WebService(serviceName = UsersService.NAME, targetNamespace = UsersService.NAMESPACE, endpointInterface = UsersService.INTERFACE)
public class SoapUsersServiceWeb extends SoapResource<UsersException> implements UsersService {

    private static Logger Log = Logger.getLogger(UsersResource.class.getName());
    final Users impl;

    public SoapUsersServiceWeb() {
        super((result) -> new UsersException(result.error().toString()));
        this.impl = new JavaUsers();
    }


    @Override
    public String createUser(User user) throws UsersException {
       return super.fromJavaResult(impl.createUser(user));

    }

    @Override
    public User getUser(String name, String pwd) throws UsersException {
        return super.fromJavaResult(impl.getUser(name, pwd));
    }

    @Override
    public User updateUser(String name, String pwd, User user) throws UsersException {
        return super.fromJavaResult(impl.updateUser(name, pwd, user));
    }

    @Override
    public User deleteUser(String name, String pwd) throws UsersException {
      return super.fromJavaResult(impl.deleteUser(name, pwd));
    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {
        return super.fromJavaResult(impl.searchUsers(pattern));
    }

    @Override
    public Boolean hasUserNoPwd(String name) throws UsersException {
        return super.fromJavaResult(impl.hasUserNoPwd(name));
    }

    @Override
    public Void verifyPassword(String name, String pwd) throws UsersException {
       return super.fromJavaResult(impl.verifyPassword(name, pwd));
    }
}
