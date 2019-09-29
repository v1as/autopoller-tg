package ru.v1as.tg.autopoller;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.v1as.tg.autopoller.model.AutoPollChatData;
import ru.v1as.tg.autopoller.model.DbData;

@Component
@Getter
public class AutoPollBotData extends DbData<AutoPollChatData> {

    public AutoPollBotData() {
        super(AutoPollChatData::new);
    }

}
