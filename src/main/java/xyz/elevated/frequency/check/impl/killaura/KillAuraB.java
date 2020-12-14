package xyz.elevated.frequency.check.impl.killaura;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.MathUtil;

import java.util.Deque;

@CheckData(name = "KillAura (B)")
public final class KillAuraB extends RotationCheck {

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    private double buffer = 0.0d, lastAverage = 0.0d;

    public KillAuraB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();

        final boolean attacking = System.currentTimeMillis() - playerData.getActionManager().getLastAttack() < 500L;

        if (deltaYaw > 0.0 && deltaPitch > 0.0 && attacking) {
            samplesYaw.add(deltaYaw);
            samplesPitch.add(deltaPitch);
        }

        if (samplesPitch.size() == 20 && samplesYaw.size() == 20) {
            final double averageYaw = samplesYaw.stream().mapToDouble(d -> d).average().orElse(0.0);
            final double averagePitch = samplesPitch.stream().mapToDouble(d -> d).average().orElse(0.0);

            final double deviation = MathUtil.getStandardDeviation(samplesPitch);
            final double averageDelta = Math.abs(averagePitch - lastAverage);

            if (deviation > 6.f && averageDelta > 1.5f && averageYaw < 30.d) {
                buffer += 0.5;

                if (buffer > 2.0) fail();
            } else {
                buffer = Math.max(buffer - 0.125, 0);
            }

            samplesYaw.clear();
            samplesPitch.clear();
            lastAverage = averagePitch;
        }
    }
}
