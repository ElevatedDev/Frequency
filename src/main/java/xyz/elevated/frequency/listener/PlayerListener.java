package xyz.elevated.frequency.listener;

import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.packet.PacketHandler;
import xyz.elevated.frequency.tick.TickManager;
import xyz.elevated.frequency.util.NmsUtil;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = Frequency.INSTANCE.getPlayerDataManager().getData(player);

        final int ticks = playerData.getTicks().get();
        final ChannelPipeline channelPipeline = NmsUtil.getPlayerPipeline(player);

        playerData.getActionManager().onTeleport();
        playerData.getJoined().set(ticks);

        Frequency.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.addBefore("packet_handler", "frequency_packet_handler", new PacketHandler(playerData)));
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = Frequency.INSTANCE.getPlayerDataManager().getData(player);

        /*
        * There's a bug with Minecraft where the START_DIGGING packet will never be sent, making it impossible
        * for us to know if the player is digging a block or not. Thankfully, the spigot itself does the raytrace
        * for us meaning we don't have to waste any performance by doing it ourselves.
         */
        block: {
            if (event.getAction() != Action.LEFT_CLICK_BLOCK) break block;

            playerData.getActionManager().onBukkitDig();
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ChannelPipeline channelPipeline = NmsUtil.getPlayerPipeline(player);

        /*
        * We need to remove the player's pipeline in the case it does not get auto-removed
        * by the channel. Some spigots who have messed with pipelines might have a bug where the
        * pipeline is never removed creating a memory leak. We're handling that here.
         */
        removal: {
            if (channelPipeline.get("frequency_packet_handler") == null) break removal;

            Frequency.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.remove("frequency_packet_handler"));
        }

        /*
        * To prevent any memory leaks from showing up, we have to remove the player from
        * the map we have inside. We don't need to keep the data inside of the memory if we have
        * no use for it anymore. This can be abused, but if needed, one can make a caching system,
         */
        Frequency.INSTANCE.getPlayerDataManager().remove(player);
    }
}
