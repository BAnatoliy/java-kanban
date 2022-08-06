package Kanban;

import Kanban.Constant.Status;
import Kanban.Service.Managers;
import Kanban.Service.TaskManager;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = createTasks();

        history_Of_Tasks_Should_Comprises_TasksId_From_One_To_Six_InOrder(taskManager);
        history_Of_Tasks_Should_Comprises_TasksId_InOrder_3_1_2_6_0_4_5(taskManager);
        after_Delete_TaskId0_And_TaskID1_History_Of_Tasks_Should_Comprises_TasksId_6_And_5(taskManager);
    }

    private static TaskManager createTasks() {
        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task("1 Task", "description Task 1_1", Status.DONE));
        taskManager.addEpic(new Epic("2 Epic", "description Epic 1_2", Status.NEW));
        taskManager.addSubTask(new SubTask("2 Epic - 3 SubTask", "description 3 Subtask", Status.NEW, 1));
        taskManager.addSubTask(new SubTask("2 Epic - 4 SubTask", "description 4 Subtask", Status.NEW, 1));
        taskManager.addSubTask(new SubTask("2 Epic - 5 SubTask", "description 5 Subtask", Status.NEW, 1));
        taskManager.addTask(new Task("6 Task", "description 6 Task", Status.NEW));
        taskManager.addEpic(new Epic("7 Epic", "description 7 Epic", Status.NEW));

        return taskManager;
    }

    private static void history_Of_Tasks_Should_Comprises_TasksId_From_One_To_Six_InOrder(TaskManager taskManager) {
        taskManager.getTask(0);
        taskManager.getEpic(1);
        taskManager.getSubTask(2);
        taskManager.getSubTask(3);
        taskManager.getSubTask(4);
        taskManager.getTask(5);
        taskManager.getEpic(6);

        List<Task> actual = taskManager.getHistoryManager().getHistory();
        List<Task> expected = new ArrayList<>(List.of(taskManager.getTask(0),
                taskManager.getEpic(1),
                taskManager.getSubTask(2),
                taskManager.getSubTask(3),
                taskManager.getSubTask(4),
                taskManager.getTask(5),
                taskManager.getEpic(6)));

        if (actual.equals(expected)) {
            System.out.println("Тест 1 пройден");
        } else {
            System.out.println("Тест 1 не пройден");
        }
    }

    private static void history_Of_Tasks_Should_Comprises_TasksId_InOrder_3_1_2_6_0_4_5(TaskManager taskManager) {
        taskManager.getSubTask(3);
        taskManager.getEpic(1);
        taskManager.getSubTask(2);
        taskManager.getEpic(6);
        taskManager.getTask(0);
        taskManager.getSubTask(4);
        taskManager.getTask(5);

        List<Task> actual = taskManager.getHistoryManager().getHistory();
        List<Task> expected = new ArrayList<>(List.of(taskManager.getSubTask(3),
                taskManager.getEpic(1),
                taskManager.getSubTask(2),
                taskManager.getEpic(6),
                taskManager.getTask(0),
                taskManager.getSubTask(4),
                taskManager.getTask(5)));

        if (actual.equals(expected)) {
            System.out.println("Тест 2 пройден");
        } else {
            System.out.println("Тест 2 не пройден");
        }
    }

    private static void after_Delete_TaskId0_And_TaskID1_History_Of_Tasks_Should_Comprises_TasksId_6_And_5(TaskManager taskManager) {
        taskManager.deleteTask(0);
        taskManager.deleteEpic(1);

        List<Task> actual = taskManager.getHistoryManager().getHistory();
        List<Task> expected = new ArrayList<>(List.of(taskManager.getEpic(6),
                taskManager.getTask(5)));

        if (actual.equals(expected)) {
            System.out.println("Тест 3 пройден");
        } else {
            System.out.println("Тест 3 не пройден");
        }
    }
}

