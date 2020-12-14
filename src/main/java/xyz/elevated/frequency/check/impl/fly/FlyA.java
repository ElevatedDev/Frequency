package xyz.elevated.frequency.check.impl.fly;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.util.NmsUtil;

@CheckData(name = "Fly (A)")
public final class FlyA extends PositionCheck {

    private double buffer = 0.0d;

    public FlyA(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final PositionUpdate positionUpdate) {
        // Get the locations from the position update
        final Location from = positionUpdate.getFrom();
        final Location to = positionUpdate.getTo();

        // Get the entity player from the nms util
        final EntityPlayer entityPlayer = NmsUtil.getEntityPlayer(playerData);

        // If the posY of the player is modulo by (1/64) he's on ground.
        final boolean clientGround = entityPlayer.onGround;
        final boolean serverGround = to.getY() % 0.015625 == 0.0 && from.getY() % 0.015625 == 0.0;

        final boolean illegal = playerData.getPositionManager().getTouchingClimbable().get() || playerData.getPositionManager().getTouchingLiquid().get();

        if (!illegal && clientGround != serverGround) {
            if (++buffer > 4) fail();
        } else {
            buffer = 0;
        }
    }
}
