package ru.mybot.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mybot.data.HashMapData;
import ru.mybot.service.SendMessageOperationService;

import java.time.LocalDate;

import static ru.mybot.constants.Constants.*;

@Component
public class CoreBot extends TelegramLongPollingBot {

    private final SendMessageOperationService sendMessageOperationService;
    private final HashMapData data;
    private boolean startPlanning;

    @Autowired
    public CoreBot(SendMessageOperationService sendMessageOperationService, HashMapData data) {
        this.sendMessageOperationService = sendMessageOperationService;
        this.data = data;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case START:
                    executeMessage(sendMessageOperationService.createGreetingInformation(update));
                    executeMessage(sendMessageOperationService.createInstructionMethod(update));
                    break;
                case START_PLANNING:
                    startPlanning = true;
                    executeMessage(sendMessageOperationService.createPlanningMessage(update));
                    break;
                case END_PLANNING:
                    startPlanning = false;
                    executeMessage(sendMessageOperationService.createEndPlanningMessage(update));
                    break;
                case SHOW_DEALS:
                    if (startPlanning == false) {
                        executeMessage(sendMessageOperationService.createSimpleMessage(update, data.selectAll(LocalDate.now())));
                    }
                    break;
                default:
                    if (startPlanning == true) {
                        data.save(LocalDate.now(), update.getMessage().getText());
                    }
            }
        }
        if (update.hasCallbackQuery()) {
            String instruction = "Бот для создания списка дел. Что-бы воспользоваться ботом следуйте его инструкциям ";
            String callDate = update.getCallbackQuery().getData();
            switch (callDate.toLowerCase()) {
                case YES:
                    EditMessageText text = sendMessageOperationService.createEditMessage(update, instruction);
                    executeMessage(text);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "mybotinochek_bot";
    }

    @Override
    public String getBotToken() {
        return "5074435407:AAHnnCbTjAQzvDQdMXue79rkPkhSem29ymo";
    }

    private <T extends BotApiMethod> void executeMessage(T sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
