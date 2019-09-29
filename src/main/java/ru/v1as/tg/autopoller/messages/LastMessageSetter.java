package ru.v1as.tg.autopoller.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPollChatData;

@Slf4j
@Component
@RequiredArgsConstructor
public class LastMessageSetter implements MessageHandler {

    private final AutoPollBotData data;

    @Override
    public void handle(Message message, Chat chat, User user) {
        AutoPollChatData chatData = data.getChatData(chat.getId());
        chatData.setLastMessage(message);
        log.debug("Last message set: {}", message);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
