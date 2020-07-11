package xyz.elevated.frequency.check.impl.autoclicker;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

import java.util.Deque;
import java.util.List;

@CheckData(name = "AutoClicker (F)")
public final class AutoClickerF extends PacketCheck {

    private int movements = 0;
    private double buffer = 0.0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public AutoClickerF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = movements < 4 && !playerData.getActionManager().getDigging().get();

            if (valid) samples.add(movements);

            if (samples.size() == 15) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final double deviation = MathUtil.getStandardDeviation(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final double cps = 20 / samples.stream().mapToDouble(d -> d).average().orElse(0.0);

                if (deviation < 0.3 && outliers < 2 && cps % 1.0 == 0.0) {
                    buffer += 0.25;

                    if (buffer > 0.75) {
                        fail();
                    }
                } else {
                    buffer = Math.max(buffer - 0.2, 0);
                }

                samples.clear();
            }
            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }

}
