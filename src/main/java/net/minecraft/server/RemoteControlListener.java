package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoteControlListener extends RemoteConnectionThread {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ServerSocket e;
    private final String f;
    private final List<RemoteControlSession> g = Lists.newArrayList();
    private final IMinecraftServer h;

    private RemoteControlListener(IMinecraftServer iminecraftserver, ServerSocket serversocket, String s) {
        super("RCON Listener");
        this.h = iminecraftserver;
        this.e = serversocket;
        this.f = s;
    }

    private void d() {
        this.g.removeIf((remotecontrolsession) -> {
            return !remotecontrolsession.c();
        });
    }

    public void run() {
        try {
            while (this.a) {
                try {
                    Socket socket = this.e.accept();
                    RemoteControlSession remotecontrolsession = new RemoteControlSession(this.h, this.f, socket);

                    remotecontrolsession.a();
                    this.g.add(remotecontrolsession);
                    this.d();
                } catch (SocketTimeoutException sockettimeoutexception) {
                    this.d();
                } catch (IOException ioexception) {
                    if (this.a) {
                        RemoteControlListener.LOGGER.info("IO exception: ", ioexception);
                    }
                }
            }
        } finally {
            this.a(this.e);
        }

    }

    @Nullable
    public static RemoteControlListener a(IMinecraftServer iminecraftserver) {
        DedicatedServerProperties dedicatedserverproperties = iminecraftserver.getDedicatedServerProperties();
        String s = iminecraftserver.h_();

        if (s.isEmpty()) {
            s = "0.0.0.0";
        }

        int i = dedicatedserverproperties.rconPort;

        if (0 < i && 65535 >= i) {
            String s1 = dedicatedserverproperties.rconPassword;

            if (s1.isEmpty()) {
                RemoteControlListener.LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
                return null;
            } else {
                try {
                    ServerSocket serversocket = new ServerSocket(i, 0, InetAddress.getByName(s));

                    serversocket.setSoTimeout(500);
                    RemoteControlListener remotecontrollistener = new RemoteControlListener(iminecraftserver, serversocket, s1);

                    if (!remotecontrollistener.a()) {
                        return null;
                    } else {
                        RemoteControlListener.LOGGER.info("RCON running on {}:{}", s, i);
                        return remotecontrollistener;
                    }
                } catch (IOException ioexception) {
                    RemoteControlListener.LOGGER.warn("Unable to initialise RCON on {}:{}", s, i, ioexception);
                    return null;
                }
            }
        } else {
            RemoteControlListener.LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", i);
            return null;
        }
    }

    @Override
    public void b() {
        this.a = false;
        this.a(this.e);
        super.b();
        Iterator iterator = this.g.iterator();

        while (iterator.hasNext()) {
            RemoteControlSession remotecontrolsession = (RemoteControlSession) iterator.next();

            if (remotecontrolsession.c()) {
                remotecontrolsession.b();
            }
        }

        this.g.clear();
    }

    private void a(ServerSocket serversocket) {
        RemoteControlListener.LOGGER.debug("closeSocket: {}", serversocket);

        try {
            serversocket.close();
        } catch (IOException ioexception) {
            RemoteControlListener.LOGGER.warn("Failed to close socket", ioexception);
        }

    }
}
