package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayOutTransaction extends PacketWrapper {

    public WrappedPlayOutTransaction(Packet<?> instance) {
        super(instance, PacketPlayOutTransaction.class);
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public short getHash() {
        return get("b");
    }
}
