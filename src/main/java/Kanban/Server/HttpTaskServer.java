package Kanban.Server;

import Kanban.Service.FileBackedTasksManager;
import Kanban.Service.Managers;
import Kanban.Task.Epic;
import Kanban.Task.SubTask;
import Kanban.Task.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public final static int PORT = 8080;
    private final Gson gson;
    private final HttpServer server;
    private final FileBackedTasksManager fbtm;

    public HttpTaskServer() throws IOException {
        gson = Managers.getGson();
        this.fbtm = new FileBackedTasksManager(new File("Files" + File.separator + "Tasks.CSV"));
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handle);
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }
    private void handle(HttpExchange exchange) {
        try {
            System.out.println("обработка запроса URI: " + exchange.getRequestURI());
            String path = exchange.getRequestURI().getPath().replaceFirst("/tasks", "");
            switch (path) {
                case "": {
                    handleTasks(exchange);
                    break;
                }
                case "/task": {
                    handleTask(exchange);
                    break;
                }
                case "/epic": {
                    handleEpic(exchange);
                    break;
                }
                case "/subtask": {
                    handleSubTask(exchange);
                    break;
                }
                case "/subtask/epic": {
                    handleSubTaskOfEpic(exchange);
                    break;
                }
                case "/history": {
                    handleHistory(exchange);
                    break;
                }
                default: {
                    sendWrongPath(exchange);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обработке запроса!");
        } finally {
            exchange.close();
        }
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equals(method) && Objects.isNull(exchange.getRequestURI().getQuery())) {
            String text = gson.toJson(fbtm.getPrioritizedTasks());
            sendText(exchange, text);
        } else {
            System.out.println("не корректный запрос");
            exchange.sendResponseHeaders(405, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("".getBytes());
            }
        }
    }

    private void handleTask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery(); // TODO: проверить getQuery &

        switch (method) {
            case "GET": {
                if (Objects.isNull(query)) {
                    String text = gson.toJson(fbtm.getTasks());
                    sendText(exchange, text);
                } else if (Pattern.matches("id=\\d+$", query)) {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        String text = gson.toJson(fbtm.getTask(id));
                        sendText(exchange, text);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "POST": {
                String body = readText(exchange);
                Task newTask = gson.fromJson(body, Task.class); // TODO: exception
                if (Objects.isNull(query)) {
                    fbtm.addTask(newTask);
                    sendAfterPost(exchange);
                } else if (Pattern.matches("id=\\d+$", query)) {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        fbtm.updateTask(newTask, id);
                        sendAfterPost(exchange);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "DELETE": {
                if (Objects.isNull(query)) {
                    fbtm.deleteAllTasks();
                    sendAfterDelete(exchange);
                } else if (Pattern.matches("id=\\d+$", query)) {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        if (fbtm.getMapOfTasks().containsKey(id)) {
                            fbtm.deleteTask(id);
                            sendAfterDelete(exchange);
                        } else {
                            sendWrongId(exchange);
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            default: {
                sendWrongMethod(exchange);
            }
        }
    }

    private void handleEpic(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        switch (method) {
            case "GET": {
                if (Objects.isNull(query)) {
                    String text = gson.toJson(fbtm.getEpics());
                    sendText(exchange, text);
                } else {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        String text = gson.toJson(fbtm.getEpic(id));
                        sendText(exchange, text);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "POST": {
                String body = readText(exchange);
                Epic newEpic = gson.fromJson(body, Epic.class); // TODO: exception
                if (Objects.isNull(query)) {
                    fbtm.addEpic(newEpic);
                    sendAfterPost(exchange);
                } else if (Pattern.matches("id=\\d+$", query)) {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        if (fbtm.getMapOfEpic().containsKey(id)) {
                            fbtm.updateEpic(newEpic, id);
                            sendAfterPost(exchange);
                        } else {
                            sendWrongId(exchange);
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "DELETE": {
                if (Objects.isNull(query)) {
                    fbtm.deleteAllEpics();
                    sendAfterDelete(exchange);
                } else {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        fbtm.deleteEpic(id);
                        sendAfterDelete(exchange);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            default: {
                sendWrongMethod(exchange);
            }
        }
    }

    private void handleSubTask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        switch (method) {
            case "GET": {
                if (Objects.isNull(query)) {
                    String text = gson.toJson(fbtm.getSubTasks());
                    sendText(exchange, text);
                } else {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        String text = gson.toJson(fbtm.getSubTask(id));
                        sendText(exchange, text);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "POST": {
                String body = readText(exchange);
                SubTask newSubTask = gson.fromJson(body, SubTask.class); // TODO: exception
                if (Objects.isNull(query)) {
                    fbtm.addSubTask(newSubTask);
                    sendAfterPost(exchange);
                } else if (Pattern.matches("id=\\d+$", query)) {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        fbtm.updateSubTask(newSubTask, id);
                        sendAfterPost(exchange);
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            case "DELETE": {
                if (Objects.isNull(query)) {
                    fbtm.deleteAllSubTasks();
                    sendAfterDelete(exchange);
                } else {
                    String idString = query.substring(3);
                    try {
                        int id = Integer.parseInt(idString);
                        if (fbtm.getMapOfSubTasks().containsKey(id)) {
                            fbtm.deleteSubTask(id);
                            sendAfterDelete(exchange);
                        } else {
                            sendWrongId(exchange);
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        sendWrongId(exchange);
                    }
                }
                break;
            }

            default: {
                sendWrongMethod(exchange);
            }
        }
    }

    private void handleSubTaskOfEpic(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        if ("GET".equals(method) && Pattern.matches("id=\\d+$", query)) {
            String idString = query.substring(3);
            try {
                int id = Integer.parseInt(idString);
                String text = gson.toJson(fbtm.getSubtaskOfEpic(id));
                sendText(exchange, text);
            } catch (NumberFormatException | NullPointerException e) {
                sendWrongId(exchange);
            }
        } else {
            sendWrongMethod(exchange);
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equals(method) && Objects.isNull(exchange.getRequestURI().getQuery())) {
            String text = gson.toJson(fbtm.getHistoryManager().getHistory());
            sendText(exchange, text);
        } else {
            System.out.println("не корректный запрос");
            exchange.sendResponseHeaders(405, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("".getBytes());
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private void sendAfterDelete(HttpExchange h) throws IOException {
        h.sendResponseHeaders(202, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write("".getBytes());
        }
    }

    private void sendAfterPost(HttpExchange h) throws IOException {
        h.sendResponseHeaders(201, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write("".getBytes());
        }
    }

    private void sendWrongMethod(HttpExchange h) throws IOException {
        System.out.println("не корректный запрос");
        h.sendResponseHeaders(405, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write("".getBytes());
        }
    }

    private void sendWrongPath(HttpExchange h) throws IOException {
        System.out.println("не корректный путь");
        h.sendResponseHeaders(404, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write("".getBytes());
        }
    }

    private void sendWrongId(HttpExchange h) throws IOException {
        System.out.println("не корректный ID");
        h.sendResponseHeaders(404, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write("".getBytes());
        }
    }
}
