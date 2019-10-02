package ru.v1as.tg.autopoller.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Setter
public class AutoPollChatData extends ChatData {

    private Message lastMessage;
    private Map<Integer, AutoPoll> msgIdToPoll = new HashMap<>();
    private Set<Integer> disabledUserIds = new HashSet<>();

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

    public boolean turnOffForUser(User user) {
        return disabledUserIds.add(user.getId());
    }

    public boolean turnOnForUser(User user) {
        return disabledUserIds.remove(user.getId());
    }

    public boolean isUserDisabled(User user) {
        return disabledUserIds.contains(user.getId());
    }

}
