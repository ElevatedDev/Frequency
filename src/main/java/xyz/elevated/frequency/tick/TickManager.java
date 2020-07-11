package xyz.elevated.frequency.tick;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import xyz.elevated.frequency.FrequencyAPI;
import xyz.elevated.frequency.FrequencyPlugin;
import xyz.elevated.frequency.util.NmsUtil;
import xyz.elevated.frequency.util.Pair;

public class TickManager implements Runnable {

    private int ticks;
    private static BukkitTask task;

    public void start() {
        //Ensuring that there is no previous task running to prevent memory leaks.
        assert task == null : "TickProcessor has already been started";

        task = Bukkit.getScheduler().runTaskTimer(FrequencyAPI.INSTANCE.getPlugin(), this, 0L, 1L);
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

        //We use parallel in this instance since we could be looping through many players at once.
        FrequencyAPI.INSTANCE.getPlayerDataManager().getUniversalData().parallelStream()
                .forEach(data -> {
                    Entity target = data.getTarget().get();
                    if(target != null) {
                        AxisAlignedBB aabb = NmsUtil.getEntity(target).getBoundingBox();
                        data.getTargetLocations().add(new Pair<>(aabb, ticks));
                    }
                });
    }

    public int getTicks() {
        return ticks;
    }
}
