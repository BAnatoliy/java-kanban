package Kanban.Constant;

public enum TypeTasks {
    TASK,
    EPIC,
    SUBTASK;

    public static TypeTasks fromString(String value) {
        switch (value) {
            case "TASK" : return TASK;
            case "EPIC" : return EPIC;
            case "SUBTASK" : return SUBTASK;
            default: return null;
        }
    }


}
