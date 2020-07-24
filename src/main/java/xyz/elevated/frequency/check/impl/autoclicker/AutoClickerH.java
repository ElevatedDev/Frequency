package xyz.elevated.frequency.check.impl.autoclicker;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

import java.util.Deque;

@CheckData(name = "AutoClicker (H)")
public final class AutoClickerH extends PacketCheck {
    private int movements = 0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public AutoClickerH(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = playerData.getCps().get() > 7.d && movements < 4 && !playerData.getActionManager().getDigging().get();

            if (valid) samples.add(movements);

            if (samples.size() == 20) {
                final double average = samples.stream().mapToDouble(d -> d).average().orElse(0.0);
                final double deviation = MathUtil.getStandardDeviation(samples);

                final double duplicates = samples.size() - samples.stream().distinct().count();

                if (average < 4 && deviation < 1.25 && duplicates > 10) fail();
            }

            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            movements++;
        }
    }
}
