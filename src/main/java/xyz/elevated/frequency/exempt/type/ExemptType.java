package xyz.elevated.frequency.exempt.type;

import lombok.Getter;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.timings.Timings;

import java.util.function.Function;

@Getter
public enum ExemptType {
    /**
     * Returns true if the tps of the server is too low to ensure check stability
     */
    TPS(playerData -> Timings.TPS.getNumber() < 18),

    /**
     * Returns true if the player is inside or close to the void
     */
    VOID(playerData -> playerData.getBukkitPlayer().getLocation().getY() < 4),

    /**
     * Returns true if the player is lagging
     */
    LAGGING(playerData -> playerData.getActionManager().getDelayed().get()),

    /**
     * Return if a player sent a teleport packet in the last 120ms.
     */
    TELEPORTING(playerData -> playerData.getActionManager().getTeleported().get() || System.currentTimeMillis() - playerData.getJoined().get() < 5000L),

    /**
     * Returns true if the player has had any velocity changes in the past 9000ms
     */
    VELOCITY(playerData -> playerData.getVelocityManager().getMaxVertical() > 0.0 || playerData.getVelocityManager().getMaxHorizontal() > 0.0);

    private final Function<PlayerData, Boolean> exception;

    ExemptType(final Function<PlayerData, Boolean> exception) {
        this.exception = exception;
    }
}
