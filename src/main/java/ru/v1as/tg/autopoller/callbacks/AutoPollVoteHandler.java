package ru.v1as.tg.autopoller.callbacks;

import static ru.v1as.tg.autopoller.tg.KeyboardUtils.getUpdateButtonsMsg;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class AutoPollVoteHandler implements TgCallBackHandler<String> {

    public static final String PREFIX = "rpl";

    private final AutoPollBotData data;
    private final UnsafeAbsSender sender;

    @Override
    public void handle(String value, Chat chat, User user, CallbackQuery callbackQuery) {
        Integer replyToMsgId = callbackQuery.getMessage().getMessageId();
        AutoPoll poll = data.getChatData(chat.getId()).getPoll(replyToMsgId);
        if (poll == null) {
            sender.executeUnsafe(
                    new AnswerCallbackQuery()
                            .setCallbackQueryId(callbackQuery.getId())
                            .setText("⏰"));
            return;
        }
        sender.executeUnsafe(
                new AnswerCallbackQuery().setCallbackQueryId(callbackQuery.getId()).setText(value));
        poll.vote(user.getId(), value);

        sender.executeUnsafe(
                getUpdateButtonsMsg(chat, poll.getVoteMessageId(), poll.getReplyKeyboard()));
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String parse(String value) {
        return value.substring(PREFIX.length());
    }
}
