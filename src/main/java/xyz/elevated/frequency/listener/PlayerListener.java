package xyz.elevated.frequency.listener;

import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.packet.PacketHandler;
import xyz.elevated.frequency.util.NmsUtil;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = Frequency.INSTANCE.getPlayerDataManager().getData(player);

        final ChannelPipeline channelPipeline = NmsUtil.getPlayerPipeline(player);

        final int now = playerData.getTicks().get();

        playerData.getJoined().set(now);
        Frequency.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.addBefore("packet_handler", "frequency_packet_handler", new PacketHandler(playerData)));
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = Frequency.INSTANCE.getPlayerDataManager().getData(player);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) playerData.getActionManager().onBukkitDig();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final PlayerData playerData = Frequency.INSTANCE.getPlayerDataManager().getData(player);

        final ChannelPipeline channelPipeline = NmsUtil.getPlayerPipeline(player);

        if (channelPipeline.get("frequency_packet_handler") != null) {
            Frequency.INSTANCE.getExecutorPacket().execute(() -> channelPipeline.remove("frequency_packet_handler"));
        }

        Frequency.INSTANCE.getPlayerDataManager().remove(player);
    }
}
