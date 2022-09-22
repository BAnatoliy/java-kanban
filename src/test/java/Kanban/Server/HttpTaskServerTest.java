package Kanban.Server;

import Kanban.Constant.Status;
import Kanban.Service.Managers;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer hts;
    Gson gson;

    @BeforeEach
    public void init() throws IOException {
        hts = new HttpTaskServer();
        hts.start();
        gson = Managers.getGson();
    }

    @AfterEach
    public void stop() {
        hts.stop();
    }

    @Test
    public void should_Get_Priority_Tasks_When_Client_Send_Request_Get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        URI url2 = URI.create("http://localhost:8080/tasks");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        Type tasksType = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfTask = gson.fromJson(response3.body(), tasksType);
        assertNotNull(listOfTask);
        assertEquals(2, listOfTask.size());
    }

    @Test
    public void should_Get_Empty_List_Of_Task_When_Client_Send_Request_Get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfTask = gson.fromJson(response.body(), taskType);
        assertNotNull(listOfTask);
        assertEquals(0, listOfTask.size());
    }

    @Test
    public void should_Added_Two_Task_When_Client_Send_Request_Post() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        List<Task> listOfTask = get_List_Of_Task_Request(client);
        assertNotNull(listOfTask);
        assertEquals(2, listOfTask.size());
    }

    @Test
    public void should_Get_Task_When_Request_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response3.body(), Task.class);
        assertEquals(1, task.getId());
        assertEquals(200, response3.statusCode());
    }

    @Test
    public void should_Not_Get_Task_When_Request_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        URI url = URI.create("http://localhost:8080/tasks/task?id=11");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response3.body(), Task.class);
        assertNull(task);
        assertEquals(404, response3.statusCode());
    }

    @Test
    public void should_Deleted_All_Tasks_When_Request_Method_Delete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        URI url = URI.create("http://localhost:8080/tasks/task");
        List<Task> list = get_List_Of_Task_Request(client);
        assertEquals(2, list.size());

        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Task> list2 = get_List_Of_Task_Request(client);
        assertEquals(0, list2.size());
    }

    @Test
    public void should_Deleted_Tasks_When_Request_Method_Delete_And_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        List<Task> list = get_List_Of_Task_Request(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/task?id=0");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Task> list2 = get_List_Of_Task_Request(client);
        assertEquals(1, list2.size());
    }

    @Test
    public void should_Deleted_Tasks_When_Request_Method_Delete_And_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Task(client);

        List<Task> list = get_List_Of_Task_Request(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/task?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<Task> list2 = get_List_Of_Task_Request(client);
        assertEquals(2, list2.size());
    }

    @Test
    public void should_Get_Empty_List_Of_Epic_When_Client_Send_Request_Get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Epic>>() {

        }.getType();
        List<Epic> listOfEpic = gson.fromJson(response.body(), taskType);
        assertNotNull(listOfEpic);
        assertEquals(0, listOfEpic.size());
    }

    @Test
    public void should_Added_Two_Epic_When_Client_Send_Request_Post() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<Epic> listOfEpic = get_List_Of_Epic_Request(client);
        assertNotNull(listOfEpic);
        assertEquals(2, listOfEpic.size());
    }

    @Test
    public void should_Get_Epic_When_Request_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response3.body(), Epic.class);
        assertEquals(1, epic.getId());
        assertEquals(200, response3.statusCode());
    }

    @Test
    public void should_Not_Get_Epic_When_Request_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic?id=11");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response3.body(), Epic.class);
        assertNull(epic);
        assertEquals(404, response3.statusCode());
    }

    @Test
    public void should_Deleted_All_Epics_When_Request_Method_Delete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic");
        List<Epic> list = get_List_Of_Epic_Request(client);
        assertEquals(2, list.size());

        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Epic> list2 = get_List_Of_Epic_Request(client);
        assertEquals(0, list2.size());
        List<SubTask> list3 = get_List_Of_SubTask_Request(client);
        assertTrue(list3.isEmpty());
    }

    @Test
    public void should_Deleted_Epics_When_Request_Method_Delete_And_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<Epic> list = get_List_Of_Epic_Request(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=0");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Epic> list2 = get_List_Of_Epic_Request(client);
        assertEquals(1, list2.size());
        List<SubTask> list3 = get_List_Of_SubTask_Request(client);
        assertTrue(list3.isEmpty());
    }

    @Test
    public void should_Deleted_Epics_When_Request_Method_Delete_And_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<Epic> list = get_List_Of_Epic_Request(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<Epic> list2 = get_List_Of_Epic_Request(client);
        assertEquals(2, list2.size());
        List<SubTask> list3 = get_List_Of_SubTask_Request(client);
        assertEquals(2, list3.size());
    }

    @Test
    public void should_Get_Empty_List_Of_SubTask_When_Client_Send_Request_Get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<List<SubTask>>() {

        }.getType();
        List<SubTask> listOfSubTask = gson.fromJson(response.body(), subTaskType);
        assertNotNull(listOfSubTask);
        assertTrue(listOfSubTask.isEmpty());
    }

    @Test
    public void should_Added_Two_SubTask_When_Client_Send_Request_Post() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<SubTask> listOfSubTask = get_List_Of_SubTask_Request(client);
        assertNotNull(listOfSubTask);
        assertEquals(2, listOfSubTask.size());
    }

    @Test
    public void should_Get_SubTask_When_Request_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(2, subTask.getId());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void should_Not_Get_SubTask_When_Request_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertNull(subTask);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void should_Deleted_All_SubTasks_When_Request_Method_Delete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<SubTask> list = get_List_Of_SubTask_Request(client);
        assertEquals(2, list.size());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response.statusCode());

        List<SubTask> list2 = get_List_Of_SubTask_Request(client);
        assertEquals(0, list2.size());

        List<Epic> listOfEpic = get_List_Of_Epic_Request(client);
        assertTrue(listOfEpic.get(0).getSubTaskIds().isEmpty());
    }

    @Test
    public void should_Deleted_SubTasks_When_Request_Method_Delete_And_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<SubTask> list = get_List_Of_SubTask_Request(client);
        assertEquals(2, list.size());

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response.statusCode());

        List<SubTask> list2 = get_List_Of_SubTask_Request(client);
        assertEquals(1, list2.size());

        List<Epic> listOfEpic = get_List_Of_Epic_Request(client);
        assertEquals(1, listOfEpic.get(0).getSubTaskIds().size());
    }

    @Test
    public void should_Deleted_SubTasks_When_Request_Method_Delete_And_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        List<Epic> list = get_List_Of_Epic_Request(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<SubTask> list2 = get_List_Of_SubTask_Request(client);
        assertEquals(2, list2.size());

        List<Epic> listOfEpic = get_List_Of_Epic_Request(client);
        assertEquals(2, listOfEpic.get(0).getSubTaskIds().size());
    }

    @Test
    public void should_Get_List_Of_SubTasks_Of_Epic_When_Request_Method_Contain_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeListOfSubTask = new TypeToken<List<SubTask>>() {

        }.getType();
        List<SubTask> listOfSubTask = gson.fromJson(response.body(), typeListOfSubTask);

        assertEquals(2, listOfSubTask.size());
    }

    @Test
    public void should_Get_List_Of_SubTasks_Of_Epic_When_Request_Method_Contain_Wrong_Id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=11");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeListOfSubTask = new TypeToken<List<SubTask>>() {

        }.getType();
        List<SubTask> listOfSubTask = gson.fromJson(response.body(), typeListOfSubTask);

        assertTrue(listOfSubTask.isEmpty());
    }

    @Test
    public void should_Get_Empty_History_When_Request_Method_Get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);
        add_Two_Task(client);

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeHistoryTask = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfSubTask = gson.fromJson(response.body(), typeHistoryTask);

        assertTrue(listOfSubTask.isEmpty());
    }

    @Test
    public void should_Get_History_When_Request_Method_Get_And_Get_One_Epic_One_Task_One_SubTask_And_After_Delete_One_Task() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        add_Two_Epic_And_Two_SubTask(client);
        add_Two_Task(client);

        URI url = URI.create("http://localhost:8080/tasks/epic?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/task?id=4");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url4 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).GET().build();
        HttpResponse<String> response = client.send(request4, HttpResponse.BodyHandlers.ofString());
        Type typeHistoryTask = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfSubTask = gson.fromJson(response.body(), typeHistoryTask);

        assertEquals(3, listOfSubTask.size());

        URI url5 = URI.create("http://localhost:8080/tasks/task?id=4");
        HttpRequest request5 = HttpRequest.newBuilder().uri(url5).DELETE().build();
        client.send(request5, HttpResponse.BodyHandlers.ofString());

        URI url6 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request6 = HttpRequest.newBuilder().uri(url6).GET().build();
        HttpResponse<String> response2 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        Type typeHistoryTask2 = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfSubTask2 = gson.fromJson(response2.body(), typeHistoryTask2);
        assertEquals(2, listOfSubTask2.size());
    }

    public void add_Two_Task(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(new Task("Task 1", "description Task", Status.NEW,
            "PT3H35M", "2022-02-01T15:50"));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(new Task("Task 2", "description Task2", Status.DONE,
            "PT1H35M", "2022-02-07T15:50"));
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
    }

    public void add_Two_Epic_And_Two_SubTask(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(new Epic("Epic 1", "description Epic", Status.NEW));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(new Epic("Epic 2", "description Epic", Status.NEW));
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        String json3 = gson.toJson(new SubTask("SubTask 1", "description Subtask", Status.NEW,
                "PT3H35M", "2022-01-03T15:50", 0));
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        String json4 = gson.toJson(new SubTask("SubTask 2", "description Subtask", Status.IN_PROGRESS,
                "PT1H35M", "2022-01-02T15:50", 0));
        final HttpRequest.BodyPublisher body4 = HttpRequest.BodyPublishers.ofString(json4);
        HttpRequest request4 = HttpRequest.newBuilder().uri(url2).POST(body4).build();
        client.send(request4, HttpResponse.BodyHandlers.ofString());
    }

    public List<Task> get_List_Of_Task_Request(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {

        }.getType();
        return gson.fromJson(response.body(), taskType);
    }

    public List<Epic> get_List_Of_Epic_Request(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Epic>>() {

        }.getType();
        return gson.fromJson(response.body(), taskType);
    }

    public List<SubTask> get_List_Of_SubTask_Request(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskType = new TypeToken<List<SubTask>>() {

        }.getType();
        return gson.fromJson(response.body(), subTaskType);
    }
}