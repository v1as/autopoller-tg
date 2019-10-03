package ru.v1as.tg.autopoller.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class PollDetailsMessageHandler implements MessageHandler {

    private final AutoPollBotData data;
    private final UnsafeAbsSender sender;

    @Override
    public void handle(Message message, Chat chat, User user) {
        if (!message.getChat().isUserChat() || message.getForwardDate() == null) {
            return;
        }

        AutoPoll poll = data.findByForwardedMessage(message);
        if (poll != null) {
            sender.executeUnsafe(
                    new SendMessage(chat.getId(), poll.getDetails(data))
                            .setReplyToMessageId(message.getMessageId()));
        }
    }
}
