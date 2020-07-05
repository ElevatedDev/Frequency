package xyz.elevated.frequency.exempt;

import lombok.RequiredArgsConstructor;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;

import java.util.Arrays;
import java.util.function.Function;

@RequiredArgsConstructor
public final class ExemptManager {
    private final PlayerData playerData;

    /**
     *
     * @param exceptType - The type of exception you want return.
     * @return - True/False depending on appliance.
     */
    public boolean isExempt(final ExemptType exceptType) {
        return exceptType.getException().apply(playerData);
    }

    /**
     *
     * @param exceptTypes - An array of possible exceptions.
     * @return - True/False depending on if any match the appliance.
     */
    public boolean isExempt(final ExemptType... exceptTypes) {
        return Arrays.stream(exceptTypes).anyMatch(this::isExempt);
    }

    /**
     *
     * @param exception - A custom function-based exception
     * @return - True/False depending on appliance.
     */
    public boolean isExempt(final Function<PlayerData, Boolean> exception) {
        return exception.apply(playerData);
    }
}
