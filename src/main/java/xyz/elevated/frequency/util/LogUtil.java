package xyz.elevated.frequency.util;

public final class LogUtil {

    // We don't want to initialise a class that has every method declared as static.
    public LogUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

    /**
     *
     * @param log - The string you want to print under Frequency.
     */
    public static void log(final String log) {
        System.out.println("[Frequency]: " + log);
    }
}
