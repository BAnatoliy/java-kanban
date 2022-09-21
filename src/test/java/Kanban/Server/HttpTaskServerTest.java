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
    public void shouldGetPriorityTasksWhenClientSendRequestGet() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

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
    public void shouldGetEmptyListOfTaskWhenClientSendRequestGet() throws IOException, InterruptedException {
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
    public void shouldAddedTwoTaskWhenClientSendRequestPost() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        List<Task> listOfTask = getListOfTaskRequest(client);
        assertNotNull(listOfTask);
        assertEquals(2, listOfTask.size());
    }

    @Test
    public void shouldGetTaskWhenRequestContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response3.body(), Task.class);
        assertEquals(1, task.getId());
        assertEquals(200, response3.statusCode());
    }

    @Test
    public void shouldNotGetTaskWhenRequestContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        URI url = URI.create("http://localhost:8080/tasks/task?id=11");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response3.body(), Task.class);
        assertNull(task);
        assertEquals(404, response3.statusCode());
    }

    @Test
    public void shouldDeletedAllTasksWhenRequestMethodDelete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        URI url = URI.create("http://localhost:8080/tasks/task");
        List<Task> list = getListOfTaskRequest(client);
        assertEquals(2, list.size());

        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Task> list2 = getListOfTaskRequest(client);
        assertEquals(0, list2.size());
    }

    @Test
    public void shouldDeletedTasksWhenRequestMethodDeleteAndContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        List<Task> list = getListOfTaskRequest(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/task?id=0");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Task> list2 = getListOfTaskRequest(client);
        assertEquals(1, list2.size());
    }

    @Test
    public void shouldDeletedTasksWhenRequestMethodDeleteAndContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoTask(client);

        List<Task> list = getListOfTaskRequest(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/task?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<Task> list2 = getListOfTaskRequest(client);
        assertEquals(2, list2.size());
    }

    @Test
    public void shouldGetEmptyListOfEpicWhenClientSendRequestGet() throws IOException, InterruptedException {
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
    public void shouldAddedTwoEpicWhenClientSendRequestPost() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<Epic> listOfEpic = getListOfEpicRequest(client);
        assertNotNull(listOfEpic);
        assertEquals(2, listOfEpic.size());
    }

    @Test
    public void shouldGetEpicWhenRequestContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response3.body(), Epic.class);
        assertEquals(1, epic.getId());
        assertEquals(200, response3.statusCode());
    }

    @Test
    public void shouldNotGetEpicWhenRequestContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic?id=11");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response3.body(), Epic.class);
        assertNull(epic);
        assertEquals(404, response3.statusCode());
    }

    @Test
    public void shouldDeletedAllEpicsWhenRequestMethodDelete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/epic");
        List<Epic> list = getListOfEpicRequest(client);
        assertEquals(2, list.size());

        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Epic> list2 = getListOfEpicRequest(client);
        assertEquals(0, list2.size());
        List<SubTask> list3 = getListOfSubTaskRequest(client);
        assertTrue(list3.isEmpty());
    }

    @Test
    public void shouldDeletedEpicsWhenRequestMethodDeleteAndContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<Epic> list = getListOfEpicRequest(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=0");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response3.statusCode());

        List<Epic> list2 = getListOfEpicRequest(client);
        assertEquals(1, list2.size());
        List<SubTask> list3 = getListOfSubTaskRequest(client);
        assertTrue(list3.isEmpty());
    }

    @Test
    public void shouldDeletedEpicsWhenRequestMethodDeleteAndContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<Epic> list = getListOfEpicRequest(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<Epic> list2 = getListOfEpicRequest(client);
        assertEquals(2, list2.size());
        List<SubTask> list3 = getListOfSubTaskRequest(client);
        assertEquals(2, list3.size());
    }

    @Test
    public void shouldGetEmptyListOfSubTaskWhenClientSendRequestGet() throws IOException, InterruptedException {
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
    public void shouldAddedTwoSubTaskWhenClientSendRequestPost() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<SubTask> listOfSubTask = getListOfSubTaskRequest(client);
        assertNotNull(listOfSubTask);
        assertEquals(2, listOfSubTask.size());
    }

    @Test
    public void shouldGetSubTaskWhenRequestContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(2, subTask.getId());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldNotGetSubTaskWhenRequestContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertNull(subTask);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldDeletedAllSubTasksWhenRequestMethodDelete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<SubTask> list = getListOfSubTaskRequest(client);
        assertEquals(2, list.size());

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response.statusCode());

        List<SubTask> list2 = getListOfSubTaskRequest(client);
        assertEquals(0, list2.size());

        List<Epic> listOfEpic = getListOfEpicRequest(client);
        assertTrue(listOfEpic.get(0).getSubTaskIds().isEmpty());
    }

    @Test
    public void shouldDeletedSubTasksWhenRequestMethodDeleteAndContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<SubTask> list = getListOfSubTaskRequest(client);
        assertEquals(2, list.size());

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(202, response.statusCode());

        List<SubTask> list2 = getListOfSubTaskRequest(client);
        assertEquals(1, list2.size());

        List<Epic> listOfEpic = getListOfEpicRequest(client);
        assertEquals(1, listOfEpic.get(0).getSubTaskIds().size());
    }

    @Test
    public void shouldDeletedSubTasksWhenRequestMethodDeleteAndContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        List<Epic> list = getListOfEpicRequest(client);
        assertEquals(2, list.size());

        URI url2 = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());

        List<SubTask> list2 = getListOfSubTaskRequest(client);
        assertEquals(2, list2.size());

        List<Epic> listOfEpic = getListOfEpicRequest(client);
        assertEquals(2, listOfEpic.get(0).getSubTaskIds().size());
    }

    @Test
    public void shouldGetListOfSubTasksOfEpicWhenRequestMethodContainId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeListOfSubTask = new TypeToken<List<SubTask>>() {

        }.getType();
        List<SubTask> listOfSubTask = gson.fromJson(response.body(), typeListOfSubTask);

        assertEquals(2, listOfSubTask.size());
    }

    @Test
    public void shouldGetListOfSubTasksOfEpicWhenRequestMethodContainWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=11");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeListOfSubTask = new TypeToken<List<SubTask>>() {

        }.getType();
        List<SubTask> listOfSubTask = gson.fromJson(response.body(), typeListOfSubTask);

        assertTrue(listOfSubTask.isEmpty());
    }

    @Test
    public void shouldGetEmptyHistoryWhenRequestMethodGet() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);
        addTwoTask(client);

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type typeHistoryTask = new TypeToken<List<Task>>() {

        }.getType();
        List<Task> listOfSubTask = gson.fromJson(response.body(), typeHistoryTask);

        assertTrue(listOfSubTask.isEmpty());
    }

    @Test
    public void shouldGetHistoryWhenRequestMethodGetAndGetOneEpicOneTaskOneSubTaskAndAfterDeleteOneTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        addTwoEpicAndTwoSubTask(client);
        addTwoTask(client);

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

    public void addTwoTask(HttpClient client) throws IOException, InterruptedException {
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

    public void addTwoEpicAndTwoSubTask(HttpClient client) throws IOException, InterruptedException {
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

    public List<Task> getListOfTaskRequest(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {

        }.getType();
        return gson.fromJson(response.body(), taskType);
    }

    public List<Epic> getListOfEpicRequest(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Epic>>() {

        }.getType();
        return gson.fromJson(response.body(), taskType);
    }

    public List<SubTask> getListOfSubTaskRequest(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskType = new TypeToken<List<SubTask>>() {

        }.getType();
        return gson.fromJson(response.body(), subTaskType);
    }
}