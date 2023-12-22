package dev.brace;

public final class Utilities {
    public static boolean hasEmoji(String str) {
        return str.chars().anyMatch((c) -> {
            int type = Character.getType((char) c);
            return type == Character.SURROGATE || type == Character.OTHER_SYMBOL;
        });
    }
}
