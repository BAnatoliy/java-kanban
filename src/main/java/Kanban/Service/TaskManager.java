package Kanban.Service;

import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    void addTask(Task task) throws IOException;

    void addEpic(Epic epic) throws IOException;

    void addSubTask(SubTask subTask) throws IOException;

    void updateTask(Task task, int taskId);

    void updateEpic(Epic epic, int epicId);

    void updateSubTask(SubTask subTask, int subTaskId);

    Task getTask(int taskId) throws IOException;

    Epic getEpic(int taskId) throws IOException;

    SubTask getSubTask(int taskId) throws IOException;

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    void deleteTask(int taskId);

    void deleteEpic(int taskId);

    void deleteSubTask(int taskId);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<SubTask> getSubtaskOfEpic(int epic_id);

    HistoryManager getHistoryManager();
}
