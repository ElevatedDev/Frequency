package xyz.elevated.frequency.check.impl.pingspoof;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "PingSpoof (A)")
public final class PingSpoofA extends PacketCheck {

    public PingSpoofA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        final boolean flying = object instanceof WrappedPlayInFlying;

        if (flying) {
            final boolean joined = playerData.getTicks().get() - playerData.getJoined().get() < 10;
            final boolean exempt = this.isExempt(ExemptType.LAGGING, ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.CHUNK);

            /*
            * We're essentially checking if the transaction and the keepalive have different delays. This can only
            * happen if the transaction and keepalive are responded in wrong times. We're allowing a 50L window
            * since the keepalive is an async packet, meaning that it may only be delayed occasionally by a single tick.
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
                * was higher than a whole tick. This should almost never really happen, and if it does it usually
                * only happens for a single entry so we technically don't even need a buffer.
                 */
                if (transactionPing > keepAlivePing && transactionPing - keepAlivePing > 50L) fail();
            }
        }
    }
}
