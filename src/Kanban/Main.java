package Kanban;

import Kanban.Constant.Status;
import Kanban.Service.Managers;
import Kanban.Service.PrintConsole;
import Kanban.Service.TaskManager;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        PrintConsole printConsole = new PrintConsole();

        Task task = new Task("Task 1", "description Task 1", Status.NEW);
        Task task2 = new Task("Task 2", "description Task 2", Status.IN_PROGRESS);

        taskManager.addTask(task);
        taskManager.addTask(task2);

        Epic epic = new Epic("Epic 1", "description Epic 1", Status.NEW); //Status.NEW
        Epic epic2 = new Epic("Epic 2", "description Epic 2", Status.NEW); //Status.NEW

        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        SubTask subTask = new SubTask("SubTask1", "description Subtask1", Status.IN_PROGRESS, epic.getId());
        SubTask subTask2 = new SubTask("SubTask2", "description Subtask2", Status.DONE, epic2.getId());
        SubTask subTask3 = new SubTask("SubTask3", "description Subtask3", Status.DONE, epic2.getId());
        SubTask subTask4 = new SubTask("SubTask4", "description Subtask4", Status.NEW, epic2.getId());
        SubTask subTask5 = new SubTask("SubTask5", "description Subtask5", Status.DONE, epic2.getId());
        SubTask subTask6 = new SubTask("SubTask6", "description Subtask6", Status.DONE, epic2.getId());

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
        taskManager.addSubTask(subTask5);
        taskManager.addSubTask(subTask6);

        // Получаем списки всех Тасков, Эпиков, всех Субтасков и Субтасков Эпика2
        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<SubTask> subTasks = taskManager.getSubtask();
        List<SubTask> subTasks2 = taskManager.getSubtaskOfEpic(epic2.getId());

        // Печатаем результат
        printConsole.printTasks(tasks);
        printConsole.printEpics(epics);
        printConsole.printSubTasks(subTasks);
        System.out.println("----------------");
        printConsole.printSubtasksOfEpic(epic.getTitle(), subTasks2);

        System.out.println("----------------");

        // Удаляем все Таски, Получаем новый список всех Тасков, выводим результат
        taskManager.deleteAllTasks();
        List<Task> tasks1 = taskManager.getTasks();
        printConsole.printTasks(tasks1);

        System.out.println("----------------");

        taskManager.deleteSubTask(subTask4.getId());
        List<Epic> epics2 = taskManager.getEpics();
        printConsole.printEpics(epics2);

        System.out.println("----------------");

        // Задаем новые объекты для замены старых
        Task task3 = new Task("Task 2.1", "description Task 2", Status.DONE);
        Epic epic3 = new Epic("Epic 2.1", "description Epic 2", Status.NEW); //Status.NEW
        SubTask subTask7 = new SubTask("SubTask 5.1", "description Subtask 5", Status.IN_PROGRESS, epic2.getId());

        // Обновление (замена старых объектов новыми)
        taskManager.updateTask(task3, task2.getId());
        taskManager.updateEpic(epic3, epic2.getId());
        taskManager.updateSubTask(subTask7, subTask5.getId());

        printConsole.printTask(taskManager.getTask(task3.getId()));
        printConsole.printEpic(taskManager.getEpic(epic3.getId()));
        printConsole.printSubTask(taskManager.getSubTask(subTask7.getId()));
        printConsole.printTask(taskManager.getTask(task3.getId()));
        printConsole.printEpic(taskManager.getEpic(epic3.getId()));
        printConsole.printSubTask(taskManager.getSubTask(subTask7.getId()));
        printConsole.printTask(taskManager.getTask(task3.getId()));
        printConsole.printEpic(taskManager.getEpic(epic3.getId()));
        printConsole.printSubTask(taskManager.getSubTask(subTask7.getId()));
        printConsole.printTask(taskManager.getTask(task3.getId()));
        printConsole.printEpic(taskManager.getEpic(epic3.getId()));
        printConsole.printSubTask(taskManager.getSubTask(subTask7.getId()));

        printConsole.printHistoryTasks(taskManager.getHistoryManager().getHistory());


        // Получение всех Задач и Подзадач Эпика3
        List<Task> tasks3 = taskManager.getTasks();
        List<Epic> epics3 = taskManager.getEpics();
        List<SubTask> subTasks3 = taskManager.getSubtask();
        List<SubTask> subTasks3_2 = taskManager.getSubtaskOfEpic(epic2.getId());

        // Печать всех Задач и Подзадач Эпика3
        printConsole.printTasks(tasks3);
        printConsole.printSubTasks(subTasks3);
        printConsole.printSubtasksOfEpic(epic3.getTitle(), subTasks3_2);
        printConsole.printEpics(epics3);
        System.out.println("----------------");

        // Печать Таск, Эпик, Субтаск по идентификаторам
        printConsole.printTask(taskManager.getTask(task3.getId()));
        printConsole.printEpic(taskManager.getEpic(epic3.getId()));
        printConsole.printSubTask(taskManager.getSubTask(subTask7.getId()));

        // Создание эпика со статусом Done (без Субтасков), печать его нового статуса исходя из условий задачи (NEW)
        System.out.println("----------------");
        printConsole.printHistoryTasks(taskManager.getHistoryManager().getHistory());

        System.out.println("----------------");

        Epic epic4 = new Epic("Epic 27", "description Epic 27", Status.DONE);
        taskManager.addEpic(epic4);
        List<Epic> epics4 = taskManager.getEpics();
        printConsole.printEpics(epics4);
        System.out.println(epic4.getSubTaskIds());

        // Печать списка эпиков после удаления эпика по идентификатору
        System.out.println("__________________");
        taskManager.deleteEpic(3);
        List<Epic> epics6 = taskManager.getEpics();
        List<SubTask> subTasks4 = taskManager.getSubtask();
        printConsole.printEpics(epics6);
        printConsole.printSubTasks(subTasks4);

        // Печать списка Эпиков после удаления всех Субтасков
        System.out.println("----------------");
        taskManager.deleteAllSubTasks();
        List<Epic> epics5 = taskManager.getEpics();
        printConsole.printEpics(epics5);

        taskManager.deleteTask(1);

        System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Проверка ТЗ-5. Вывод задач и истории просмотров\n");

        Task task_1 = new Task("1 Task", "description Task 1_1", Status.DONE);
        taskManager.addTask(task_1);
        Epic epic_1 = new Epic("2 Epic", "description Epic 1_2", Status.NEW);
        taskManager.addEpic(epic_1);
        SubTask subTask_1 = new SubTask("2 Epic - 3 SubTask", "description 3 Subtask", Status.NEW, epic_1.getId());
        taskManager.addSubTask(subTask_1);
        SubTask subTask_2 = new SubTask("2 Epic - 4 SubTask", "description 4 Subtask", Status.NEW, epic_1.getId());
        taskManager.addSubTask(subTask_2);
        SubTask subTask_3 = new SubTask("2 Epic - 5 SubTask", "description 5 Subtask", Status.NEW, epic_1.getId());
        taskManager.addSubTask(subTask_3);

        Task task_2 = new Task("6 Task", "description 6 Task", Status.NEW);
        taskManager.addTask(task_2);
        Epic epic_2 = new Epic("7 Epic", "description 7 Epic", Status.NEW);
        taskManager.addEpic(epic_2);

        printConsole.printTask(taskManager.getTask(task_1.getId())); // печать таски ID = 11;
        printConsole.printEpic(taskManager.getEpic(epic_1.getId())); // печать эпика ID = 12;
        printConsole.printSubTask(taskManager.getSubTask(subTask_1.getId())); // печать субтаски ID = 13;
        printConsole.printSubTask(taskManager.getSubTask(subTask_2.getId())); // печать субтаски ID = 14;
        printConsole.printSubTask(taskManager.getSubTask(subTask_3.getId())); // печать субтаски ID = 15;
        printConsole.printTask(taskManager.getTask(task_2.getId())); // печать таски ID = 16;
        printConsole.printEpic(taskManager.getEpic(epic_2.getId())); // печать эпика ID = 17;

        // печать истории просмотров
        printConsole.printHistoryTasks(taskManager.getHistoryManager().getHistory());
        System.out.println("----------------");

        printConsole.printSubTask(taskManager.getSubTask(subTask_2.getId())); // печать субтаски ID = 14;
        printConsole.printEpic(taskManager.getEpic(epic_1.getId())); // печать эпика ID = 12;
        printConsole.printSubTask(taskManager.getSubTask(subTask_1.getId())); // печать субтаски ID = 13;
        printConsole.printEpic(taskManager.getEpic(epic_2.getId())); // печать эпика ID = 17;
        printConsole.printTask(taskManager.getTask(task_1.getId())); // печать таски ID = 11;
        printConsole.printSubTask(taskManager.getSubTask(subTask_3.getId())); // печать субтаски ID = 15;
        printConsole.printTask(taskManager.getTask(task_2.getId())); // печать таски ID = 16;

        // печать истории просмотров после изменения порядка
        printConsole.printHistoryTasks(taskManager.getHistoryManager().getHistory());
        System.out.println("----------------");

        taskManager.deleteTask(task_1.getId());  // удаление таски ID = 11;
        taskManager.deleteEpic(epic_1.getId()); // удаление эпика ID = 12;

        // печать истории просмотров после изменения порядка после удаления таски и эпика(вместе с его субтасками)
        printConsole.printHistoryTasks(taskManager.getHistoryManager().getHistory());

    }
}

