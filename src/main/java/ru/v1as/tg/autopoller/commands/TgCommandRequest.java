package ru.v1as.tg.autopoller.commands;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;
import static org.apache.http.util.TextUtils.isEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@RequiredArgsConstructor
public class TgCommandRequest {

    private static final Pattern PATTERN_WITH_NAME =
            Pattern.compile("/([\\w0-9_]+)[@]?([\\w0-9_]+)?(\\s)?(.*)");

    private final String name;
    private final String botName;
    private final List<String> arguments;
    private Message message;

    public static TgCommandRequest parse(String text) {
        Matcher matcher = PATTERN_WITH_NAME.matcher(text);
        checkArgument(matcher.matches(), "Unsupported command format: " + text);
        String name = matcher.group(1);
        String botName = matcher.group(2);
        String argsStr = matcher.group(4);
        List<String> args = isEmpty(argsStr) ? emptyList() : Arrays.asList(argsStr.split(" "));
        return new TgCommandRequest(name, botName, args);
    }

    public String getFirstArgument() {
        return getArgument(1);
    }

    public String getSecondArgument() {
        return getArgument(2);
    }

    public String getThirdArgument() {
        return getArgument(3);
    }

    public String getArgument(int i) {
        checkArgument(i > 1, "Argument index should be positive");
        return arguments.size() > i - 1 ? arguments.get(i - 1) : null;
    }
}
