package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.*;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.processor.type.Processor;
import xyz.elevated.frequency.wrapper.impl.server.WrappedPlayOutKeepAlive;
import xyz.elevated.frequency.wrapper.impl.server.WrappedPlayOutEntityVelocity;
import xyz.elevated.frequency.wrapper.impl.server.WrappedPlayOutTeleport;
import xyz.elevated.frequency.wrapper.impl.server.WrappedPlayOutTransaction;

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
        } else if (packet instanceof PacketPlayOutEntityTeleport) {
            final WrappedPlayOutTeleport wrapper = new WrappedPlayOutTeleport(packet);

            final int entityId = wrapper.getEntityId();
            final int playerId = playerData.getBukkitPlayer().getEntityId();

            if (entityId == playerId) {
                playerData.getActionManager().onTeleport();
            }
        } else if (packet instanceof PacketPlayOutPosition) {
            playerData.getActionManager().onTeleport();
        }
    }
}
