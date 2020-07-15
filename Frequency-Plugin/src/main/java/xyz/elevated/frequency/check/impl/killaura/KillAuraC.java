package xyz.elevated.frequency.check.impl.killaura;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.RotationUpdate;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

@CheckData(name = "KillAura (C)")
public final class KillAuraC extends RotationCheck {

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    public KillAuraC(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();

        final boolean exempt = this.isExempt(ExemptType.TELEPORTING);

        if (deltaYaw > 0.0 && deltaPitch > 0.0 && !exempt) {
            samplesPitch.add(deltaPitch);
            samplesYaw.add(deltaYaw);
        }

        if (samplesPitch.size() == 10 && samplesYaw.size() == 10) {
            final AtomicInteger level = new AtomicInteger(0);

            final double averageYaw = samplesYaw.stream().mapToDouble(d -> d).average().orElse(0.0);
            final double averagePitch = samplesPitch.stream().mapToDouble(d -> d).average().orElse(0.0);

            samplesYaw.stream().filter(delta -> delta % 1.0 == 0.0).forEach(delta -> level.incrementAndGet());
            samplesPitch.stream().filter(delta -> delta % 1.0 == 0.0).forEach(delta -> level.incrementAndGet());

            if (level.get() >= 8 && averageYaw > 1.d && averagePitch > 1.d) {
                fail();
            }
        }
    }
}
