package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayOutTeleport extends PacketWrapper {

    public WrappedPlayOutTeleport(final Packet<?> instance) {
        super(instance, PacketPlayOutEntityTeleport.class);
    }

    public int getEntityId() {
        return get("a");
    }
}
