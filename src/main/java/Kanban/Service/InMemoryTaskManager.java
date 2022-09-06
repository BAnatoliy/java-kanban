package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int generationTaskId = 0;
    protected TreeSet<Task> sortTask = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else {
            return 0;
        }
    });

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public Map<Integer, Task> getMapOfTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getMapOfEpic() {
        return epics;
    }

    public Map<Integer, SubTask> getMapOfSubTasks() {
        return subTasks;
    }
    public TreeSet<Task> getPrioritizedTasks() {
        return sortTask;
    }

    private void addTaskInMapAndSet (Task task) {
        int taskId = generationTaskId++;
        task.setId(taskId);
        tasks.put(task.getId(), task);
        sortTask.add(task);
    }

    // Добавление Таск
    @Override
    public void addTask(Task task) throws IOException {
        if (sortTask.isEmpty()) {
            addTaskInMapAndSet(task);
        } else {
            int i = 0;
            for (Task taskFromSet : sortTask) {
                if (taskFromSet.getStartTime().isAfter(task.getEndTime()) ||
                        taskFromSet.getEndTime().isBefore(task.getStartTime())) {
                    i++;
                }
            }
            if (i == sortTask.size()) {
                addTaskInMapAndSet(task);
            }
        }
    }

    // Добавление Эпика
    @Override
    public void addEpic(Epic epic) throws IOException {
        int epicId = generationTaskId++;
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    private void addSubTaskInMapAndSet(SubTask subTask) throws IOException {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return;
        }

        int subTaskId = generationTaskId++;
        subTask.setId(subTaskId);
        subTasks.put(subTask.getId(), subTask);
        sortTask.add(subTask);

        epic.addSubTask(subTaskId);
        updateEpicTime(epic);
        updateEpicStatus(epic);
    }

    // Добавление Субтаск
    @Override
    public void addSubTask(SubTask subTask) throws IOException {
        if (sortTask.isEmpty()) {
            addSubTaskInMapAndSet(subTask);
        } else {
            int i = 0;
            for (Task taskFromSet : sortTask) {
                if (taskFromSet.getStartTime().isAfter(subTask.getEndTime()) ||
                        taskFromSet.getEndTime().isBefore(subTask.getStartTime())) {
                    i++;
                }
            }
            if (i == sortTask.size()) {
                addSubTaskInMapAndSet(subTask);
            }
        }
    }

    // Обновление Таска (замена старого объекта новым)
    @Override
    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            task.setId(taskId);
            tasks.put(task.getId(), task);
        }
    }

    // Обновление Эпика (замена старого объекта новым)
    @Override
    public void updateEpic(Epic epic, int epicId) {
        if (epics.containsKey(epicId)) {
            epic.setId(epicId);
            epic.setSubTaskIds(epics.get(epicId).getSubTaskIds());
            epics.put(epic.getId(), epic);
        }
    }

    // Обновление СубТаска (замена старого объекта новым)
    @Override
    public void updateSubTask(SubTask subTask, int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            int epicId = subTask.getEpicId();
            Epic epic = epics.get(epicId);

            if (epic == null) {
                return;
            }

            subTask.setId(subTaskId);
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task getTask(int taskId) throws IOException {
        historyManager.addHistoryTask(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int taskId) throws IOException {
        historyManager.addHistoryTask(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public SubTask getSubTask(int taskId) throws IOException {
        historyManager.addHistoryTask(subTasks.get(taskId));
        return subTasks.get(taskId);
    }

    // Удаление всех Тасков
    @Override
    public void deleteAllTasks() {
        for (Task task : historyManager.getHistory()) {
            if (task.getClass() == Task.class) {
                historyManager.remove(task.getId());
            }
        }

        for (Task task : tasks.values()) {
            sortTask.remove(task);
        }

        tasks.clear();
    }

    // Удаление всех Эпиков
    @Override
    public void deleteAllEpics() {
        for (Task task : historyManager.getHistory()) {
            if (task instanceof Epic || task instanceof SubTask) {
                historyManager.remove(task.getId());
            }
        }

        epics.clear();
        subTasks.clear();

        sortTask.removeIf(task -> task instanceof SubTask);
    }

    // Удаление всех СубТасков
    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            sortTask.remove(subTask);
        }
        subTasks.clear();
        for (Task task : historyManager.getHistory()) {
            if (task instanceof SubTask) {
                historyManager.remove(task.getId());
            }
        }
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
        historyManager.remove(taskId);
        sortTask.removeIf(task -> task.getId() == taskId);
    }

    // Удаление Эпика по идентификатору
    @Override
    public void deleteEpic(int taskId) {
        for (Integer i : epics.get(taskId).getSubTaskIds()) {
            sortTask.remove(subTasks.get(i));
            subTasks.remove(i);
            historyManager.remove(i);
        }
        epics.remove(taskId);
        historyManager.remove(taskId);
        sortTask.removeIf(task -> task.getId() == taskId);
    }

    // Удаление СубТаска по идентификатору
    @Override
    public void deleteSubTask(int taskId) {
        Epic epic = epics.get(subTasks.get(taskId).getEpicId());
        subTasks.remove(taskId);
        epic.removeSubTask(taskId);
        updateEpicTime(epic);
        updateEpicStatus(epic);
        historyManager.remove(taskId);
        sortTask.removeIf(task -> task.getId() == taskId);
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
    public List<SubTask> getSubTasks() {
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

    private void updateEpicTime (Epic epic) {
        if (!epic.getSubTaskIds().isEmpty()) {
        LocalDateTime min = LocalDateTime.MAX;
        LocalDateTime max = LocalDateTime.MIN;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStartTime().isBefore(min)) {
                min = subTask.getStartTime();
            }

            if (subTask.getEndTime().isAfter(max)) {
                max = subTask.getEndTime();
            }
        }
            epic.setStartTime(min);
            epic.setEndTime(max);
            epic.setDuration(Duration.between(min, max));
        }
    }

    // Определение статуса Эпика
    private void updateEpicStatus(Epic epic) {
        int i = 0;

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {

                if (subTask.getStatus() == Status.NEW && epic.getStatus() == Status.NEW) {
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.NEW);
                    newEpic.setStartTime(epic.getStartTime());
                    newEpic.setEndTime(epic.getEndTime());
                    newEpic.setDuration(epic.getDuration());
                    updateEpic(newEpic, epic.getId());
                } else if ((subTask.getStatus() == Status.IN_PROGRESS) ||
                    ((subTask.getStatus() == Status.NEW) && !(epic.getStatus() == Status.NEW))) {
                    i = 0;
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.IN_PROGRESS);
                    newEpic.setStartTime(epic.getStartTime());
                    newEpic.setEndTime(epic.getEndTime());
                    newEpic.setDuration(epic.getDuration());
                    updateEpic(newEpic, epic.getId());
                } else if (subTask.getStatus() == Status.DONE) {
                    i++;
                    Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.IN_PROGRESS);
                    newEpic.setStartTime(epic.getStartTime());
                    newEpic.setEndTime(epic.getEndTime());
                    newEpic.setDuration(epic.getDuration());
                    updateEpic(newEpic, epic.getId());
                }

            if (i == epic.getSubTaskIds().size()) {
                Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.DONE);
                newEpic.setStartTime(epic.getStartTime());
                newEpic.setEndTime(epic.getEndTime());
                newEpic.setDuration(epic.getDuration());
                updateEpic(newEpic, epic.getId());
            }
            }
        }

            if (epic.getSubTaskIds().isEmpty()) {
                Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), Status.NEW);
                newEpic.setStartTime(epic.getStartTime());
                newEpic.setEndTime(epic.getEndTime());
                newEpic.setDuration(epic.getDuration());
                updateEpic(newEpic, epic.getId());
            }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return generationTaskId == that.generationTaskId && Objects.equals(tasks, that.tasks) &&
                Objects.equals(subTasks, that.subTasks) && Objects.equals(epics, that.epics) &&
                Objects.equals(historyManager, that.historyManager) && Objects.equals(sortTask, that.sortTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subTasks, epics, historyManager, generationTaskId, sortTask);
    }
}
