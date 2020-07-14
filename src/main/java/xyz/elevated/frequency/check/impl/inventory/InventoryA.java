package xyz.elevated.frequency.check.impl.inventory;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInWindowClick;

import java.util.Deque;
import java.util.List;

@CheckData(name = "Inventory (A)")
public final class InventoryA extends PacketCheck {

    private int movements = 0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public InventoryA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInWindowClick) {
            final boolean valid = movements < 5 && !this.isExempt(ExemptType.TELEPORTING);

            // Make sure the player has not teleported and the movements are below 5
            if (valid) samples.add(movements);

            // Once the sample size is 5
            if (samples.size() == 5) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                // Get the outliers and the deviation from the math utility
                final double deviation = MathUtil.getStandardDeviation(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();

                // If the deviation is low and there are no outliers, flag
                if (deviation < 1.f && outliers == 0.0) fail();

                samples.clear();
            }

            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }
}
