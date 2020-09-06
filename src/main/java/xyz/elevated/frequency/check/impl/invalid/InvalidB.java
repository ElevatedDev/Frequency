package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Invalid (B)")
public final class InvalidB extends PositionCheck {

    public InvalidB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the client onGround from the client
        final boolean onGround = !playerData.getPositionManager().getTouchingAir().get() && positionUpdate.isOnGround();
        final boolean touchingLiquid = playerData.getPositionManager().getTouchingLiquid().get();
        final boolean onHalfBlock = !playerData.getPositionManager().getTouchingHalfBlock().get();
        final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.LAGGING);

        // Get the deltas for each axis
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // If the delta is greater than 0.0 and the player is on ground (impossible)
        if (deltaY > 0.15 && onGround && !touchingLiquid && !onHalfBlock && !exempt) {
            final double horizontalDistance = Math.hypot(deltaX, deltaZ);

            // If the player is moving too, flag
            if (horizontalDistance > 0.1) {
                fail();
            }
        }
    }
}
