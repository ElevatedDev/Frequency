package xyz.elevated.frequency.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class PlayerUtil {

    /**
     * Bukkit's getNearbyEntities method looks for all entities in all chunks
     * This is a lighter method and can also be used Asynchronously since we won't load any chunks
     *
     * @param location The location to scan for nearby entities
     * @param radius   The radius to expand
     * @return The entities within that radius
     * @author Nik
     */
    public List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {

        final double divider = 16.0D;

        final double locationX = location.getX();
        final double locationZ = location.getZ();

        final int minX = (int) Math.floor((locationX - radius) / divider);
        final int maxX = (int) Math.floor((locationX + radius) / divider);

        final int minZ = (int) Math.floor((locationZ - radius) / divider);
        final int maxZ = (int) Math.floor((locationZ + radius) / divider);

        final World world = location.getWorld();

        List<Entity> entities = new LinkedList<>();

        for (int x = minX; x <= maxX; x++) {

            for (int z = minZ; z <= maxZ; z++) {

                if (!world.isChunkLoaded(x, z)) continue;

                for (Entity entity : world.getChunkAt(x, z).getEntities()) {

                    if (entity == null) continue;

                    if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}