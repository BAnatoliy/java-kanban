package Kanban.Service;

import Kanban.Task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> historyTasks = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public void addHistoryTask(Task task) {
        historyTasks.add(task);
        if (historyTasks.size() > 10) {
            historyTasks.remove(0);
        }
    }
}
