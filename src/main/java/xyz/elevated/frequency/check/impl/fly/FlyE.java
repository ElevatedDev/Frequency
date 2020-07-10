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
    private Location lastGroundLocation = null;
    private int movements = 0;

    public FlyE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();
        
        final double distanceH = Math.hypot(deltaX, deltaZ);
        final double distanceY = Math.abs(deltaY);

        final boolean moving = distanceH > 0.0 || distanceY > 0.0;
        final boolean exempt = this.isExempt(ExemptType.VELOCITY, ExemptType.TELEPORTING);
        final boolean ground = positionUpdate.isOnGround();

        final int jumpModifier = MathUtil.getPotionLevel(playerData.getBukkitPlayer(), PotionEffectType.JUMP);
        
        if (moving && !exempt && !ground) {
            final double distanceGround = MathUtil.getDistance(to, lastGroundLocation);
            final double threshold = jumpModifier > 0 ? 5.0 + (Math.pow(jumpModifier + 4.2, 2.0) / 16.0) : 5.0;

            if (distanceGround > threshold) {
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
