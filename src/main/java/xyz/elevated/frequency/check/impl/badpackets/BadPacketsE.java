package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PostCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.PacketWrapper;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInHeldItemSlot;

@CheckData(name = "BadPackets (E)")
public final class BadPacketsE extends PostCheck {

    public BadPacketsE(final PlayerData playerData) {
        super(playerData, WrappedPlayInHeldItemSlot.class);
    }

    @Override
    public void process(final Object object) {
        final boolean post = this.isPost(object);

        if (post) fail();
    }
}
