package xyz.elevated.frequency.wrapper.impl.client;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import xyz.elevated.frequency.wrapper.PacketWrapper;

public final class WrappedPlayInClientCommand extends PacketWrapper {

    public WrappedPlayInClientCommand(final Packet<?> instance) {
        super(instance, PacketPlayInClientCommand.class);
    }

    public PacketPlayInClientCommand.EnumClientCommand getCommand() {
        return get("a");
    }
}
