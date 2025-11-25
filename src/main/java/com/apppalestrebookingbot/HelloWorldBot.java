package com.apppalestrebookingbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class HelloWorldBot extends TelegramLongPollingBot {

    private static final Dotenv dotenv = Dotenv.load();

    @Override
    public String getBotUsername() {
        return dotenv.get("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
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
