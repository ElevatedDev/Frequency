package xyz.elevated.frequency.check.impl.speed;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Speed (A)")
public final class SpeedA extends PositionCheck {
    private double buffer = 0.0;
    private double blockSlipperiness = 0.91;
    private double lastHorizontalDistance = 0.0;
    private boolean belowBlock = false;

    public SpeedA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the location update from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the entity player from the NMS util
        final Player player = playerData.getBukkitPlayer();
        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);

        // Get the pos deltas
        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        // Get the player's attribute speed and last friction
        double blockSlipperiness = this.blockSlipperiness;
        double attributeSpeed = 1.d;

        // Run calculations to if the player is on ground and if they're exempt
        final boolean onGround = entityPlayer.onGround;
        final boolean exempt = this.isExempt(ExemptType.TPS, ExemptType.TELEPORTING);

        // Calculate the player's amplifier speed
        final int amplifierSpeed = MathUtil.getPotionLevel(player, PotionEffectType.SPEED);

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

        // Add to the attribute speed according to velocity
        attributeSpeed += playerData.getVelocityManager().getMaxVertical();

        // Get the proper speedup threshold
        double threshold = belowBlock ? 3.6 : 1.0;

        // Get the horizontal distance and convert to the movement speed
        final double horizontalDistance = Math.hypot(deltaX, deltaZ);
        final double movementSpeed = (horizontalDistance - lastHorizontalDistance) / attributeSpeed;

        // Increase the threshold according to the amplifier speed
        if (amplifierSpeed > 0) threshold += amplifierSpeed * 0.17;

        // If thr movement speed is greater than the threshold and the player isn't exempt, fail
        if (movementSpeed > threshold && !exempt) {
            buffer += 0.25;

            if (buffer > 0.5) {
                fail();
            }
        } else {
            buffer = Math.max(buffer - 0.005, 0);
        }

        // Update previous values
        this.lastHorizontalDistance = horizontalDistance * blockSlipperiness;
        this.belowBlock = playerData.getBoundingBox().get().expand(0.25, 0.0, 0.25)
                .move(0.0, 1.0, 0.0)
                .checkBlocks(player.getWorld(),
                m -> m != Material.AIR);

        this.blockSlipperiness = entityPlayer.world.getType(new BlockPosition(MathHelper.floor(to.getX()),
                MathHelper.floor(entityPlayer.getBoundingBox().b) - 1,
                MathHelper.floor(to.getZ())))
                .getBlock()
                .frictionFactor * 0.91F;
    }
}
