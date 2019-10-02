package ru.v1as.tg.autopoller.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class TurnOnCommand implements CommandHandler {

    private final AutoPollBotData data;
    private final UnsafeAbsSender sender;

    @Override
    public String getCommandName() {
        return "on";
    }

    @Override
    public void handle(TgCommandRequest command, Chat chat, User user) {
        String message =
                data.getChatData(chat.getId()).turnOnForUser(user)
                        ? "Теперь для этого юзера опросы будут создаваться"
                        : "Опросы и так создавались для этого юзера";
        sender.executeUnsafe(
                new SendMessage(chat.getId(), message)
                        .setReplyToMessageId(command.getMessage().getMessageId()));
    }

}
