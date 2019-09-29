package ru.v1as.tg.autopoller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class AbstractTelegramTest {

    protected Integer lastMsgId = 0;
    protected Integer lastCallbackQueryId = 0;

    private int userId = 0;

    protected void switchToFirstUser() {
        userId = 0;
    }

    protected void switchToSecondUser() {
        userId = 1;
    }

    protected void switchToThirdUser() {
        userId = 2;
    }

    protected void switchToFourthUser() {
        userId = 3;
    }

    protected Update getMessageUpdate() {
        Message message = getMessage(++lastMsgId);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        return update;
    }

    protected Message getMessage(Integer newId) {
        Message message = mock(Message.class);
        when(message.getMessageId()).thenReturn(newId);
        when(message.isUserMessage()).thenReturn(true);
        User user = getUser();
        when(message.getFrom()).thenReturn(user);
        Chat chat = getChat();
        when(message.getChat()).thenReturn(chat);
        return message;
    }

    protected User getUser() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(getUserId());
        String userName = "User" + getUserId();
        when(user.toString()).thenReturn(userName);
        when(user.getUserName()).thenReturn(userName);
        when(user.getFirstName()).thenReturn("User");
        when(user.getLastName()).thenReturn(getUserId().toString());
        return user;
    }

    protected Integer getUserId() {
        return userId;
    }

    protected Chat getChat() {
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn(getChatId());
        when(chat.isSuperGroupChat()).thenReturn(true);
        return chat;
    }

    protected Long getChatId() {
        return 0L;
    }


}
