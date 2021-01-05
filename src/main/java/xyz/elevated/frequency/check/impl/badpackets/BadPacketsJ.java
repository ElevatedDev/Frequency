package xyz.elevated.frequency.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInWindowClick;

@CheckData(name = "BadPackets (J)")
public final class BadPacketsJ extends PacketCheck {

    public BadPacketsJ(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;
            handle: {
                if (wrapper.getAction() != EnumEntityUseAction.ATTACK) break handle;

                final boolean placing = playerData.getActionManager().getPlacing().get();

                if (placing) fail();
            }
        }
    }
}
