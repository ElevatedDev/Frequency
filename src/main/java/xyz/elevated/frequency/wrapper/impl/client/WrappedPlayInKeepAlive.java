package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInKeepAlive extends PacketWrapper {
    public WrappedPlayInKeepAlive(Packet<?> instance) {
        super(instance, PacketPlayInKeepAlive.class);
    }

    public int getId() {
        return get("a");
    }
}
