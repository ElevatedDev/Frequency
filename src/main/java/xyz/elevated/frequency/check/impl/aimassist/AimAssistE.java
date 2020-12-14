package xyz.elevated.frequency.check.impl.aimassist;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.MathUtil;

import java.util.concurrent.atomic.AtomicBoolean;

@CheckData(name = "AimAssist (E)")
public final class AimAssistE extends RotationCheck {
    private float lastDeltaYaw = 0.0f, lastDeltaPitch = 0.0f;
    private int buffer = 0;

    private static final double MODULO_THRESHOLD = 90F;
    private static final double LINEAR_THRESHOLD = 0.1F;

    public AimAssistE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final int now = playerData.getTicks().get();

        // Get the deltas from the rotation update
        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();

        // Grab the gcd using an expander.
        final double divisorYaw = MathUtil.getGcd((long) (deltaYaw * MathUtil.EXPANDER), (long) (lastDeltaYaw * MathUtil.EXPANDER));
        final double divisorPitch = MathUtil.getGcd((long) (deltaPitch * MathUtil.EXPANDER), (long) (lastDeltaPitch * MathUtil.EXPANDER));

        // Get the constant for both rotation updates by dividing by the expander
        final double constantYaw = divisorYaw / MathUtil.EXPANDER;
        final double constantPitch = divisorPitch / MathUtil.EXPANDER;

        // Get the estimated mouse delta from the constant
        final double currentX = deltaYaw / constantYaw;
        final double currentY = deltaPitch / constantPitch;

        // Get the estimated mouse delta from the old rotations using the new constant
        final double previousX = lastDeltaYaw / constantYaw;
        final double previousY = lastDeltaPitch / constantPitch;

        // Make sure the player is attacking or placing to filter out the check
        final boolean action = now - playerData.getActionManager().getLastAttack() < 3
                || now - playerData.getActionManager().getLastPlace() < 3;

        // Make sure the rotation is not very large and not equal to zero and get the modulo of the xys
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.f && deltaPitch < 20.f && action) {
            final double moduloX = currentX % previousX;
            final double moduloY = currentY % previousY;

            // Get the floor delta of the the moduloes
            final double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            final double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);

            // Impossible to have a different constant in two rotations
            final boolean invalidX = moduloX > MODULO_THRESHOLD && floorModuloX > LINEAR_THRESHOLD;
            final boolean invalidY = moduloY > MODULO_THRESHOLD && floorModuloY > LINEAR_THRESHOLD;

            if (invalidX && invalidY) {
                buffer = Math.min(buffer + 1, 200);

                if (buffer > 6) fail();
            } else {
                buffer = 0;
            }
        }

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}
