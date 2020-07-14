package xyz.elevated.frequency.check.impl.inventory;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInClientCommand;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

@CheckData(name = "Inventory (B)", threshold = 3)
public final class InventoryB extends PacketCheck {

    private boolean inventory = false;

    public InventoryB(final PlayerData playerData) {
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

            if (inventory) {
                final boolean attacking = playerData.getActionManager().getAttacking().get();
                final boolean swinging = playerData.getActionManager().getSwinging().get();

                if (attacking || swinging) {
                    fail();
                }
            }
        } else if (object instanceof WrappedPlayInFlying) {
            inventory = false;
        }
    }
}
