package xyz.elevated.frequency.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInSteerVehicle;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "BadPackets (O)")
public class BadPacketsO extends PacketCheck {

    public BadPacketsO(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInSteerVehicle) {
            final WrappedPlayInSteerVehicle wrapper = (WrappedPlayInSteerVehicle) object;

            final float forward = Math.abs(wrapper.getForward());
            final float side = Math.abs(wrapper.getSide());

            // The max forward/side value is .98 or -.98
            final boolean invalid = side > .98F || forward > .98F;

            if (invalid) {
                fail();
            }
        }
    }
}
