package xyz.elevated.frequency.data.impl;

import lombok.RequiredArgsConstructor;
import xyz.elevated.frequency.data.PlayerData;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class ConnectionManager {
    private final PlayerData playerData;

    public void onTransaction(final short actionNumber, final long now) {
        final Optional<Long> entry = this.getTransactionTime(actionNumber);

        entry.ifPresent(time -> playerData.getTransactionPing().set(now - time));
    }

    public void onKeepAlive(final int identification, final long now) {
        final Optional<Long> entry = this.getKeepAliveTime(identification);

        entry.ifPresent(time -> playerData.getKeepAlivePing().set(now - time));
    }

    public Optional<Long> getTransactionTime(final short actionNumber) {
        final Map<Short, Long> entries = playerData.getTransactionUpdates();

        if (entries.containsKey(actionNumber)) return Optional.of(entries.get(actionNumber));

        return Optional.empty();
    }

    public Optional<Long> getKeepAliveTime(final int identification) {
        final Map<Integer, Long> entries = playerData.getKeepAliveUpdates();

        if (entries.containsKey(identification)) return Optional.of(entries.get(identification));

        return Optional.empty();
    }
}
