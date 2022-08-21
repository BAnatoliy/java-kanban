package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager{
    private File file;

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

    public String toString(Task task) {
        if (task.getClass() == Task.class) {
            return task.getId() + "," + task.getTypeOfTasks() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        } else if (task.getClass() == Epic.class) {
            return task.getId() + "," + task.getTypeOfTasks() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        } else {
            return task.getId() + "," + task.getTypeOfTasks() + "," + task.getTitle() + "," + task.getStatus() + "," +
                    task.getDescription() + "," + ((SubTask) task).getEpicId();
        }
    }

    Task fromString(String value) {
        Task task = null;
        String[] split = value.split(",");

        if (split.length > 1) {
            switch (split[1]) {
                case "SUBTASK":
                    task = new SubTask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
                    task.setId(Integer.parseInt(split[0]));
                    break;
                case "TASK":
                    task = new Task(split[2], split[4], Status.valueOf(split[3]));
                    task.setId(Integer.parseInt(split[0]));
                    break;
                case "EPIC":
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

    void save() {

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            fileWriter.write("id,type,name,status,description,epic");
            fileWriter.newLine();

            for(Task task : getTasks()) {
                fileWriter.write(toString(task));
                fileWriter.newLine();
            }

            for(Epic epic : getEpics()) {
                fileWriter.write(toString(epic));
                fileWriter.newLine();
            }

            for(SubTask subTask : getSubtask()) {
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

    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file);
        List<String> linesFromLine = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String task;

            while (fileReader.ready()) {
                task = fileReader.readLine();
                linesFromLine.add(task);
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        int maxId = -1;
        for(String s : linesFromLine) {
            Task task = fbtm.fromString(s);

            if (task != null) {
                if (task.getClass() == Task.class) {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addTask(task);
                    if (id > maxId) {
                        maxId = id;
                    }
                } else if (task.getClass() == Epic.class) {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addEpic((Epic) task);
                    if (id > maxId) {
                        maxId = id;
                    }
                } else {
                    int id = task.getId();
                    fbtm.generationTaskId = id;
                    fbtm.addSubTask((SubTask) task);
                    task.setId(id);
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

    public static void main(String[] args) throws IOException {
        File file = new File("Files" + File.separator + "Tasks.CSV");
        FileBackedTasksManager fbtm = FileBackedTasksManager.loadFromFile(file);

                // добавятся новые Таски
       /*fbtm.addTask(new Task("ID 10 Task", "description Task 1_1", Status.DONE));
       fbtm.addEpic(new Epic("ID 11 Epic", "description Epic 1_2", Status.NEW));
       fbtm.addSubTask(new SubTask("ID 12 SubTask", "description 3 Subtask", Status.NEW, 11));*/

                // добавится история просмотров созданных Тасков
       /*fbtm.getTask(10);
       fbtm.getEpic(11);
       fbtm.getSubTask(12);*/

        // удалится Эпик, обновится история просмотров
       /*fbtm.deleteEpic(11);*/
    }
}
