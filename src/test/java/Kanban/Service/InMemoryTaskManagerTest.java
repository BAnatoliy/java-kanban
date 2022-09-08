package Kanban.Service;

import Kanban.Constant.Status;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    @Override
    protected void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void addTask_Test() throws IOException {
        setTasks();

        int result = taskManager.getMapOfTasks().size();
        int expect = 2;
        assertEquals(expect, result);

        DateTimeParseException exception = assertThrows(DateTimeParseException.class,
                () -> taskManager.addTask(new Task(
                        "ID 0 Task","description Task", Status.NEW,"PT3H35M", "dfsdfsd"))
        );
    }

    @Test
    public void addEpic_Test() throws IOException {
        setEpics();

        int result = taskManager.getMapOfEpic().size();
        int expect = 2;
        assertEquals(expect, result);
    }

    @Test
    public void addSubTask_Test() throws IOException {
        setSubTasksForEpicId_0();
        boolean isNotAdd = taskManager.getMapOfSubTasks().isEmpty();
        assertTrue(isNotAdd);

        setEpicsTasksSubTasks();
        int result = taskManager.getMapOfSubTasks().size();
        int expect = 4;
        assertEquals(expect, result);
    }

    @Test
    public void updateTask_Test() throws IOException {
        setTasks();

        Task expect = new Task("Task After Update", "description NewTask", Status.DONE,
                "PT0H35M", "2022-07-27T17:30");
        taskManager.updateTask(expect, 0);
        Task result = taskManager.getTask(0);
        assertEquals(expect, result);

        taskManager.updateTask(new Task("Task After Update", "description NewTask", Status.DONE,
                "PT0H35M", "2022-07-27T17:30"), 5);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getTask(5));

        int expectSize = 2;
        int resultSize = taskManager.getTasks().size();

        assertEquals(expectSize, resultSize);
    }

    @Test
    public void updateEpic_Test() throws IOException {
        setEpics();

        Epic expect = new Epic("Epic After Update", "description Epic", Status.NEW);
        taskManager.updateEpic(expect, 0);
        Epic result = taskManager.getEpic(0);
        assertEquals(expect, result);

        taskManager.updateTask(new Epic("Epic After Update", "description Epic", Status.NEW), 5);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getEpic(5));

        int expectSize = 2;
        int resultSize = taskManager.getEpics().size();
        assertEquals(expectSize, resultSize);
    }

    @Test
    public void updateSubTask_Test() throws IOException {
        setEpicsTasksSubTasks();

        SubTask expect = new SubTask("SubTask After Update", "description Subtask", Status.IN_PROGRESS,
                "PT1H27M", "2022-03-07T15:50", 0);
        taskManager.updateSubTask(expect, 5);
        SubTask result = taskManager.getSubTask(5);
        assertEquals(expect, result);

        taskManager.updateSubTask(new SubTask("SubTask After Update", "description Subtask", Status.IN_PROGRESS,
                "PT1H27M", "2022-03-07T15:50", 0), 11);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getSubTask(11));

        int expectSize = 4;
        int resultSize = taskManager.getSubTasks().size();
        assertEquals(expectSize, resultSize);
    }

    @Test
    public void getTask_Test() throws IOException {
        assertTrue(taskManager.getTasks().isEmpty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getTask(0));

        setEpicsTasksSubTasks();
        String expectTaskName = "Task 2";
        String resultTaskName = taskManager.getTask(3).getTitle();
        assertEquals(expectTaskName, resultTaskName);

        NullPointerException exception2 = assertThrows(NullPointerException.class, () -> taskManager.getTask(10));
    }

    @Test
    public void getEpic_Test() throws IOException {
        assertTrue(taskManager.getEpics().isEmpty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getEpic(0));

        setEpicsTasksSubTasks();
        String expectEpicName = "Epic 2";
        String resultEpicName = taskManager.getEpic(1).getTitle();
        assertEquals(expectEpicName, resultEpicName);

        NullPointerException exception2 = assertThrows(NullPointerException.class, () -> taskManager.getEpic(10));
    }

    @Test
    public void getSubTask_Test() throws IOException {
        assertTrue(taskManager.getSubTasks().isEmpty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.getSubTask(0));

        setEpicsTasksSubTasks();
        String expectSubTaskName = "SubTask 2";
        String resultSubTaskName = taskManager.getSubTask(5).getTitle();
        assertEquals(expectSubTaskName, resultSubTaskName);

        NullPointerException exception2 = assertThrows(NullPointerException.class, () -> taskManager.getSubTask(10));
    }

    @Test
    public void deleteAllTasks_Test() throws IOException {
        assertTrue(taskManager.getTasks().isEmpty());
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getTasks().isEmpty());

        setEpicsTasksSubTasks();
        int result = taskManager.getTasks().size();
        int expect = 2;
        assertEquals(expect, result);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void deleteAllEpics_Test() throws IOException {
        assertTrue(taskManager.getEpics().isEmpty());
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getEpics().isEmpty());

        setEpicsTasksSubTasks();
        int resultAmountEpics = taskManager.getEpics().size();
        int expectAmountEpics = 2;
        assertEquals(expectAmountEpics, resultAmountEpics);
        int expectAmountSubTasks = 4;
        int resultAmountSubTasks = taskManager.getSubTasks().size();
        assertEquals(expectAmountSubTasks, resultAmountSubTasks);
        taskManager.deleteAllEpics();
        assertAll(() -> {
            assertTrue(taskManager.getEpics().isEmpty());
            assertTrue(taskManager.getSubTasks().isEmpty());
        });
    }

    @Test
    public void deleteAllSubTasks_Test() throws IOException {
        assertTrue(taskManager.getSubTasks().isEmpty());
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());

        setEpicsTasksSubTasks();
        int expectAmountEpics = 4;
        int resultAmountEpics = taskManager.getEpic(0).getSubTaskIds().size();
        assertEquals(expectAmountEpics, resultAmountEpics);
        int expectAmountSubTasks = 4;
        int resultAmountSubTasks = taskManager.getSubTasks().size();
        assertEquals(expectAmountSubTasks, resultAmountSubTasks);
        taskManager.deleteAllSubTasks();
        assertAll(() -> {
            assertTrue(taskManager.getEpic(0).getSubTaskIds().isEmpty());
            assertTrue(taskManager.getSubTasks().isEmpty());
        });
    }

    @Test
    public void deleteTask_Test() throws IOException {
        assertTrue(taskManager.getTasks().isEmpty());
        taskManager.deleteTask(0);
        assertTrue(taskManager.getTasks().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 2;
        int result = taskManager.getTasks().size();
        assertEquals(expect, result);
        taskManager.deleteTask(2);
        int expectAfterDelete = 1;
        int resultAfterDelete = taskManager.getTasks().size();
        assertEquals(expectAfterDelete, resultAfterDelete);
    }

    @Test
    public void deleteEpic_Test() throws IOException {
        assertTrue(taskManager.getEpics().isEmpty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.deleteEpic(0));
        assertTrue(taskManager.getEpics().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 2;
        int result = taskManager.getEpics().size();
        assertEquals(expect, result);
        taskManager.deleteEpic(0);
        int expectAmountEpicAfterDelete = 1;
        int resultAmountEpicAfterDelete = taskManager.getEpics().size();
        assertAll(() -> {
            assertEquals(expectAmountEpicAfterDelete, resultAmountEpicAfterDelete);
            assertTrue(taskManager.getSubTasks().isEmpty());
        }
        );

        NullPointerException exception2 = assertThrows(NullPointerException.class, () -> taskManager.deleteEpic(10));
    }

    @Test
    public void deleteSubTask_Test() throws IOException {
        assertTrue(taskManager.getSubTasks().isEmpty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> taskManager.deleteSubTask(0));
        assertTrue(taskManager.getSubTasks().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 4;
        int result = taskManager.getSubTasks().size();
        assertEquals(expect, result);
        taskManager.deleteSubTask(4);
        int expectAmountSubTasksAfterDelete = 3;
        int resultAmountSubTasksAfterDelete = taskManager.getSubTasks().size();
        int expectAmountSubTasksOfEpicAfterDelete = 3;
        int resultAmountSubTasksOfEpicAfterDelete = taskManager.getSubtaskOfEpic(0).size();
        assertAll(() -> {
                    assertEquals(expectAmountSubTasksAfterDelete, resultAmountSubTasksAfterDelete);
                    assertEquals(expectAmountSubTasksOfEpicAfterDelete, resultAmountSubTasksOfEpicAfterDelete);
                }
        );

        NullPointerException exception2 = assertThrows(NullPointerException.class, () -> taskManager.deleteSubTask(10));
    }

    @Test
    public void getTasks_Test() throws IOException {
        assertTrue(taskManager.getTasks().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 2;
        int result = taskManager.getTasks().size();
        assertEquals(expect, result);
    }

    @Test
    public void getEpics_Test() throws IOException {
        assertTrue(taskManager.getEpics().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 2;
        int result = taskManager.getEpics().size();
        assertEquals(expect, result);
    }

    @Test
    public void getSubTasks_Test() throws IOException {
        assertTrue(taskManager.getSubTasks().isEmpty());

        setEpicsTasksSubTasks();
        int expect = 4;
        int result = taskManager.getSubTasks().size();
        assertEquals(expect, result);
    }

    @Test
    public void getSubTasksOfEpic_Test() throws IOException {
        assertTrue(taskManager.getSubtaskOfEpic(0).isEmpty());

        setEpicsTasksSubTasks();
        int expect = 4;
        int result = taskManager.getSubtaskOfEpic(0).size();
        assertEquals(expect, result);

        assertTrue(taskManager.getSubtaskOfEpic(10).isEmpty());
    }

    @Test
    public void epic_Status_Should_Be_New_When_All_Its_Subtasks_Status_New() throws IOException {
        taskManager.addEpic(new Epic("ID 0 Epic", "description Epic", Status.NEW));
        taskManager.addSubTask(new SubTask("ID 1 SubTask", "description Subtask", Status.NEW,
                "PT1H35M", "2022-01-01T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.NEW,
                "PT2H35M", "2022-01-02T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 3 SubTask", "description Subtask", Status.NEW,
                "PT3H35M", "2022-01-03T15:00", 0));

        Status result = taskManager.getEpic(0).getStatus();
        Status expect = Status.NEW;

        Assertions.assertEquals(expect, result);
    }

    @Test
    public void epic_Status_Should_Be_Done_When_All_Its_Subtasks_Status_Done() throws IOException {
        taskManager.addEpic(new Epic("ID 0 Epic", "description Epic", Status.NEW));
        taskManager.addSubTask(new SubTask("ID 1 SubTask", "description Subtask", Status.DONE,
                "PT1H35M", "2022-01-01T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.DONE,
                "PT2H35M", "2022-01-02T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 3 SubTask", "description Subtask", Status.DONE,
                "PT3H35M", "2022-01-03T15:00", 0));

        Status result = taskManager.getEpic(0).getStatus();
        Status expect = Status.DONE;

        Assertions.assertEquals(expect, result);
    }

    @Test
    public void epic_Status_Should_Be_In_Progress_When_Its_Subtasks_Status_New_And_Done() throws IOException {
        taskManager.addEpic(new Epic("ID 0 Epic", "description Epic", Status.NEW));
        taskManager.addSubTask(new SubTask("ID 1 SubTask", "description Subtask", Status.DONE,
                "PT1H35M", "2022-01-01T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.NEW,
                "PT2H35M", "2022-01-02T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 3 SubTask", "description Subtask", Status.DONE,
                "PT3H35M", "2022-01-03T15:00", 0));

        Status result = taskManager.getEpic(0).getStatus();
        Status expect = Status.IN_PROGRESS;

        Assertions.assertEquals(expect, result);
    }

    @Test
    public void epic_Status_Should_Be_In_Progress_When_Its_Subtask_Status_In_Progress() throws IOException {
        taskManager.addEpic(new Epic("ID 0 Epic", "description Epic", Status.NEW));
        taskManager.addSubTask(new SubTask("ID 1 SubTask", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-01T15:50", 0));
        taskManager.addSubTask(new SubTask("ID 2 SubTask", "description Subtask", Status.IN_PROGRESS,
                "PT2H35M", "2022-01-02T15:50", 0));

        Status result = taskManager.getEpic(0).getStatus();
        Status expect = Status.IN_PROGRESS;

        Assertions.assertEquals(expect, result);
    }

    @Test
    public void epic_StartTime_Should_Be_Like_StartTime_First_Subtask_And_Epic_EndTime_Should_Be_Like_EndTime_Last_Subtask_And_Epic_Duration_Should_Be_Difference_Between_Start_And_EndTime() throws IOException {
        setEpicsTasksSubTasks();
        LocalDateTime expectStartTime = taskManager.getSubTask(5).getStartTime();
        LocalDateTime resultStartTime = taskManager.getEpic(0).getStartTime();
        LocalDateTime expectEndTime = taskManager.getSubTask(6).getEndTime();
        LocalDateTime resultEndTime = taskManager.getEpic(0).getEndTime();
        Duration expectDuration = Duration.between(expectStartTime, expectEndTime);
        Duration resultDuration = taskManager.getEpic(0).getDuration();
        assertAll (() -> {
                    assertEquals(expectStartTime, resultStartTime);
                    assertEquals(expectEndTime, resultEndTime);
                    assertEquals(expectDuration, resultDuration);
                }
        );

        taskManager.deleteSubTask(5);
        taskManager.deleteSubTask(6);
        LocalDateTime expectStartTimeAfterDeleteSubTasks5_6 = taskManager.getSubTask(4).getStartTime();
        LocalDateTime resultStartTimeDeleteSubTasks5_6 = taskManager.getEpic(0).getStartTime();
        LocalDateTime expectEndTimeDeleteSubTasks5_6 = taskManager.getSubTask(7).getEndTime();
        LocalDateTime resultEndTimeDeleteSubTasks5_6 = taskManager.getEpic(0).getEndTime();
        Duration expectDurationDeleteSubTasks5_6 = Duration.between(expectStartTimeAfterDeleteSubTasks5_6, expectEndTimeDeleteSubTasks5_6);
        Duration resultDurationDeleteSubTasks5_6 = taskManager.getEpic(0).getDuration();
        assertAll (() -> {
                    assertEquals(expectStartTimeAfterDeleteSubTasks5_6, resultStartTimeDeleteSubTasks5_6);
                    assertEquals(expectEndTimeDeleteSubTasks5_6, resultEndTimeDeleteSubTasks5_6);
                    assertEquals(expectDurationDeleteSubTasks5_6, resultDurationDeleteSubTasks5_6);
                }
        );
    }

    @Test
    public void task_Should_Not_Be_Added_When_Time_Cross_With_Early_Added_Task() throws IOException {
        setEpicsTasksSubTasks();
        int expectSizeBeforeAddedNewTask = 2;
        int resultSizeBeforeAddedNewTask = taskManager.getTasks().size();
        int expectSizeBeforeAddedNewSubTask = 4;
        int resultSizeBeforeAddedNewSubTask = taskManager.getSubTasks().size();
        int expectSizeSortedListBeforeAddedTasks = 6;
        int resultSizeSortedListBeforeAddedTasks = taskManager.getPrioritizedTasks().size();
        assertAll(() -> {
                    assertEquals(expectSizeBeforeAddedNewTask, resultSizeBeforeAddedNewTask);
                    assertEquals(expectSizeBeforeAddedNewSubTask, resultSizeBeforeAddedNewSubTask);
                    assertEquals(expectSizeSortedListBeforeAddedTasks, resultSizeSortedListBeforeAddedTasks);
        }
        );
        taskManager.addTask(new Task("Task Not Add", "description Task2", Status.NEW,
                "PT1H35M", "2022-02-07T14:50"));
        taskManager.addTask(new Task("Task Will Be Added", "description Task2", Status.NEW,
                "PT1H35M", "2022-02-08T14:50"));
        taskManager.addSubTask(new SubTask("SubTask Not Add", "description Subtask", Status.NEW,
                "PT1H35M", "2022-01-02T16:50", 1));
        taskManager.addSubTask(new SubTask("SubTask Will Be Added", "description Subtask", Status.NEW,
                "PT1H35M", "2022-01-02T12:50", 1));

        int expectSizeAfterAddedNewTask = 3;
        int resultSizeAfterAddedNewTask = taskManager.getTasks().size();
        int expectSizeAfterAddedNewSubTask = 5;
        int resultSizeAfterAddedNewSubTask = taskManager.getSubTasks().size();
        int expectSizeSortedListAfterAddedTasks = 8;
        int resultSizeSortedListAfterAddedTasks = taskManager.getPrioritizedTasks().size();
        assertAll(() -> {
            assertEquals(expectSizeAfterAddedNewTask, resultSizeAfterAddedNewTask);
            assertEquals(expectSizeAfterAddedNewSubTask, resultSizeAfterAddedNewSubTask);
            assertEquals(expectSizeSortedListAfterAddedTasks, resultSizeSortedListAfterAddedTasks);
        }
        );
    }

    @Test
    public void historyManager_Should_Add_Tasks_When_TaskManager_Get_This_Tasks() throws IOException {
        assertAll(() -> {
                    NullPointerException exception = assertThrows(NullPointerException.class,
                            () -> {
                                taskManager.getEpic(1);
                                taskManager.getEpic(0);
                                taskManager.getTask(3);
                                taskManager.getTask(2);
                                taskManager.getSubTask(4);
                                taskManager.getSubTask(7);
                                taskManager.getSubTask(6);
                                taskManager.getSubTask(5);
                            });
                    assertTrue(taskManager.getHistoryManager().getHistory().isEmpty());
                }
        );

        setEpicsTasksSubTasks();
        taskManager.getEpic(1);
        taskManager.getEpic(0);
        taskManager.getTask(3);
        taskManager.getTask(2);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.getEpic(100);
                    taskManager.getTask(101);
                    taskManager.getSubTask(102);
                });
        List<Integer> extend = List.of(1, 0, 3, 2, 4, 7, 6, 5);
        List<Task> listOfHistoryTask = taskManager.getHistoryManager().getHistory();
        List<Integer> result = new ArrayList<>();
        for (Task task : listOfHistoryTask) {
            result.add(task.getId());
        }
        assertEquals(extend, result);
    }

    @Test
    public void historyManager_Should_Has_Unique_Task_When_TaskManager_Get_Same_Tasks() throws IOException {
        setEpicsTasksSubTasks();
        taskManager.getEpic(1);
        taskManager.getEpic(0);
        taskManager.getTask(3);
        taskManager.getTask(2);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);

        taskManager.getEpic(1);
        taskManager.getEpic(0);
        taskManager.getTask(3);
        taskManager.getTask(2);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);
        taskManager.getSubTask(5);
        taskManager.getSubTask(5);

        List<Integer> extend = List.of(1, 0, 3, 2, 4, 7, 6, 5);
        List<Task> listOfHistoryTask = taskManager.getHistoryManager().getHistory();
        List<Integer> result = new ArrayList<>();
        for (Task task : listOfHistoryTask) {
            result.add(task.getId());
        }
        assertEquals(extend, result);
    }

    @Test
    public void historyManager_Should_Delete_History_About_Task_When_TaskManager_Delete_Task() throws IOException {
        setEpicsTasksSubTasks();
        taskManager.getEpic(1);
        taskManager.getEpic(0);
        taskManager.getTask(3);
        taskManager.getTask(2);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);

        taskManager.deleteAllTasks();
        taskManager.deleteEpic(0);
        List<Integer> extend = List.of(1);
        List<Task> listOfHistoryTask = taskManager.getHistoryManager().getHistory();
        List<Integer> result = new ArrayList<>();
        for (Task task : listOfHistoryTask) {
            result.add(task.getId());
        }
        assertEquals(extend, result);
    }
}