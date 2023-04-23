package sd2223.trab1.server.javas;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import sd2223.trab1.api.User;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.java.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
public class JavaUsers implements Users {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    @Override
    public Result<String> createUser(User user) {

        // Check if user data is valid
        if (user.getName() == null || user.getPwd() == null || user.getDisplayName() == null || user.getDomain() == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        // Insert user, checking if name already exists
        if (users.putIfAbsent(user.getName(), user) != null) {
            return Result.error(Result.ErrorCode.CONFLICT);
        }
        users.put(user.getName(), user);
        return Result.ok(user.getName() + "@" + user.getDomain());
    }

    @Override
    public Result<User> getUser(String name, String pwd) {

        // Check if user is valid
        if (name == null || pwd == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        User user = users.get(name);

        // Check if user exists
        if (user == null) {
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if (!user.getPwd().equals(pwd)) {
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        return Result.ok(user);
    }

    @Override
    public Result<User> updateUser(String name, String pwd, User newUser) {
        // Log.info("updateUser : user = " + name + "; pwd = " + pwd);

        // Check if user is valid
        if (name == null && pwd == null && newUser.getDisplayName() == null && newUser.getDomain() == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        //check if the given name equals the name of the user
        if (!name.equals(newUser.getName())) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        //Now that the inputs are valid, check if the user exists in order to change it
        User existingUser = this.users.get(name);
        if (existingUser == null) {
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }
        //Check if password is correct
        if (!existingUser.getPwd().equals(pwd)) {
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
        //Then,change
        existingUser.setPwd(newUser.getPwd() != null ? newUser.getPwd() : existingUser.getPwd());
        existingUser.setDisplayName(newUser.getDisplayName() != null ? newUser.getDisplayName() : existingUser.getDisplayName());
        existingUser.setDomain(newUser.getDomain() != null ? newUser.getDomain() : existingUser.getDomain());

        return Result.ok(existingUser);
    }

    @Override
    public Result<User> deleteUser(String name, String pwd) {
        //Log.info("deleteUser : user = " + name + "; pwd = " + pwd);

        // Check if user is valid
        if (name == null || pwd == null) {
            // Log.info("Name or Password null.");
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        // Check if user exists
        User existingUser = users.get(name);
        if (existingUser == null) {
            //Log.info("User does not exist.");
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        // Check if the password is correct
        if (!existingUser.getPwd().equals(pwd)) {
            // Log.info("Password is incorrect.");
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
        users.remove(name);
        return Result.ok(existingUser);
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        //Log.info("searchUsers : pattern = " + pattern);
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getName().toLowerCase().contains(pattern.toLowerCase()))
                filteredUsers.add(new User(user.getName(), "", user.getDomain(), user.getDisplayName()));
        }
        return Result.ok(filteredUsers);
    }


    public Result<Void> verifyPassword(String name, String pwd) {
        var res = getUser(name, pwd);

        if (res.isOK())
            return Result.ok();
        else
            return Result.error(res.error());
    }

    public Result<Boolean> hasUserNoPwd(String name) {
        return Result.ok(users.containsKey(name));
    }


}
