package TZ_3.Service;

import TZ_3.Task.Epic;
import TZ_3.Task.SubTask;
import TZ_3.Task.Task;

import java.util.List;

public class PrintConsole implements PrintService {

    // Печать всех Тасков
    @Override
    public void printTasks(List<Task> tasks) {
        System.out.println("Таски:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    // Печать всех Эпиков
    @Override
    public void printEpics(List<Epic> epics) {
        System.out.println("Эпики:");
        for (Epic epic : epics) {
            System.out.println("у эпика " + epic + " есть субтаски: " + epic.getSubTaskIds());
        }
    }

    // Печать всех Субтасков
    @Override
    public void printSubTasks(List<SubTask> subTasks) {
        System.out.println("СубТаски:");
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }
    }

    // Печать Субтаски выбранного Эпика
    @Override
    public void printSubtasksOfEpic(String epicTitle, List<SubTask> subTasks) {
        System.out.println("СубТаски эпика " + epicTitle +":");
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }
    }

    // Печать Таск по идентификатору
    @Override
    public void printTask(Task task) {
        System.out.println("Таск:");
        System.out.println(task);
    }

    // Печать Эпик по идентификатору
    @Override
    public void printEpic(Epic epic) {
        System.out.println("Эпик:");
        System.out.println("у эпика " + epic + " есть субтаски: " + epic.getSubTaskIds());
    }

    // Печать Субтаск по идентификатору
    @Override
    public void printSubTask(SubTask subTask) {
        System.out.println("СубТаск:");
        System.out.println(subTask);
    }

    @Override
    public void printHistoryTasks(List<Task> historyTasks) {
        System.out.println("История задач: ");
        System.out.println(historyTasks);
    }

}
