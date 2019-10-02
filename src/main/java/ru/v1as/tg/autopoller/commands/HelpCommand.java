package ru.v1as.tg.autopoller.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Component
@RequiredArgsConstructor
public class HelpCommand implements CommandHandler {

    private final UnsafeAbsSender sender;
    private static final String HELP_MESSAGE =
            "Чтобы я мог создавать опросы, меня нужно сделать администратором с правом удалять сообщения.\n\n"
                    + "Для того чтобы самому создать опрос - отправьте текстовое сообщение и ответьте на него с помощью"
                    + " эмоджи или знаков \"+\" и \"-\". Оба сообщения должны идти подряд друг за другом.\n"
                    + "\n"
                    + "Когда вы отвечаете на чужое текстовое сообщение с помощью эмоджи или знаков \"+\" и \"-\", "
                    + "так же создаётся опрос в виде ответа на изначальное сообщение.\n"
                    + "\n"
                    + "Чтобы дополнить опрос - ответьте на него новым эмоджи.\n\n"
                    + "C помощью комманд /on /off можно включить или выключить создание опросов для"
                    + " юзера, который выполнил эту команду";

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void handle(TgCommandRequest command, Chat chat, User user) {
        sender.executeUnsafe(new SendMessage(chat.getId(), HELP_MESSAGE));
    }
}
