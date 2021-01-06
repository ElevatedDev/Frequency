package xyz.elevated.frequency.check.impl.badpackets;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInSteerVehicle;

import java.util.Arrays;

@CheckData(name = "BadPackets (K)")
public final class BadPacketsK extends PacketCheck {

    public BadPacketsK(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInSteerVehicle) {
            final boolean exempt = isExempt(ExemptType.VEHICLE);

            if (exempt) {
                fail();
            }
        }
    }
}
