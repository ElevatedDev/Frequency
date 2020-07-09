package xyz.elevated.frequency.check.impl.speed;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Speed")
public final class Speed extends PositionCheck {
    private double blockSlipperiness = 0.91;
    private double lastHorizontalDistance = 0.0;

    public Speed(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);

        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        double blockSlipperiness = this.blockSlipperiness;
        double attributeSpeed = entityPlayer.bI();

        final boolean onGround = positionUpdate.isOnGround();
        final boolean exempt = this.isExempt(ExemptType.TPS, ExemptType.TELEPORTING);

        if (onGround) {
            blockSlipperiness *= 0.91f;

            attributeSpeed *= blockSlipperiness > 0.708 ? 1.3 : 0.23315;
            attributeSpeed *= 0.16277136 / Math.pow(blockSlipperiness, 3);

            if (deltaY > 0.4199) {
                final double var1 = to.getYaw() * 0.017453292F;
                final double extraSpeedJumpX = Math.sin(var1) * 0.2F;
                final double extraSpeedJumpZ = Math.cos(var1) * 0.2F;

                final double extraJumpSpeed = Math.sqrt(Math.pow(extraSpeedJumpX, 2) + Math.pow(extraSpeedJumpZ, 2));

                attributeSpeed += extraJumpSpeed;
            } else {
                attributeSpeed -= .1053;
            }
        } else {
            attributeSpeed = 0.026;
            blockSlipperiness = 0.91f;
        }

        attributeSpeed += playerData.getVelocityManager().getMaxVertical();

        final double threshold = playerData.getPositionManager().getUnderBlock().get() ? 3.6 : 1.0;

        final double horizontalDistance = Math.hypot(deltaX, deltaZ);
        final double movementSpeed = (horizontalDistance - lastHorizontalDistance) / attributeSpeed;

        if (movementSpeed > threshold && !exempt) {
            fail();
        }

        this.lastHorizontalDistance = horizontalDistance / blockSlipperiness;
        this.blockSlipperiness = entityPlayer.world.getType(new BlockPosition(MathHelper.floor(to.getX()),
                MathHelper.floor(entityPlayer.getBoundingBox().b) - 1,
                MathHelper.floor(to.getZ())))
                .getBlock()
                .frictionFactor * 0.91F;
    }
}
