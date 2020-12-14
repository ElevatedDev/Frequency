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

        /*
        * Essentially, the ping of the player is the delay between the server and the client.
        * We can easily get it by comparing the time it took for the client to respond to the transaction.
        * And since the receivedTime - sentTime => delay, we can get it through the entry.
         */
        entry.ifPresent(time -> playerData.getTransactionPing().set(now - time));
    }

    public void onKeepAlive(final int identification, final long now) {
        final Optional<Long> entry = this.getKeepAliveTime(identification);

        /*
        * Identically to transactions, the ping of the player is the delay between the server and the client.
        * We can easily get it like transaction, basically by checking the difference of the time sent and the
        * time received. So, receivedTime  sentTime => delay, meaning we can get it through the entry.
         */
        entry.ifPresent(time -> playerData.getKeepAlivePing().set(now - time));
    }

    /**
     * @param actionNumber - The action-number of the transaction
     * @return An Optional<Long> which contains the sent time.
     */
    public Optional<Long> getTransactionTime(final short actionNumber) {
        final Map<Short, Long> entries = playerData.getTransactionUpdates();

        /*
        * If the variable is found within the map, we can easily parse it through an
        * optional. If it's not found, then we return an empty optional. We could use the
        * direct output from entries.get() method and wrap it with an optional but that's simply unsafe
        * calling in development, thus, we're doing it safely.
         */
        if (entries.containsKey(actionNumber)) return Optional.of(entries.get(actionNumber));

        return Optional.empty();
    }

    /**
     * @param identification - The server-tick of the keepalive
     * @return An Optional<Long> which contains the sent time.
     */
    public Optional<Long> getKeepAliveTime(final int identification) {
        final Map<Integer, Long> entries = playerData.getKeepAliveUpdates();

        /*
         * Likewise to transactions, we're trying to find the variable within the map and
         * simply return an Optional.of() if found. If it's not found, we're returning an empt
         * optional. This is generally a safer practice when it comes to programming and we follow it
         * like we should.
         */
        if (entries.containsKey(identification)) return Optional.of(entries.get(identification));

        return Optional.empty();
    }
}
