package net.minecraft.server;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;

import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginListener implements PacketLoginInListener {

    private static final AtomicInteger b = new AtomicInteger(0);
    private static final Logger c = LogManager.getLogger();
    private static final Random random = new Random();
    private final byte[] e = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private EnumProtocolState g;
    private int h;
    private GameProfile i;
    private String j;
    private SecretKey loginKey;
    public String hostname = ""; // CraftBukkit - add field

    public LoginListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.g = EnumProtocolState.HELLO;
        this.j = "";
        this.server = minecraftserver;
        this.networkManager = networkmanager;
        random.nextBytes(this.e);
    }

    public void a() {
        if (this.g == EnumProtocolState.READY_TO_ACCEPT) {
            this.c();
        }

        if (this.h++ == 600) {
            this.disconnect("Took too long to log in");
        }
    }

    public void disconnect(String s) {
        try {
            c.info("Disconnecting " + this.getName() + ": " + s);
            ChatComponentText chatcomponenttext = new ChatComponentText(s);

            this.networkManager.handle(new PacketLoginOutDisconnect(chatcomponenttext), new GenericFutureListener[0]);
            this.networkManager.a((IChatBaseComponent) chatcomponenttext);
        } catch (Exception exception) {
            c.error("Error whilst disconnecting player", exception);
        }
    }

    public void c() {
        if (!this.i.isComplete()) {
            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.i.getName()).getBytes(Charsets.UTF_8));

            this.i = new GameProfile(uuid.toString().replaceAll("-", ""), this.i.getName());
        }

        // CraftBukkit start
        EntityPlayer s = this.server.getPlayerList().attemptLogin(this, this.i, this.hostname);

        if (s == null) {
            // this.disconnect(s);
            // CraftBukkit end
        } else {
            this.g = EnumProtocolState.e;
            this.networkManager.handle(new PacketLoginOutSuccess(this.i), new GenericFutureListener[0]);
            this.server.getPlayerList().a(this.networkManager, this.server.getPlayerList().processLogin(this.i, s)); // CraftBukkit - add player reference
        }
    }

    public void a(IChatBaseComponent ichatbasecomponent) {
        c.info(this.getName() + " lost connection: " + ichatbasecomponent.c());
    }

    public String getName() {
        return this.i != null ? this.i.toString() + " (" + this.networkManager.getSocketAddress().toString() + ")" : String.valueOf(this.networkManager.getSocketAddress());
    }

    public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1) {
        Validate.validState(this.g == EnumProtocolState.e || this.g == EnumProtocolState.HELLO, "Unexpected change in protocol", new Object[0]);
        Validate.validState(enumprotocol1 == EnumProtocol.PLAY || enumprotocol1 == EnumProtocol.LOGIN, "Unexpected protocol " + enumprotocol1, new Object[0]);
    }

    public void a(PacketLoginInStart packetlogininstart) {
        Validate.validState(this.g == EnumProtocolState.HELLO, "Unexpected hello packet", new Object[0]);
        this.i = packetlogininstart.c();
        if (this.server.getOnlineMode() && !this.networkManager.c()) {
            this.g = EnumProtocolState.KEY;
            this.networkManager.handle(new PacketLoginOutEncryptionBegin(this.j, this.server.I().getPublic(), this.e), new GenericFutureListener[0]);
        } else {
            this.g = EnumProtocolState.READY_TO_ACCEPT;
        }
    }

    public void a(PacketLoginInEncryptionBegin packetlogininencryptionbegin) {
        Validate.validState(this.g == EnumProtocolState.KEY, "Unexpected key packet", new Object[0]);
        PrivateKey privatekey = this.server.I().getPrivate();

        if (!Arrays.equals(this.e, packetlogininencryptionbegin.b(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        } else {
            this.loginKey = packetlogininencryptionbegin.a(privatekey);
            this.g = EnumProtocolState.AUTHENTICATING;
            this.networkManager.a(this.loginKey);
            (new ThreadPlayerLookupUUID(this, "User Authenticator #" + b.incrementAndGet())).start();
        }
    }

    static String a(LoginListener loginlistener) {
        return loginlistener.j;
    }

    static MinecraftServer b(LoginListener loginlistener) {
        return loginlistener.server;
    }

    static SecretKey c(LoginListener loginlistener) {
        return loginlistener.loginKey;
    }

    static GameProfile a(LoginListener loginlistener, GameProfile gameprofile) {
        return loginlistener.i = gameprofile;
    }

    static GameProfile d(LoginListener loginlistener) {
        return loginlistener.i;
    }

    static Logger e() {
        return c;
    }

    static EnumProtocolState a(LoginListener loginlistener, EnumProtocolState enumprotocolstate) {
        return loginlistener.g = enumprotocolstate;
    }
}
