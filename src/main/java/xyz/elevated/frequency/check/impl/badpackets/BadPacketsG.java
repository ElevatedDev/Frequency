package xyz.elevated.frequency.check.impl.badpackets;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInEntityAction;

@CheckData(name = "BadPackets (G)")
public final class BadPacketsG extends PacketCheck {
    private int count = 0;

    public BadPacketsG(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInEntityAction) {
            final boolean invalid = ++count > 1;

            if (invalid) {
                fail();
            }
        } else {
            count = 0;
        }
    }
}
