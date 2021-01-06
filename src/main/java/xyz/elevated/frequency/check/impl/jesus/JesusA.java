package xyz.elevated.frequency.check.impl.jesus;

import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.MathUtil;

@CheckData(name = "Jesus (A)")
public final class JesusA extends PositionCheck {

    public JesusA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the deltas for each axis
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // Get the player's on ground and make sure he is stationary
        final boolean onGround = positionUpdate.isOnGround();
        final boolean touchingLiquid = playerData.getPositionManager().getTouchingLiquid().get();
        final boolean stationary = deltaX % 1.0 == 0.0 && deltaZ % 1.0 == 0.0;

        // If the delta is greater than 0.0 and the player is stationary
        if (deltaY > 0.0 && !onGround && !touchingLiquid && stationary) {
            final double horizontalDistance = MathUtil.magnitude(deltaX, deltaZ);

            // If the player is moving too, flag
            if (horizontalDistance > 0.1) {
                fail();
            }
        }
    }
}
