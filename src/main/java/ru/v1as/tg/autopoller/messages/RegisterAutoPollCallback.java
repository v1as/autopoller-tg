package ru.v1as.tg.autopoller.messages;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.tg.LogSentCallback;

@RequiredArgsConstructor
public class RegisterAutoPollCallback extends LogSentCallback<Message> {

    private final AutoPoll poll;
    private final AutoPollBotData data;

    @Override
    public void onResult(BotApiMethod<Message> method, Message response) {
        super.onResult(method, response);
        data.getChatData(response.getChatId()).registerPoll(poll, response);
    }
}
