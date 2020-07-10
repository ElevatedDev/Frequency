package xyz.elevated.frequency.util;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.elevated.frequency.data.PlayerData;

public final class NmsUtil {

    // We don't want to initialise a class that has every method declared as static.
    public NmsUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

    public static EntityPlayer getEntityPlayer(final Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static EntityPlayer getEntityPlayer(final PlayerData playerData) {
        final Player player = playerData.getBukkitPlayer();

        return getEntityPlayer(player);
    }

    public static Block getBlock(final Location location) {
        return isChunkLoaded(location) ? location.getBlock() : null;
    }

    public static boolean isChunkLoaded(Location loc) {
        net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();

        return !world.isClientSide && world.isLoaded(new BlockPosition(loc.getBlockX(), 0, loc.getBlockZ()));
    }

    public static Entity getEntity(org.bukkit.entity.Entity entity) {
        return ((CraftEntity)entity).getHandle();
    }

    public static WorldServer getWorld(World world) {
        return ((CraftWorld)world).getHandle();
    }

    public static Vector getMotion(final Player player) {
        final EntityPlayer entityPlayer = getEntityPlayer(player);

        final double motionX = entityPlayer.motX;
        final double motionY = entityPlayer.motY;
        final double motionZ = entityPlayer.motZ;

        return new Vector(motionX, motionY, motionZ);
    }

    public static Vector getMotion(final PlayerData playerData) {
        final Player player = playerData.getBukkitPlayer();

        return getMotion(player);
    }
}
