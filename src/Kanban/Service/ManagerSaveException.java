package Kanban.Service;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }

    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(Throwable cause) {
        super(cause);
    }
}
