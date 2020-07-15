package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInHeldItemSlot extends PacketWrapper {

    public WrappedPlayInHeldItemSlot(final Packet<?> instance) {
        super(instance, PacketPlayInHeldItemSlot.class);
    }

    public int getSlot() {
        return get("itemInHandIndex");
    }
}
