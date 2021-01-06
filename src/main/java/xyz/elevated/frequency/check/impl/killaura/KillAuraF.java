package xyz.elevated.frequency.check.impl.killaura;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "KillAura (F)")
public final class KillAuraF extends PacketCheck {

    private double lastPosX = 0.0d, lastPosZ = 0.0d, lastHorizontalDistance = 0.0d;
    private float lastYaw = 0L, lastPitch = 0L;

    public KillAuraF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            if (!wrapper.hasLook() || !wrapper.hasPos()) return;

            final double posX = wrapper.getX();
            final double posZ = wrapper.getZ();

            final float yaw = wrapper.getYaw();
            final float pitch = wrapper.getPitch();

            final double horizontalDistance = MathUtil.magnitude(posX - lastPosX, posZ - lastPosZ);

            // Player moved
            if (horizontalDistance > 0.0) {
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
