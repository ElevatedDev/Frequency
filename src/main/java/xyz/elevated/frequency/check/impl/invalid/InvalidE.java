package xyz.elevated.frequency.check.impl.invalid;

import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;

@CheckData(name = "Invalid (E)")
public final class InvalidE extends PositionCheck {
    private double lastOffsetH = 0.0;
    private int buffer = 0;

    public InvalidE(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        final double offsetH = Math.hypot(deltaX, deltaZ);
        final double offsetY = Math.abs(deltaY);

        final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.VELOCITY);
        final boolean touchingAir = playerData.getPositionManager().getTouchingAir().get();

        if (!exempt && touchingAir && offsetH > 0.005 && offsetY < 90.d) {
            double attributeSpeed = lastOffsetH * 0.91F + 0.02;

            final boolean sprinting = playerData.getSprinting().get();
            if (sprinting) attributeSpeed += 0.0063;

            if (offsetH - attributeSpeed > 1e-12 && offsetH > 0.1 && attributeSpeed > 0.075) {
                if (++buffer > 5) {
                    fail();
                }
            } else {
                buffer = 0;
            }
        }

        this.lastOffsetH = offsetH;
    }
}
