package ru.v1as.tg.autopoller.messages;

import static java.util.Optional.ofNullable;
import static ru.v1as.tg.autopoller.Const.isReaction;
import static ru.v1as.tg.autopoller.tg.KeyboardUtils.deleteMsg;
import static ru.v1as.tg.autopoller.tg.KeyboardUtils.getUpdateButtonsMsg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollReplyHandler implements MessageHandler {

    private final UnsafeAbsSender sender;
    private final AutoPollBotData data;

    @Override
    public void handle(Message message, Chat chat, User user) {
        AutoPollChatData chatData = data.getChatData(chat.getId());
        if (chatData.isUserDisabled(user)) {
            return;
        }
        Integer replyToMsgId =
                ofNullable(message.getReplyToMessage()).map(Message::getMessageId).orElse(null);
        AutoPoll poll = chatData.getMsgIdToPoll().get(replyToMsgId);
        if (poll != null && isReaction(message.getText())) {
            poll.vote(user.getId(), message.getText());
            sender.executeUnsafe(
                    getUpdateButtonsMsg(chat, poll.getVoteMessageId(), poll.getReplyKeyboard()));
            sender.executeUnsafe(deleteMsg(chat.getId(), message));
        }
    }
}
