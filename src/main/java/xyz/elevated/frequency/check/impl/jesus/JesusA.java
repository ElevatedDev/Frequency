package xyz.elevated.frequency.check.impl.jesus;

import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;

@CheckData(name = "Jesus (A)")
public final class JesusA extends PositionCheck {
    /**
     *
     * TODO: Invalid (B) Operates the same way as a Jesus check would
     * TODO: So I will continue to make this check more Jesus specific that makes sense!
     */
    public JesusA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the client onGround from the client & also get touchingLiquid
        final boolean onGround = positionUpdate.isOnGround();
        final boolean touchingLiquid = playerData.getPositionManager().getTouchingLiquid().get();

        // Get the deltas for each axis
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // If the delta is greater than 0.0 and the player is on ground (impossible)
        if (deltaY > 0.0 && onGround) {
            final double horizontalDistance = Math.hypot(deltaX, deltaZ);

            // If the player is moving too, flag
            if (horizontalDistance > 0.1) {
                fail();
            }
        }
    }
}
