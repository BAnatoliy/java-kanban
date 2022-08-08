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
        TaskManager taskManager;

        taskManager = createTasksForTestTaskManager();
        lists_Of_Tasks_Should_Comprises_Create_Tasks(taskManager);
        lists_Of_Epics_Should_Comprises_Create_Epics(taskManager);
        lists_Of_SubTasks_Should_Comprises_Create_SubTasks(taskManager);
        lists_Of_SubTasks_Should_Comprises_Subtasks_Of_Epic(taskManager);
        new_Task_Should_Replace_Existing_Task_By_Id(taskManager);
        new_Epic_Should_Replace_Existing_Epic_By_Id(taskManager);
        new_SubTask_Should_Replace_Existing_SubTask_By_Id(taskManager);
        delete_Task_Should_List_Of_Tasks_Without_Remote_Task(taskManager);
        delete_Epic_Should_List_Of_Epics_Without_Remote_Epic_And_SupTask_Of_This_Epic(taskManager);
        delete_SubTask_Should_List_Of_SubTasks_And_Epic_Without_Remote_SubTask(taskManager);


        taskManager = createTasksForTestHistoryManager();
        history_Of_Tasks_Should_Comprises_TasksId_From_One_To_Six_InOrder(taskManager);
        history_Of_Tasks_Should_Comprises_TasksId_InOrder_3_1_2_6_0_4_5(taskManager);
        after_Delete_TaskId0_And_TaskID1_History_Of_Tasks_Should_Comprises_TasksId_6_And_5(taskManager);
    }



    private static TaskManager createTasksForTestTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(new Task("Task 1", "description Task 1", Status.NEW));
        taskManager.addTask(new Task("Task 2", "description Task 2", Status.IN_PROGRESS));
        taskManager.addEpic(new Epic("Epic 1", "description Epic 1", Status.NEW));
        taskManager.addEpic(new Epic("Epic 2", "description Epic 2", Status.NEW));
        taskManager.addSubTask(new SubTask("SubTask1", "description Subtask1", Status.IN_PROGRESS, 2));
        taskManager.addSubTask(new SubTask("SubTask2", "description Subtask2", Status.DONE, 3));
        taskManager.addSubTask(new SubTask("SubTask3", "description Subtask3", Status.DONE, 3));
        taskManager.addSubTask(new SubTask("SubTask4", "description Subtask4", Status.NEW, 3));
        taskManager.addSubTask(new SubTask("SubTask5", "description Subtask5", Status.DONE, 3));
        taskManager.addSubTask(new SubTask("SubTask6", "description Subtask6", Status.DONE, 3));
        return taskManager;
    }

    private static void lists_Of_Tasks_Should_Comprises_Create_Tasks(TaskManager taskManager) {
        List<Task> actual = taskManager.getTasks();
        List<Task> expected = List.of(taskManager.getTask(0),
                taskManager.getTask(1));
        if (actual.equals(expected)) {
            System.out.println("Тест 1 пройден");
        } else {
            throw new AssertionError("Тест 1 не пройден.");
        }
    }

    private static void lists_Of_Epics_Should_Comprises_Create_Epics(TaskManager taskManager) {
        List<Epic> actual = taskManager.getEpics();
        List<Epic> expected = List.of(taskManager.getEpic(2),
                taskManager.getEpic(3));
        if (actual.equals(expected)) {
            System.out.println("Тест 2 пройден");
        } else {
            throw new AssertionError("Тест 2 не пройден.");
        }
    }

    private static void lists_Of_SubTasks_Should_Comprises_Create_SubTasks(TaskManager taskManager) {
        List<SubTask> actual = taskManager.getSubtask();
        List<SubTask> expected = List.of(taskManager.getSubTask(4),
                taskManager.getSubTask(5),
                taskManager.getSubTask(6),
                taskManager.getSubTask(7),
                taskManager.getSubTask(8),
                taskManager.getSubTask(9));
        if (actual.equals(expected)) {
            System.out.println("Тест 3 пройден");
        } else {
            throw new AssertionError("Тест 3 не пройден.");
        }
    }

    private static void lists_Of_SubTasks_Should_Comprises_Subtasks_Of_Epic(TaskManager taskManager) {
        List<SubTask> actual = taskManager.getSubtaskOfEpic(3);
        List<SubTask> expected = List.of(taskManager.getSubTask(5),
                taskManager.getSubTask(6),
                taskManager.getSubTask(7),
                taskManager.getSubTask(8),
                taskManager.getSubTask(9));
        if (actual.equals(expected)) {
            System.out.println("Тест 4 пройден");
        } else {
            throw new AssertionError("Тест 4 не пройден.");
        }
    }

    private static void new_Task_Should_Replace_Existing_Task_By_Id(TaskManager taskManager) {
        Task task = new Task("Task New", "description Task New", Status.NEW);
        taskManager.updateTask(task, 0);
        Task actual = taskManager.getTask(0);
        Task expected = task;

        if (actual.equals(expected)) {
            System.out.println("Тест 5 пройден");
        } else {
            throw new AssertionError("Тест 5 не пройден.");
        }
    }

    private static void new_Epic_Should_Replace_Existing_Epic_By_Id(TaskManager taskManager) {
        Epic epic = new Epic("Epic New", "description Epic New", Status.NEW);
        taskManager.updateEpic(epic, 3);
        Epic actual = taskManager.getEpic(3);
        Epic expected = epic;

        if (actual.equals(expected)) {
            System.out.println("Тест 6 пройден");
        } else {
            throw new AssertionError("Тест 6 не пройден.");
        }
    }

    private static void new_SubTask_Should_Replace_Existing_SubTask_By_Id(TaskManager taskManager) {
        SubTask subTask = new SubTask("SubTask New", "description Subtask New", Status.IN_PROGRESS, 2);
        taskManager.updateSubTask(subTask, 4);
        SubTask actual = taskManager.getSubTask(4);
        SubTask expected = subTask;

        if (actual.equals(expected)) {
            System.out.println("Тест 7 пройден");
        } else {
            throw new AssertionError("Тест 7 не пройден.");
        }
    }

    private static void delete_Task_Should_List_Of_Tasks_Without_Remote_Task(TaskManager taskManager) {
        taskManager.deleteTask(0);
        List<Task> actual = taskManager.getTasks();
        List<Task> expected = List.of(taskManager.getTask(1));

        if (actual.equals(expected)) {
            System.out.println("Тест 8 пройден");
        } else {
            throw new AssertionError("Тест 8 не пройден.");
        }
    }

    private static void delete_Epic_Should_List_Of_Epics_Without_Remote_Epic_And_SupTask_Of_This_Epic(TaskManager taskManager) {
        taskManager.deleteEpic(3);
        List<Epic> actualEpic = taskManager.getEpics();
        List<Epic> expectedEpic = List.of(taskManager.getEpic(2));

        List<SubTask> actualSubTask = taskManager.getSubtask();
        List<SubTask> expectedSubTask = List.of(taskManager.getSubTask(4));

        if (actualEpic.equals(expectedEpic) && actualSubTask.equals(expectedSubTask)) {
            System.out.println("Тест 9 пройден");
        } else {
            throw new AssertionError("Тест 9 не пройден.");
        }
    }

    private static void delete_SubTask_Should_List_Of_SubTasks_And_Epic_Without_Remote_SubTask(TaskManager taskManager) {
        taskManager.deleteSubTask(4);
        List<SubTask> actualSubtaskOfEpic = taskManager.getSubtaskOfEpic(2);
        List<SubTask> expectedSubtaskOfEpic = List.of();

        List<SubTask> actualSubTask = taskManager.getSubtask();
        List<SubTask> expectedSubTask = List.of();

        if (actualSubtaskOfEpic.equals(expectedSubtaskOfEpic) && actualSubTask.equals(expectedSubTask)) {
            System.out.println("Тест 10 пройден");
        } else {
            throw new AssertionError("Тест 10 не пройден.");
        }
    }

    private static TaskManager createTasksForTestHistoryManager() {
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
            System.out.println("Тест 11 пройден");
        } else {
            throw new AssertionError("Тест 11 не пройден.");
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
            System.out.println("Тест 12 пройден");
        } else {
            throw new AssertionError("Тест 12 не пройден.");
        }
    }

    private static void after_Delete_TaskId0_And_TaskID1_History_Of_Tasks_Should_Comprises_TasksId_6_And_5(TaskManager taskManager) {
        taskManager.deleteTask(0);
        taskManager.deleteEpic(1);

        List<Task> actual = taskManager.getHistoryManager().getHistory();
        List<Task> expected = new ArrayList<>(List.of(taskManager.getEpic(6),
                taskManager.getTask(5)));

        if (actual.equals(expected)) {
            System.out.println("Тест 13 пройден");
        } else {
            throw new AssertionError("Тест 13 не пройден.");
        }
    }
}

