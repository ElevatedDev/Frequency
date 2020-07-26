package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayOutKeepAlive extends PacketWrapper {
    public WrappedPlayOutKeepAlive(Packet<?> instance) {
        super(instance, PacketPlayOutKeepAlive.class);
    }

    public int getTime() {
        return get("a");
    }

    public void setTime(int time) {
        set("a", time);
    }
}
