package TZ_3.Service;

import TZ_3.Constant.Status;
import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.*;

public class TaskManagerService {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    int generationTaskId = 0;

    // Добавление Таск
    public void addTask(Task task) {
        int taskId = generationTaskId++;
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }
    // Добавление Эпика
    public void addEpic(Epic epic) {
        int epicId = generationTaskId++;
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }
    // Добавление Субтаск
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
    public void updateTask(Task task, int taskId) {
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    // Обновление Эпика (замена старого объекта новым)
    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epic.setSubTaskIds(epics.get(epicId).getSubTaskIds());
        epics.put(epic.getId(), epic);
    }

    // Обновление СубТаска (замена старого объекта новым)
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

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpic(int taskId) {
        return epics.get(taskId);
    }

    public SubTask getSubTask(int taskId) {
        return subTasks.get(taskId);
    }
    // Удаление всех Тасков
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удаление всех Эпиков
    public void deleteAllEpics() {
        epics.clear();
    }

    // Удаление всех СубТасков
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
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    // Удаление Эпика по идентификатору
    public void deleteEpic(int taskId) {
        epics.remove(taskId);
    }

    // Удаление СубТаска по идентификатору
    public void deleteSubTask(int taskId) {
        Epic epic = epics.get(subTasks.get(taskId).getEpicId());
        subTasks.remove(taskId);
        epic.removeSubTask(taskId);
        updateEpicStatus(epic);

    }

    // Получение списка Тасков
    public List<Task> getTasks() {
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    // Получение списка Эпиков
    public List<Epic> getEpics() {
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    // Получение списка СубТасков
    public List<SubTask> getSubtask() {
        Collection<SubTask> values = subTasks.values();
        return new ArrayList<>(values);
    }

    // Получение списка СубТасков определенного Эпика
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
