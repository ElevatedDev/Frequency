package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInSteerVehicle extends PacketWrapper {

    public WrappedPlayInSteerVehicle(final Packet<?> instance) {
        super(instance, PacketPlayInSteerVehicle.class);
    }

    public float getForward() {
        return get("a");
    }

    public float getSide() {
        return get("b");
    }
}
