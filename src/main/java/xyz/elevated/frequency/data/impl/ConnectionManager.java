package xyz.elevated.frequency.data.impl;

import lombok.RequiredArgsConstructor;
import xyz.elevated.frequency.data.PlayerData;

import java.util.Map;

@RequiredArgsConstructor
public class ConnectionManager {
    private final PlayerData playerData;

    public void onTransaction(final short actionNumber, final long now) {
        final Map<Short, Long> transactions = playerData.getTransactionUpdates();

        transactions.computeIfPresent(actionNumber, (action, timestamp) -> {
            final long delay = now - timestamp;

            playerData.getTransactionPing().set(delay);
            return timestamp;
        });
    }

    public void onKeepAlive(final int identification, final long now) {
        final Map<Integer, Long> keepAlives = playerData.getKeepAliveUpdates();

        keepAlives.computeIfPresent(identification, (id, timestamp) -> {
            final long delay = now - timestamp;

            playerData.getKeepAlivePing().set(delay);
            return timestamp;
        });
    }
}
