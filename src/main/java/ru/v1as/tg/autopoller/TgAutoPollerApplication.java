package ru.v1as.tg.autopoller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@ComponentScan
@EnableScheduling
@SpringBootConfiguration
public class TgAutoPollerApplication implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired private AutoPoller autoPoller;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TgAutoPollerApplication.class, args);
    }

    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationStartedEvent event) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(autoPoller);
    }

}
