package xyz.elevated.frequency.check.impl.inventory;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInClientCommand;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Inventory (C)", threshold = 6)
public final class InventoryC extends PacketCheck {

    private boolean inventory = false;
    private int buffer = 0;

    public InventoryC(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInClientCommand) {
            final WrappedPlayInClientCommand wrapper = (WrappedPlayInClientCommand) object;

            achievement: {
                if (wrapper.getCommand() != PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) break achievement;

                inventory = true;
            }
        } else if (object instanceof WrappedPlayInFlying) {
            final Location from = playerData.getPositionUpdate().get().getFrom();
            final Location to = playerData.getPositionUpdate().get().getTo();

            movement: {
                final double deltaX = to.getX() - from.getX();
                final double deltaY = to.getY() - from.getY();
                final double deltaZ = to.getZ() - from.getZ();

                final boolean moving = Math.hypot(deltaX, deltaZ) > 0.15;
                final boolean negative = deltaY < 0.0;
                final boolean exempt = this.isExempt(ExemptType.TELEPORTING, ExemptType.TPS, ExemptType.VELOCITY);

                if (!moving || negative || exempt || !inventory) {
                    buffer = 0;

                    break movement;
                }

                if (++buffer > 5) {
                    fail();
                }

                Bukkit.broadcastMessage("B: " + buffer);

                inventory = false;
            }
        }
    }
}
