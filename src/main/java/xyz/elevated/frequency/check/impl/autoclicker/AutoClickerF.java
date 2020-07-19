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
    private double buffer = 0.0d;
    private final Deque<Integer> samples = Lists.newLinkedList();

    public AutoClickerF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = movements < 4 && !playerData.getActionManager().getDigging().get();

            // If the movements is smaller than 4 and the player isn't digging
            if (valid) samples.add(movements);

            // Once the samples size is equal to 15
            if (samples.size() == 15) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                // Get the deviation outliers the the cps from the math util
                final double deviation = MathUtil.getStandardDeviation(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final double cps = playerData.getCps().get();

                // If the deviation is relatively low along with the outliers and the cps is rounded
                if (deviation < 0.3 && outliers < 2 && cps % 1.0 == 0.0) {
                    buffer += 0.25;

                    if (buffer > 0.75) {
                        fail();
                    }
                } else {
                    buffer = Math.max(buffer - 0.2, 0);
                }

                // Clear the samples
                samples.clear();
            }

            // Reset the movements
            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }

}
