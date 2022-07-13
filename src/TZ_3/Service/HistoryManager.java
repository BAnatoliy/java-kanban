package TZ_3.Service;

import TZ_3.Task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistoryTasks(Task task);
    List<Task> getHistory();

}
