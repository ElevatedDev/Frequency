package xyz.elevated.frequency.check.impl.autoclicker;

import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "AutoClicker (D)")
public final class AutoClickerD extends PacketCheck {

    private int movements = 0, clicks = 0;

    public AutoClickerD(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInArmAnimation) {
            final boolean valid = movements < 100 && !playerData.getActionManager().getDigging().get();

            // If the player has clicked recently and the player isn't digging
            if (valid) ++clicks;

            // 20 movements = 1 second
            if (movements == 20) {
                final boolean flag = clicks > 20;

                // Sent an extra swing in a tick
                if (flag) fail();

                // Reset the movements
                movements = 0;
            }
        } else if (object instanceof WrappedPlayInFlying) {
            ++movements;
        }
    }
}
