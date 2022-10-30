package baumann.javaassignment.assignment.data;

import baumann.javaassignment.assignment.exceptions.SerializationException;
import baumann.javaassignment.assignment.model.Item;
import baumann.javaassignment.assignment.model.Member;
import baumann.javaassignment.assignment.model.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Database {

    private final List<User> users;
    private List<Member> members;
    private List<Item> items;

    private static final String DATA_FILE_NAME = "db.dat";

    private final Logger log = Logger.getLogger(this.getClass().getName());

    public Database() {

        members = new ArrayList<>();
        items = new ArrayList<>();
        users = new ArrayList<>();
        users.addAll(List.of(new User("admin", "admin"), new User("user", "user"), new User("asd", "asd")));

        try {
            deserialize();
            return;
        } catch (SerializationException e) {
            log.info("%s.%nCould not deserialize data file. Creating new database.".formatted(e.getMessage()));
        }
        members.addAll(List.of(new Member(1, "John", "Doe", LocalDate.of(1990, 2, 16)),
                new Member(2, "Jane", "Doe", LocalDate.of(1982, 7, 4)),
                new Member(3, "Max", "Mustermann", LocalDate.of(2000, 1, 1)),
                new Member(4, "Albert", "Einstein", LocalDate.of(1879, 3, 14))));

        items.addAll(List.of(new Item(1, "The Lord of the Rings", "J.R.R. Tolkien"),
                new Item(2, "War and Peace", "Leo Tolstoy"),
                new Item(3, "Clean Code", "Robert C. Martin"),
                new Item(4, "The Hitchhiker's Guide to the Galaxy", "Douglas Adams"),
                new Item(5, "A Song of Ice and Fire", "George R.R. Martin")));
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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

    public void serialize() {
        log.info("Serializing database...");

        try (FileOutputStream fos = new FileOutputStream(DATA_FILE_NAME); ObjectOutputStream oos = new ObjectOutputStream(
                fos)) {

            for (Item item : items) {
                oos.writeObject(item);
            }
            for (Member member : members) {
                oos.writeObject(member);
            }

        } catch (FileNotFoundException fnfe) {
            throw new SerializationException("File not found: " + fnfe);
        } catch (IOException ioe) {
            throw new SerializationException("IO error: " + ioe);
        }
    }

    public void deserialize() {
        log.info("Deserializing database...");

        try (FileInputStream fis = new FileInputStream(DATA_FILE_NAME); ObjectInputStream ois = new ObjectInputStream(
                fis)) {

            boolean hasMore = true;
            while (hasMore) {
                if (fis.available() != 0) {
                    Object obj = ois.readObject();
                    if (obj instanceof Item item) {
                        items.add(item);
                    } else if (obj instanceof Member member) {
                        members.add(member);
                    }
                } else {
                    hasMore = false;
                }
            }

        } catch (FileNotFoundException e) {
            throw new SerializationException("data file not found");
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("could not deserialize object");
        }
    }
}
