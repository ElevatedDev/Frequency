package xyz.elevated.frequency.check.impl.hitbox;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import xyz.elevated.frequency.FrequencyAPI;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.tick.TickProcessor;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.Pair;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

import java.util.List;
import java.util.stream.Collectors;

@CheckData(name = "Hitbox (A)")
public final class HitboxA extends PacketCheck {

    public HitboxA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(Object object) {
        if(object instanceof WrappedPlayInUseEntity) {
            Entity target = playerData.getTarget().get();

            if(!(target instanceof LivingEntity)
                    || playerData.getTargetLocations().size() < 30) return;

            int now = FrequencyAPI.INSTANCE.getTickProcessor().getTicks();
            int ping = MathUtil.getPingInTicks(playerData.getPing().get()) + 3;

            Vector origin = playerData.getPositionUpdate().getTo().toVector();

            double distance = playerData.getTargetLocations().stream()
                    .filter(pair -> Math.abs(now - pair.getY() - ping) < 2)
                    .mapToDouble(pair -> {
                        AxisAlignedBB aabb = pair.getX();

                        double widthX = Math.abs(aabb.a - aabb.d) / 2, widthZ = Math.abs(aabb.c - aabb.f) / 2;

                        Vector loc = new Vector(aabb.a + widthX, 0, aabb.c + widthZ);

                        return origin.setY(0).distance(loc) - Math.hypot(widthX, widthZ) - .1f;
                    }).min().orElse(-1);

            if(distance > 3) {
                fail();
            }
        }
    }
}
