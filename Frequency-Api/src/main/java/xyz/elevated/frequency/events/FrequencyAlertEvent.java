package xyz.elevated.frequency.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FrequencyAlertEvent extends Event {

    public FrequencyAlertEvent(Player player, String checkName, int violations, int checkThreshold) {
        if (player == null)
            throw new NullPointerException(getClass().getName() + " can't be instantiated with a null player!");
        if (checkName == null)
            throw new NullPointerException(getClass().getName() + " can't be instantiated with a null check name!");
        this.player = player;
        this.checkName = checkName;
        this.violations = violations;
        this.checkThreshold = checkThreshold;
    }

    private final Player player; // the player who triggered the alert.
    private final String checkName; // the name of the check.
    private final int violations; // the number of violations for this check.
    private final int checkThreshold; // the threshold of this check.

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public String getCheckName() {
        return checkName;
    }

    @NotNull
    public int getViolations() {
        return violations;
    }

    @NotNull
    public int getCheckThreshold() {
        return checkThreshold;
    }
}
