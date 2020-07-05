package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInArmAnimation extends PacketWrapper {

    public WrappedPlayInArmAnimation(final Packet<?> instance) {
        super(instance, PacketPlayInArmAnimation.class);
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public long getPacketTimestamp() {
        return get("timestamp");
    }
}
