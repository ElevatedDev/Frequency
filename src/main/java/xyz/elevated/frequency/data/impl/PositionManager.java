package xyz.elevated.frequency.data.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;

@RequiredArgsConstructor
public final class PositionManager {
    private final PlayerData playerData;

    private double lastPosX, lastPosY, lastPosZ;

    public void handle(final double posX, final double posY, final double posZ) {
        final World world = playerData.getBukkitPlayer().getWorld();

        final Location location = new Location(world, posX, posY, posZ);
        final Location lastLocation = new Location(world, lastPosX, lastPosY, lastPosZ);

        final PositionUpdate positionUpdate = new PositionUpdate(location, lastLocation);

        playerData.getCheckManager().getChecks().stream().filter(PositionCheck.class::isInstance).forEach(check -> check.process(positionUpdate));

        this.lastPosX = posX;
        this.lastPosY = posY;
        this.lastPosZ = posZ;
    }
}
