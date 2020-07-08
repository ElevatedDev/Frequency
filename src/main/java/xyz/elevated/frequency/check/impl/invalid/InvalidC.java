package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Invalid (C)")
public final class InvalidC extends PositionCheck {
    private double buffer = 0.0;

    public InvalidC(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);

        final double deltaX = to.getX() - from.getZ();
        final double deltaZ = to.getZ() - from.getZ();

        final double horizontalDistance = Math.hypot(deltaX, deltaZ);
        final double horizontalVelocity = playerData.getVelocityManager().getMaxHorizontal();

        final boolean onGround = positionUpdate.isOnGround();

        if (!onGround) {
            final boolean invalid = horizontalDistance > 0.3 && horizontalVelocity == 0.0;

            if (invalid) {
                buffer += 0.5;

                if (buffer > 1.5) {
                    fail();
                }
            } else {
                buffer = Math.max(buffer - 0.025, 0);
            }
        }
    }
}
