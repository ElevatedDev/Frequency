package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInFlying extends PacketWrapper {

    public WrappedPlayInFlying(final Packet<?> instance) {
        super(instance, PacketPlayInFlying.class);
    }

    public double getX() {
        return get("x");
    }

    public double getY() {
        return get("y");
    }

    public double getZ() {
        return get("z");
    }

    public float getYaw() {
        return get("yaw");
    }

    public float getPitch() {
        return get("pitch");
    }

    public boolean hasPos() {
        return get("hasPos");
    }

    public boolean hasLook() {
        return get("hasLook");
    }

    public boolean onGround() {
        return get("f");
    }
}
