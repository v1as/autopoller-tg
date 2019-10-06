package ru.v1as.tg.autopoller.messages;

import static java.util.Optional.ofNullable;
import static ru.v1as.tg.autopoller.Const.isReaction;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.Const;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.model.UserData;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtherOneReactionMessageHandler implements MessageHandler {

    private final UnsafeAbsSender sender;
    private final AutoPollBotData data;

    @Override
    public void handle(Message msg, Chat chat, User user) {
        AutoPollChatData chatData = data.getChatData(chat.getId());
        UserData userData = data.getUserData(user);
        Message lastMessage =
                ofNullable(msg.getReplyToMessage())
                        .map(reply -> chatData.findLastMessage(reply, null))
                        .orElse(null);
        if (chatData.isUserDisabled(user) || lastMessage == null) {
            return;
        }
        String firstChoose = msg.getText();
        AutoPoll createdPoll = chatData.getMsgIdToPoll().get(lastMessage.getMessageId());
        Integer lastMessageUserId = lastMessage.getFrom().getId();
        if (createdPoll == null
                && !Objects.equals(lastMessageUserId, user.getId())
                && isReaction(firstChoose)) {
            log.info(
                    "User '{}' created poll by replying with reaction '{}'",
                    userData.getUsernameOrFullName(),
                    firstChoose);
            AutoPoll poll = new AutoPoll(chatData, lastMessage, user.getId(), firstChoose);
            SendMessage sendMsg =
                    new SendMessage()
                            .setText(Const.THOUGHT_BALLOON_EMOJI)
                            .setChatId(chat.getId())
                            .setReplyToMessageId(lastMessage.getMessageId())
                            .setReplyMarkup(poll.getReplyKeyboard());
            sender.executeUnsafe(new DeleteMessage(chat.getId(), msg.getMessageId()));
            sender.executeAsyncUnsafe(sendMsg, new RegisterAutoPollCallback(poll, data));
        }
    }
}
