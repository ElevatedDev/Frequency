package xyz.elevated.frequency.check.impl.timer;

import org.bukkit.Bukkit;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.EvictingList;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.MovingStats;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Timer")
public final class Timer extends PacketCheck {
    private long lastFlying = 0L;
    private final MovingStats movingStats = new MovingStats(20);
    private int streak = 0;

    public Timer(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final long now = System.currentTimeMillis();
            final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.LAGGING);

            if (!exempt) {
                movingStats.add(now - lastFlying);
            }

            final double threshold = 7.07;
            final double deviation = movingStats.getStdDev(threshold);

            if (deviation < threshold && !Double.isNaN(deviation)) {
                if (++streak > 7) {
                    fail();
                }
            } else {
                streak = 0;
            }

            this.lastFlying = now;
        }
    }
}
