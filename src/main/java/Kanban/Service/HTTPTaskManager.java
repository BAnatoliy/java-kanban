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
        try {
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
    } catch (IOException e) {
        throw new RuntimeException(e);
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

    private void loadTasks(ArrayList<? extends Task> list) throws IOException {
        int maxId = -1;
        if (Objects.nonNull(list)) {
            for (Task task : list) {
                int id = task.getId();
                task.setId(id);
                if (task instanceof Epic) {
                    getMapOfEpic().put(task.getId(), (Epic) task);
                    updateEpicStatus((Epic) task);
                } else if (task instanceof SubTask) {
                    if(taskValidationCheck(task)) {
                        getMapOfSubTasks().put(task.getId(), (SubTask) task);
                        sortTask.add(task);
                    }
                } else {
                    if (taskValidationCheck(task)) {
                        sortTask.add(task);
                        getMapOfTasks().put(task.getId(), task);
                    }
                }
                if (id > maxId) {
                    maxId = id;
                }
            }
            if (maxId > generationTaskId) {
                generationTaskId = maxId + 1;
            }
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