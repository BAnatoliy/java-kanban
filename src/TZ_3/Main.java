package TZ_3;

import TZ_3.Constant.Status;
import TZ_3.Service.Managers;
import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        Task task = new Task("Task 1", "description Task 1", Status.NEW);
        Task task2 = new Task("Task 2", "description Task 2", Status.IN_PROGRESS);

        Managers.getDefault().addTask(task);
        Managers.getDefault().addTask(task2);

        Epic epic = new Epic("Epic 1", "description Epic 1", Status.NEW); //Status.NEW
        Epic epic2 = new Epic("Epic 2", "description Epic 2", Status.NEW); //Status.NEW

        Managers.getDefault().addEpic(epic);
        Managers.getDefault().addEpic(epic2);

        SubTask subTask = new SubTask("SubTask1", "description Subtask1", Status.IN_PROGRESS, epic.getId());
        SubTask subTask2 = new SubTask("SubTask2", "description Subtask2", Status.DONE, epic2.getId());
        SubTask subTask3 = new SubTask("SubTask3", "description Subtask3", Status.DONE, epic2.getId());
        SubTask subTask4 = new SubTask("SubTask4", "description Subtask4", Status.NEW, epic2.getId());
        SubTask subTask5 = new SubTask("SubTask5", "description Subtask5", Status.DONE, epic2.getId());
        SubTask subTask6 = new SubTask("SubTask6", "description Subtask6", Status.DONE, epic2.getId());


        Managers.getDefault().addSubTask(subTask);
        Managers.getDefault().addSubTask(subTask2);
        Managers.getDefault().addSubTask(subTask3);
        Managers.getDefault().addSubTask(subTask4);
        Managers.getDefault().addSubTask(subTask5);
        Managers.getDefault().addSubTask(subTask6);


        // Получаем списки всех Тасков, Эпиков, всех Субтасков и Субтасков Эпика2
        List<Task> tasks = Managers.getDefault().getTasks();
        List<Epic> epics = Managers.getDefault().getEpics();
        List<SubTask> subTasks = Managers.getDefault().getSubtask();
        List<SubTask> subTasks2 = Managers.getDefault().getSubtaskOfEpic(epic2.getId());

        // Печатаем результат
        Managers.getDefaultPrint().printTasks(tasks);
        Managers.getDefaultPrint().printEpics(epics);
        Managers.getDefaultPrint().printSubTasks(subTasks);
        System.out.println("----------------");
        Managers.getDefaultPrint().printSubtasksOfEpic(epic.getTitle(), subTasks2);

        System.out.println("----------------");

        // Удаляем все Таски, Получаем новый список всех Тасков, выводим результат
        Managers.getDefault().deleteAllTasks();
        List<Task> tasks1 = Managers.getDefault().getTasks();
        Managers.getDefaultPrint().printTasks(tasks1);

        System.out.println("----------------");

        Managers.getDefault().deleteSubTask(subTask4.getId());
        List<Epic> epics2 = Managers.getDefault().getEpics();
        Managers.getDefaultPrint().printEpics(epics2);

        System.out.println("----------------");

        // Задаем новые объекты для замены старых
        Task task3 = new Task("Task 2.1", "description Task 2", Status.DONE);
        Epic epic3 = new Epic("Epic 2.1", "description Epic 2", Status.NEW); //Status.NEW
        SubTask subTask7 = new SubTask("SubTask 5.1", "description Subtask 5", Status.IN_PROGRESS, epic2.getId());

        // Обновление (замена старых объектов новыми)
        Managers.getDefault().updateTask(task3, task2.getId());
        Managers.getDefault().updateEpic(epic3, epic2.getId());
        Managers.getDefault().updateSubTask(subTask7, subTask5.getId());

        Managers.getDefaultPrint().printTask(Managers.getDefault().getTask(task3.getId()));
        Managers.getDefaultPrint().printEpic(Managers.getDefault().getEpic(epic3.getId()));
        Managers.getDefaultPrint().printSubTask(Managers.getDefault().getSubTask(subTask7.getId()));
        Managers.getDefaultPrint().printTask(Managers.getDefault().getTask(task3.getId()));
        Managers.getDefaultPrint().printEpic(Managers.getDefault().getEpic(epic3.getId()));
        Managers.getDefaultPrint().printSubTask(Managers.getDefault().getSubTask(subTask7.getId()));
        Managers.getDefaultPrint().printTask(Managers.getDefault().getTask(task3.getId()));
        Managers.getDefaultPrint().printEpic(Managers.getDefault().getEpic(epic3.getId()));
        Managers.getDefaultPrint().printSubTask(Managers.getDefault().getSubTask(subTask7.getId()));
        Managers.getDefaultPrint().printTask(Managers.getDefault().getTask(task3.getId()));
        Managers.getDefaultPrint().printEpic(Managers.getDefault().getEpic(epic3.getId()));
        Managers.getDefaultPrint().printSubTask(Managers.getDefault().getSubTask(subTask7.getId()));

        // Получение всех Задач и Подзадач Эпика3
        List<Task> tasks3 = Managers.getDefault().getTasks();
        List<Epic> epics3 = Managers.getDefault().getEpics();
        List<SubTask> subTasks3 = Managers.getDefault().getSubtask();
        List<SubTask> subTasks3_2 = Managers.getDefault().getSubtaskOfEpic(epic2.getId());

        // Печать всех Задач и Подзадач Эпика3
        Managers.getDefaultPrint().printTasks(tasks3);
        Managers.getDefaultPrint().printSubTasks(subTasks3);
        Managers.getDefaultPrint().printSubtasksOfEpic(epic3.getTitle(), subTasks3_2);
        Managers.getDefaultPrint().printEpics(epics3);
        System.out.println("----------------");

        // Печать Таск, Эпик, Субтаск по идентификаторам
        Managers.getDefaultPrint().printTask(Managers.getDefault().getTask(task3.getId()));
        Managers.getDefaultPrint().printEpic(Managers.getDefault().getEpic(epic3.getId()));
        Managers.getDefaultPrint().printSubTask(Managers.getDefault().getSubTask(subTask7.getId()));

        // Создание эпика со статусом Done (без Субтасков), печать его нового статуса исходя из условий задачи (NEW)
        System.out.println("----------------");
        System.out.println("----------------");

        Epic epic4 = new Epic("Epic 27", "description Epic 27", Status.DONE);
        Managers.getDefault().addEpic(epic4);
        List<Epic> epics4 = Managers.getDefault().getEpics();
        Managers.getDefaultPrint().printEpics(epics4);
        System.out.println(epic4.getSubTaskIds());

        // Печать списка эпиков после удаления эпика по идентификатору
        System.out.println("__________________");
        Managers.getDefault().deleteEpic(3);
        List<Epic> epics6 = Managers.getDefault().getEpics();
        List<SubTask> subTasks4 = Managers.getDefault().getSubtask();
        Managers.getDefaultPrint().printEpics(epics6);
        Managers.getDefaultPrint().printSubTasks(subTasks4);

        // Печать списка Эпиков после удаления всех Субтасков
        System.out.println("----------------");
        System.out.println("----------------");
        Managers.getDefault().deleteAllSubTasks();
        List<Epic> epics5 = Managers.getDefault().getEpics();
        Managers.getDefaultPrint().printEpics(epics5);

        System.out.println("++++++++++++++++");


        Managers.getDefaultPrint().printHistoryTasks(Managers.getDefaultHistory().getHistory());


    }

}

