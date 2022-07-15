package Kanban.Service;

import Kanban.Task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistoryTask(Task task);

    List<Task> getHistory();
}
