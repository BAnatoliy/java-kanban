package Kanban.Task;

import Kanban.Constant.Status;
import Kanban.Constant.TypeTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }


    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
    public TypeTasks getTypeTasks() {
        return TypeTasks.EPIC;
    }

    @Override
    public String toString() {
        if(duration != null && startTime != null) {
            return "Epic{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", duration=" + duration.toHours() + ":" + duration.toMinutesPart() +
                    ", startTime=" + startTime.format(FORMATTER) +
                    ", endTime=" + getEndTime().format(FORMATTER) +
                    '}';
        } else {
            return "Epic{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", duration=null" +
                    ", startTime=null" +
                    ", endTime=null" +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds) && Objects.equals(endTime, epic.endTime);
    }
}
