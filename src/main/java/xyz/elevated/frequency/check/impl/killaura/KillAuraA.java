package xyz.elevated.frequency.check.impl.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PostCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

@CheckData(name = "KillAura (A)")
public final class KillAuraA extends PostCheck {

    private boolean sent = false;
    private long lastFlying = 0L, lastPacket = 0L;
    private double buffer = 0.0d;

    public KillAuraA(final PlayerData playerData) {
        super(playerData, WrappedPlayInUseEntity.class);
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final boolean empty = !sent;

            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            /*
            * Essentially what we're doing here is trying to ensure that the packet got sent in the right
            * order in comparison to the flying packet which is always sent in the end of the tick. If the
            * PacketPlayInUseEntity packet is sent after the tick, the player's order is wrong. This is normally
            * impossible since TCP always ensures the packets come in the right order.
             */
            post: {
                if (empty) break post;

                /*
                * We're checking the distance between the flying packet and the last attack packet
                * that was a valid entry. If the difference in time is outside of the range we desire,
                * we're flagging the player. If not, we're decreasing the buffer.
                 */
                if (delay > 40L && delay < 100L) {
                    buffer += 0.25;

                    if (buffer > 0.5) fail();
                } else {
                    buffer = Math.max(buffer - 0.025, 0);
                }

                /*
                * We need to make sure we reset the sent variable as it signifies that a valid
                * attack entry was issued from the player. If we don't reset this the check will run every
                * flying packet which we don't want since the player is not always attacking or sending a validated packet.
                 */
                sent = false;
            }

            this.lastFlying = now;
        } else if (object instanceof WrappedPlayInUseEntity) {
            final WrappedPlayInUseEntity wrapper = (WrappedPlayInUseEntity) object;

            /*
            * We're validating the distance between the flying packet and the attack to make sure the
            * player was really close to the last tick issued upon his attack. If he wasn't we can decrease
            * the buffer, but if it was we can process the packet and it's timestamp to be issued in the tick.
             */
            validate: {
                if (wrapper.getAction() != EnumEntityUseAction.ATTACK) break validate;

                /*
                * We're validating the packet through checking how close by the packet was to the
                * end of the tick. We're doing this by checking the current time versus the last
                * tick time. This is not optimal but it's fine for general use.
                 */
                final long now = System.currentTimeMillis();
                final long delay = now - lastFlying;

                /*
                * Technically, an invalid timestamp would be any that is lower than 50ms, but
                * when dealing with timestamps, which is not optimal, we have a larger margin of error,
                * thus, we're running it through 10ms to make sure the check is functional.
                 */
                if (delay < 10L) {
                    lastPacket = now;
                    sent = true;
                } else {
                    buffer = Math.max(buffer - 0.025, 0.0);
                }
            }
        }
    }
}
