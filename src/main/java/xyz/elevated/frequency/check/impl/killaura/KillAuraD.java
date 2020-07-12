package xyz.elevated.frequency.check.impl.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PacketCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "KillAura (D)")
public final class KillAuraD extends PacketCheck {

    private int streak = 0;

    public KillAuraD(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;

            if (wrapper.getAction() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final boolean invalid = !playerData.getActionManager().getSwinging().get();

                // Player swung and attacked
                if (invalid) {
                    if (++streak > 2) {
                        fail();
                    }
                } else {
                    streak = 0;
                }
            }
        }
    }
}
