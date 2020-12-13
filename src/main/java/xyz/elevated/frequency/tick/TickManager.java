package xyz.elevated.frequency.tick;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.util.NmsUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInTransaction;

@Getter
public class TickManager implements Runnable {

    private int ticks;
    private static BukkitTask task;

    public void start() {
        //Ensuring that there is no previous task running to prevent memory leaks.
        assert task == null : "TickProcessor has already been started";

        task = Bukkit.getScheduler().runTaskTimer(Frequency.INSTANCE.getPlugin(), this, 0L, 1L);
    }

    public void stop() {
        //Ensuring that the task isn't already stopped to prevent any errors.
        if(task == null) return;

        task.cancel();
        task = null;
    }

    @Override
    public void run() {
        ticks++;

        Frequency.INSTANCE.getPlayerDataManager().getEntries().parallelStream().forEach(playerData -> {
            final Entity target = playerData.getTarget().get();

            attack: {
                if (target == null) break attack;

                /*
                * For the reach check to work on every entity, we need to set its boundind box as is
                * from the game. To avoid needing to do this constantly for the last target we're only doing it when
                * there has been someone attacked and not when the target is null.
                 */
                final AxisAlignedBB boundingBox = NmsUtil.getEntity(target).getBoundingBox();

                // Set the bounding box in the targetLocations storage along with the time.
                playerData.getTargetLocations().add(new Pair<>(boundingBox, ticks));
            }

            transaction: {
                if (ticks == 1) break transaction;

                /*
                * We're using a timestamp to get a more dynamic time of when we sent the packet to the player.
                * This can be used later on for a pingspoof check or for getting the player's ping with ease on response.
                 */
                final long timestamp = System.currentTimeMillis();

                /*
                * The identification used by the transaction packet should always 0 or a negative
                * number to not mess up with actual inventory transactions. The way we're doing this here is fine
                * and we should not be having any issues regarding the game mechanics.
                 */
                final int identification = 0;
                final short actionNumber = (short) (Short.MAX_VALUE % ticks);

                /*
                * We're setting in the identification and the action number we're about to send, plus ensuring
                * that the packet is received as an unaccepted once because of the false boolean in the end.
                 */
                final PacketPlayOutTransaction transaction =
                        new PacketPlayOutTransaction(identification, actionNumber, false);

                /*
                * We're sending the packet directly into the player's pipeline to avoid any
                * problems in the future. We don't need to touch the actual anticheat pipeline.
                * We also need to store the values within the packets sent to the player.
                 */
                playerData.getConnection().sendPacket(transaction);
                playerData.getTransactionUpdates().put(actionNumber, timestamp);
            }

            keepalive: {
                if (ticks == 1) break keepalive;

                /*
                 * Like transaction, We're using a timestamp to get a more dynamic time of when we sent the packet to the player.
                 * This can be used later on for a pingspoof check or for getting the player's ping with ease on response.
                 */
                final long timestamp = System.currentTimeMillis();

                /*
                * We don't need to do any fancy randomization for keepalives since they're much simpler
                * as operations in comparison to keepalives, and no matter how many of them we send there won't be
                * and collisions with the actual game functionality.
                 */
                final int identification = ticks;

                /*
                * We're creating the keepalive with the tick variable inside which is going to be used
                * for is verification later on. Other than that everything can be left as is for keepalive.
                 */
                final PacketPlayOutKeepAlive keepAlive = new PacketPlayOutKeepAlive(identification);

                /*
                * Similarly to transactions, we can simply send the packet directly onto the player's pipeline
                * and not have to do any fancy work with the anticheat pipeline. We also need to store the values
                * within the packets sent to the player.
                 */
                playerData.getConnection().sendPacket(keepAlive);
                playerData.getKeepAliveUpdates().put(identification, timestamp);
            }
        });
    }
}
