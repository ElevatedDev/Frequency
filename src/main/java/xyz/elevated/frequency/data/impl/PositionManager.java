package xyz.elevated.frequency.data.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.BoundingBox;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.ExemptManager;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.observable.Observable;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.PlayerUtil;

import java.util.Arrays;

@RequiredArgsConstructor @Getter
public final class PositionManager {
    @Getter(AccessLevel.NONE)
    private final PlayerData playerData;

    @Getter(AccessLevel.NONE)
    private double lastPosX, lastPosY, lastPosZ;

    private final Observable<Boolean> touchingAir = new Observable<>(false);
    private final Observable<Boolean> touchingLiquid = new Observable<>(false);
    private final Observable<Boolean> touchingHalfBlock = new Observable<>(false);
    private final Observable<Boolean> touchingClimbable = new Observable<>(false);
    private final Observable<Boolean> touchingIllegalBlock = new Observable<>(false);
    private final Observable<Object[]> nearbyEntities = new Observable<>(null);

    public synchronized void handle(final World world, final double posX, final double posY, final double posZ, final boolean onGround) {
        final BoundingBox boundingBox = new BoundingBox(posX, posY, posZ, world);

        final Player bukkitPlayer = playerData.getBukkitPlayer();
        final Object[] entities = PlayerUtil.getEntitiesWithinRadius(bukkitPlayer.getLocation(), 3).toArray();

        // Convert the data to bukkit locations and parse them
        final Location location = new Location(world, posX, posY, posZ);
        final Location lastLocation = new Location(world, lastPosX, lastPosY, lastPosZ);

        final PositionUpdate positionUpdate = new PositionUpdate(lastLocation, location, onGround);
        final ExemptManager exemptManager = playerData.getExemptManager();

        playerData.getPositionUpdate().set(positionUpdate);

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

        // Make sure players are not near vehicles
        if (Arrays.stream(entities).anyMatch(entity -> entity instanceof Vehicle)) return;

        // Compensate for nearby entities
        nearbyEntities.set(entities);

        // Parse the bounding boxes properly
        playerData.getBoundingBox().set(boundingBox);
        playerData.getBoundingBoxes().add(boundingBox);

        // Handle collisions
        this.handleCollisions(boundingBox);

        // Parse the position update to the checks
        playerData.getCheckManager().getChecks().stream().filter(PositionCheck.class::isInstance).forEach(check -> check.process(positionUpdate));

        // Pass the data to the last variables.
        this.lastPosX = posX;
        this.lastPosY = posY;
        this.lastPosZ = posZ;
    }

    private synchronized void handleCollisions(final BoundingBox boundingBox) {
        boundingBox.expand(0.5, 0.07, 0.5).move(0.0, -0.55, 0.0);

        final boolean touchingAir = boundingBox.checkBlocks(material -> material == Material.AIR);
        final boolean touchingLiquid = boundingBox.checkBlocks(material -> material == Material.WATER || material == Material.LAVA || material == Material.STATIONARY_WATER || material == Material.STATIONARY_LAVA);
        final boolean touchingHalfBlock = boundingBox.checkBlocks(material -> material.getData() == Stairs.class || material.getData() == Step.class);
        final boolean touchingClimbable = boundingBox.checkBlocks(material -> material == Material.LADDER || material == Material.LAVA);
        final boolean touchingIllegalBlock = boundingBox.checkBlocks(material -> material == Material.WATER_LILY || material == Material.BREWING_STAND);

        this.touchingAir.set(touchingAir && !touchingIllegalBlock);
        this.touchingLiquid.set(touchingLiquid);
        this.touchingHalfBlock.set(touchingHalfBlock);
        this.touchingClimbable.set(touchingClimbable);
        this.touchingIllegalBlock.set(touchingIllegalBlock);
    }
}