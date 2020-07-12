package xyz.elevated.frequency.check.impl.hitbox;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import xyz.elevated.frequency.FrequencyAPI;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "Hitbox (A)")
public final class HitboxA extends PacketCheck {

    private long lastFail;

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

            //Added lastFail time check to ensure the player doesn't hit 2 attack packets within ~5ms to false the check
            if(distance > 3 && System.currentTimeMillis() - this.lastFail >= 50) {
                fail();
                this.lastFail = System.currentTimeMillis();
            }
        }
    }
}
