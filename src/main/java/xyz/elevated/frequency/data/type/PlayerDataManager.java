package xyz.elevated.frequency.data.type;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import xyz.elevated.frequency.data.PlayerData;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = Maps.newConcurrentMap();

    public PlayerData getData(final Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(player));
    }

    public PlayerData remove(final Player player) {
        final UUID uuid = player.getUniqueId();

        return playerDataMap.remove(uuid);
    }

    public Collection<PlayerData> getEntries() {
        return playerDataMap.values();
    }
}
