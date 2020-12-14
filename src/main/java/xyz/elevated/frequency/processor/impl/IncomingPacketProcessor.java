package xyz.elevated.frequency.processor.impl;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Entity;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.BoundingBox;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.data.impl.PositionManager;
import xyz.elevated.frequency.data.impl.RotationManager;
import xyz.elevated.frequency.processor.type.Processor;
import xyz.elevated.frequency.util.NmsUtil;
import xyz.elevated.frequency.wrapper.impl.client.*;

import java.util.Random;

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
            final boolean onGround = wrapper.onGround();

            if (hasPos) {
                final PositionManager positionManager = playerData.getPositionManager();

                positionManager.handle(posX, posY, posZ, onGround);
            }

            if (hasLook) {
                final RotationManager rotationManager = playerData.getRotationManager();

                rotationManager.handle(yaw, pitch);
            }

            playerData.getVelocityManager().apply();
            playerData.getActionManager().onFlying();
            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = new WrappedPlayInUseEntity(packet);

            if (wrapper.getAction() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                playerData.getActionManager().onAttack();

                Entity entity = wrapper.getTarget(NmsUtil.getWorld(playerData.getBukkitPlayer().getWorld()));
                playerData.getTarget().set(entity);
            }

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
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

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInHeldItemSlot) {
            final WrappedPlayInHeldItemSlot wrapper = new WrappedPlayInHeldItemSlot(packet);

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
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

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInCustomPayload) {
            final WrappedPlayInCustomPayload wrapper = new WrappedPlayInCustomPayload(packet);

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInArmAnimation) {
            final WrappedPlayInArmAnimation wrapper = new WrappedPlayInArmAnimation(packet);

            playerData.getActionManager().onArmAnimation();
            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if(packet instanceof PacketPlayInKeepAlive) {
            final WrappedPlayInKeepAlive wrapper = new WrappedPlayInKeepAlive(packet);

            playerData.getConnectionManager().onKeepAlive(wrapper.getTime(), System.currentTimeMillis());
            playerData.getCheckManager().getChecks().stream().filter(PacketCheck.class::isInstance).forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInClientCommand) {
            final WrappedPlayInClientCommand wrapper = new WrappedPlayInClientCommand(packet);

            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInBlockPlace) {
            final WrappedPlayInBlockPlace wrapper =  new WrappedPlayInBlockPlace(packet);

            playerData.getActionManager().onPlace();
            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInSteerVehicle) {
            final WrappedPlayInSteerVehicle wrapper = new WrappedPlayInSteerVehicle(packet);

            playerData.getActionManager().onSteerVehicle();
            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        } else if (packet instanceof PacketPlayInTransaction) {
            final WrappedPlayInTransaction wrapper = new WrappedPlayInTransaction(packet);

            final long now = System.currentTimeMillis();

            playerData.getConnectionManager().onTransaction(wrapper.getHash(), now);
            playerData.getCheckManager().getChecks().stream()
                    .filter(PacketCheck.class::isInstance)
                    .forEach(check -> check.process(wrapper));
        }
    }
}
