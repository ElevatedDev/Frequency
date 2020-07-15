package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInBlockDig extends PacketWrapper {

    public WrappedPlayInBlockDig(final Packet<?> instance) {
        super(instance, PacketPlayInBlockDig.class);
    }

    public PacketPlayInBlockDig.EnumPlayerDigType getDigType() {
        return get("c");
    }

    public EnumDirection getDirection() {
        return get("b");
    }
}
