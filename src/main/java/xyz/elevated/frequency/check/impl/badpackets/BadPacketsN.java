package xyz.elevated.frequency.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInArmAnimation;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "BadPackets (N)")
public class BadPacketsN extends PacketCheck {

    private boolean swung;

    public BadPacketsN(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;

            check: {
                if (wrapper.getAction() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) break check;

                /*
                 * This ensures the player is swinging before they send an attack packet. This will detect any
                 * combat checks that mess with the packet order such as criticals.
                 */

                if (!swung) fail();
            }
        }

        else if (object instanceof WrappedPlayInArmAnimation) {
            swung = true;
        }

        else if (object instanceof WrappedPlayInFlying) {
            swung = false;
        }
    }
}
