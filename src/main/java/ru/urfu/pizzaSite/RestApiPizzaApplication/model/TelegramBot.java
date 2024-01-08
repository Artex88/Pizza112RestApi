package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientInfoService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram_bot_name}")
    private String botName;

    private final ClientInfoService clientInfoService;

    private static final String HELP = "/help";

    private static final String CONNECT = "/connect";

    private static final String STATUS = "/status";

    private static final String DISCONNECT = "/disconnect";

    private static final String INFO = "/info";

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
        if (message.startsWith("/start")) {
            String[] commandParts = message.split(" ");
            if (commandParts.length > 1) {
                String uniqueIdentifier = commandParts[1];
                Optional<ClientInfo> clientInfoOptional = clientInfoService.findByTgToken(uniqueIdentifier);
                if (clientInfoOptional.isPresent()) {
                    ClientInfo clientInfo = clientInfoOptional.get();
                    clientInfo.setChatId(chatId);
                    clientInfoService.save(clientInfo);
                } else
                    unknownCommand(chatId);
            } else
                unknownID(chatId);
        } else if (message.equals(CONNECT)) {
            setNotificationStatus(chatId, true, "Уведомления подключены");
        } else if (message.equals(DISCONNECT)) {
            setNotificationStatus(chatId, false, "Уведомления отключены");
        }   else if (message.equals(HELP))
                helpCommand(chatId);
            else if (message.equals(STATUS))
                statusCommand(chatId);
            else if (message.equals(INFO))
              infoCommand(chatId);
            else
                unknownCommand(chatId);
        try {
            execute(hermitageInlineKeyboardAb(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage hermitageInlineKeyboardAb (Long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Меню \uD83D\uDC47");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> rowsInline = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.addAll(Arrays.asList(
                new KeyboardButton("/status"),
                new KeyboardButton("/connect"),
                new KeyboardButton("/disconnect"),
                new KeyboardButton("/info"),
                new KeyboardButton("/help")
        ));

        rowsInline.add(row);

        replyKeyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }

    private void infoCommand(Long chatId) {
        String text= """
                Добро пожаловать в бот пиццерии Pizza112
                
                Здесь вы можете подключить уведомления о заказах и т.п. в нашей пиццерии.
                Чтобы подключить уведомления, вам необходимо ПЕРВЫЙ РАЗ перейти СТРОГО по ссылке в нашем личном кабинет, для связи аккаунта пиццерии с ТГ.
                Иначе функциональность нашего бота будет вам недоступна.
                
                Для этого воспользуйтесь командами:
                
                /connect - подключение уведомлений
                /disconnect - отключение уведомлений
                
                Дополнительные команды:
                /help - получение справки
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!\nСписок корректных команд доступен ниже";
        sendMessage(chatId, text);
    }
    private void unknownID(Long chatId) {
        var text = "Идентификатор пользователя отсутствует, либо неправильный, либо вы уже привязали свой аккаунт.\n" +
                "Для проверки введите команду /status.\nДля полного списка команд /help.\n Для информации о боте /info.";

        sendMessage(chatId, text);
    }

    private void statusCommand(Long chatId){
        Optional<ClientInfo> clientInfoOptional = clientInfoService.findByChatId(chatId);
        if (clientInfoOptional.isPresent()) {
            ClientInfo clientInfo = clientInfoOptional.get();
            boolean notifications = clientInfo.isNotificationsOn();
            if (notifications)
                sendMessage(chatId, "Аккаунт подключен, уведомления подключены.\nЕсли хотите отключить уведомления, напишите команду /disconnect");
            else
                sendMessage(chatId, "Аккаунт подключен, уведомления отключены.\nЕсли хотите подключить уведомления, напишите команду /connect");
        }
        else
            sendMessage(chatId, "Аккаунт не привязан.\nЕсли вы хотите привязать аккаунт, вам необходимо зайти на сайт пиццерии Pizza112 и в личном кабинете пользователь нажать на кнопку поключения tg бота");
    }

    private void setNotificationStatus(Long chatId, boolean notificationsOn, String successMessage){
        Optional<ClientInfo> clientInfoOptional = clientInfoService.findByChatId(chatId);
        if (clientInfoOptional.isPresent()){
            ClientInfo clientInfo = clientInfoOptional.get();
            boolean notifications = clientInfo.isNotificationsOn();
            if (notifications == notificationsOn)
                sendMessage(chatId, "Вы уже подключали/отключали уведомления. Проверьте текущее состояние командой /status");
            else {
                clientInfo.setNotificationsOn(notificationsOn);
                clientInfoService.save(clientInfo);
                sendMessage(chatId, successMessage);
            }
        } else {
            unknownID(chatId);
        }
    }

    private void helpCommand(Long chatId) {
        var text = """
                Справочная информация по боту
                          
                /connect - подключить уведомления.
                /disconnect - отключить уведомления.
                /status - статус аккаунта.
                /info - информация о боте.
                """;
        sendMessage(chatId, text);
    }

    public void sendNotify(Long chatId, String text){
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
