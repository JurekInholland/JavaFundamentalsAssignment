package baumann.javaassignment.assignment.exceptions;

public class SerializationException extends RuntimeException {
    public SerializationException(String message) {
        super("Error during serialization: " + message);
    }
}
