package xyz.elevated.frequency.check.impl.pingspoof;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
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
            final long keepAlivePing = playerData.getPing().get();

            if (keepAlivePing > transactionPing && Math.abs(keepAlivePing - transactionPing) > 100L) fail();
        }
    }
}
