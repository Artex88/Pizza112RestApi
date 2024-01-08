package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientInfoService;

import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram_bot_name}")
    private String botName;

    private final ClientInfoService clientInfoService;

    private static final String START = "/start";
    private static final String HELP = "/help";

    private static final String CONNECT = "/connect";

    private static final String DISCONNECT = "/disconnect";

    @Autowired
    public TelegramBot(@Value("${telegram_bot_token}") String token, ClientInfoService clientInfoService){
        super(token);
        this.clientInfoService = clientInfoService;
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
                String[] commandParts = message.split(" ");
                if (commandParts.length > 1) {
                    String uniqueIdentifier = commandParts[1];
                    Optional<ClientInfo> clientInfoOptional = clientInfoService.getByTgToken(uniqueIdentifier);
                    if (clientInfoOptional.isPresent()){
                        ClientInfo clientInfo = clientInfoOptional.get();
                        clientInfo.setChatId(chatId);
                        String userName = update.getMessage().getChat().getUserName();
                        startCommand(chatId, userName);
                    }
                    else
                        unknownCommand(chatId);
                }
                else
                    unknownID(chatId);

            }
            default -> unknownCommand(chatId);
        }
    }

    private void startCommand(Long chatId, String userName) {
        String text= """
                Добро пожаловать в бот пиццерии Pizza112, $s
                
                Здесь вы можете подключить уведомления о заказах и т.п. в нашей пиццерии.
                Чтобы подключить уведомления, вам необходимо перейти СТРОГО по ссылке в нашем личном кабинет.
                Иначе функциональность нашего бота будет вам недоступна.
                
                Для этого воспользуйтесь командами:
                
                /connect - подключение уведомлений
                /disconnect - отключение уведомлений
                
                Дополнительные команды:
                /help - получение справки
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }
    private void unknownID(Long chatId) {
        var text = "Идентификатор пользователя отсутствует либо неправильный.";
        sendMessage(chatId, text);
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
