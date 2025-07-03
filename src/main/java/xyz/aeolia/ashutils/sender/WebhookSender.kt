package xyz.aeolia.ashutils.sender

import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class WebhookSender {
  companion object {
    @JvmStatic
    fun postWebhook(uri: URI, content: String) : HttpResponse<String?> {
      val builder = HttpRequest.newBuilder(uri)
      val json = JsonObject()
      json.addProperty("content", content)

      builder.POST(HttpRequest.BodyPublishers.ofString(json.toString()))
      builder.headers("content-type", "application/json")
      val request = builder.build()
      return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).join()
    }
  }
}