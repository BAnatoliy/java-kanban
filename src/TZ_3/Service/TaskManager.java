package TZ_3.Service;

import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubTask(SubTask subTask);
    void updateTask(Task task, int taskId);
    void updateEpic(Epic epic, int epicId);
    void updateSubTask(SubTask subTask, int subTaskId);
    Task getTask(int taskId);
    Epic getEpic(int taskId);
    SubTask getSubTask(int taskId);
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTasks();
    void deleteTask(int taskId);
    void deleteEpic(int taskId);
    void deleteSubTask(int taskId);
    List<Task> getTasks();
    List<Epic> getEpics();
    List<SubTask> getSubtask();
    List<SubTask> getSubtaskOfEpic(int epic_id);
    void updateEpicStatus(Epic epic);




}
