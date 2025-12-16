package com.apppalestrebookingbot.bot;

import com.apppalestrebookingbot.model.login.LoginResponse;
import com.apppalestrebookingbot.model.timeline.Timeline;
import com.apppalestrebookingbot.utils.UtilityClass;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

import static com.apppalestrebookingbot.bot.HelloWorldBot.BOT_TOKEN;

public class GymBook {
  public static LoginResponse login(HttpClient client) throws IOException, InterruptedException {
    final String url = UtilityClass.getEnv("APP_PALESTRE_URI") + "/loginApp";

    String body = "versione=36&tipo=web&pass=" + UtilityClass.getEnv("APP_PALESTRE_PSW")  +
      "&mail="+ UtilityClass.getEnv("APP_PALESTRE_MAIL")+"&langauge=it";

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .POST(HttpRequest.BodyPublishers.ofString(body))
      .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(response.body(), LoginResponse.class);

  }

  public static String getGymTimelineId (String day,
                                         String startTime,
                                         String gymId,
                                         String sessionId,
  HttpClient client) throws IOException, InterruptedException {
    try {
      final String url = UtilityClass.getEnv("APP_PALESTRE_URI") + "palinsesti";
      String body = "  id_sede=" + gymId + "&codice_sessione=" + sessionId + "&giorno=" + day + "\n";
      System.out.println(">>>body>>>" + body);
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();

      final LoginResponse loginResponse = objectMapper.readValue(response.body(), LoginResponse.class);
      final Timeline timeline = loginResponse.getParametri().getLista_risultati().get(0);

      return timeline.getGiorni().get(0).getGiornoDetails().stream()
        .filter(tmd ->
              tmd.getOrarioInizio().equals(startTime) &&
                tmd.getNomeCorso().equals("WOD")
          )
        .collect(Collectors.toList()).get(0).getIdOrarioPalinsesto();

    } catch (Exception e) {
      return null;
    }
  }

  public static void sendMessage(HttpClient client, String message) throws IOException, InterruptedException {
    String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

    String json = String.format(
      "{\"chat_id\":\"%s\",\"text\":\"%s\"}",
      UtilityClass.getEnv("BOT_CHAT_ID"),
      message
    );

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();

    try{
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Telegram response: " + response.body());

    } catch (Exception e){
      System.out.println(e);
    }
  }
}
