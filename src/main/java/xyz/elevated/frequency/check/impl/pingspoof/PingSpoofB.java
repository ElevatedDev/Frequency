package xyz.elevated.frequency.check.impl.pingspoof;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "PingSpoof (B)")
public final class PingSpoofB extends PacketCheck {

    public PingSpoofB(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        final boolean flying = object instanceof WrappedPlayInFlying;

        if (flying) {
            final boolean joined = playerData.getTicks().get() - playerData.getJoined().get() < 10;
            final boolean exempt = this.isExempt(ExemptType.LAGGING, ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.CHUNK);

            /*
            * Likewise to the first check, we're checking the keepalive and the transaction had different wrong delays.
            * Since the keepalive is an async packet it sometimes will arrive in odd times, meaning we need to increase
            * it's window to 100ms instead of 50ms to decrease the margin of error. If we don't there may be occasional falses.
             */
            validate: {
                if (exempt || joined) break validate;

                /*
                 * We're getting the delays straight from the PlayerData since we're already computing them
                 * ourselves every server tick already. We don't need to do any computing again.
                 */
                final long transactionPing = playerData.getTransactionPing().get();
                final long keepAlivePing = playerData.getKeepAlivePing().get();

                /*
                 * As mentioned above, we're checking if the delay between the transaction and the keepalive
                 * was higher than two whole ticks. This should almost never really happen, and if it does it usually
                 * only happens for a single entry so we technically don't even need a buffer.
                 */
                if (keepAlivePing > transactionPing && transactionPing - keepAlivePing > 100L) fail();
            }
        }
    }
}
