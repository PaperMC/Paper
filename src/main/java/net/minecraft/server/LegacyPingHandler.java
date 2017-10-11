package net.minecraft.server;

import com.destroystokyo.paper.network.PaperLegacyStatusClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegacyPingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ServerConnection b;
    private ByteBuf buf; // Paper

    public LegacyPingHandler(ServerConnection serverconnection) {
        this.b = serverconnection;
    }

    public void channelRead(ChannelHandlerContext channelhandlercontext, Object object) throws Exception {
        ByteBuf bytebuf = (ByteBuf) object;

        // Paper start - Make legacy ping handler more reliable
        if (this.buf != null) {
            try {
                readLegacy1_6(channelhandlercontext, bytebuf);
            } finally {
                bytebuf.release();
            }
            return;
        }
        // Paper end
        bytebuf.markReaderIndex();
        boolean flag = true;

        try {
            if (bytebuf.readUnsignedByte() != 254) {
                return;
            }

            InetSocketAddress inetsocketaddress = (InetSocketAddress) channelhandlercontext.channel().remoteAddress();
            MinecraftServer minecraftserver = this.b.d();
            int i = bytebuf.readableBytes();
            String s;
            //org.bukkit.event.server.ServerListPingEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callServerListPingEvent(minecraftserver.server, inetsocketaddress.getAddress(), minecraftserver.getMotd(), minecraftserver.getPlayerCount(), minecraftserver.getMaxPlayers()); // CraftBukkit // Paper
            com.destroystokyo.paper.event.server.PaperServerListPingEvent event; // Paper

            switch (i) {
                case 0:
                    LegacyPingHandler.LOGGER.debug("Ping: (<1.3.x) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                // Paper start - Call PaperServerListPingEvent and use results
                event = PaperLegacyStatusClient.processRequest(minecraftserver, inetsocketaddress, 39, null);
                if (event == null) {
                    channelhandlercontext.close();
                    break;
                }
                s = String.format("%s\u00a7%d\u00a7%d", PaperLegacyStatusClient.getUnformattedMotd(event), event.getNumPlayers(), event.getMaxPlayers());
                    this.a(channelhandlercontext, this.a(s));
                    break;
                case 1:
                    if (bytebuf.readUnsignedByte() != 1) {
                        return;
                    }

                    LegacyPingHandler.LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                // Paper start - Call PaperServerListPingEvent and use results
                event = PaperLegacyStatusClient.processRequest(minecraftserver, inetsocketaddress, 127, null); // Paper
                if (event == null) {
                    channelhandlercontext.close();
                    break;
                }
                s = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] { event.getProtocolVersion(), minecraftserver.getVersion(), event.getMotd(), event.getNumPlayers(), event.getMaxPlayers()}); // CraftBukkit
                // Paper end
                    this.a(channelhandlercontext, this.a(s));
                    break;
                default:
                // Paper start - Replace with improved version below
                if (bytebuf.readUnsignedByte() != 0x01 || bytebuf.readUnsignedByte() != 0xFA) return;
                readLegacy1_6(channelhandlercontext, bytebuf);
                /*
                    boolean flag1 = bytebuf.readUnsignedByte() == 1;

                    flag1 &= bytebuf.readUnsignedByte() == 250;
                    flag1 &= "MC|PingHost".equals(new String(bytebuf.readBytes(bytebuf.readShort() * 2).array(), StandardCharsets.UTF_16BE));
                    int j = bytebuf.readUnsignedShort();

                    flag1 &= bytebuf.readUnsignedByte() >= 73;
                    flag1 &= 3 + bytebuf.readBytes(bytebuf.readShort() * 2).array().length + 4 == j;
                    flag1 &= bytebuf.readInt() <= 65535;
                    flag1 &= bytebuf.readableBytes() == 0;
                    if (!flag1) {
                        return;
                    }

                    LegacyPingHandler.LOGGER.debug("Ping: (1.6) from {}:{}", inetsocketaddress.getAddress(), inetsocketaddress.getPort());
                    String s1 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, minecraftserver.getVersion(), event.getMotd(), event.getNumPlayers(), event.getMaxPlayers()); // CraftBukkit
                    ByteBuf bytebuf1 = this.a(s1);

                    try {
                        this.a(channelhandlercontext, bytebuf1);
                    } finally {
                        bytebuf1.release();
                    }
                */ // Paper end - Replace with improved version below
            }

            bytebuf.release();
            flag = false;
        } catch (RuntimeException runtimeexception) {
            ;
        } finally {
            if (flag) {
                bytebuf.resetReaderIndex();
                channelhandlercontext.channel().pipeline().remove("legacy_query");
                channelhandlercontext.fireChannelRead(object);
            }

        }

    }

    // Paper start
    private static String readLegacyString(ByteBuf buf) {
        int size = buf.readShort() * Character.BYTES;
        if (!buf.isReadable(size)) {
            return null;
        }

        String result = buf.toString(buf.readerIndex(), size, StandardCharsets.UTF_16BE);
        buf.skipBytes(size); // toString doesn't increase readerIndex automatically
        return result;
    }

    private void readLegacy1_6(ChannelHandlerContext ctx, ByteBuf part) {
        ByteBuf buf = this.buf;

        if (buf == null) {
            this.buf = buf = ctx.alloc().buffer();
            buf.markReaderIndex();
        } else {
            buf.resetReaderIndex();
        }

        buf.writeBytes(part);

        if (!buf.isReadable(Short.BYTES + Short.BYTES + Byte.BYTES + Short.BYTES + Integer.BYTES)) {
            return;
        }

        String s = readLegacyString(buf);
        if (s == null) {
            return;
        }

        if (!s.equals("MC|PingHost")) {
            removeHandler(ctx);
            return;
        }

        if (!buf.isReadable(Short.BYTES) || !buf.isReadable(buf.readShort())) {
            return;
        }

        MinecraftServer server = this.b.d();
        int protocolVersion = buf.readByte();
        String host = readLegacyString(buf);
        if (host == null) {
            removeHandler(ctx);
            return;
        }
        int port = buf.readInt();

        if (buf.isReadable()) {
            removeHandler(ctx);
            return;
        }

        buf.release();
        this.buf = null;

        LOGGER.debug("Ping: (1.6) from {}", ctx.channel().remoteAddress());

        InetSocketAddress virtualHost = com.destroystokyo.paper.network.PaperNetworkClient.prepareVirtualHost(host, port);
        com.destroystokyo.paper.event.server.PaperServerListPingEvent event = PaperLegacyStatusClient.processRequest(
                server, (InetSocketAddress) ctx.channel().remoteAddress(), protocolVersion, virtualHost);
        if (event == null) {
            ctx.close();
            return;
        }

        String response = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", event.getProtocolVersion(), event.getVersion(),
            PaperLegacyStatusClient.getMotd(event), event.getNumPlayers(), event.getMaxPlayers());
        this.a(ctx, this.a(response));
    }

    private void removeHandler(ChannelHandlerContext ctx) {
        ByteBuf buf = this.buf;
        this.buf = null;

        buf.resetReaderIndex();
        ctx.pipeline().remove(this);
        ctx.fireChannelRead(buf);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (this.buf != null) {
            this.buf.release();
            this.buf = null;
        }
    }
    // Paper end

    private void a(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf) {
        channelhandlercontext.pipeline().firstContext().writeAndFlush(bytebuf).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf a(String s) {
        ByteBuf bytebuf = Unpooled.buffer();

        bytebuf.writeByte(255);
        char[] achar = s.toCharArray();

        bytebuf.writeShort(achar.length);
        char[] achar1 = achar;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar1[j];

            bytebuf.writeChar(c0);
        }

        return bytebuf;
    }
}
