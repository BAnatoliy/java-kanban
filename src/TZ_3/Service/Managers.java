package TZ_3.Service;

public class Managers {

    private static final HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static final PrintService printConsole = new PrintConsole();

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static PrintService getDefaultPrint() {
        return printConsole;
    }
}
