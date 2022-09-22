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
        loadTasks(listOfTask);

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        ArrayList<Epic> listOfEpic = gson.fromJson(client.load("epics"), epicType);
        loadTasks(listOfEpic);

        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        ArrayList<SubTask> listOfSubTask = gson.fromJson(client.load("subtasks"), subTaskType);
        loadTasks(listOfSubTask);

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

    private void loadTasks(ArrayList<? extends Task> list) {
        if (Objects.nonNull(list)) {
            list.forEach((task) -> {
                int maxId = -1;
                int id = task.getId();
                generationTaskId = id;
                task.setId(id);
                if (task instanceof Epic) {
                    getMapOfEpic().put(task.getId(), (Epic) task);
                } else if (task instanceof SubTask) {
                    getMapOfSubTasks().put(task.getId(), (SubTask) task);
                } else {
                    getMapOfTasks().put(task.getId(), task);
                }
                if (id > maxId) {
                    maxId = id;
                }
                generationTaskId = maxId + 1;
            });
        }
    }
}