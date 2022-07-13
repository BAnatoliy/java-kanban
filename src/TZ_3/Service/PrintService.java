package TZ_3.Service;

import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.List;

public interface PrintService {
    void printTasks(List<Task> tasks);
    void printEpics(List<Epic> epics);
    void printSubTasks(List<SubTask> subTasks);
    void printSubtasksOfEpic(String epicTitle, List<SubTask> subTasks);
    void printTask(Task task);
    void printEpic(Epic epic);
    void printSubTask(SubTask subTask);
    void printHistoryTasks(List<Task> historyTasks);
}
