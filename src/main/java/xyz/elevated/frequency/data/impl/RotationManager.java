package xyz.elevated.frequency.data.impl;

import lombok.RequiredArgsConstructor;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;

@RequiredArgsConstructor
public final class RotationManager {
    private final PlayerData playerData;

    private float lastYaw, lastPitch;

    public void handle(final float yaw, final float pitch) {
        final float deltaYaw = Math.abs(yaw - lastYaw);
        final float deltaPitch = Math.abs(pitch - lastPitch);

        final RotationUpdate rotationUpdate = new RotationUpdate(deltaYaw, deltaPitch);

        playerData.getCheckManager().getChecks().stream().filter(RotationCheck.class::isInstance).forEach(check -> check.process(rotationUpdate));
        
        this.lastYaw = yaw;
        this.lastPitch = pitch;
    }
}
