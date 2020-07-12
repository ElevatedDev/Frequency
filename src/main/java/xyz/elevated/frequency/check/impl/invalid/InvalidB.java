package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Invalid (B)")
public final class InvalidB extends PositionCheck {

    private double buffer = 0.0;

    public InvalidB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the client onGround from the client
        final boolean onGround = positionUpdate.isOnGround();

        // Get the deltas for each axis
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // If the delta is greater than 0.0 and the player is on ground (impossible)
        if (deltaY > 0.0 && onGround) {
            // Small amount of buffer, because there might be small false positives when you're lagging and taking falldamage
            if(++this.buffer > 1) {
                final double horizontalDistance = Math.hypot(deltaX, deltaZ);

                // If the player is moving too, flag
                if (horizontalDistance > 0.1) {
                    fail();
                }
            } else {
                this.buffer = 0;
            }
        }
    }
}
