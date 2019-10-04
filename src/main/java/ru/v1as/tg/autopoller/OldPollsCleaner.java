package ru.v1as.tg.autopoller;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.v1as.tg.autopoller.model.AutoPoll;
import ru.v1as.tg.autopoller.model.AutoPollChatData;

@Slf4j
@Component
@RequiredArgsConstructor
public class OldPollsCleaner {
    private final AutoPollBotData data;

    @PostConstruct
    public void init() {
        log.info("OldPollsCleaner started.");
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void removeOldPolls() {
        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        for (AutoPollChatData chat : data.getChats()) {
            Map<Integer, AutoPoll> polls = chat.getMsgIdToPoll();
            Iterator<Integer> iterator = polls.keySet().iterator();
            while (iterator.hasNext()) {
                AutoPoll poll = polls.get(iterator.next());
                if (poll.getCreated().isBefore(yesterday)) {
                    iterator.remove();
                    log.info("Poll removed because of age: {}", poll);
                }
            }
        }
    }
}
