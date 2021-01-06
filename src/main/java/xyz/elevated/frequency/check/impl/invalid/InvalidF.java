package xyz.elevated.frequency.check.impl.invalid;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.MathUtil;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Invalid (F)")
public final class InvalidF extends PositionCheck {

    public InvalidF(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final Player player = playerData.getBukkitPlayer();
        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);

        final double deltaY = to.getY() - from.getY();

        final boolean step = deltaY % 0.015625 == 0.0 && from.getY() % 0.015625 == 0.0;

        final double modifierJump = MathUtil.getPotionLevel(player, PotionEffectType.JUMP) * 0.1F;
        final double expectedJumpMotion = 0.42F + modifierJump;

        final boolean onGround = entityPlayer.onGround;

        final boolean exempt = this.isExempt(ExemptType.VELOCITY, ExemptType.TELEPORTING);
        final boolean invalid = deltaY > expectedJumpMotion && !onGround && !step;

        if (invalid && !exempt) {
            fail();
        }

        if (step && deltaY > 0.6F && !exempt) {
            fail();
        }
    }
}
