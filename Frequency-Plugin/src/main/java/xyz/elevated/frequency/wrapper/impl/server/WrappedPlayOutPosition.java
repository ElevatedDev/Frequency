package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayOutPosition extends PacketWrapper {

    public WrappedPlayOutPosition(final Packet<?> instance) {
        super(instance, PacketPlayOutPosition.class);
    }

    public double getX() {
        return (double) (get("a")) / 32.d;
    }

    public double getY() {
        return (double) (get("b")) / 32.d;
    }

    public double getZ() {
        return (double) (get("c")) / 32.d;
    }
}
