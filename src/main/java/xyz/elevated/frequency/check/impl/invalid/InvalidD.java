package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
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
    private double lastPosX = 0.0, lastPosZ = 0.0;
    private double lastHorizontalDistance = 0.0, buffer = 0.0;
    private boolean attacked = false;

    public InvalidD(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            if (wrapper.hasPos()) {
                final double posX = wrapper.getX();
                final double posZ = wrapper.getZ();

                final double horizontalDistance = Math.hypot(posX - lastPosX, posZ - lastPosZ);
                final double acceleration = Math.abs(horizontalDistance - lastHorizontalDistance);

                final boolean exempt = this.isExempt(ExemptType.TPS, ExemptType.TELEPORTING);
                final boolean attacking = attacked;

                if (attacking && !exempt) {
                    final Entity entity = playerData.getTarget().get();

                    final boolean exist = entity instanceof Player;
                    final boolean flag = acceleration < 1e-04 && horizontalDistance > lastHorizontalDistance * 0.99;

                    if (exist && flag) {
                        buffer += 0.25;

                        if (buffer > 1.25) {
                            fail();
                        }
                    } else {
                        buffer = Math.max(buffer - 0.5, 0);
                    }
                }

                attacked = false;
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
