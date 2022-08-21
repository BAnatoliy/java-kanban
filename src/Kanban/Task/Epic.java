package Kanban.Task;

import Kanban.Constant.Status;
import Kanban.Constant.TypeOfTasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);

    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(List<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    // Добавление ID Субтаска в список Субтасков Эпика
    public void addSubTask(int subTask_id) {
        subTaskIds.add(subTask_id);
    }

    // Удаление из списка Субтасков элемента по заданному идентификатору
    public void removeSubTask(Integer subTask_id) {
        subTaskIds.remove(subTask_id);
    }

    @Override
    public TypeOfTasks getTypeOfTasks() {
        return TypeOfTasks.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
