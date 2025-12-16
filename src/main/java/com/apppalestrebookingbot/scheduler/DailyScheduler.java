package com.apppalestrebookingbot.scheduler;

import com.apppalestrebookingbot.bot.GymBook;
import com.apppalestrebookingbot.model.booking.BookingResponse;
import com.apppalestrebookingbot.model.login.Gym;
import com.apppalestrebookingbot.model.login.LoginResponse;
import com.apppalestrebookingbot.utils.UtilityClass;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;
import java.time.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.apppalestrebookingbot.bot.GymBook.sendMessage;

public class DailyScheduler {

  private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public static void startDailyTask(String bookingTime) {
    Runnable task = () -> {
      try {
        HttpClient client = HttpClient.newHttpClient();
        LocalDate today = LocalDate.now();
        String formattedDate = today.plusWeeks(1).toString();

        sendPost(bookingTime, client, formattedDate);
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    long initialDelay = computeInitialDelay(Integer.parseInt(bookingTime.split(":")[0]), Integer.parseInt(bookingTime.split(":")[1]));
    long period = TimeUnit.DAYS.toMillis(1);

    scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    //scheduler.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
  }

  private static long computeInitialDelay(int targetHour, int targetMinute) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime nextRun = now.withHour(targetHour)
      .withMinute(targetMinute)
      .withSecond(0)
      .withNano(0);

    if (now.compareTo(nextRun) >= 0) {
      nextRun = nextRun.plusDays(1);
    }

    return Duration.between(now, nextRun).toMillis();
  }

  private static void sendPost(String bookingTime, HttpClient client, String formattedDate) throws Exception {
    ScheduledExecutorService retryScheduler = Executors.newSingleThreadScheduledExecutor();

    LoginResponse loginResponse = GymBook.login(client);

    final String uptownBarbellId = loginResponse
      .getParametri()
      .getSedi_collegate()
      .stream()
      .filter(sc -> sc.getNome().contains("Dora"))
      .collect(Collectors.toList())
      .get(0)
      .getId_sede();

     final String timelineId = GymBook
       .getGymTimelineId(formattedDate,
         bookingTime.split(":")[0],
      uptownBarbellId,
      loginResponse.getParametri().getSessione().getCodice_sessione(),
         client
      );

    Runnable retryTask = new Runnable() {
      @Override
      public void run() {
        try {
          String bookState = bookTraining(client,
            loginResponse.getParametri().getSessione().getCodice_sessione(),
            uptownBarbellId,
            timelineId,
            formattedDate);

          if ("2".equals(bookState)) { // prenotazione riuscita
            GymBook.sendMessage(client, "OK - Prenotazione riuscita per la lezione del " + formattedDate);
            retryScheduler.shutdown(); // stop del retry loop
          }
          else{
            GymBook.sendMessage(client, "RITENTO - Prenotazione per la lezione del " + formattedDate);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    retryScheduler.scheduleAtFixedRate(retryTask, 0, 10, TimeUnit.SECONDS);

    retryScheduler.schedule(retryScheduler::shutdown, 1, TimeUnit.MINUTES);

  }


  private static String bookTraining(HttpClient client, String sessionId, String gymId, String timelineId, String day) throws IOException, InterruptedException {
    final String url = UtilityClass.getEnv("APP_PALESTRE_URI") + "prenotazione_new";
    String body = " id_sede="+gymId+"&codice_sessione="+sessionId+"&id_orario_palinsesto="+timelineId+"&data="+day;
    System.out.println(">>>body>>>" + body);
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .POST(HttpRequest.BodyPublishers.ofString(body))
      .build();

    try{
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      ObjectMapper objectMapper = new ObjectMapper();

      final BookingResponse bookingResponse = objectMapper.readValue(response.body(), BookingResponse.class);

      return bookingResponse.getStatus();

    } catch (Exception e){
      System.out.println("Exception: " + e);
      return "1";
    }


  }
}
