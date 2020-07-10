package xyz.elevated.frequency.check.impl.autoclicker;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.EvictingList;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

import java.util.Deque;

@CheckData(name = "AutoClicker (B)")
public final class AutoClickerB extends PacketCheck {
    private final Deque<Integer> samples = Lists.newLinkedList();
    private int movements = 0, streak = 0;

    private double lastKurtosis, lastSkewness, lastDeviation;

    public AutoClickerB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = movements < 4 && !playerData.getActionManager().getDigging().get();

            if (valid) samples.add(movements);

            if (samples.size() == 10) {
                final double deviation = MathUtil.getStandardDeviation(samples);
                final double skewness = MathUtil.getSkewness(samples);
                final double kurtosis = MathUtil.getKurtosis(samples);

                if (deviation == lastDeviation && skewness == lastSkewness && kurtosis == lastKurtosis) {
                    if (++streak > 1) {
                        fail();
                    }
                } else {
                    streak = 0;
                }

                lastDeviation = deviation;
                lastKurtosis = kurtosis;
                lastSkewness = skewness;
                samples.clear();
            }

            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }
}
