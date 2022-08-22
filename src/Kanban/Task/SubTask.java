package Kanban.Task;

import Kanban.Constant.Status;
import Kanban.Constant.TypeTasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTasks getTypeTasks() {
        return TypeTasks.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic_id=" + epicId +
                ", task_id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}

