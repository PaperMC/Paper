package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker b = MarkerManager.getMarker("PACKET_SENT", NetworkManager.b);
    private final EnumProtocolDirection c;

    public PacketEncoder(EnumProtocolDirection enumprotocoldirection) {
        this.c = enumprotocoldirection;
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, Packet<?> packet, ByteBuf bytebuf) throws Exception {
        EnumProtocol enumprotocol = (EnumProtocol) channelhandlercontext.channel().attr(NetworkManager.c).get();

        if (enumprotocol == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet);
        } else {
            Integer integer = enumprotocol.a(this.c, packet);

            if (PacketEncoder.LOGGER.isDebugEnabled()) {
                PacketEncoder.LOGGER.debug(PacketEncoder.b, "OUT: [{}:{}] {}", channelhandlercontext.channel().attr(NetworkManager.c).get(), integer, packet.getClass().getName());
            }

            if (integer == null) {
                throw new IOException("Can't serialize unregistered packet");
            } else {
                PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf);

                packetdataserializer.d(integer);

                try {
                    packet.b(packetdataserializer);
                } catch (Throwable throwable) {
                    PacketEncoder.LOGGER.error(throwable);
                    throwable.printStackTrace(); // Paper - WHAT WAS IT? WHO DID THIS TO YOU? WHAT DID YOU SEE?
                    if (packet.a()) {
                        throw new SkipEncodeException(throwable);
                    } else {
                        throw throwable;
                    }
                }

                // Paper start
                int packetLength = bytebuf.readableBytes();
                if (packetLength > MAX_PACKET_SIZE) {
                    throw new PacketTooLargeException(packet, packetLength);
                }
                // Paper end
            }
        }
    }

    // Paper start
    private static int MAX_PACKET_SIZE = 2097152;

    public static class PacketTooLargeException extends RuntimeException {
        private final Packet<?> packet;

        PacketTooLargeException(Packet<?> packet, int packetLength) {
            super("PacketTooLarge - " + packet.getClass().getSimpleName() + " is " + packetLength + ". Max is " + MAX_PACKET_SIZE);
            this.packet = packet;
        }

        public Packet<?> getPacket() {
            return packet;
        }
    }
    // Paper end
}
