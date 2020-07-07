package xyz.elevated.frequency.data.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.ExemptManager;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;

@RequiredArgsConstructor
public final class PositionManager {
    private final PlayerData playerData;

    private double lastPosX, lastPosY, lastPosZ;

    public void handle(final double posX, final double posY, final double posZ) {
        final World world = playerData.getBukkitPlayer().getWorld();
        final Player bukkitPlayer = playerData.getBukkitPlayer();

        // Convert the data to bukkit locations and parse them
        final Location location = new Location(world, posX, posY, posZ);
        final Location lastLocation = new Location(world, lastPosX, lastPosY, lastPosZ);

        final PositionUpdate positionUpdate = new PositionUpdate(lastLocation, location);
        final ExemptManager exemptManager = playerData.getExceptManager();

        // Make sure the player isn't inside the void or getting teleported
        if (exemptManager.isExempt(ExemptType.TELEPORTING, ExemptType.VOID)) {
            return;
        }

        // Make sure the player is actually moving
        if (location.distanceSquared(lastLocation) == 0.0) {
            return;
        }

        // Make sure the player isn't flying and he isn't in a vehicle
        if (bukkitPlayer.isInsideVehicle() || bukkitPlayer.getAllowFlight()) {
            return;
        }

        // Parse the position update to the checks
        playerData.getCheckManager().getChecks().stream().filter(PositionCheck.class::isInstance).forEach(check -> check.process(positionUpdate));

        // Pass the data to the last variables.
        this.lastPosX = posX;
        this.lastPosY = posY;
        this.lastPosZ = posZ;
    }
}