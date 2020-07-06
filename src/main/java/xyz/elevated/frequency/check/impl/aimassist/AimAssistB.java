package xyz.elevated.frequency.check.impl.aimassist;

import org.bukkit.Rotation;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;

@CheckData(name = "AimAssist (B)")
public final class AimAssistB extends RotationCheck {
    private int lastRoundedYaw, lastRoundedPitch, streak;

    public AimAssistB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final long now = System.currentTimeMillis();

        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();

        final int roundedYaw = Math.round(deltaYaw);
        final int roundedPitch = Math.round(deltaPitch);
        final int roundedDelta = Math.abs(roundedPitch - lastRoundedPitch);

        final boolean attacking = now - playerData.getActionManager().getLastAttack() < 500L;
        final boolean cinematic = playerData.getCinematic().get();

        if (roundedDelta < 5 && deltaYaw < 20.f && deltaPitch < 20.f && attacking && cinematic) {
            if (++streak > 7) {
                fail();
            }
        } else {
            streak = 0;
        }

        this.lastRoundedYaw = roundedYaw;
        this.lastRoundedPitch = roundedPitch;
    }
}
