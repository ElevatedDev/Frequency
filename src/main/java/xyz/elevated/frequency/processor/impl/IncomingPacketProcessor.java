package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.*;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.BoundingBox;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.data.impl.RotationManager;
import xyz.elevated.frequency.processor.type.Processor;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.wrapper.impl.client.*;

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

            playerData.getActionManager().onFlying();
            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = new WrappedPlayInUseEntity(packet);

            if (wrapper.getAction() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                playerData.getActionManager().onAttack();
            }

            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInBlockDig) {
            final WrappedPlayInBlockDig wrapper = new WrappedPlayInBlockDig(packet);

            switch (wrapper.getDigType()) {
                case START_DESTROY_BLOCK:
                case ABORT_DESTROY_BLOCK:
                case STOP_DESTROY_BLOCK: {
                    playerData.getActionManager().onDig();
                    break;
                }
            }

            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInHeldItemSlot) {
            final WrappedPlayInHeldItemSlot wrapper = new WrappedPlayInHeldItemSlot(packet);

            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInEntityAction) {
            final WrappedPlayInEntityAction wrapper = new WrappedPlayInEntityAction(packet);

            switch (wrapper.getAction()) {
                case START_SPRINTING: {
                    playerData.getSprinting().set(true);
                    break;
                }

                case STOP_SPRINTING: {
                    playerData.getSprinting().set(false);
                    break;
                }
            }

            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInCustomPayload) {
            final WrappedPlayInCustomPayload wrapper = new WrappedPlayInCustomPayload(packet);

            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        }
    }
}
