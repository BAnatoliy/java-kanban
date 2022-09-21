package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.io.IOException;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract void setTaskManager() throws IOException;

    protected void setTasks() throws IOException {
        taskManager.addTask(new Task("Task 1", "description Task", Status.NEW,
                "PT3H35M", "2022-02-01T15:50"));
        taskManager.addTask(new Task("Task 2", "description Task2", Status.DONE,
                "PT1H35M", "2022-02-07T15:50"));
    }

    protected void setEpics() throws IOException {
        taskManager.addEpic(new Epic("Epic 1", "description Epic", Status.NEW));
        taskManager.addEpic(new Epic("Epic 2", "description Epic", Status.NEW));
    }

    protected void setSubTasksForEpicId_0() throws IOException {
        taskManager.addSubTask(new SubTask("SubTask 1", "description Subtask", Status.NEW,
                "PT3H35M", "2022-01-03T15:50", 0));
        taskManager.addSubTask(new SubTask("SubTask 2", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-02T15:50", 0));
        taskManager.addSubTask(new SubTask("SubTask 3", "description Subtask", Status.DONE,
                "PT5H35M", "2022-01-07T15:00", 0));
        taskManager.addSubTask(new SubTask("SubTask 4", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-05T13:00", 0));
    }

    protected void setEpicsTasksSubTasks() throws IOException {
        setEpics();
        setTasks();
        setSubTasksForEpicId_0();
    }



}
