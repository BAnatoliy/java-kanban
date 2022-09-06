package Kanban.Task;

import Kanban.Constant.Status;
import Kanban.Constant.TypeTasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, Status status, String duration, String startTime, int epicId) {
        super(title, description, status, duration, startTime);
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
        return "SubTask{" +
                "id=" + id +
                ", epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration.toHours() + ":" + duration.toMinutesPart() +
                ", startTime=" + startTime.format(FORMATER) +
                ", endTime=" + getEndTime().format(FORMATER) +
                '}';
    }
}

