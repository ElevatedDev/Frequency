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
        if (object instanceof WrappedPlayInFlying) {
            final long transactionPing = playerData.getTransactionPing().get();
            final long keepAlivePing = playerData.getKeepAlivePing().get();

            final boolean joined = playerData.getTicks().get() - playerData.getJoined().get() < 10;
            final boolean exempt = this.isExempt(ExemptType.LAGGING, ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.CHUNK);

            if (!exempt && !joined && keepAlivePing > transactionPing && Math.abs(keepAlivePing - transactionPing) > 50L) fail();
        }
    }
}
