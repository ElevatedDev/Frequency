package xyz.elevated.frequency.check.impl.aimassist;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.MathUtil;

@CheckData(name = "AimAssist (D)")
public final class AimAssistD extends RotationCheck {

    private float lastDeltaPitch = 0.0f;
    private boolean applied = false;

    private int rotations = 0;
    private final long[] grid = new long[10];

    public AimAssistD(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final long now = System.currentTimeMillis();

        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();

        final boolean cinematic = playerData.getCinematic().get();
        final boolean attacking = now - playerData.getActionManager().getLastAttack() < 500L;

        final long deviation = getDeviation(deltaPitch);

        ++rotations;
        grid[rotations % grid.length] = deviation;

        // If the player wasn't using cinematic, where attacking and weren't spamming their aim
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 30.f && deltaPitch < 30.f && !cinematic && attacking) {
            final boolean reached = rotations > grid.length;

            // If the rotations made were greater than the gcd length
            if (reached) {
                double deviationMax = 0;

                // Get the max deviation from the gcd log
                for (final double l : grid) {
                    if (deviation != 0 && l != 0)
                        deviationMax = Math.max(Math.max(l, deviation) % Math.min(l, deviation), deviationMax);
                }

                // If both the deviation and the max deviation were greater than 0,9
                if (deviationMax > 0.0 && deviation > 0.0) {
                    fail();

                    applied = false;
                }
            }
        }

        this.lastDeltaPitch = deltaPitch;
    }

    // Get the GCD from the stored rotations and return a result whenever applied isn't false.
    private long getDeviation(final float deltaPitch) {
        final long expandedPitch = (long) (deltaPitch * MathUtil.EXPANDER);
        final long previousExpandedPitch = (long) (lastDeltaPitch * MathUtil.EXPANDER);

        final long result = applied ? MathUtil.getGcd(expandedPitch, previousExpandedPitch) : 0;

        if (applied) {
            applied = false;

            return result;
        }

        return 0L;
    }
}
