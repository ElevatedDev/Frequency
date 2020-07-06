package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.processor.type.Processor;
import xyz.elevated.frequency.wrapper.impl.server.WrappedPlayOutEntityVelocity;

public final class OutgoingPacketProcessor implements Processor<Packet<PacketListenerPlayOut>> {

    @Override
    public void process(final PlayerData playerData, final Packet<PacketListenerPlayOut> packet) {
        if (packet instanceof PacketPlayOutEntityVelocity) {
            final WrappedPlayOutEntityVelocity wrapper = new WrappedPlayOutEntityVelocity(packet);

            final int packetEntityId = wrapper.getEntityId();
            final int playerEntityId = playerData.getBukkitPlayer().getEntityId();

            if (packetEntityId == playerEntityId) {
                final double velocityX = wrapper.getX();
                final double velocityY = wrapper.getY();
                final double velocityZ = wrapper.getZ();

                playerData.getVelocityManager().addVelocityEntry(velocityX, velocityY, velocityZ);
            }
        }
    }
}
