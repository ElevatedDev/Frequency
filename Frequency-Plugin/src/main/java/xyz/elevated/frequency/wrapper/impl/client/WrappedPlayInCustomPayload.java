package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInCustomPayload extends PacketWrapper {

    public WrappedPlayInCustomPayload(final Packet<?> instance) {
        super(instance, PacketPlayInCustomPayload.class);
    }

    public String getPayload() {
        return get("a");
    }
}
