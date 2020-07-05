package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.BoundingBox;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.data.impl.RotationManager;
import xyz.elevated.frequency.processor.type.Processor;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

public final class IncomingPacketProcessor implements Processor<Packet<PacketListenerPlayIn>> {

    @Override
    public void process(final PlayerData playerData, final Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInFlying) {
            final WrappedPlayInFlying wrapper = new WrappedPlayInFlying(packet);

            final double posX = wrapper.getX();
            final double posY = wrapper.getY();
            final double posZ = wrapper.getZ();

            final float yaw = wrapper.getYaw();
            final float pitch = wrapper.getPitch();

            final boolean hasPos = wrapper.hasPos();
            final boolean hasLook = wrapper.hasLook();

            if (hasPos) {
                final BoundingBox boundingBox = new BoundingBox(posX, posY, posZ);

                playerData.getPositionManager().handle(posX, posY, posZ);
                playerData.getBoundingBox().set(boundingBox);
                playerData.getBoundingBoxes().add(boundingBox);
            }

            if (hasLook) {
                final RotationManager rotationManager = playerData.getRotationManager();

                rotationManager.handle(yaw, pitch);
            }
        }
    }
}
