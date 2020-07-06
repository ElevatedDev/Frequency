package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInHeldItemSlot;

@CheckData(name = "BadPackets (F)")
public final class BadPacketsF extends PacketCheck {
    private int lastSlot;

    public BadPacketsF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInHeldItemSlot) {
            final WrappedPlayInHeldItemSlot wrapper = (WrappedPlayInHeldItemSlot) object;

            if (wrapper.getSlot() == lastSlot) {
                fail();
            }

            this.lastSlot = wrapper.getSlot();
        }
    }
}
