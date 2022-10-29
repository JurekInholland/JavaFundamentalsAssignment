package baumann.javaassignment.assignment.data;

import baumann.javaassignment.assignment.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final List<User> users;

    public Database() {
        users = new ArrayList<>();
        users.addAll(List.of(
                new User(0, "John", "Doe", LocalDate.of(1990, 2, 12), "password123"),
                new User(1, "Jane", "Doe", LocalDate.of(1982, 3, 4), "s6cur!ty"),
                new User(2, "asd", "Mustermann", LocalDate.of(2000, 1, 1), "asd")
        ));

    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByName(String name) {
        return users.stream().filter(user -> user.getFirstName().equals(name)).findFirst().orElse(null);
    }
}
