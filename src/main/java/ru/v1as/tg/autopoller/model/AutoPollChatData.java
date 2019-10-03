package ru.v1as.tg.autopoller.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Setter
public class AutoPollChatData extends ChatData {

    public static final int LAST_MESSAGE_DEQUE_SIZE = 3;
    private Deque<Message> lastMessages = new ArrayDeque<>();
    private Map<Integer, AutoPoll> msgIdToPoll = new HashMap<>();
    private Set<Integer> disabledUserIds = new HashSet<>();

    public AutoPollChatData(Chat chat) {
        super(chat, chat.isUserChat());
    }

    public AutoPoll getPoll(Integer replyToMsgId) {
        return msgIdToPoll.get(replyToMsgId);
    }

    public void registerPoll(AutoPoll poll, Message pollMessage) {
        poll.setVoteMessage(pollMessage);
        msgIdToPoll.put(pollMessage.getMessageId(), poll);
        msgIdToPoll.put(poll.getInitialMessageId(), poll);
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

    public void addLastMessage(Message message) {
        lastMessages.addLast(message);
        if (lastMessages.size() > LAST_MESSAGE_DEQUE_SIZE) {
            lastMessages.removeFirst();
        }
    }

    public Message findLastMessage(Message message, Integer userId) {
        Integer messageId = message.getMessageId();
        Stream<Message> findStream = lastMessages.stream();
        if (null != userId) {
            findStream = findStream
                .filter(m -> userId.equals(m.getFrom().getId()));
        }
        return findStream
            .filter(m -> messageId.equals(m.getMessageId()))
            .findFirst().orElse(null);
    }

}
