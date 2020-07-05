package xyz.elevated.frequency.util;

import net.minecraft.server.v1_8_R3.EntityPlayer;
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
