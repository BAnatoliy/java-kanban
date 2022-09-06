package Kanban.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    File file;

    @BeforeEach
    @Override
    protected void setTaskManager() {
        file = new File("Files" + File.separator + "Tasks.CSV");
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            //Удаление данных из файла
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    public void tasksManager_Should_Throw_Exception_When_Load_From_Nonexistent_File() {
        assertAll(() -> {
                    ManagerSaveException exception = assertThrows(ManagerSaveException.class,
                            () -> {
                        File file = new File("Files" + File.separator + "Tasks123.CSV");
                        taskManager = FileBackedTasksManager.loadFromFile(file);
                            }
                    );
                    String expect = "java.io.FileNotFoundException: Files\\Tasks123.CSV (Не удается найти указанный файл)";
                    String result = exception.getMessage();
                    assertEquals(expect, result);
                }
        );
    }

    @Test
    public void tasksManager_Should_Not_Write_Information_In_File_And_Load_Empty_TaskManager_From_File_When_TaskManager_Empty_And() throws IOException {
        //Тест записи данных в файл из Менеджера
        List<String> result = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String task;
            while ((task = fileReader.readLine()) != null) {
                result.add(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        assertAll(() -> {
            assertTrue(result.isEmpty());
            assertTrue(taskManager.getTasks().isEmpty());
            assertTrue(taskManager.getEpics().isEmpty());
            assertTrue(taskManager.getSubTasks().isEmpty());
        }
        );

        //Тест загрузки Менеджера из файла
        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager, loadedTaskManager);
    }

    @Test
    public void tasksManager_Should_Write_Information_In_File_With_Unique_History_Of_Tasks_And_Load_TaskManager_From_File_When_One_Epic_Without_Subtask() throws IOException {
        //Тест записи данных в файл из Менеджера
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

        List<String> result = new ArrayList<>();
        List<String> expect = List.of("id,type,name,status,description,startTime,endTime,duration,epic",
                "2,TASK,Task 1,NEW,description Task,2022-02-01T15:50,2022-02-01T19:25,PT3H35M,",
                "3,TASK,Task 2,DONE,description Task2,2022-02-07T15:50,2022-02-07T17:25,PT1H35M,",
                "0,EPIC,Epic 1,IN_PROGRESS,description Epic,2022-01-02T15:50,2022-01-07T20:35,PT124H45M,",
                "1,EPIC,Epic 2,NEW,description Epic,null,null,null,",
                "4,SUBTASK,SubTask 1,NEW,description Subtask,2022-01-03T15:50,2022-01-03T19:25,PT3H35M,0",
                "5,SUBTASK,SubTask 2,IN_PROGRESS,description Subtask,2022-01-02T15:50,2022-01-02T17:25,PT1H35M,0",
                "6,SUBTASK,SubTask 3,DONE,description Subtask,2022-01-07T15:00,2022-01-07T20:35,PT5H35M,0",
                "7,SUBTASK,SubTask 4,IN_PROGRESS,description Subtask,2022-01-05T13:00,2022-01-05T14:35,PT1H35M,0",
                "",
                "1,0,3,2,4,7,6,5,"
        );

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String task;
            while ((task = fileReader.readLine()) != null) {
                result.add(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        assertEquals(expect, result);

        //Тест загрузки Менеджера из файла
        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager, loadedTaskManager);
    }

    @Test
    public void tasksManager_Should_Write_Information_In_File_And_Load_TaskManager_From_File_When_History_Of_Task_Empty() throws IOException {
        //Тест записи данных в файл из Менеджера
        setEpicsTasksSubTasks();

        List<String> result = new ArrayList<>();
        List<String> expect = List.of("id,type,name,status,description,startTime,endTime,duration,epic",
                "2,TASK,Task 1,NEW,description Task,2022-02-01T15:50,2022-02-01T19:25,PT3H35M,",
                "3,TASK,Task 2,DONE,description Task2,2022-02-07T15:50,2022-02-07T17:25,PT1H35M,",
                "0,EPIC,Epic 1,IN_PROGRESS,description Epic,2022-01-02T15:50,2022-01-07T20:35,PT124H45M,",
                "1,EPIC,Epic 2,NEW,description Epic,null,null,null,",
                "4,SUBTASK,SubTask 1,NEW,description Subtask,2022-01-03T15:50,2022-01-03T19:25,PT3H35M,0",
        "5,SUBTASK,SubTask 2,IN_PROGRESS,description Subtask,2022-01-02T15:50,2022-01-02T17:25,PT1H35M,0",
        "6,SUBTASK,SubTask 3,DONE,description Subtask,2022-01-07T15:00,2022-01-07T20:35,PT5H35M,0",
        "7,SUBTASK,SubTask 4,IN_PROGRESS,description Subtask,2022-01-05T13:00,2022-01-05T14:35,PT1H35M,0",
        ""
        );

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String task;
            while ((task = fileReader.readLine()) != null) {
                result.add(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        assertEquals(expect, result);

        //Тест загрузки Менеджера из файла
        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager, loadedTaskManager);
    }
}