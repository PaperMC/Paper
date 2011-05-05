package net.minecraft.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.IOException; // CraftBukkit -- instead of SocketException
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkManager {

    public static final Object a = new Object();
    public static int b;
    public static int c;
    private Object e = new Object();
    public Socket socket; // CraftBukkit -- private->public
    private final SocketAddress g;
    private DataInputStream input;
    private DataOutputStream output;
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
        this.socket = socket;
        this.g = socket.getRemoteSocketAddress();
        this.n = nethandler;

        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);

            // CraftBukkit start -- cant compile these outside the try
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
            // CraftBukkit end
        } catch (IOException socketexception) {
            System.err.println(socketexception.getMessage());
        }

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

            if (!this.l.isEmpty() && (this.d == 0 || System.currentTimeMillis() - ((Packet) this.l.get(0)).timestamp >= (long) this.d)) {
                flag = false;
                object = this.e;
                synchronized (this.e) {
                    packet = (Packet) this.l.remove(0);
                    this.v -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
            }

            // CraftBukkit - don't allow low priority packet to be sent unless it was placed in the queue before the first packet on the high priority queue
            if ((flag || this.w-- <= 0) && !this.m.isEmpty() && (this.l.isEmpty() || ((Packet) this.l.get(0)).timestamp > ((Packet) this.m.get(0)).timestamp)) {
                flag = false;
                object = this.e;
                synchronized (this.e) {
                    packet = (Packet) this.m.remove(0);
                    this.v -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
                this.w = 50;
            }

            if (flag) {
                Thread.sleep(10L);
            } else {
                this.output.flush();
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
            Packet packet = Packet.a(this.input, this.n.c());

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

    public SocketAddress getSocketAddress() {
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
