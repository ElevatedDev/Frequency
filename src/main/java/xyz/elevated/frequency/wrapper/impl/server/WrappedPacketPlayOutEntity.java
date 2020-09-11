package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPacketPlayOutEntity extends PacketWrapper {

    public WrappedPacketPlayOutEntity(final Packet<?> instance) {
        super(instance, PacketPlayOutEntity.class);
    }

    public int getEntityId() {
        return get("a");
    }

    public double getDeltaX() {
        return ((byte) get("b")) / 32.d;
    }

    public double getDeltaY() {
        return (byte) (get("c")) / 32.d;
    }

    public double getDeltaZ() {
        return ((byte) get("d")) / 32.d;
    }
}
