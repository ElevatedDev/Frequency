package xyz.elevated.frequency.check.impl.invalidposition;

import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.MathUtil;

@CheckData(name = "InvalidPosition")
public final class InvalidPosition extends PositionCheck {

    private double lastHorizontalDistance = 0.0d, buffer = 0.0d;

    public InvalidPosition(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        final double horizontalDistance = MathUtil.magnitude(deltaX, deltaZ);
        final double acceleration = Math.abs(horizontalDistance - lastHorizontalDistance);

        final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.VELOCITY);
        final boolean sprinting = playerData.getSprinting().get();

        if (exempt || !sprinting) return;

        if (acceleration > 0.3) {
            buffer += 0.5;

            if (buffer > 1.5) {
                fail();

                buffer = 0;
            }
        } else {
            buffer = Math.max(buffer - 0.125, 0);
        }

        // Its impossible to make that small of a movement without it being rounded to 0
        if (deltaY >= 0.0 && horizontalDistance < 1e-06 && acceleration == 0.0) {
            fail();
        }

        this.lastHorizontalDistance = horizontalDistance;
    }
}
