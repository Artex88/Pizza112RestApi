package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram_bot_name}")
    private String botName;

    private static final String START = "/start";
    private static final String HELP = "/help";

    private static final String CONNECT = "/connect";

    private static final String DISCONNECT = "/disconnect";

    public TelegramBot(@Value("${telegram_bot_token}") String token){
        super(token);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message){
            case START -> {

            }
        }
    }


    private void sendMessage(Long chatId, String text){
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored){

        }
    }

}
