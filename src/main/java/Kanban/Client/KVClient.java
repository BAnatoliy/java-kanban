package Kanban.Client;

import Kanban.Service.ManagerSaveException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private static final String URL_REGISTER = "/register";
    private static final String URL_SAVE = "/save/";
    private static final String URL_LOAD = "/load/";
    private final String url;
    private final String apiToken;

    public KVClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + URL_REGISTER))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ManagerSaveException("не верный код статуса, введен: " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
           throw new ManagerSaveException(e);
        }
    }

    public String load (String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + URL_LOAD + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new ManagerSaveException(e);
        }
    }

    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + URL_SAVE + key + "?API_TOKEN=" + apiToken))
                    .POST(body)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("не верный код статуса, введен: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new ManagerSaveException(e);
        }
    }
}
