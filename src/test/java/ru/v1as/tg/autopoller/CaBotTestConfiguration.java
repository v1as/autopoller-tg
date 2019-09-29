package ru.v1as.tg.autopoller;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import ru.v1as.tg.autopoller.callbacks.TgCallbackProcessor;
import ru.v1as.tg.autopoller.commands.TgCommandProcessor;
import ru.v1as.tg.autopoller.messages.TgMessageProcessor;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

@Configuration
@ComponentScan({"ru.v1as.tg.autopoller.messages", "ru.v1as.tg.autopoller.callbacks", "ru.v1as.tg.autopoller.commands"})
@ActiveProfiles("test")
public class CaBotTestConfiguration {

    @Bean
    @Primary
    public UnsafeAbsSender unsafeAbsSender() {
        return new TestAbsSender();
    }

//    @Bean
//    public AutoPollBotData getDbData(ScoreData scoreData) {
//        return new AutoPollBotData(scoreData);
//    }
//
//    @Bean
//    public ScoreData getScoreData() {
//        return mock(ScoreData.class);
//    }

    @Bean
    public AutoPoller getCatBot(
            AutoPollBotData autoPollBotData,
            TgCallbackProcessor callbackProcessor,
            TgCommandProcessor commandProcessor,
            TgMessageProcessor messageProcessor) {
        return new AutoPoller(autoPollBotData, callbackProcessor, commandProcessor, messageProcessor);
    }

}
