package xyz.elevated.frequency.check.impl.aimassist;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.MathUtil;

@CheckData(name = "AimAssist (E)")
public final class AimAssistE extends RotationCheck {
    private float lastDeltaYaw = 0.0f, lastDeltaPitch = 0.0f;
    private int buffer = 0;

    public AimAssistE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
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

        // Make sure the rotation is not very large and not equal to zero and get the modulo of the xys
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.f && deltaPitch < 20.f) {
            final double moduloX = currentX % previousX;
            final double moduloY = currentY % previousY;

            // Get the floor delta of the the moduloes
            final double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            final double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);

            // Impossible to have a different constant in two rotations
            final boolean invalidX = moduloX > 90.d && floorModuloX > 0.1;
            final boolean invalidY = moduloY > 90.d && floorModuloY > 0.1;

            if (invalidX || invalidY) {
                buffer = Math.min(buffer + 1, 200);

                if (buffer > 10) {
                    fail();
                }
            } else {
                buffer = 0;
            }
        }

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}
