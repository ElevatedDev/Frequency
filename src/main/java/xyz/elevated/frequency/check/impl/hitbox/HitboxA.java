package xyz.elevated.frequency.check.impl.hitbox;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "Hitbox (A)")
public final class HitboxA extends PacketCheck {
    private double buffer = 0.0;

    public HitboxA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(Object object) {
        if(object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;

            final Entity target = playerData.getTarget().get();

            if(!(target instanceof LivingEntity)
                    || playerData.getTargetLocations().size() < 30) return;

            if (wrapper.getAction() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK
                    || playerData.getBukkitPlayer().getGameMode() == GameMode.CREATIVE) return;

            final int now = Frequency.INSTANCE.getTickManager().getTicks();
            final int ping = MathUtil.getPingInTicks(playerData.getKeepAlivePing().get()) + 3;

            final Vector origin = playerData.getPositionUpdate().get().getTo().toVector();

            final double distance = playerData.getTargetLocations().stream()
                    .filter(pair -> Math.abs(now - pair.getY() - ping) < 2)
                    .mapToDouble(pair -> {
                        final AxisAlignedBB box = pair.getX();

                        final double widthX = Math.abs(box.a - box.d) / 2;
                        final double widthZ = Math.abs(box.c - box.f) / 2;

                        final Vector loc = new Vector(box.a + widthX, 0, box.c + widthZ);

                        return origin.setY(0).distance(loc) - MathUtil.magnitude(widthX, widthZ) - .1f;
                    }).min().orElse(-1);

            if (distance > 3) {
                buffer += 1.5;

                if (buffer > 3) {
                    fail();
                }
            } else {
                buffer = Math.max(buffer - 0.75, 0);
            }
        }
    }
}
