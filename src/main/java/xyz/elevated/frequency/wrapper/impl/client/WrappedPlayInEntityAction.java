package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInEntityAction extends PacketWrapper {

    public WrappedPlayInEntityAction(final Packet<?> instance) {
        super(instance, PacketPlayInEntityAction.class);
    }

    public PacketPlayInEntityAction.EnumPlayerAction getAction() {
        return get("animation");
    }
}