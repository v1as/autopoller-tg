package ru.v1as.tg.autopoller.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class WhoCommandHandler implements CommandHandler {

    private final AutoPollBotData data;
    private final UnsafeAbsSender sender;

    @Override
    public String getCommandName() {
        return "who";
    }

    @Override
    public void handle(TgCommandRequest command, Chat chat, User user) {
        Message msg = command.getMessage();
        AutoPollChatData chatData = data.getChatData(chat.getId());
        AutoPoll autoPoll = null;
        if (msg.getReplyToMessage() != null) {
            Integer msgId = msg.getReplyToMessage().getMessageId();
            autoPoll = chatData.getPoll(msgId);
        } else {
            Integer pollMsgId =
                    chatData.getMsgIdToPoll().keySet().stream()
                            .max(Integer::compareTo)
                            .orElse(null);
            if (pollMsgId != null) {
                autoPoll = chatData.getMsgIdToPoll().get(pollMsgId);
            }
        }
        if (autoPoll != null) {
            sender.executeUnsafe(new SendMessage(chat.getId(), autoPoll.getDetails(data)));
        } else {
            sender.executeUnsafe(new SendMessage(chat.getId(), "Нет деталей для опроса."));
        }
    }
}
