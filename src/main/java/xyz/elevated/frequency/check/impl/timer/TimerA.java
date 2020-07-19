package xyz.elevated.frequency.check.impl.timer;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.MovingStats;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Timer (A)")
public final class TimerA extends PacketCheck {

    private long lastFlying = 0L;
    private final MovingStats movingStats = new MovingStats(20);
    private int streak = 0;

    public TimerA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final long now = System.currentTimeMillis();
            final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.LAGGING);

            sample: {
                if (exempt) break sample;

                movingStats.add(now - lastFlying);
            }

            analyze: {
                final double threshold = 7.07;
                final double deviation = movingStats.getStdDev(threshold);

                if (deviation >= threshold || Double.isNaN(deviation)) {
                    streak = 0;

                    break analyze;
                }

                if (++streak > 7) {
                    fail();
                }
            }

            this.lastFlying = now;
        }
    }
}
