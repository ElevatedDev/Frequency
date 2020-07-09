package xyz.elevated.frequency.listener;

import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.elevated.frequency.FrequencyAPI;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.packet.PacketHandler;
import xyz.elevated.frequency.util.NmsUtil;

import java.time.Instant;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final long now = System.currentTimeMillis();

        final Player player = event.getPlayer();
        final PlayerData playerData = FrequencyAPI.INSTANCE.getPlayerDataManager().getData(player);

        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);
        final ChannelPipeline channelPipeline = entityPlayer.playerConnection.networkManager.channel.pipeline();

        playerData.getJoined().set(now);
        FrequencyAPI.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.addBefore("packet_handler", "frequency_packet_handler", new PacketHandler(playerData)));
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = FrequencyAPI.INSTANCE.getPlayerDataManager().getData(player);

        final Instant instantFirst = Instant.now();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) playerData.getActionManager().onBukkitDig();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final PlayerData playerData = FrequencyAPI.INSTANCE.getPlayerDataManager().getData(player);

        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);
        final ChannelPipeline channelPipeline = entityPlayer.playerConnection.networkManager.channel.pipeline();

        if (channelPipeline.get("frequency_packet_handler") != null) {
            FrequencyAPI.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.remove("frequency_packet_handler"));
        }

        FrequencyAPI.INSTANCE.getPlayerDataManager().remove(player);
    }
}
