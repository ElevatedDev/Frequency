package xyz.elevated.frequency.check.impl.fly;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.MathUtil;

@CheckData(name = "Fly (E)")
public final class FlyE extends PositionCheck {

    private Location lastGroundLocation;
    private int movements = 0;

    public FlyE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the delta of all the position values
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // Get the distance moved on the vertical level and the horizontal level
        final double distanceH = Math.hypot(deltaX, deltaZ);
        final double distanceY = Math.abs(deltaY);

        // Make sure the player is moving isn't exempt and isn't on ground
        final boolean moving = distanceH > 0.0 || distanceY > 0.0;
        final boolean exempt = this.isExempt(ExemptType.VELOCITY, ExemptType.TELEPORTING);
        final boolean ground = positionUpdate.isOnGround() && !playerData.getPositionManager().getTouchingAir().get();

        // Get the jump modifier from the math util
        final int jumpModifier = MathUtil.getPotionLevel(playerData.getBukkitPlayer(), PotionEffectType.JUMP);

        if (moving && !exempt && !ground && deltaY >= 0.0) {
            final double distanceGround = lastGroundLocation != null ? MathUtil.getMagnitude(to, lastGroundLocation) : 0.0;
            final double threshold = jumpModifier > 0 ? 5.0 + (Math.pow(jumpModifier + 4.2, 2.0) / 16.0) : 5.0;

            // Normally I would avoid using the sqrt as its quite heavy on performance.
            if (Math.sqrt(distanceGround) > threshold) {
                if (++movements > 5) {
                    fail();
                }
            } else {
                movements = 0;
            }
        } else {
            movements = 0;
            lastGroundLocation = to;
        }
    }
}
