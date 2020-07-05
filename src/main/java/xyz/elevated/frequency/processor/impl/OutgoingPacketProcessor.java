package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.processor.type.Processor;

public final class OutgoingPacketProcessor implements Processor<Packet<PacketListenerPlayOut>> {

    @Override
    public void process(final PlayerData playerData, final Packet<PacketListenerPlayOut> packet) {
        if (packet instanceof PacketPlayOutEntity) {

        }
    }
}
