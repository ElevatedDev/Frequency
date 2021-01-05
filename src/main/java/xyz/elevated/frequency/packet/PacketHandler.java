package xyz.elevated.frequency.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.processor.impl.IncomingPacketProcessor;
import xyz.elevated.frequency.processor.impl.OutgoingPacketProcessor;

@RequiredArgsConstructor
public final class PacketHandler extends ChannelDuplexHandler {

    private final PlayerData playerData;

    @Override
    public void write(final ChannelHandlerContext channelHandlerContext, final Object object, final ChannelPromise channelPromise) throws Exception {
        super.write(channelHandlerContext, object, channelPromise);

        try {
            final Packet<PacketListenerPlayOut> packet = (Packet<PacketListenerPlayOut>) object;

            Frequency.INSTANCE.getProcessorManager()
                    .getProcessor(OutgoingPacketProcessor.class)
                    .process(playerData, packet);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext channelHandlerContext, final Object object) throws Exception {
        super.channelRead(channelHandlerContext, object);

        try {
            final Packet<PacketListenerPlayIn> packet = (Packet<PacketListenerPlayIn>) object;

            Frequency.INSTANCE.getProcessorManager()
                    .getProcessor(IncomingPacketProcessor.class)
                    .process(playerData, packet);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
