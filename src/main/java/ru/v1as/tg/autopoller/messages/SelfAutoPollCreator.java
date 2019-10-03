package ru.v1as.tg.autopoller.messages;

import static ru.v1as.tg.autopoller.Const.isReaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.model.UserData;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class SelfAutoPollCreator implements MessageHandler {

    private final UnsafeAbsSender sender;
    private final AutoPollBotData data;

    @Override
    public void handle(Message msg, Chat chat, User user) {
        UserData userData = data.getUserData(user);
        AutoPollChatData chatData = data.getChatData(chat.getId());
        if (chatData.isUserDisabled(user) || msg.getReplyToMessage() == null) {
            return;
        }
        String msgTxt = msg.getText();
        Message lastMessage = chatData.findLastMessage(msg.getReplyToMessage(), user.getId());
        if (lastMessage != null && isReaction(msgTxt)) {
            AutoPoll poll = new AutoPoll(chatData, lastMessage, user.getId(), msgTxt);
            SendMessage sendMsg =
                    new SendMessage()
                            .setText(
                                    userData.getUsernameAndFullName()
                                            + ": \n"
                                            + lastMessage.getText())
                            .setChatId(chat.getId())
                            .setReplyMarkup(poll.getReplyKeyboard());
            sender.executeUnsafe(new DeleteMessage(chat.getId(), lastMessage.getMessageId()));
            sender.executeUnsafe(new DeleteMessage(chat.getId(), msg.getMessageId()));
            sender.executeAsyncUnsafe(sendMsg, new RegisterAutoPollCallback(poll, data));
        }
    }
}
