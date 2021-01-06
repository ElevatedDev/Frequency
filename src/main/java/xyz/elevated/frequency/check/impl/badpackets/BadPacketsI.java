package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "BadPackets (I)")
public final class BadPacketsI extends PacketCheck {

    private float lastYaw = 0.0f, lastPitch = 0.0f;

    public BadPacketsI(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            if (!wrapper.hasLook() || playerData.getBukkitPlayer().isInsideVehicle()
                    || playerData.getActionManager().getSteer().get()) return;

            final float yaw = wrapper.getYaw();
            final float pitch = wrapper.getPitch();

            final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.LAGGING, ExemptType.TPS, ExemptType.VEHICLE);

            if (yaw == lastYaw && pitch == lastPitch && !exempt) {
                fail();
            }

            this.lastYaw = yaw;
            this.lastPitch = pitch;
        }
    }
}
