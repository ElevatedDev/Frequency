package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.NmsUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "Invalid (D)")
public final class InvalidD extends PacketCheck {

    private double lastPosX = 0.0d, lastPosZ = 0.0d, lastHorizontalDistance = 0.0d, buffer = 0.0d;
    private boolean attacked = false;

    public InvalidD(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            if (wrapper.hasPos()) {
                // Get position values from wrapper
                final double posX = wrapper.getX();
                final double posZ = wrapper.getZ();

                // Calculate the basic horizontal distance and the acceleration
                final double horizontalDistance = Math.hypot(posX - lastPosX, posZ - lastPosZ);
                final double acceleration = Math.abs(horizontalDistance - lastHorizontalDistance);

                /*
                * The theory is, when the player attacks an entity they get slowed down by 0.6. Here we're simply checking
                * if the player had any sort of slowdown when attacking a player. If not, that means that the player's
                * motion was not messed with. When that happens, increase a buffer to reduce possible false positives.
                 */
                motion: {
                    final Entity target = playerData.getTarget().get();

                    /*
                    * The player only gets slowed down in Minecraft when he's hitting another player. Not if
                    * he is hitting a mob. Thus, we need to make sure that the entity attacked is surely a
                    * player, and not a mob or this check is going to false to bits since the theory is wrong then,
                     */
                    final boolean exists = target instanceof Player;
                    final boolean exempt = this.isExempt(ExemptType.TPS, ExemptType.TELEPORTING);

                    /*
                    * We don't want to run the check if the player has not attacked or if the entity attacked
                    * was not a player of if the player did not attack at all. Thus, we're breaking on these scenarios.
                     */
                    if (exempt || !exists || !attacked) break motion;

                    /*
                    * The check only is valid if the player is sprinting, so we also need to make sure that the player'ss
                    * sprinting status are true, or the check again is not going to work as expected.
                     */
                    final boolean accelerated = acceleration < 1e-04 && horizontalDistance > lastHorizontalDistance * 0.99;
                    final boolean sprinting = playerData.getSprinting().get();

                    if (accelerated && sprinting) {
                        buffer += 0.25;

                        if (buffer > 1.25) fail();
                    } else {
                        buffer = Math.max(buffer - 0.25, 0);
                    }

                    attacked = false;
                }

                lastHorizontalDistance = horizontalDistance;
                lastPosX = posX;
                lastPosZ = posZ;
            }
        } else if (object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;

            final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);
            final World world = entityPlayer.world;

            attacked: {
                if (wrapper.getAction() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) break attacked;

                if (wrapper.getTarget(world) instanceof Player) {
                    attacked = true;
                }
            }
        }
    }
}
