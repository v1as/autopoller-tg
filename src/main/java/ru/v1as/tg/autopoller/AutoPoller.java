package ru.v1as.tg.autopoller;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.callbacks.TgCallbackProcessor;
import ru.v1as.tg.autopoller.commands.TgCommandProcessor;
import ru.v1as.tg.autopoller.commands.TgCommandRequest;
import ru.v1as.tg.autopoller.messages.TgMessageProcessor;

@Getter
@Component
class AutoPoller extends AbstractTgBot {

    private final AutoPollBotData data;
    private final TgCallbackProcessor callbackProcessor;
    private final TgCommandProcessor commandProcessor;
    private final TgMessageProcessor messageProcessor;

    public AutoPoller(
            @Lazy AutoPollBotData data,
            @Lazy TgCallbackProcessor callbackProcessor,
            @Lazy TgCommandProcessor commandProcessor,
            @Lazy TgMessageProcessor messageProcessor) {
        this.data = data;
        this.callbackProcessor = callbackProcessor;
        this.commandProcessor = commandProcessor;
        this.messageProcessor = messageProcessor;
    }

    @Override
    protected void onUpdateCommand(TgCommandRequest command, Chat chat, User user) {
        this.commandProcessor.process(command, chat, user);
    }

    @Override
    protected void before(Update update) {
        data.register(update);
    }

    @Override
    @SneakyThrows
    protected void onUpdateCallbackQuery(CallbackQuery callbackQuery, Chat chat, User user) {
        callbackProcessor.process(callbackQuery, chat, user);
    }

    @Override
    protected void onUpdateMessage(Message message, Chat chat, User user) {
        messageProcessor.process(message, chat, user);
    }

}
