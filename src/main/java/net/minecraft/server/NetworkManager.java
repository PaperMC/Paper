package net.minecraft.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.IOException; // CraftBukkit - instead of SocketException
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkManager {

    public static final Object a = new Object();
    public static int b;
    public static int c;
    private Object g = new Object();
    public Socket socket; // CraftBukkit - private -> public
    private final SocketAddress i;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean l = true;
    private List m = Collections.synchronizedList(new ArrayList());
    private List n = Collections.synchronizedList(new ArrayList());
    private List o = Collections.synchronizedList(new ArrayList());
    private NetHandler p;
    private boolean q = false;
    private Thread r;
    private Thread s;
    private boolean t = false;
    private String u = "";
    private Object[] v;
    private int w = 0;
    private int x = 0;
    private transient boolean y = false;
    public static int[] d = new int[256];
    public static int[] e = new int[256];
    public int f = 0;
    private int z = 50;

    public NetworkManager(Socket socket, String s, NetHandler nethandler) {
        this.socket = socket;
        this.i = socket.getRemoteSocketAddress();
        this.p = nethandler;

        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);

            // CraftBukkit start - cant compile these outside the try
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
            // CraftBukkit end
        } catch (IOException socketexception) {
            System.err.println(socketexception.getMessage());
        }

        this.s = new NetworkReaderThread(this, s + " read thread");
        this.r = new NetworkWriterThread(this, s + " write thread");
        this.s.start();
        this.r.start();
    }

    public void a(NetHandler nethandler) {
        this.p = nethandler;
    }

    public void a(Packet packet) {
        if (!this.q) {
            Object object = this.g;

            synchronized (this.g) {
                this.x += packet.a() + 1;
                if (packet.k) {
                    this.o.add(packet);
                } else {
                    this.n.add(packet);
                }
            }
        }
    }

    private void f() {
        try {
            Object object;
            Packet packet;
            int i;
            int[] aint;

            if (!this.n.isEmpty() && (this.f == 0 || System.currentTimeMillis() - ((Packet) this.n.get(0)).timestamp >= (long) this.f)) {
                object = this.g;
                synchronized (this.g) {
                    packet = (Packet) this.n.remove(0);
                    this.x -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
                aint = e;
                i = packet.b();
                aint[i] += packet.a();
            }

            // Craftbukkit - we used to enforce package priorities, we don't now. Do we need to redo this? - TODO
            if (this.z-- <= 0 && !this.o.isEmpty() && (this.f == 0 || System.currentTimeMillis() - ((Packet) this.o.get(0)).timestamp >= (long) this.f)) {
                object = this.g;
                synchronized (this.g) {
                    packet = (Packet) this.o.remove(0);
                    this.x -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
                aint = e;
                i = packet.b();
                aint[i] += packet.a();
                this.z = 50;
            }
        } catch (Exception exception) {
            if (!this.t) {
                this.a(exception);
            }
        }
    }

    public void a() {
        this.y = true;
    }

    private void g() {
        try {
            Packet packet = Packet.a(this.input, this.p.c());

            if (packet != null) {
                int[] aint = d;
                int i = packet.b();

                aint[i] += packet.a();
                this.m.add(packet);
            } else {
                this.a("disconnect.endOfStream", new Object[0]);
            }
        } catch (Exception exception) {
            if (!this.t) {
                this.a(exception);
            }
        }
    }

    private void a(Exception exception) {
        exception.printStackTrace();
        this.a("disconnect.genericReason", new Object[] { "Internal exception: " + exception.toString()});
    }

    public void a(String s, Object... aobject) {
        if (this.l) {
            this.t = true;
            this.u = s;
            this.v = aobject;
            (new NetworkMasterThread(this)).start();
            this.l = false;

            try {
                this.input.close();
                this.input = null;
            } catch (Throwable throwable) {
                ;
            }

            try {
                this.output.close();
                this.output = null;
            } catch (Throwable throwable1) {
                ;
            }

            try {
                this.socket.close();
                this.socket = null;
            } catch (Throwable throwable2) {
                ;
            }
        }
    }

    public void b() {
        if (this.x > 1048576) {
            this.a("disconnect.overflow", new Object[0]);
        }

        if (this.m.isEmpty()) {
            if (this.w++ == 1200) {
                this.a("disconnect.timeout", new Object[0]);
            }
        } else {
            this.w = 0;
        }

        int i = 100;

        while (!this.m.isEmpty() && i-- >= 0) {
            Packet packet = (Packet) this.m.remove(0);

            packet.a(this.p);
        }

        if (this.t && this.m.isEmpty()) {
            this.p.a(this.u, this.v);
        }
    }

    public SocketAddress getSocketAddress() {
        return this.i;
    }

    public void d() {
        this.a();
        this.q = true;
        this.s.interrupt();
        (new ThreadMonitorConnection(this)).start();
    }

    public int e() {
        return this.o.size();
    }

    static boolean a(NetworkManager networkmanager) {
        return networkmanager.l;
    }

    static boolean b(NetworkManager networkmanager) {
        return networkmanager.q;
    }

    static void c(NetworkManager networkmanager) {
        networkmanager.g();
    }

    static void d(NetworkManager networkmanager) {
        networkmanager.f();
    }

    static boolean e(NetworkManager networkmanager) {
        return networkmanager.y;
    }

    static boolean a(NetworkManager networkmanager, boolean flag) {
        return networkmanager.y = flag;
    }

    static DataOutputStream f(NetworkManager networkmanager) {
        return networkmanager.output;
    }

    static Thread g(NetworkManager networkmanager) {
        return networkmanager.s;
    }

    static Thread h(NetworkManager networkmanager) {
        return networkmanager.r;
    }
}
