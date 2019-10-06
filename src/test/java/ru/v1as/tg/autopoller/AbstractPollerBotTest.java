package ru.v1as.tg.autopoller;

public class AbstractPollerBotTest extends AbstractTelegramBotTest {

    public AutoPollBotData getBotData() {
        return getBot().getData();
    }

    public AutoPoller getBot() {
        return (AutoPoller) bot;
    }

}
