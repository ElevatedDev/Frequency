package xyz.elevated.frequency.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class PlayerUtil {

    /**
     * Bukkit's getNearbyEntities method looks for all entities in all chunks
     * This is a much lighter method and can also be used Asynchronously since we won't load any chunks
     *
     * @param location The location to scan for nearby entities
     * @param radius   The radius to expand
     * @return The entities within that radius
     * @author Nik
     */
    public List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {

        final double expander = 16.0D;

        final double x = location.getX();
        final double z = location.getZ();

        final int minX = (int) Math.floor((x - radius) / expander);
        final int maxX = (int) Math.floor((x + radius) / expander);

        final int minZ = (int) Math.floor((z - radius) / expander);
        final int maxZ = (int) Math.floor((z + radius) / expander);

        final World world = location.getWorld();

        List<Entity> entities = new ArrayList<>();

        for (int xVal = minX; xVal <= maxX; xVal++) {
            for (int zVal = minZ; zVal <= maxZ; zVal++) {
                if (world.isChunkLoaded(xVal, zVal)) {
                    entities.addAll(Arrays.asList(world.getChunkAt(xVal, zVal).getEntities()));
                }
            }
        }

        entities.removeIf(entity -> entity.getLocation().distanceSquared(location) > radius * radius);

        return entities;
    }
}