package xyz.elevated.frequency.check.impl.killaura;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "KillAura (F)")
public final class KillAuraF extends PacketCheck {
    private double lastPosX, lastPosZ, lastHorizontalDistance;
    private float lastYaw, lastPitch;

    public KillAuraF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            final double posX = wrapper.getX();
            final double posZ = wrapper.getZ();

            final float yaw = wrapper.getYaw();
            final float pitch = wrapper.getPitch();

            final double horizontalDistance = Math.hypot(posX - lastPosX, posZ - lastPosZ);

            // Player moved
            if (posX != lastPosX || posZ != lastPosZ) {
                final float deltaYaw = Math.abs(yaw - lastYaw);
                final float deltaPitch = Math.abs(pitch - lastPitch);

                final boolean attacking = playerData.getActionManager().getAttacking().get();
                final double acceleration = Math.abs(horizontalDistance - lastHorizontalDistance);

                // Player made a large head rotation and didn't accelerate / decelerate which is impossible
                if (acceleration < 1e-02 && deltaYaw > 30.f && deltaPitch > 15.f && attacking) {
                    fail();
                }
            }

            this.lastHorizontalDistance = horizontalDistance;
            this.lastYaw = yaw;
            this.lastPitch = pitch;
            this.lastPosX = posX;
            this.lastPosZ = posZ;
        }
    }
}
