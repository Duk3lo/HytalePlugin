package com.astral.server.redis;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProxyHttpClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl;

    public ProxyHttpClient(String baseUrl) { this.baseUrl = baseUrl; }

    public String getServersJson() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/metrics/servers"))
                .GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return resp.body();
    }
}