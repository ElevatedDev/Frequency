package xyz.elevated.frequency.check.impl.invalid;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Invalid (D)")
public final class InvalidD extends PacketCheck {
    private double lastPosX = 0.0, lastPosZ = 0.0;
    private double lastHorizontalDistance = 0.0;

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
                final boolean attacking = playerData.getActionManager().getAttacking().get();

                if (attacking && !exempt) {
                    final Entity entity = playerData.getTarget().get();

                    final boolean exist = entity instanceof Player;
                    final boolean flag = acceleration < 1e-01 && horizontalDistance > lastHorizontalDistance * 0.99;

                    if (exist && flag) fail();
                }

                lastHorizontalDistance = horizontalDistance;
                lastPosX = posX;
                lastPosZ = posZ;
            }
        }
    }
}
