package baumann.javaassignment.assignment.data;

import baumann.javaassignment.assignment.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final List<User> users;
    private List<Item> items;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public Database() {
        items = new ArrayList<>();
        users = new ArrayList<>();
        users.addAll(List.of(new User("admin", "admin"), new User("user", "user"), new User("asd", "asd")));

    }

        items.addAll(List.of(new Item(1, "The Lord of the Rings", "J.R.R. Tolkien"),
                new Item(2, "War and Peace", "Leo Tolstoy"),
                new Item(3, "Clean Code", "Robert C. Martin"),
                new Item(4, "The Hitchhiker's Guide to the Galaxy", "Douglas Adams"),
                new Item(5, "A Song of Ice and Fire", "George R.R. Martin")));
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
    public User getUserByName(String name) {
        return users.stream().filter(u -> u.getUserName().equals(name)).findFirst().orElse(null);
    }
    }
}
