package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInSteerVehicle;

@CheckData(name = "BadPackets (L)")
public final class BadPacketsL extends PacketCheck {

    private int streak = 0;

    public BadPacketsL(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final WrappedPlayInFlying wrapper = (WrappedPlayInFlying) object;

            if (!wrapper.hasPos() && playerData.getBukkitPlayer().getVehicle() == null) {
                // There must be a position update by the client every 20 ticks
                if (++streak > 20) {
                    fail();
                }
            } else {
                streak = 0;
            }

        } else if (object instanceof WrappedPlayInSteerVehicle) {
            streak = 0;
        }
    }
}
