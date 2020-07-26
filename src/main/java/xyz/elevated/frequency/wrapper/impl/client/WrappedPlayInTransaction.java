package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInTransaction extends PacketWrapper {

    public WrappedPlayInTransaction(final Packet<?> instance) {
        super(instance, PacketPlayInTransaction.class);
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public long getId() {
        return get("a");
    }

    public short getHash() {
        return get("b");
    }
}
