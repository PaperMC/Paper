package net.minecraft.server;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.EncoderException; // Paper
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Marker a = MarkerManager.getMarker("NETWORK");
    public static final Marker b = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.a);
    public static final AttributeKey<EnumProtocol> c = AttributeKey.valueOf("protocol");
    public static final LazyInitVar<NioEventLoopGroup> d = new LazyInitVar<>(() -> {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    });
    public static final LazyInitVar<EpollEventLoopGroup> e = new LazyInitVar<>(() -> {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
    });
    public static final LazyInitVar<DefaultEventLoopGroup> f = new LazyInitVar<>(() -> {
        return new DefaultEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
    });
    private final EnumProtocolDirection h;
    private final Queue<NetworkManager.QueuedPacket> packetQueue = Queues.newConcurrentLinkedQueue();
    public Channel channel;
    public SocketAddress socketAddress;
    // Spigot Start
    public java.util.UUID spoofedUUID;
    public com.mojang.authlib.properties.Property[] spoofedProfile;
    public boolean preparing = true;
    // Spigot End
    private PacketListener packetListener;
    private IChatBaseComponent m;
    private boolean n;
    private boolean o;
    private int p;
    private int q;
    private float r;
    private float s;
    private int t;
    private boolean u;
    // Paper start - NetworkClient implementation
    public int protocolVersion;
    public java.net.InetSocketAddress virtualHost;
    private static boolean enableExplicitFlush = Boolean.getBoolean("paper.explicit-flush");
    // Optimize network
    boolean isPending = true;
    boolean queueImmunity = false;
    EnumProtocol protocol;
    // Paper end

    public NetworkManager(EnumProtocolDirection enumprotocoldirection) {
        this.h = enumprotocoldirection;
    }

    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        this.channel = channelhandlercontext.channel();
        this.socketAddress = this.channel.remoteAddress();
        // Spigot Start
        this.preparing = false;
        // Spigot End

        try {
            this.setProtocol(EnumProtocol.HANDSHAKING);
        } catch (Throwable throwable) {
            NetworkManager.LOGGER.fatal(throwable);
        }

    }

    public void setProtocol(EnumProtocol enumprotocol) {
        protocol = enumprotocol; // Paper
        this.channel.attr(NetworkManager.c).set(enumprotocol);
        this.channel.config().setAutoRead(true);
        NetworkManager.LOGGER.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception {
        this.close(new ChatMessage("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) {
        // Paper start
        if (throwable instanceof EncoderException && throwable.getCause() instanceof PacketEncoder.PacketTooLargeException) {
            if (((PacketEncoder.PacketTooLargeException) throwable.getCause()).getPacket().packetTooLarge(this)) {
                return;
            } else {
                throwable = throwable.getCause();
            }
        }
        // Paper end
        if (throwable instanceof SkipEncodeException) {
            NetworkManager.LOGGER.debug("Skipping packet due to errors", throwable.getCause());
        } else {
            boolean flag = !this.u;

            this.u = true;
            if (this.channel.isOpen()) {
                if (throwable instanceof TimeoutException) {
                    NetworkManager.LOGGER.debug("Timeout", throwable);
                    this.close(new ChatMessage("disconnect.timeout"));
                } else {
                    ChatMessage chatmessage = new ChatMessage("disconnect.genericReason", new Object[]{"Internal Exception: " + throwable});

                    if (flag) {
                        NetworkManager.LOGGER.debug("Failed to sent packet", throwable);
                        this.sendPacket(new PacketPlayOutKickDisconnect(chatmessage), (future) -> {
                            this.close(chatmessage);
                        });
                        this.stopReading();
                    } else {
                        NetworkManager.LOGGER.debug("Double fault", throwable);
                        this.close(chatmessage);
                    }
                }

            }
        }
        if (MinecraftServer.getServer().isDebugging()) throwable.printStackTrace(); // Spigot
    }

    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet<?> packet) throws Exception {
        if (this.channel.isOpen()) {
            try {
                a(packet, this.packetListener);
            } catch (CancelledPacketHandleException cancelledpackethandleexception) {
                ;
            }

            ++this.p;
        }

    }

    private static <T extends PacketListener> void a(Packet<T> packet, PacketListener packetlistener) {
        packet.a((T) packetlistener); // CraftBukkit - decompile error
    }

    public void setPacketListener(PacketListener packetlistener) {
        Validate.notNull(packetlistener, "packetListener", new Object[0]);
        this.packetListener = packetlistener;
    }
    // Paper start
    EntityPlayer getPlayer() {
        if (packetListener instanceof PlayerConnection) {
            return ((PlayerConnection) packetListener).player;
        } else {
            return null;
        }
    }
    private static class InnerUtil { // Attempt to hide these methods from ProtocolLib so it doesn't accidently pick them up.
        private static java.util.List<Packet> buildExtraPackets(Packet packet) {
            java.util.List<Packet> extra = packet.getExtraPackets();
            if (extra == null || extra.isEmpty()) {
                return null;
            }
            java.util.List<Packet> ret = new java.util.ArrayList<>(1 + extra.size());
            buildExtraPackets0(extra, ret);
            return ret;
        }

        private static void buildExtraPackets0(java.util.List<Packet> extraPackets, java.util.List<Packet> into) {
            for (Packet extra : extraPackets) {
                into.add(extra);
                java.util.List<Packet> extraExtra = extra.getExtraPackets();
                if (extraExtra != null && !extraExtra.isEmpty()) {
                    buildExtraPackets0(extraExtra, into);
                }
            }
        }
        // Paper start
        private static boolean canSendImmediate(NetworkManager networkManager, Packet<?> packet) {
            return networkManager.isPending || networkManager.protocol != EnumProtocol.PLAY ||
                packet instanceof PacketPlayOutKeepAlive ||
                packet instanceof PacketPlayOutChat ||
                packet instanceof PacketPlayOutTabComplete;
        }
        // Paper end
    }
    // Paper end

    public void sendPacket(Packet<?> packet) {
        this.sendPacket(packet, (GenericFutureListener) null);
    }

    public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
        // Paper start - handle oversized packets better
        boolean connected = this.isConnected();
        if (!connected && !preparing) {
            return; // Do nothing
        }
        packet.onPacketDispatch(getPlayer());
        if (connected && (InnerUtil.canSendImmediate(this, packet) || (
            MCUtil.isMainThread() && packet.isReady() && this.packetQueue.isEmpty() &&
            (packet.getExtraPackets() == null || packet.getExtraPackets().isEmpty())
        ))) {
            this.dispatchPacket(packet, genericfuturelistener);
            return;
        }
        // write the packets to the queue, then flush - antixray hooks there already
        java.util.List<Packet> extraPackets = InnerUtil.buildExtraPackets(packet);
        boolean hasExtraPackets = extraPackets != null && !extraPackets.isEmpty();
        if (!hasExtraPackets) {
            this.packetQueue.add(new NetworkManager.QueuedPacket(packet, genericfuturelistener));
        } else {
            java.util.List<NetworkManager.QueuedPacket> packets = new java.util.ArrayList<>(1 + extraPackets.size());
            packets.add(new NetworkManager.QueuedPacket(packet, null)); // delay the future listener until the end of the extra packets

            for (int i = 0, len = extraPackets.size(); i < len;) {
                Packet extra = extraPackets.get(i);
                boolean end = ++i == len;
                packets.add(new NetworkManager.QueuedPacket(extra, end ? genericfuturelistener : null)); // append listener to the end
            }

            this.packetQueue.addAll(packets); // atomic
        }
        this.sendPacketQueue();
        // Paper end
    }

    private void dispatchPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericFutureListener) { this.b(packet, genericFutureListener); } // Paper - OBFHELPER
    private void b(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
        EnumProtocol enumprotocol = EnumProtocol.a(packet);
        EnumProtocol enumprotocol1 = (EnumProtocol) this.channel.attr(NetworkManager.c).get();

        ++this.q;
        if (enumprotocol1 != enumprotocol) {
            NetworkManager.LOGGER.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        EntityPlayer player = getPlayer(); // Paper
        if (this.channel.eventLoop().inEventLoop()) {
            if (enumprotocol != enumprotocol1) {
                this.setProtocol(enumprotocol);
            }
            // Paper start
            if (!isConnected()) {
                packet.onPacketDispatchFinish(player, null);
                return;
            }
            try {
                // Paper end

            ChannelFuture channelfuture = this.channel.writeAndFlush(packet);

            if (genericfuturelistener != null) {
                channelfuture.addListener(genericfuturelistener);
            }
            // Paper start
            if (packet.hasFinishListener()) {
                channelfuture.addListener((ChannelFutureListener) channelFuture -> packet.onPacketDispatchFinish(player, channelFuture));
            }
            // Paper end

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            // Paper start
            } catch (Exception e) {
                LOGGER.error("NetworkException: " + player, e);
                close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));;
                packet.onPacketDispatchFinish(player, null);
            }
            // Paper end
        } else {
            this.channel.eventLoop().execute(() -> {
                if (enumprotocol != enumprotocol1) {
                    this.setProtocol(enumprotocol);
                }

                // Paper start
                if (!isConnected()) {
                    packet.onPacketDispatchFinish(player, null);
                    return;
                }
                try {
                    // Paper end
                ChannelFuture channelfuture1 = this.channel.writeAndFlush(packet);


                if (genericfuturelistener != null) {
                    channelfuture1.addListener(genericfuturelistener);
                }
                // Paper start
                if (packet.hasFinishListener()) {
                    channelfuture1.addListener((ChannelFutureListener) channelFuture -> packet.onPacketDispatchFinish(player, channelFuture));
                }
                // Paper end

                channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                // Paper start
                } catch (Exception e) {
                    LOGGER.error("NetworkException: " + player, e);
                    close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));;
                    packet.onPacketDispatchFinish(player, null);
                }
                // Paper end
            });
        }

    }

    // Paper start - rewrite this to be safer if ran off main thread
    private boolean sendPacketQueue() { return this.p(); } // OBFHELPER // void -> boolean
    private boolean p() { // void -> boolean
        if (!isConnected()) {
            return true;
        }
        if (MCUtil.isMainThread()) {
            return processQueue();
        } else if (isPending) {
            // Should only happen during login/status stages
            synchronized (this.packetQueue) {
                return this.processQueue();
            }
        }
        return false;
    }
    private boolean processQueue() {
        if (this.packetQueue.isEmpty()) return true;
        // If we are on main, we are safe here in that nothing else should be processing queue off main anymore
        // But if we are not on main due to login/status, the parent is synchronized on packetQueue
        java.util.Iterator<QueuedPacket> iterator = this.packetQueue.iterator();
        while (iterator.hasNext()) {
            NetworkManager.QueuedPacket queued = iterator.next(); // poll -> peek

            // Fix NPE (Spigot bug caused by handleDisconnection())
            if (queued == null) {
                return true;
            }

            Packet<?> packet = queued.getPacket();
            if (!packet.isReady()) {
                return false;
            } else {
                iterator.remove();
                this.dispatchPacket(packet, queued.getGenericFutureListener());
            }
        }
        return true;
    }
    // Paper end

    private static final int MAX_PER_TICK = com.destroystokyo.paper.PaperConfig.maxJoinsPerTick; // Paper
    private static int joinAttemptsThisTick; // Paper
    private static int currTick; // Paper
    public void a() {
        this.p();
        // Paper start
        if (currTick != MinecraftServer.currentTick) {
            currTick = MinecraftServer.currentTick;
            joinAttemptsThisTick = 0;
        }
        // Paper end
        if (this.packetListener instanceof LoginListener) {
            if ( ((LoginListener) this.packetListener).getLoginState() != LoginListener.EnumProtocolState.READY_TO_ACCEPT // Paper
                     || (joinAttemptsThisTick++ < MAX_PER_TICK)) { // Paper - limit the number of joins which can be processed each tick
            ((LoginListener) this.packetListener).tick();
            } // Paper
        }

        if (this.packetListener instanceof PlayerConnection) {
            ((PlayerConnection) this.packetListener).tick();
        }

        if (this.channel != null) {
            if (enableExplicitFlush) this.channel.eventLoop().execute(() -> this.channel.flush()); // Paper - we don't need to explicit flush here, but allow opt in incase issues are found to a better version
        }

        if (this.t++ % 20 == 0) {
            this.b();
        }

    }

    protected void b() {
        this.s = MathHelper.g(0.75F, (float) this.q, this.s);
        this.r = MathHelper.g(0.75F, (float) this.p, this.r);
        this.q = 0;
        this.p = 0;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    // Paper start
    public void clearPacketQueue() {
        EntityPlayer player = getPlayer();
        packetQueue.forEach(queuedPacket -> {
            Packet<?> packet = queuedPacket.getPacket();
            if (packet.hasFinishListener()) {
                packet.onPacketDispatchFinish(player, null);
            }
        });
        packetQueue.clear();
    } // Paper end
    public void close(IChatBaseComponent ichatbasecomponent) {
        // Spigot Start
        this.preparing = false;
        clearPacketQueue(); // Paper
        // Spigot End
        if (this.channel.isOpen()) {
            this.channel.close(); // We can't wait as this may be called from an event loop.
            this.m = ichatbasecomponent;
        }

    }

    public boolean isLocal() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public void a(SecretKey secretkey) {
        this.n = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(MinecraftEncryption.a(2, secretkey)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(MinecraftEncryption.a(1, secretkey)));
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen();
    }

    public boolean i() {
        return this.channel == null;
    }

    public PacketListener j() {
        return this.packetListener;
    }

    @Nullable
    public IChatBaseComponent k() {
        return this.m;
    }

    public void stopReading() {
        this.channel.config().setAutoRead(false);
    }

    public void setCompressionLevel(int i) {
        if (i >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
                ((PacketDecompressor) this.channel.pipeline().get("decompress")).a(i);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new PacketDecompressor(i));
            }

            if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
                ((PacketCompressor) this.channel.pipeline().get("compress")).a(i);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new PacketCompressor(i));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
                this.channel.pipeline().remove("decompress");
            }

            if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
                this.channel.pipeline().remove("compress");
            }
        }

    }

    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (this.o) {
                //NetworkManager.LOGGER.warn("handleDisconnection() called twice"); // Paper - Do not log useless message
            } else {
                this.o = true;
                if (this.k() != null) {
                    this.j().a(this.k());
                } else if (this.j() != null) {
                    this.j().a(new ChatMessage("multiplayer.disconnect.generic"));
                }
                clearPacketQueue(); // Paper
                // Paper start - Add PlayerConnectionCloseEvent
                final PacketListener packetListener = this.j();
                if (packetListener instanceof PlayerConnection) {
                    /* Player was logged in */
                    final PlayerConnection playerConnection = (PlayerConnection) packetListener;
                    new com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent(playerConnection.player.uniqueID,
                        playerConnection.player.getName(), ((java.net.InetSocketAddress)socketAddress).getAddress(), false).callEvent();
                } else if (packetListener instanceof LoginListener) {
                    /* Player is login stage */
                    final LoginListener loginListener = (LoginListener) packetListener;
                    switch (loginListener.getLoginState()) {
                        case READY_TO_ACCEPT:
                        case DELAY_ACCEPT:
                        case ACCEPTED:
                            final com.mojang.authlib.GameProfile profile = loginListener.getGameProfile(); /* Should be non-null at this stage */
                            new com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent(profile.getId(), profile.getName(),
                                ((java.net.InetSocketAddress)socketAddress).getAddress(), false).callEvent();
                    }
                }
                // Paper end
            }

        }
    }

    public float n() {
        return this.r;
    }

    static class QueuedPacket {

        private final Packet<?> a; private final Packet<?> getPacket() { return this.a; } // Paper - OBFHELPER
        @Nullable
        private final GenericFutureListener<? extends Future<? super Void>> b; private final GenericFutureListener<? extends Future<? super Void>> getGenericFutureListener() { return this.b; } // Paper - OBFHELPER

        public QueuedPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
            this.a = packet;
            this.b = genericfuturelistener;
        }
    }

    // Spigot Start
    public SocketAddress getRawAddress()
    {
        return this.channel.remoteAddress();
    }
    // Spigot End
}
