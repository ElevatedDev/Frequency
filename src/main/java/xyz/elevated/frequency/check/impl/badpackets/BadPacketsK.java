package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInSteerVehicle;

@CheckData(name = "BadPackets (K)")
public final class BadPacketsK extends PacketCheck {

    public BadPacketsK(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        final boolean vehicle = object instanceof WrappedPlayInSteerVehicle;
        final boolean empty = playerData.getBukkitPlayer().getVehicle() == null;

        if (vehicle && empty) fail();
    }
}
