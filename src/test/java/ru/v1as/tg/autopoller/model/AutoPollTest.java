package ru.v1as.tg.autopoller.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.v1as.tg.autopoller.AbstractTelegramTest;

public class AutoPollTest extends AbstractTelegramTest {

    @Test
    public void createTest() {
        AutoPoll autoPoll =
                new AutoPoll(
                        new AutoPollChatData(getChat()), getMessage(++lastMsgId), getUserId(), "+");
        autoPoll.vote(getUserId(), "+");
        InlineKeyboardButton button = assertButton(autoPoll.getReplyKeyboard());
        assertEquals("+", button.getText());
        assertEquals("rpl+", button.getCallbackData());

        autoPoll.vote(getUserId(), "+");
        button = assertButton(autoPoll.getReplyKeyboard());
        assertEquals("+ (1)", button.getText());
        assertEquals("rpl+", button.getCallbackData());
    }

    private InlineKeyboardButton assertButton(InlineKeyboardMarkup keyboard) {
        assertEquals(1, keyboard.getKeyboard().size());
        List<InlineKeyboardButton> line = keyboard.getKeyboard().iterator().next();
        assertEquals(1, line.size());
        return line.iterator().next();
    }
}
