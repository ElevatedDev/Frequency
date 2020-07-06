package xyz.elevated.frequency.util;

import org.bukkit.ChatColor;

public final class ColorUtil {

    public ColorUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

    // & to paragraph symbol
    public static String format(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
