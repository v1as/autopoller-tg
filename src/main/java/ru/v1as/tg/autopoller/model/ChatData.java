package ru.v1as.tg.autopoller.model;

import lombok.Data;
import lombok.extern.java.Log;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Data
@Log
public class ChatData {

    protected final Long chatId;
    protected final boolean isPrivate;
    protected String name;

    public ChatData(Chat chat, boolean isPrivate) {
        this.chatId = chat.getId();
        this.isPrivate = isPrivate;
        log.info("Chat data created: " + this);
    }

    public void update(Chat chat) {
        this.name = chat.getTitle();
    }

    @Override
    public String toString() {
        return "ChatData{"
                + "chatId="
                + chatId
                + ", isPrivate="
                + isPrivate
                + ", name='"
                + name
                + '\''
                + '}';
    }
}
