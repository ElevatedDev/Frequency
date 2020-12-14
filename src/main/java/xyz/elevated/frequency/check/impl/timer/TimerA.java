package xyz.elevated.frequency.check.impl.timer;

import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.MovingStats;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Timer (A)")
public final class TimerA extends PacketCheck {
    private final MovingStats movingStats = new MovingStats(20);

    private long lastFlying = 0L;
    private long allowance = 0;

    public TimerA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        final boolean flying = object instanceof WrappedPlayInFlying;

        if (flying) {
            // Get the server ticks and the current time for the check processing
            final int serverTicks = Frequency.INSTANCE.getTickProcessor().getTicks();
            final long now = System.currentTimeMillis();

            /*
            * We want to make sure the player is not exempt from any of the actions below.
            * Additionally, we want to ensure that the player is not lagged out and accepted a keepalive.
            * Thankfully, we shouldn't have any bypasses because of this because of PingSpoof.
             */
            final boolean exempt = this.isExempt(ExemptType.TPS, ExemptType.TELEPORTING, ExemptType.LAGGING);
            final boolean accepted = playerData.getConnectionManager().getKeepAliveTime(serverTicks).isPresent();

            /*
            * Basic theory is that the player is going to deviate through 50ms without many changes to that
            * basic range of [48, 50]. So we're running a deviation check to check how much the player has deviated
            * from the basic limit of 50ms (square-root of 50 is 7.07). We're also making sure the player is actually
            * increasing speed through an allowance that accounts for the tick expected versus the tick received.
             */
            handle: {
                if (exempt || !accepted) break handle;

                // Get the delay from the current and the last flying packet
                final long delay = now - lastFlying;

                // Add the delay to the MovingStats to analyze our input
                movingStats.add(delay);

                /*
                * As mentioned above, the basic threshold of the player is the square-root of 50. Because
                * the expected tick should always be 50ms. To check how much the player has deviated from that
                * single point, we're using it as a threshold value in the deviation check below.
                 */
                final double threshold = 7.07;
                final double deviation = movingStats.getStdDev(threshold);

                // We're making sure the deviation check processed and the deviation is smaller than the threshold.
                if (deviation < threshold && !Double.isNaN(deviation)) {
                    /*
                    * I am sure many of you have seen this before, and it's essentially the most basic version of
                    * a balance / allowance check. We're increasing the allowance with the expected amount of 50,
                    * and we're subtracting from it the actual delay received. This will act as our "buffer" system.
                     */
                    allowance += 50;
                    allowance = Math.min(allowance - delay, 0);

                    // Our previous threshold can act as one here too.
                    if (allowance > Math.floor(threshold)) fail();
                }

                else {
                    allowance = 0;
                }
            }

            this.lastFlying = now;
        }
    }
}
