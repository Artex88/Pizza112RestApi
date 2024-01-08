package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram_bot_name}")
    private String botName;

    public TelegramBot(@Value("${telegram_bot_token}") String token){
        super(token);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }






    @Override
    public void onRegister() {
        super.onRegister();
    }
}
