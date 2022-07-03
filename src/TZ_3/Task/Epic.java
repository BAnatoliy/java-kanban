package TZ_3.Task;

import TZ_3.Constant.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);

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

}
