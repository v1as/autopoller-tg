package ru.v1as.tg.autopoller.messages;

import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class TgMessageProcessor {

    private final List<MessageHandler> handlers;


    @PostConstruct
    public void init() {
        handlers.sort(Comparator.comparingInt(MessageHandler::order));
        handlers.forEach(
            h -> log.info("Message handler registered '{}' with order {}",
                h.getClass().getSimpleName(), h.order()));
    }

    public void process(Message message, Chat chat, User user) {
        for (MessageHandler handler : handlers) {
            handler.handle(message, chat, user);
        }
    }
}
