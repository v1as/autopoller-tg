package ru.v1as.tg.autopoller.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Setter
public class AutoPollChatData extends ChatData {

    private Message lastMessage;
    private Map<Integer, AutoPoll> msgIdToPoll = new HashMap<>();

    public AutoPollChatData(Chat chat) {
        super(chat, chat.isUserChat());
    }

    public AutoPoll getPoll(Integer replyToMsgId) {
        return msgIdToPoll.get(replyToMsgId);
    }

    public void registerPoll(AutoPoll poll, Message response) {
        poll.setVoteMessage(response);
        msgIdToPoll.put(response.getMessageId(), poll);
    }
}
