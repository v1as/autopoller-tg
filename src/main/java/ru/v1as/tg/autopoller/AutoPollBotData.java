package ru.v1as.tg.autopoller;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.model.DbData;

@Component
@Getter
public class AutoPollBotData extends DbData<AutoPollChatData> {

    public AutoPollBotData() {
        super(AutoPollChatData::new);
    }

    public AutoPoll findByForwardedMessage(Message message) {
        if (message.getForwardDate() == null) {
            return null;
        }
        Set<AutoPoll> polls =
                getChats().stream()
                        .flatMap(c -> c.getMsgIdToPoll().values().stream())
                        .filter(p -> forwardedMessageIsMatch(message, p))
                        .collect(Collectors.toSet());
        return polls.size() == 1 ? polls.iterator().next() : null;
    }

    private boolean forwardedMessageIsMatch(Message message, AutoPoll p) {
        Message voteMessage = p.getVoteMessage();
        boolean sameDate = voteMessage.getDate().equals(message.getForwardDate());
        boolean sameText = voteMessage.getText().equals(message.getText());
        return sameDate && sameText;
    }
}
