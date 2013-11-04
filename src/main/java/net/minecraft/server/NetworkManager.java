package net.minecraft.server;

import java.net.SocketAddress;
import java.util.Queue;
import javax.crypto.SecretKey;

import net.minecraft.util.com.google.common.collect.Queues;
import net.minecraft.util.com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFutureListener;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.util.io.netty.channel.local.LocalChannel;
import net.minecraft.util.io.netty.channel.local.LocalServerChannel;
import net.minecraft.util.io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.io.netty.util.AttributeKey;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler {

    private static final Logger g = LogManager.getLogger();
    public static final Marker a = MarkerManager.getMarker("NETWORK");
    public static final Marker b = MarkerManager.getMarker("NETWORK_PACKETS", a);
    public static final AttributeKey c = new AttributeKey("protocol");
    public static final AttributeKey d = new AttributeKey("receivable_packets");
    public static final AttributeKey e = new AttributeKey("sendable_packets");
    public static final NioEventLoopGroup f = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    private final boolean h;
    private final Queue i = Queues.newConcurrentLinkedQueue();
    private final Queue j = Queues.newConcurrentLinkedQueue();
    private Channel k;
    private SocketAddress l;
    private PacketListener m;
    private EnumProtocol n;
    private IChatBaseComponent o;

    public NetworkManager(boolean flag) {
        this.h = flag;
    }

    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception { // CraftBukkit - throws Exception
        super.channelActive(channelhandlercontext);
        this.k = channelhandlercontext.channel();
        this.l = this.k.remoteAddress();
        this.a(EnumProtocol.HANDSHAKING);
    }

    public void a(EnumProtocol enumprotocol) {
        this.n = (EnumProtocol) this.k.attr(c).getAndSet(enumprotocol);
        this.k.attr(d).set(enumprotocol.a(this.h));
        this.k.attr(e).set(enumprotocol.b(this.h));
        this.k.config().setAutoRead(true);
        g.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext channelhandlercontext) {
        this.a((IChatBaseComponent) (new ChatMessage("disconnect.endOfStream", new Object[0])));
    }

    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) {
        this.a((IChatBaseComponent) (new ChatMessage("disconnect.genericReason", new Object[] { "Internal Exception: " + throwable})));
    }

    protected void a(ChannelHandlerContext channelhandlercontext, Packet packet) {
        if (this.k.isOpen()) {
            if (packet.a()) {
                packet.handle(this.m);
            } else {
                this.i.add(packet);
            }
        }
    }

    public void a(PacketListener packetlistener) {
        Validate.notNull(packetlistener, "packetListener", new Object[0]);
        g.debug("Set listener of {} to {}", new Object[] { this, packetlistener});
        this.m = packetlistener;
    }

    public void handle(Packet packet, GenericFutureListener... agenericfuturelistener) {
        if (this.k != null && this.k.isOpen()) {
            this.h();
            this.b(packet, agenericfuturelistener);
        } else {
            this.j.add(new QueuedPacket(packet, agenericfuturelistener));
        }
    }

    private void b(Packet packet, GenericFutureListener[] agenericfuturelistener) {
        EnumProtocol enumprotocol = EnumProtocol.a(packet);
        EnumProtocol enumprotocol1 = (EnumProtocol) this.k.attr(c).get();

        if (enumprotocol1 != enumprotocol) {
            g.debug("Disabled auto read");
            this.k.config().setAutoRead(false);
        }

        if (this.k.eventLoop().inEventLoop()) {
            if (enumprotocol != enumprotocol1) {
                this.a(enumprotocol);
            }

            this.k.writeAndFlush(packet).addListeners(agenericfuturelistener).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.k.eventLoop().execute(new QueuedProtocolSwitch(this, enumprotocol, enumprotocol1, packet, agenericfuturelistener));
        }
    }

    private void h() {
        if (this.k != null && this.k.isOpen()) {
            while (!this.j.isEmpty()) {
                QueuedPacket queuedpacket = (QueuedPacket) this.j.poll();

                this.b(QueuedPacket.a(queuedpacket), QueuedPacket.b(queuedpacket));
            }
        }
    }

    public void a() {
        this.h();
        EnumProtocol enumprotocol = (EnumProtocol) this.k.attr(c).get();

        if (this.n != enumprotocol) {
            if (this.n != null) {
                this.m.a(this.n, enumprotocol);
            }

            this.n = enumprotocol;
        }

        if (this.m != null) {
            for (int i = 1000; !this.i.isEmpty() && i >= 0; --i) {
                Packet packet = (Packet) this.i.poll();

                // CraftBukkit start
                if (!this.d() || !this.k.config().isAutoRead()) { // Should be isConnected
                    continue;
                }
                // CraftBukkit end

                packet.handle(this.m);
            }

            this.m.a();
        }

        this.k.flush();
    }

    public SocketAddress getSocketAddress() {
        return this.l;
    }

    public void a(IChatBaseComponent ichatbasecomponent) {
        if (this.k.isOpen()) {
            this.k.close();
            this.o = ichatbasecomponent;
        }
    }

    public boolean c() {
        return this.k instanceof LocalChannel || this.k instanceof LocalServerChannel;
    }

    public void a(SecretKey secretkey) {
        this.k.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(MinecraftEncryption.a(2, secretkey)));
        this.k.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(MinecraftEncryption.a(1, secretkey)));
    }

    public boolean d() {
        return this.k != null && this.k.isOpen();
    }

    public PacketListener getPacketListener() {
        return this.m;
    }

    public IChatBaseComponent f() {
        return this.o;
    }

    public void g() {
        this.k.config().setAutoRead(false);
    }

    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Object object) {
        this.a(channelhandlercontext, (Packet) object);
    }

    static Channel a(NetworkManager networkmanager) {
        return networkmanager.k;
    }
}
