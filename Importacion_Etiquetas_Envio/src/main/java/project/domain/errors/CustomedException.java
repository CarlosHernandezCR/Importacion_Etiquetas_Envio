package project.domain.errors;

public class CustomedException extends RuntimeException{
    public CustomedException(String message) {
        super(message);
    }
}