package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInWindowClick extends PacketWrapper {

    public WrappedPlayInWindowClick(final Packet<?> instance) {
        super(instance, PacketPlayInWindowClick.class);
    }

    public int getSlot() {
        return get("slot");
    }

    public int getShift() {
        return get("shift");
    }

    public int getButton() {
        return get("button");
    }
}
