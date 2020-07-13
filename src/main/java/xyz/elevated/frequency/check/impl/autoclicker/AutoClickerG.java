package xyz.elevated.frequency.check.impl.autoclicker;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

import java.util.Deque;
import java.util.List;

@CheckData(name = "AutoClicker (G)")
public final class AutoClickerG extends PacketCheck {

    private int movements = 0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public AutoClickerG(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = movements < 4 && !playerData.getActionManager().getDigging().get();

            if (valid) samples.add(movements);

            // Sample size is assigned to 15
            if (samples.size() == 15) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final double skewness = MathUtil.getSkewness(samples);
                final double kurtosis = MathUtil.getKurtosis(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();

                // See if skewness and kurtosis is exceeding a specific limit.
                if (skewness < 0.035 && kurtosis < 0.1 && outliers < 2) fail();

                samples.clear();
            }
            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }

}
