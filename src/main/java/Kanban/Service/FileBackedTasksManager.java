package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Constant.TypeTasks;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager{
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) throws IOException {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws IOException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) throws IOException {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public Task getTask(int taskId) throws IOException {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int taskId) throws IOException {
        Epic epic = super.getEpic(taskId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int taskId) throws IOException {
        SubTask subTask = super.getSubTask(taskId);
        save();
        return subTask;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int taskId) {
        super.deleteEpic(taskId);
        save();
    }

    @Override
    public void deleteSubTask(int taskId) {
        super.deleteSubTask(taskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    public String toString(Task task) {
        if (task instanceof SubTask) {
            return task.getId() + "," + task.getTypeTasks() + "," + task.getTitle() + "," + task.getStatus() + "," +
                    task.getDescription() + "," + task.getStartTime() + "," + task.getEndTime() + "," +
                    task.getDuration() + "," + ((SubTask) task).getEpicId();
        } else if (task instanceof Epic) {
            return task.getId() + "," + task.getTypeTasks() + "," + task.getTitle() + "," + task.getStatus() + "," +
                    task.getDescription() + "," + task.getStartTime() + "," + ((Epic) task).getEndTime() + "," +
                    task.getDuration() + ",";
        } else {
            return task.getId() + "," + task.getTypeTasks() + "," + task.getTitle() + "," + task.getStatus() + "," +
                    task.getDescription() + "," + task.getStartTime() + "," + task.getEndTime() + "," +
                    task.getDuration() + ",";
        }
    }

    Task fromString(String value) {
        Task task = null;
        String[] split = value.split(",");

        if (split.length > 1 && TypeTasks.fromString(split[1]) != null) {
            switch (TypeTasks.fromString(split[1])) {
                case SUBTASK:
                    task = new SubTask(split[2], split[4], Status.valueOf(split[3]), split[7],
                            split[5], Integer.parseInt(split[8]));
                    task.setId(Integer.parseInt(split[0]));
                    break;
                case TASK:
                    task = new Task(split[2], split[4], Status.valueOf(split[3]), split[7],
                           split[5]);
                    task.setId(Integer.parseInt(split[0]));
                    break;
                case EPIC:
                    task = new Epic(split[2], split[4], Status.valueOf(split[3]));
                    task.setId(Integer.parseInt(split[0]));
                    break;
            }
        }
    return task;
    }

    static String historyToString(HistoryManager manager) {
        List<Task> taskList = manager.getHistory();
        List<Integer> listOfTaskId = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for (Task task : taskList) {
            listOfTaskId.add(task.getId());
        }

        for (Integer i : listOfTaskId) {
            builder.append(i).append(",");
        }

        return builder.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> tasksId = new ArrayList<>();

        if (split.length > 1) {
            for (String s : split) {
                tasksId.add(Integer.parseInt(s));
            }
        }
        return tasksId;
    }

    private void save() {

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            fileWriter.write("id,type,name,status,description,startTime,endTime,duration,epic");
            fileWriter.newLine();

            for(Task task : getTasks()) {
                fileWriter.write(toString(task));
                fileWriter.newLine();
            }

            for(Epic epic : getEpics()) {
                fileWriter.write(toString(epic));
                fileWriter.newLine();
            }

            for(SubTask subTask : getSubTasks()) {
                fileWriter.write(toString(subTask));
                fileWriter.newLine();
            }

            fileWriter.newLine();
            if (getHistoryManager() != null) {
                fileWriter.write(historyToString(getHistoryManager()));
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file);
        List<String> linesFromLine = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String task;

            while ((task = fileReader.readLine()) != null) {
                linesFromLine.add(task);
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        int maxId = -1;
        for(String s : linesFromLine) {
            Task task = fbtm.fromString(s);

            if (task != null) {
                if (task instanceof Epic) {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addEpic((Epic) task);
                    if (id > maxId) {
                        maxId = id;
                    }
                } else if (task instanceof SubTask) {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addSubTask((SubTask) task);
                    task.setId(id);
                    if (id > maxId) {
                        maxId = id;
                    }
                } else {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addTask(task);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }

            fbtm.generationTaskId = maxId + 1;
        }

        if (!linesFromLine.isEmpty()) {
            List<Integer> listOfHistories = historyFromString(linesFromLine.get(linesFromLine.size() - 1));

            for (Integer i : listOfHistories) {
                if (fbtm.getMapOfTasks().containsKey(i)) {
                    fbtm.getTask(i);
                } else if ((fbtm.getMapOfEpic().containsKey(i))) {
                    fbtm.getEpic(i);
                } else {
                    fbtm.getSubTask(i);
                }
            }
        }

        return fbtm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBackedTasksManager that = (FileBackedTasksManager) o;
        return (generationTaskId == that.generationTaskId && Objects.equals(getMapOfTasks(), that.getMapOfTasks()) &&
                Objects.equals(getMapOfSubTasks(), that.getMapOfSubTasks()) && Objects.equals(getMapOfEpic(), that.getMapOfEpic()) &&
                Objects.equals(getHistoryManager(), that.getHistoryManager()) && Objects.equals(sortTask, that.sortTask) && Objects.equals(file, that.file));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMapOfTasks(), getMapOfSubTasks(), getMapOfEpic(), getHistoryManager(), generationTaskId, sortTask);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("Files" + File.separator + "Tasks.CSV");
        FileBackedTasksManager fbtm = FileBackedTasksManager.loadFromFile(file);

                // добавятся новые Таски
       fbtm.addTask(new Task("ID 0 Task", "description Task", Status.DONE,
               "PT3H35M", "2022-02-01T15:50"));
       fbtm.addEpic(new Epic("ID 1 Epic", "description Epic", Status.NEW));

        fbtm.addTask(new Task("Not", "description Task", Status.DONE,
                "PT3H35M", "2022-02-01T14:50"));
        fbtm.addTask(new Task("Create", "description Task", Status.DONE,
                "PT0H35M", "2022-01-01T00:50"));
        fbtm.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.NEW,
                "PT3H35M", "2022-01-03T15:50", 1));
        fbtm.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-02T15:50", 1));
        fbtm.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.NEW,
                "PT5H35M", "2022-01-07T15:00", 1));
        fbtm.addSubTask(new SubTask("Not", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-05T13:00", 1));

                // добавится история просмотров созданных Тасков
        System.out.println(fbtm.getTask(0));
        System.out.println(fbtm.getEpic(1));
        System.out.println(fbtm.getSubTask(3));
        System.out.println(fbtm.getSubTask(4));
        System.out.println(fbtm.getSubTask(5));

        // удалится Эпик, обновится история просмотров
        fbtm.deleteEpic(1);

        System.out.println(fbtm.getPrioritizedTasks());
    }
}
