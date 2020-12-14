package xyz.elevated.frequency.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInEntityAction;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "BadPackets (G)")
public final class BadPacketsG extends PacketCheck {

    private int count = 0;
    private PacketPlayInEntityAction.EnumPlayerAction lastAction;

    public BadPacketsG(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInEntityAction) {
            final WrappedPlayInEntityAction wrapper = (WrappedPlayInEntityAction) object;

            final boolean invalid = ++count > 1 && wrapper.getAction() == lastAction;

            if (invalid) fail();

            this.lastAction = wrapper.getAction();
        } else if (object instanceof WrappedPlayInFlying) {
            count = 0;
        }
    }
}
