package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayOutEntityVelocity extends PacketWrapper {

    public WrappedPlayOutEntityVelocity(Packet<?> instance) {
        super(instance, PacketPlayOutEntityVelocity.class);
    }

    public int getEntityId() {
        return get("a");
    }

    public double getX() {
        return (double) ((int) get("b")) / 8000.0;
    }

    public double getY() {
        return (double) ((int) get("c")) / 8000.0;
    }

    public double getZ() {
        return (double) ((int) get("d")) / 8000.0;
    }
}
