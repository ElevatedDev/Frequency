package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PostCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.PacketWrapper;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInCustomPayload;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "BadPackets (D)")
public final class BadPacketsD extends PostCheck {

    public BadPacketsD(final PlayerData playerData) {
        super(playerData, WrappedPlayInCustomPayload.class);
    }

    @Override
    public void process(final Object object) {
        final boolean post = this.isPost(object);

        if (post) fail();
    }
}
