package xyz.elevated.frequency.check.impl.fly;

import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.update.PositionUpdate;

@CheckData(name = "Fly (C)")
public final class FlyC extends PositionCheck {

    private double lastDeltaY = 0.0d, buffer = 0.0d;
    private int ticks = 0;

    public FlyC(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        final double deltaX = to.getX() - from.getX();
        final double deltaY = to.getY() - from.getY();
        final double deltaZ = to.getZ() - from.getZ();

        final double horizontalDistance = Math.hypot(deltaX, deltaZ);
        final double acceleration = Math.abs(deltaY - lastDeltaY);

        final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.VELOCITY);
        final boolean touchingAir = playerData.getPositionManager().getTouchingAir().get();

        if (!exempt && touchingAir) {
            ++ticks;

            if (ticks > 6 && horizontalDistance > 0.1 && (deltaY == 0.0 || acceleration == 0.0)) {
                buffer += 0.25;

                if (buffer > 0.75) fail();
            } else {
                buffer = Math.max(buffer - 0.12, 0.0);
            }
        } else {
            buffer = 0;
            ticks = 0;
        }

        this.lastDeltaY = deltaY;
    }
}
