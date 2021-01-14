package xyz.elevated.frequency.check.impl.autoclicker;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@CheckData(name = "AutoClicker (E)")
public final class AutoClickerE extends PacketCheck {

    private int movements = 0;
    private final Deque<Integer> samples = new LinkedList<>();

    public AutoClickerE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = playerData.getCps().get() > 6.5 &&
                    movements < 5 && !playerData.getActionManager().getDigging().get() && !playerData.getActionManager().getPlacing().get();
            
            if (valid) samples.add(movements);

            if (samples.size() == 20) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final int outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final int duplicates = MathUtil.getDuplicates(samples);

                // Impossible consistency
                if (outliers < 2 && duplicates > 15) fail();

                samples.clear();
            }

            movements = 0;
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }
}
