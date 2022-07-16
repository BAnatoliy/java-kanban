package Kanban.Service;

import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

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
