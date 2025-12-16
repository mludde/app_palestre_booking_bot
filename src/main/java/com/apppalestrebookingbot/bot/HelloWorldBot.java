package com.apppalestrebookingbot.bot;

import com.apppalestrebookingbot.scheduler.DailyScheduler;
import com.apppalestrebookingbot.utils.UtilityClass;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class HelloWorldBot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = UtilityClass.getEnv("BOT_TOKEN");
    public static final String BOT_NAME = UtilityClass.getEnv("BOT_USERNAME");
    public static final String BOOKING_TIME =  UtilityClass.getEnv("BOOKING_TIME");
;
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

                //execute(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DailyScheduler.startDailyTask(BOOKING_TIME);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new HelloWorldBot());
        System.out.println("Bot avviato!");
    }
}
