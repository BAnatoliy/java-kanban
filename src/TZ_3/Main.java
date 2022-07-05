package TZ_3;

import TZ_3.Constant.Status;
import TZ_3.Service.PrintConsoleService;
import TZ_3.Service.TaskManagerService;
import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManagerService taskManagerService = new TaskManagerService();

        Task task = new Task("Task 1", "description Task 1", Status.NEW);
        Task task2 = new Task("Task 2", "description Task 2", Status.IN_PROGRESS);

        taskManagerService.addTask(task);
        taskManagerService.addTask(task2);

        Epic epic = new Epic("Epic 1", "description Epic 1", Status.NEW); //Status.NEW
        Epic epic2 = new Epic("Epic 2", "description Epic 2", Status.NEW); //Status.NEW

        taskManagerService.addEpic(epic);
        taskManagerService.addEpic(epic2);

        SubTask subTask = new SubTask("SubTask1", "description Subtask1", Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("SubTask2", "description Subtask2", Status.DONE, epic2.getId());
        SubTask subTask3 = new SubTask("SubTask3", "description Subtask3", Status.DONE, epic2.getId());
        SubTask subTask4 = new SubTask("SubTask4", "description Subtask4", Status.NEW, epic2.getId());
        SubTask subTask5 = new SubTask("SubTask5", "description Subtask5", Status.DONE, epic2.getId());
        SubTask subTask6 = new SubTask("SubTask6", "description Subtask6", Status.DONE, epic2.getId());


        taskManagerService.addSubTask(subTask);
        taskManagerService.addSubTask(subTask2);
        taskManagerService.addSubTask(subTask3);
        taskManagerService.addSubTask(subTask4);
        taskManagerService.addSubTask(subTask5);
        taskManagerService.addSubTask(subTask6);

        PrintConsoleService printConsoleService = new PrintConsoleService();

        // Получаем списки всех Тасков, Эпиков, всех Субтасков и Субтасков Эпика2
        List<Task> tasks = taskManagerService.getTasks();
        List<Epic> epics = taskManagerService.getEpics();
        List<SubTask> subTasks = taskManagerService.getSubtask();
        List<SubTask> subTasks2 = taskManagerService.getSubtaskOfEpic(epic2.getId());

        // Печатаем результат
        printConsoleService.printTasks(tasks);
        printConsoleService.printEpics(epics);
        printConsoleService.printSubTasks(subTasks);
        System.out.println("----------------");
        printConsoleService.printSubtasksOfEpic(epic.getTitle(), subTasks2);

        System.out.println("----------------");

        // Удаляем все Таски, Получаем новый список всех Тасков, выводим результат
        taskManagerService.deleteAllTasks();
        List<Task> tasks1 = taskManagerService.getTasks();
        printConsoleService.printTasks(tasks1);

        System.out.println("----------------");

        taskManagerService.deleteSubTask(subTask4.getId());
        List<Epic> epics2 = taskManagerService.getEpics();
        printConsoleService.printEpics(epics2);

        System.out.println("----------------");

        // Задаем новые объекты для замены старых
        Task task3 = new Task("Task 2.1", "description Task 2", Status.DONE);
        Epic epic3 = new Epic("Epic 2.1", "description Epic 2", Status.NEW); //Status.NEW
        SubTask subTask7 = new SubTask("SubTask 5.1", "description Subtask 5", Status.IN_PROGRESS, epic2.getId());

        // Обновление (замена старых объектов новыми)
        taskManagerService.updateTask(task3, task2.getId());
        taskManagerService.updateEpic(epic3, epic2.getId());
        taskManagerService.updateSubTask(subTask7, subTask5.getId());

        // Получение всех Задач и Подзадач Эпика3
        List<Task> tasks3 = taskManagerService.getTasks();
        List<Epic> epics3 = taskManagerService.getEpics();
        List<SubTask> subTasks3 = taskManagerService.getSubtask();
        List<SubTask> subTasks3_2 = taskManagerService.getSubtaskOfEpic(epic2.getId());

        // Печать всех Задач и Подзадач Эпика3
        printConsoleService.printTasks(tasks3);
        printConsoleService.printSubTasks(subTasks3);
        printConsoleService.printSubtasksOfEpic(epic3.getTitle(), subTasks3_2);
        printConsoleService.printEpics(epics3);
        System.out.println("----------------");

        // Печать Таск, Эпик, Субтаск по идентификаторам
        printConsoleService.printTask(taskManagerService.getTask(task3.getId()));
        printConsoleService.printEpic(taskManagerService.getEpic(epic3.getId()));
        printConsoleService.printSubTask(taskManagerService.getSubTask(subTask7.getId()));

        // Создание эпика со статусом Done (без Субтасков), печать его нового статуса исходя из условий задачи (NEW)
        System.out.println("----------------");
        System.out.println("----------------");

        Epic epic4 = new Epic("Epic 27", "description Epic 27", Status.DONE);
        taskManagerService.addEpic(epic4);
        List<Epic> epics4 = taskManagerService.getEpics();
        printConsoleService.printEpics(epics4);
        System.out.println(epic4.getSubTaskIds());

        // Печать списка Эпиков после удаления всех Субтасков
        System.out.println("----------------");
        System.out.println("----------------");
        taskManagerService.deleteAllSubTasks();
        List<Epic> epics5 = taskManagerService.getEpics();
        printConsoleService.printEpics(epics5);

    }

}

