package ru.mybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import static java.lang.StrictMath.toIntExact;
import static java.util.Arrays.asList;
import static ru.mybot.constants.Constants.*;

@Service
public class SendMessageOperationService {
    private final String GREETING_MESSAGE = "Привет! Для того, чтобы запланировать событие жми -> \"Добавить событие\")";
    private final String PLANNING_MESSAGE = " Напиши список дел ->  \"Готово\"";
    private final String END_PLANNING_MESSAGE = "Событие добавлено. Полный список -> \"Мои события\"";
    private final String INSTRUCTIONS = "Хотите узнать о боте?";
    private final ButtonsService buttonsService;

    @Autowired
    public SendMessageOperationService(ButtonsService buttonsService) {
        this.buttonsService = buttonsService;
    }

    public SendMessage createGreetingInformation(Update update) {
        SendMessage message = createSimpleMessage(update, GREETING_MESSAGE);
        ReplyKeyboardMarkup replyKeyboardMarkup =
                buttonsService.setButtons(buttonsService.createButtons(
                        asList(START_PLANNING, END_PLANNING, SHOW_DEALS)));
        message.setReplyMarkup(replyKeyboardMarkup);
        return message;
    }

    public SendMessage createPlanningMessage(Update update) {
        return createSimpleMessage(update, PLANNING_MESSAGE);
    }

    public SendMessage createEndPlanningMessage(Update update) {
        return createSimpleMessage(update, END_PLANNING_MESSAGE);
    }

    public SendMessage createSimpleMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createSimpleMessage(Update update, List<String> messages) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        StringBuilder message = new StringBuilder();
        for (String s : messages) {
            message.append(s).append("\n");
        }
        sendMessage.setText(message.toString());
        return sendMessage;
    }

    public SendMessage createInstructionMethod(Update update) {
        SendMessage sendMessage = createSimpleMessage(update, INSTRUCTIONS);
        InlineKeyboardMarkup inlineKeyboardMarkup =
                buttonsService.setInlineKeyMarkup(buttonsService.createInlineButton(YES));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public EditMessageText createEditMessage(Update update, String instruction) {
        EditMessageText editMessageText = new EditMessageText();
        long mesId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(toIntExact(mesId));
        editMessageText.setText(instruction);
        return editMessageText;
    }

}
