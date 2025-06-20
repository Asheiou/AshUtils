package cymru.asheiou.ashutils.sender;

import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebhookSender {
  public static HttpResponse<String> postWebhook(URI uri, String content) {
    HttpRequest.Builder request_builder = HttpRequest.newBuilder(uri);
    JsonObject json = new JsonObject();
    json.addProperty("content", content);

    request_builder.POST(HttpRequest.BodyPublishers.ofString(json.toString()));
    request_builder.headers("content-type", "application/json");
    HttpRequest request = request_builder.build();
    return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
  }
}
