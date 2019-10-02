package ru.v1as.tg.autopoller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.isEmpty;
import static ru.v1as.tg.autopoller.model.UpdateUtils.getChat;
import static ru.v1as.tg.autopoller.model.UpdateUtils.getUser;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import ru.v1as.tg.autopoller.commands.TgCommandRequest;
import ru.v1as.tg.autopoller.tg.UnsafeAbsSender;

public abstract class AbstractTgBot extends TelegramLongPollingBot implements UnsafeAbsSender {

    private final Logger log = getLogger(this.getClass());

    private AbsSender sender = this;
    private Map<Long, Object> chatToMonitor = new ConcurrentHashMap<>();

    @Value("${tg.bot.username}")
    private String botUsername;

    @Value("${tg.bot.token}")
    private String botToken;

    @PostConstruct
    public void init() {
        log.info(
                "Bean '{}' with username '{}' starting",
                this.getClass().getSimpleName(),
                botUsername);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Chat chat = getChat(update);
            User user = getUser(update);
            if (chat == null || user == null) {
                log.warn("Such type updated does not supported '{}'", update);
                return;
            }
            synchronized (chatToMonitor.computeIfAbsent(chat.getId(), (id) -> chat.getId())) {
                before(update);
                if (update.hasMessage() && update.getMessage().isCommand()) {
                    String text = update.getMessage().getText();
                    log.info("Command '{}' received.", text);
                    TgCommandRequest command = TgCommandRequest.parse(text);
                    command.setMessage(update.getMessage());
                    onUpdateCommand(command, chat, user);
                } else if (update.hasMessage()) {
                    onUpdateMessage(update.getMessage(), chat, user);
                } else if (update.hasCallbackQuery()) {
                    log.info("Callback received '{}'", update.getCallbackQuery().getData());
                    onUpdateCallbackQuery(update.getCallbackQuery(), chat, user);
                } else {
                    log.debug("Unsupported update type: " + update);
                }
            }
        } catch (UndeclaredThrowableException ex) {
            Throwable t = ex.getUndeclaredThrowable();
            if (!isEmpty(t.getMessage())) {
                log.warn(t.getLocalizedMessage(), t);
            }
        } catch (Exception e) {
            log.error("Something gone wrong ", e);
        }
    }

    protected abstract void onUpdateCommand(TgCommandRequest command, Chat chat, User user);

    protected abstract void before(Update update);

    protected abstract void onUpdateCallbackQuery(
            CallbackQuery callbackQuery, Chat chat, User user);

    protected abstract void onUpdateMessage(Message message, Chat chat, User user);

    @SneakyThrows
    @Override
    public <
                    T extends Serializable,
                    Method extends BotApiMethod<T>,
                    Callback extends SentCallback<T>>
            void executeAsyncUnsafe(Method method, Callback callback) {
        log.debug(method.toString());
        sender.executeAsync(method, callback);
    }

    @SneakyThrows
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T executeUnsafe(Method method) {
        log.debug(method.toString());
        return sender.execute(method);
    }

    public void setSender(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
