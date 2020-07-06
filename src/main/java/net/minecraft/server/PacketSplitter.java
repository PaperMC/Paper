package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {

    private final byte[] lenBuf = new byte[3]; // Paper
    public PacketSplitter() {}

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        // Paper start - if channel is not active just discard the packet
        if (!channelhandlercontext.channel().isActive()) {
            bytebuf.skipBytes(bytebuf.readableBytes());
            return;
        }
        // Paper end
        bytebuf.markReaderIndex();
        // Paper start - reuse temporary length buffer
        byte[] abyte = lenBuf;
        java.util.Arrays.fill(abyte, (byte) 0);
        // Paper end

        for (int i = 0; i < abyte.length; ++i) {
            if (!bytebuf.isReadable()) {
                bytebuf.resetReaderIndex();
                return;
            }

            abyte[i] = bytebuf.readByte();
            if (abyte[i] >= 0) {
                PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(abyte));

                try {
                    int j = packetdataserializer.i();

                    if (bytebuf.readableBytes() >= j) {
                        list.add(bytebuf.readBytes(j));
                        return;
                    }

                    bytebuf.resetReaderIndex();
                } finally {
                    packetdataserializer.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
