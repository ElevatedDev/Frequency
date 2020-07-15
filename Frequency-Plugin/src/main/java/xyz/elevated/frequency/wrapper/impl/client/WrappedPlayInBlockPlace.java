package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInBlockPlace extends PacketWrapper {

    public WrappedPlayInBlockPlace(final Packet<?> instance) {
        super(instance, PacketPlayInBlockPlace.class);
    }

    public long getTimestamp() {
        return get("timestamp");
    }

    public long getSystemTimestamp() {
        return System.currentTimeMillis();
    }

    public int getAction() {
        return get("c");
    }
}
