package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {

    public static final Object a = new Object();
    public static int b;
    public static int c;
    private Object e = new Object();
    private Socket f;
    private final SocketAddress g;
    private DataInputStream h;
    private DataOutputStream i;
    private boolean j = true;
    private List k = Collections.synchronizedList(new ArrayList());
    private List l = Collections.synchronizedList(new ArrayList());
    private List m = Collections.synchronizedList(new ArrayList());
    private NetHandler n;
    private boolean o = false;
    private Thread p;
    private Thread q;
    private boolean r = false;
    private String s = "";
    private Object[] t;
    private int u = 0;
    private int v = 0;
    public int d = 0;
    private int w = 50;

    public NetworkManager(Socket socket, String s, NetHandler nethandler) {
        this.f = socket;
        this.g = socket.getRemoteSocketAddress();
        this.n = nethandler;

        // Craftbukkit start
        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);
        } catch (SocketException socketexception) {
            System.err.println(socketexception.getMessage());
        }
        
        try {
            this.h = new DataInputStream(socket.getInputStream());
            this.i = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Craftbukkit end

        this.q = new NetworkReaderThread(this, s + " read thread");
        this.p = new NetworkWriterThread(this, s + " write thread");
        this.q.start();
        this.p.start();
    }

    public void a(NetHandler nethandler) {
        this.n = nethandler;
    }

    public void a(Packet packet) {
        if (!this.o) {
            Object object = this.e;

            synchronized (this.e) {
                this.v += packet.a() + 1;
                if (packet.k) {
                    this.m.add(packet);
                } else {
                    this.l.add(packet);
                }
            }
        }
    }

    private void e() {
        try {
            boolean flag = true;
            Object object;
            Packet packet;

            if (!this.l.isEmpty() && (this.d == 0 || System.currentTimeMillis() - ((Packet) this.l.get(0)).j >= (long) this.d)) {
                flag = false;
                object = this.e;
                synchronized (this.e) {
                    packet = (Packet) this.l.remove(0);
                    this.v -= packet.a() + 1;
                }

                Packet.a(packet, this.i);
            }

            if ((flag || this.w-- <= 0) && !this.m.isEmpty() && (this.d == 0 || System.currentTimeMillis() - ((Packet) this.m.get(0)).j >= (long) this.d)) {
                flag = false;
                object = this.e;
                synchronized (this.e) {
                    packet = (Packet) this.m.remove(0);
                    this.v -= packet.a() + 1;
                }

                Packet.a(packet, this.i);
                this.w = 50;
            }

            if (flag) {
                Thread.sleep(10L);
            }
        } catch (InterruptedException interruptedexception) {
            ;
        } catch (Exception exception) {
            if (!this.r) {
                this.a(exception);
            }
        }
    }

    private void f() {
        try {
            Packet packet = Packet.b(this.h);

            if (packet != null) {
                this.k.add(packet);
            } else {
                this.a("disconnect.endOfStream", new Object[0]);
            }
        } catch (Exception exception) {
            if (!this.r) {
                this.a(exception);
            }
        }
    }

    private void a(Exception exception) {
        exception.printStackTrace();
        this.a("disconnect.genericReason", new Object[] { "Internal exception: " + exception.toString()});
    }

    public void a(String s, Object... aobject) {
        if (this.j) {
            this.r = true;
            this.s = s;
            this.t = aobject;
            (new NetworkMasterThread(this)).start();
            this.j = false;

            try {
                this.h.close();
                this.h = null;
            } catch (Throwable throwable) {
                ;
            }

            try {
                this.i.close();
                this.i = null;
            } catch (Throwable throwable1) {
                ;
            }

            try {
                this.f.close();
                this.f = null;
            } catch (Throwable throwable2) {
                ;
            }
        }
    }

    public void a() {
        if (this.v > 1048576) {
            this.a("disconnect.overflow", new Object[0]);
        }

        if (this.k.isEmpty()) {
            if (this.u++ == 1200) {
                this.a("disconnect.timeout", new Object[0]);
            }
        } else {
            this.u = 0;
        }

        int i = 100;

        while (!this.k.isEmpty() && i-- >= 0) {
            Packet packet = (Packet) this.k.remove(0);

            packet.a(this.n);
        }

        if (this.r && this.k.isEmpty()) {
            this.n.a(this.s, this.t);
        }
    }

    public SocketAddress b() {
        return this.g;
    }

    public void c() {
        this.o = true;
        this.q.interrupt();
        (new ThreadMonitorConnection(this)).start();
    }

    public int d() {
        return this.m.size();
    }

    static boolean a(NetworkManager networkmanager) {
        return networkmanager.j;
    }

    static boolean b(NetworkManager networkmanager) {
        return networkmanager.o;
    }

    static void c(NetworkManager networkmanager) {
        networkmanager.f();
    }

    static void d(NetworkManager networkmanager) {
        networkmanager.e();
    }

    static Thread e(NetworkManager networkmanager) {
        return networkmanager.q;
    }

    static Thread f(NetworkManager networkmanager) {
        return networkmanager.p;
    }
}
