package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    int generationTaskId = 0;

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Добавление Таск
    @Override
    public void addTask(Task task) {
        int taskId = generationTaskId++;
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    // Добавление Эпика
    @Override
    public void addEpic(Epic epic) {
        int epicId = generationTaskId++;
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    // Добавление Субтаск
    @Override
    public void addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return;
        }

        int subTaskId = generationTaskId++;
        subTask.setId(subTaskId);
        subTasks.put(subTask.getId(), subTask);

        epic.addSubTask(subTaskId);
        updateEpicStatus(epic);
    }

    // Обновление Таска (замена старого объекта новым)
    @Override
    public void updateTask(Task task, int taskId) {
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    // Обновление Эпика (замена старого объекта новым)
    @Override
    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epic.setSubTaskIds(epics.get(epicId).getSubTaskIds());
        epics.put(epic.getId(), epic);
    }

    // Обновление СубТаска (замена старого объекта новым)
    @Override
    public void updateSubTask(SubTask subTask, int subTaskId) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return;
        }

        subTask.setId(subTaskId);
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epic);
    }

    @Override
    public Task getTask(int taskId) {
        historyManager.addHistoryTask(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int taskId) {
        historyManager.addHistoryTask(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public SubTask getSubTask(int taskId) {
        historyManager.addHistoryTask(subTasks.get(taskId));
        return subTasks.get(taskId);
    }

    // Удаление всех Тасков
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удаление всех Эпиков
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    // Удаление всех СубТасков
    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            List<Integer> subTaskIds = new ArrayList<>();
            epic.setSubTaskIds(subTaskIds);
            Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.NEW);
            updateEpic(newEpic, epic.getId());
        }
    }

    // Удаление Таска по идентификатору
    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    // Удаление Эпика по идентификатору
    @Override
    public void deleteEpic(int taskId) {
        for (Integer i : epics.get(taskId).getSubTaskIds()) {
            subTasks.remove(i);
        }
        epics.remove(taskId);

    }

    // Удаление СубТаска по идентификатору
    @Override
    public void deleteSubTask(int taskId) {
        Epic epic = epics.get(subTasks.get(taskId).getEpicId());
        subTasks.remove(taskId);
        epic.removeSubTask(taskId);
        updateEpicStatus(epic);

    }

    // Получение списка Тасков
    @Override
    public List<Task> getTasks() {
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    // Получение списка Эпиков
    @Override
    public List<Epic> getEpics() {
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    // Получение списка СубТасков
    @Override
    public List<SubTask> getSubtask() {
        Collection<SubTask> values = subTasks.values();
        return new ArrayList<>(values);
    }

    // Получение списка СубТасков определенного Эпика
    @Override
    public List<SubTask> getSubtaskOfEpic(int epic_id) {
        Collection <SubTask> values = new ArrayList<>();

        for (SubTask subTasks : subTasks.values()) {
            if (subTasks.getEpicId() == epic_id) {
                values.add(subTasks);
            }
        }

        return new ArrayList<>(values);
    }

    // Определение статуса Эпика
    @Override
    public void updateEpicStatus(Epic epic) {
        int i = 0;

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {

                if (subTask.getStatus() == Status.NEW && epic.getStatus() == Status.NEW) {
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.NEW);
                    updateEpic(newEpic, epic.getId());
                } else if ((subTask.getStatus() == Status.IN_PROGRESS) ||
                    ((subTask.getStatus() == Status.NEW) && !(epic.getStatus() == Status.NEW))) {
                    i = 0;
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.IN_PROGRESS);
                    updateEpic(newEpic, epic.getId());
                } else if (subTask.getStatus() == Status.DONE) {
                    i++;
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.IN_PROGRESS);
                    updateEpic(newEpic, epic.getId());
                }

            if (i == epic.getSubTaskIds().size()) {
                Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.DONE);
                updateEpic(newEpic, epic.getId());
            }
            }
        }

            if (epic.getSubTaskIds().isEmpty()) {
                Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.NEW);
                updateEpic(newEpic, epic.getId());
            }
    }
}
