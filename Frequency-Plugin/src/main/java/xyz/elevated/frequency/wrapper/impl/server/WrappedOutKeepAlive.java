package xyz.elevated.frequency.wrapper.impl.server;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public class WrappedOutKeepAlive extends PacketWrapper {
    public WrappedOutKeepAlive(Packet<?> instance) {
        super(instance, PacketPlayOutKeepAlive.class);
    }

    public int getTime() {
        return get("a");
    }

    public void setTime(int time) {
        set("a", time);
    }
}
