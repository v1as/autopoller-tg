package ru.v1as.tg.autopoller.callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TgCallBackHandler<T> {

    void handle(T value, Chat chat, User user, CallbackQuery callbackQuery);

    String getPrefix();

    T parse(String value);
}
