package xyz.elevated.frequency.check.type;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.wrapper.PacketWrapper;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInFlying;

public class PostCheck extends PacketCheck {
    private final Class<? extends PacketWrapper> packet;
    private boolean flag = false;
    private boolean post = false;
    private boolean sent = false;

    private long lastFlying, lastPacket;
    private double buffer = 0.0;

    public PostCheck(final PlayerData playerData, final Class<? extends PacketWrapper> packet) {
        super(playerData);

        this.packet = packet;
    }

    @Override
    public void process(final Object object) {
        if (object instanceof WrappedPlayInFlying) {
            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            if (sent) {
                if (delay > 40L && delay < 100L) {
                    post = true;
                    buffer += 0.25;

                    if (buffer > 0.5) {
                        flag = true;
                    }
                } else {
                    buffer = Math.max(buffer - 0.025, 0);
                    post = false;
                }

                sent = false;
            }

            this.lastFlying = now;
        } else if (object.getClass() == packet) {
            final long now = System.currentTimeMillis();

            if (now - lastFlying < 10L) {
                lastPacket = now;
                sent = true;
            } else {
                buffer = Math.max(buffer - 0.025, 0.0);
            }
        }
    }

    // Flag only when its both a post and a flag
    public boolean isPost() {
        return flag && post;
    }
}
