package com.apppalestrebookingbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class HelloWorldBot extends TelegramLongPollingBot {

    private static final Dotenv dotenv = Dotenv.configure()
      .ignoreIfMissing() // evita errori se .env non esiste (es. Railway)
      .load();

    public static final String BOT_TOKEN = getEnv("BOT_TOKEN");
    public static final String BOT_NAME = getEnv("BOT_USERNAME");

    private static String getEnv(String key) {
        String value = System.getenv(key);
        return value != null ? value : dotenv.get(key);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                String chatId = update.getMessage().getChatId().toString();

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Hello World!");

                execute(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new HelloWorldBot());
        System.out.println("Bot avviato!");
    }
}
