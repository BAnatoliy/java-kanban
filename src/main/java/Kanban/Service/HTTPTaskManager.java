package Kanban.Service;

import Kanban.Client.KVClient;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVClient client;
    private final Gson gson;

    public HTTPTaskManager(String url) {
        super(null);
        this.client = new KVClient(url);
        this.gson = Managers.getGson();
        load();
    }

    @Override
    protected void save() {
        String tasksJson = gson.toJson(getTasks());
        client.put("tasks", tasksJson);

        String epicsJson = gson.toJson(getEpics());
        client.put("epics", epicsJson);

        String subtasksJson = gson.toJson(getSubTasks());
        client.put("subtasks", subtasksJson);

        List<Integer> history = getHistoryManager().getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(history);
        client.put("history", historyJson);
    }

    private void load() {
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        ArrayList<Task> listOfTask = gson.fromJson(client.load("tasks"), taskType);
        if (Objects.nonNull(listOfTask)) {
            listOfTask.forEach((task) -> {
                    int maxId = -1;
                    if (task != null) {
                        int id = task.getId();
                        generationTaskId = id;
                        //addTask(task);
                        task.setId(id);
                        getMapOfTasks().put(task.getId(), task);
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                    generationTaskId = maxId + 1;
            });
        }

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        ArrayList<Epic> listOfEpic = gson.fromJson(client.load("epics"), epicType);
        if (Objects.nonNull(listOfEpic)) {
            listOfEpic.forEach((epic) -> {
                    int maxId = -1;
                    int id = epic.getId();
                    generationTaskId = id;
                    epic.setId(id);
                    getMapOfEpic().put(epic.getId(), epic);
                    if (id > maxId) {
                        maxId = id;
                    }
                    generationTaskId = maxId + 1;
            });
        }

        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        ArrayList<SubTask> listOfSubTask = gson.fromJson(client.load("subtasks"), subTaskType);
        if (Objects.nonNull(listOfSubTask)) {
            listOfSubTask.forEach((subTask) -> {
                    int maxId = -1;
                    int id = subTask.getId();
                    generationTaskId = id;
                    subTask.setId(id);
                    getMapOfSubTasks().put(subTask.getId(), subTask);
                    if (id > maxId) {
                        maxId = id;
                    }
                    generationTaskId = maxId + 1;
            });
        }

        Type historyType = new TypeToken<List<Integer>>() {
        }.getType();
        ArrayList<Integer> listOfHistoryId = gson.fromJson(client.load("history"), historyType);
        if (Objects.nonNull(listOfHistoryId)) {
            listOfHistoryId.forEach((taskId) -> {
                try {
                    if (getMapOfTasks().containsKey(taskId)) {
                        getTask(taskId);
                    } else if (getMapOfEpic().containsKey(taskId)) {
                        getEpic(taskId);
                    } else if (getMapOfSubTasks().containsKey(taskId)) {
                        getSubTask(taskId);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HTTPTaskManager that = (HTTPTaskManager) o;
        return Objects.equals(getMapOfTasks(), that.getMapOfTasks())
                && Objects.equals(getMapOfEpic(), that.getMapOfEpic())
                && Objects.equals(getMapOfSubTasks(), that.getMapOfSubTasks())
                && Objects.equals(getHistoryManager(), that.getHistoryManager());
    }
}