package xyz.elevated.frequency.check.impl.aimassist;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;

import java.util.Deque;

@CheckData(name = "AimAssist (A)")
public final class AimAssistA extends RotationCheck {
    private final Deque<Float> samples = Lists.newLinkedList();
    private double buffer = 0.0;
    
    public AimAssistA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final float deltaYaw = rotationUpdate.getDeltaYaw();
        final float deltaPitch = rotationUpdate.getDeltaPitch();
        
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 30.f && deltaPitch < 30.f) {
            samples.add(deltaPitch);
        }
        
        if (samples.size() == 128) {
            final int distinct = (int) (samples.stream().distinct().count());
            final int duplicates = samples.size() - distinct;

            final double average = samples.stream().mapToDouble(d -> d).average().orElse(0.0);

            if (duplicates <= 9 && average < 30.f) {
                if (++buffer > 3) {
                    fail();
                }
            } else {
                buffer = Math.max(buffer - 3, 0);
            }
        }
    }
}
