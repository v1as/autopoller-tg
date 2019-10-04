package ru.v1as.tg.autopoller.model;

import static ru.v1as.tg.autopoller.callbacks.AutoPollVoteHandler.PREFIX;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.tg.autopoller.AutoPollBotData;
import ru.v1as.tg.autopoller.tg.KeyboardUtils;

@Slf4j
public class AutoPoll extends TgRequestPoll<String> {

    private Message initialMessage;
    private Map<Integer, Set<String>> userIdToVotes = new LinkedHashMap<>();
    private Set<String> allValues = new LinkedHashSet<>();

    public AutoPoll(
            AutoPollChatData chatData, Message lastMessage, Integer userId, String firstChoose) {
        super(chatData);
        this.initialMessage = lastMessage;
        vote(userId, firstChoose);
    }

    public void vote(Integer userId, String data) {
        log.info("User {} voted {}", userId, data);
        Set<String> userVotes =
                this.userIdToVotes.computeIfAbsent(userId, (id) -> new LinkedHashSet<>());
        allValues.add(data);
        if (userVotes.contains(data)) {
            userVotes.remove(data);
        } else {
            userVotes.add(data);
        }
    }

    public InlineKeyboardMarkup getReplyKeyboard() {
        Map<String, Integer> result = new LinkedHashMap<>();
        allValues.forEach(v -> result.put(v, 0));
        userIdToVotes.values().stream()
                .flatMap(Collection::stream)
                .forEach(vote -> result.put(vote, result.get(vote) + 1));
        String[] buttonsData =
                result.entrySet().stream()
                        .flatMap(this::getButtonDataStream)
                        .toArray(String[]::new);
        return KeyboardUtils.inlineKeyboardMarkup(buttonsData);
    }

    private Stream<String> getButtonDataStream(Entry<String, Integer> entry) {
        String description =
                entry.getValue() > 0L
                        ? String.format("%s (%s)", entry.getKey(), entry.getValue())
                        : entry.getKey();
        return Stream.of(description, PREFIX + entry.getKey());
    }

    public String getDetails(AutoPollBotData data) {
        StringBuilder result = new StringBuilder();
        this.userIdToVotes.forEach(
                (userId, values) -> {
                    result.append(data.getUserData(userId).getUsernameAndFullName()).append(' ');
                    values.forEach(result::append);
                    result.append('\n');
                });
        return result.toString();
    }

    public Set<String> getAllValues() {
        return allValues;
    }

    Integer getInitialMessageId() {
        return initialMessage.getMessageId();
    }

    @Override
    public String toString() {
        return "AutoPoll{"
                + "initialMessage="
                + initialMessage
                + ", userIdToVotes="
                + userIdToVotes
                + ", allValues="
                + allValues
                + (chat != null ? chat.getChatId() : "")
                + ", created="
                + created
                + ", finished="
                + finished
                + ", canceled="
                + canceled
                + ", voteMessage="
                + voteMessage
                + '}';
    }
}
