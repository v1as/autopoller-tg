package ru.v1as.tg.autopoller;

import com.google.common.collect.ImmutableSet;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import java.util.Set;
import java.util.stream.Collectors;

public class Const {

    public static final String THOUGHT_BALLOON_EMOJI = "\uD83D\uDCAD";

    private static final Set<String> EMOJIS =
            EmojiManager.getAll().stream().map(Emoji::getUnicode).collect(Collectors.toSet());
    private static final Set<String> ALLOWED_REPLY = ImmutableSet.of("+", "-", "~");

    public static boolean isReaction(String text) {
        return EMOJIS.contains(text) || ALLOWED_REPLY.contains(text) || isEmojiAlpha(text);
    }

    private static boolean isEmojiAlpha(String text) {
        return text.length() < 5 && text.chars().allMatch(c -> c > 1120);
    }
}
