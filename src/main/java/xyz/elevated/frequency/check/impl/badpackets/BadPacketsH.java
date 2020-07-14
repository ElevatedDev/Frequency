package xyz.elevated.frequency.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInBlockDig;

@CheckData(name = "BadPackets (H)")
public final class BadPacketsH extends PacketCheck {

    private int count = 0;

    public BadPacketsH(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInBlockDig) {
            final WrappedPlayInBlockDig wrapper = (WrappedPlayInBlockDig) object;

            if (wrapper.getDigType() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
                final boolean invalid = ++count > 1;

                if (invalid) {
                    fail();
                }
            }
        } else {
            count = 0;
        }
    }
}
