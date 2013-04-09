package exception;

public class LoopDependencyException extends RuntimeException {
    public LoopDependencyException(String errorMessage) {
        super(errorMessage);
    }
}
