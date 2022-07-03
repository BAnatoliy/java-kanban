package TZ_3.Task;

import TZ_3.Constant.Status;

public class SubTask extends Task {

    private int epic_id;

    public SubTask(String title, String description, Status status, int epic_id) {
        super(title, description, status);
        this.epic_id = epic_id;
    }

    public int getEpic_id() {
        return epic_id;
    }

    public void setEpic_id(int epic_id) {
        this.epic_id = epic_id;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic_id=" + epic_id +
                ", task_id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}

