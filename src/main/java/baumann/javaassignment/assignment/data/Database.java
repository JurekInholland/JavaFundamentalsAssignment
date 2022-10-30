package baumann.javaassignment.assignment.data;

import baumann.javaassignment.assignment.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final List<User> users;

    public Database() {
        users = new ArrayList<>();
        users.addAll(List.of(new User("admin", "admin"), new User("user", "user"), new User("asd", "asd")));

    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByName(String name) {
        return users.stream().filter(u -> u.getUserName().equals(name)).findFirst().orElse(null);
    }
    }
}
