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

@CheckData(name = "AutoClicker (A)")
public final class AutoClickerA extends PacketCheck {

    private int movements = 0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public AutoClickerA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = playerData.getCps().get() > 6.5
                    && movements < 4 && !playerData.getActionManager().getDigging().get() && !playerData.getActionManager().getPlacing().get();

            // If the movement are not incredibly low and the player isn't digging
            if (valid) samples.add(movements);

            if (samples.size() == 20) {
                // Get the outliers properly from the math utility
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                // Get the deviation from the math utility and the outliers
                final double deviation = MathUtil.getStandardDeviation(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();

                // Low deviation and low outliers
                if (deviation < 2.d && outliers < 2) fail();

                // Clear the list
                samples.clear();
            }

            // Reset the movements
            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }
}
