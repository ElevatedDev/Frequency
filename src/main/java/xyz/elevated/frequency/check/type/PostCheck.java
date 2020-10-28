package xyz.elevated.frequency.check.type;

import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.PacketWrapper;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

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
            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            if (sent) {
                if (delay > 40L && delay < 100L) {
                    buffer += 0.25;

                    if (buffer > 0.5) {
                        return true;
                    }
                } else {
                    buffer = Math.max(buffer - 0.025, 0);
                }

                sent = false;
            }

            this.lastFlying = now;
        } else if (object.getClass() == packet) {
            final long now = System.currentTimeMillis();
            final long delay = now - lastFlying;

            if (delay < 10L) {
                lastPacket = now;
                sent = true;
            } else {
                buffer = Math.max(buffer - 0.025, 0.0);
            }
        }

        return false;
    }
}
