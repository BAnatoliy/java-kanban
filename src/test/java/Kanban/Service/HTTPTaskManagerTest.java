package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Server.KVServer;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager>{
    HTTPTaskManager manager;
    KVServer server;

    @BeforeEach
    @Override
    protected void setTaskManager() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HTTPTaskManager("http://localhost:8078");
        manager.addEpic(new Epic("Epic 1", "description Epic", Status.NEW));
        manager.addEpic(new Epic("Epic 2", "description Epic", Status.NEW));
        manager.addSubTask(new SubTask("SubTask 1", "description Subtask", Status.NEW,
                "PT3H35M", "2022-01-03T15:50", 0));
        manager.addSubTask(new SubTask("SubTask 2", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-02T15:50", 0));
        manager.addSubTask(new SubTask("SubTask 3", "description Subtask", Status.DONE,
                "PT5H35M", "2022-01-07T15:00", 0));
        manager.addSubTask(new SubTask("SubTask 4", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-05T13:00", 0));
        manager.addTask(new Task("Task 1", "description Task", Status.NEW,
                "PT3H35M", "2022-02-01T15:50"));
        manager.addTask(new Task("Task 2", "description Task2", Status.DONE,
                "PT1H35M", "2022-02-07T15:50"));


    }

    /*@BeforeEach
    public void serverStart() {
        server.start();
    }
*/
    @AfterEach
    public void serverStop() {
        server.stop();
    }

    @Test
    public void shouldLoadManagerAfterSaveTask() {

        assertEquals(2, manager.getTasks().size());
        assertEquals(2, manager.getEpics().size());
        assertEquals(4, manager.getSubTasks().size());

        HTTPTaskManager newManager = new HTTPTaskManager("http://localhost:8078");
        assertEquals(2, newManager.getTasks().size());
        assertEquals(2, newManager.getEpics().size());
        assertEquals(4, newManager.getSubTasks().size());
    }

    @Test
    public void shouldLoadManagerAfterSaveTaskAndDeleteTaskID7AndAllSubTask() {
        assertEquals(2, manager.getTasks().size());
        assertEquals(2, manager.getEpics().size());
        assertEquals(4, manager.getSubTasks().size());

        manager.deleteTask(7);
        manager.deleteEpic(0);

        HTTPTaskManager newManager = new HTTPTaskManager("http://localhost:8078");
        assertEquals(1, newManager.getTasks().size());
        assertEquals(1, newManager.getEpics().size());
        assertEquals(0, newManager.getSubTasks().size());
    }

    @Test
    public void shouldLoadManagerWithHistoryAfterSaveTask() throws IOException {
        assertTrue(manager.getHistoryManager().getHistory().isEmpty());

        manager.getTask(7);
        manager.getSubTask(3);
        manager.getEpic(1);

        HTTPTaskManager newManager = new HTTPTaskManager("http://localhost:8078");
        assertEquals(3, newManager.getHistoryManager().getHistory().size());
    }

    @Test
    public void shouldLoadManagerWithHistoryAfterDeleteEpicId1() throws IOException {
        assertTrue(manager.getHistoryManager().getHistory().isEmpty());

        manager.getTask(7);
        manager.getSubTask(3);
        manager.getEpic(1);

        manager.deleteEpic(1);

        HTTPTaskManager newManager = new HTTPTaskManager("http://localhost:8078");
        assertEquals(2, newManager.getHistoryManager().getHistory().size());
    }

}