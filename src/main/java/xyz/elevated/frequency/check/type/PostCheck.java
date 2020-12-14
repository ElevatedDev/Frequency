package xyz.elevated.frequency.check.type;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.PacketWrapper;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInUseEntity;

public class PostCheck extends PacketCheck {
    private final Class<? extends PacketWrapper> packet;
    private boolean sent = false;

    public long lastFlying, lastPacket;
    public double buffer = 0.0;

    public PostCheck(final PlayerData playerData, final Class<? extends PacketWrapper> packet) {
        super(playerData);

        this.packet = packet;
    }

    @Override
    public void process(final Object object) {

    }

    // Flag only when its both a post and a flag
    public boolean isPost(final Object object) {
        if (object.getClass() == WrappedPlayInFlying.class) {
            final boolean empty = !sent;

            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            /*
             * What we're trying to do here is basically validate if the packet set was
             * sent before or after the flying packet. The flying packet is always the last packet that
             * gets sent, meaning if any packet is sent after it just being sent, or right when it's sent,
             * that packet's order has been messed with in some way which should not happen because of TCP.
             */
            post: {
                if (empty) break post;

                /*
                 * We're checking the distance between the flying packet and the last sent packet
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
                 * packet entry was issued from the player. If we don't reset this the check will run every
                 * flying packet which we don't want since the player is not always attacking or sending a validated packet.
                 */
                sent = false;
            }

            this.lastFlying = now;
        } else if (object.getClass() == packet) {
            /*
             * We're validating the distance between the flying packet and the attack to make sure the
             * player was really close to the last tick issued upon his attack. If he wasn't we can decrease
             * the buffer, but if it was we can process the packet and it's timestamp to be issued in the tick.
             */
            validate: {
                if (packet == null) break validate;

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

        return false;
    }
}
