package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public class WrappedInKeepAlive extends PacketWrapper {
    public WrappedInKeepAlive(Packet<?> instance) {
        super(instance, PacketPlayInKeepAlive.class);
    }

    public int getTime() {
        return get("a");
    }

    public void setTime(int time) {
        set("a", time);
    }
}
