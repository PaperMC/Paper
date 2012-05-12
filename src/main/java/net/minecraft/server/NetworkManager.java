package net.minecraft.server;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
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
    private List highPriorityQueue = Collections.synchronizedList(new ArrayList());
    private List lowPriorityQueue = Collections.synchronizedList(new ArrayList());
    private NetHandler packetListener;
    private boolean q = false;
    private Thread r;
    private Thread s;
    private boolean t = false;
    private String u = "";
    private Object[] v;
    private int w = 0;
    private int x = 0;
    public static int[] d = new int[256];
    public static int[] e = new int[256];
    public int f = 0;
    private int lowPriorityQueueDelay = 50;

    public NetworkManager(Socket socket, String s, NetHandler nethandler) {
        this.socket = socket;
        this.i = socket.getRemoteSocketAddress();
        this.packetListener = nethandler;

        // CraftBukkit start - IPv6 stack in Java on BSD/OSX doesn't support setTrafficClass
        try {
            socket.setTrafficClass(24);
        } catch (SocketException e) {}
        // CraftBukkit end

        try {
            // CraftBukkit start - cant compile these outside the try
            socket.setSoTimeout(30000);
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
        } catch (java.io.IOException socketexception) {
            // CraftBukkit end
            System.err.println(socketexception.getMessage());
        }

        /* CraftBukkit start - moved up
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
        // CraftBukkit end */
        this.s = new NetworkReaderThread(this, s + " read thread");
        this.r = new NetworkWriterThread(this, s + " write thread");
        this.s.start();
        this.r.start();
    }

    public void a(NetHandler nethandler) {
        this.packetListener = nethandler;
    }

    public void queue(Packet packet) {
        if (!this.q) {
            Object object = this.g;

            synchronized (this.g) {
                this.x += packet.a() + 1;
                if (packet.lowPriority) {
                    this.lowPriorityQueue.add(packet);
                } else {
                    this.highPriorityQueue.add(packet);
                }
            }
        }
    }

    private boolean g() {
        boolean flag = false;

        try {
            Object object;
            Packet packet;
            int i;
            int[] aint;

            if (!this.highPriorityQueue.isEmpty() && (this.f == 0 || System.currentTimeMillis() - ((Packet) this.highPriorityQueue.get(0)).timestamp >= (long) this.f)) {
                object = this.g;
                synchronized (this.g) {
                    packet = (Packet) this.highPriorityQueue.remove(0);
                    this.x -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
                aint = e;
                i = packet.b();
                aint[i] += packet.a() + 1;
                flag = true;
            }

            // CraftBukkit - don't allow low priority packet to be sent unless it was placed in the queue before the first packet on the high priority queue
            if ((flag || this.lowPriorityQueueDelay-- <= 0) && !this.lowPriorityQueue.isEmpty() && (this.highPriorityQueue.isEmpty() || ((Packet) this.highPriorityQueue.get(0)).timestamp > ((Packet) this.lowPriorityQueue.get(0)).timestamp)) {
                object = this.g;
                synchronized (this.g) {
                    packet = (Packet) this.lowPriorityQueue.remove(0);
                    this.x -= packet.a() + 1;
                }

                Packet.a(packet, this.output);
                aint = e;
                i = packet.b();
                aint[i] += packet.a() + 1;
                this.lowPriorityQueueDelay = 0;
                flag = true;
            }

            return flag;
        } catch (Exception exception) {
            if (!this.t) {
                this.a(exception);
            }

            return false;
        }
    }

    public void a() {
        this.s.interrupt();
        this.r.interrupt();
    }

    private boolean h() {
        boolean flag = false;

        try {
            Packet packet = Packet.a(this.input, this.packetListener.c());

            if (packet != null) {
                int[] aint = d;
                int i = packet.b();

                aint[i] += packet.a() + 1;
                if (!this.q) {
                    this.m.add(packet);
                }

                flag = true;
            } else {
                this.a("disconnect.endOfStream", new Object[0]);
            }

            return flag;
        } catch (Exception exception) {
            if (!this.t) {
                this.a(exception);
            }

            return false;
        }
    }

    private void a(Exception exception) {
        // exception.printStackTrace(); // CraftBukkit - Remove console spam
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

        int i = 1000;

        while (!this.m.isEmpty() && i-- >= 0) {
            Packet packet = (Packet) this.m.remove(0);

            if (!this.q) packet.handle(this.packetListener); // CraftBukkit
        }

        this.a();
        if (this.t && this.m.isEmpty()) {
            this.packetListener.a(this.u, this.v);
        }
    }

    public SocketAddress getSocketAddress() {
        return this.i;
    }

    public void d() {
        if (!this.q) {
            this.a();
            this.q = true;
            this.s.interrupt();
            (new NetworkMonitorThread(this)).start();
        }
    }

    public int e() {
        return this.lowPriorityQueue.size();
    }

    public Socket getSocket() {
        return this.socket;
    }

    static boolean a(NetworkManager networkmanager) {
        return networkmanager.l;
    }

    static boolean b(NetworkManager networkmanager) {
        return networkmanager.q;
    }

    static boolean c(NetworkManager networkmanager) {
        return networkmanager.h();
    }

    static boolean d(NetworkManager networkmanager) {
        return networkmanager.g();
    }

    static DataOutputStream e(NetworkManager networkmanager) {
        return networkmanager.output;
    }

    static boolean f(NetworkManager networkmanager) {
        return networkmanager.t;
    }

    static void a(NetworkManager networkmanager, Exception exception) {
        networkmanager.a(exception);
    }

    static Thread g(NetworkManager networkmanager) {
        return networkmanager.s;
    }

    static Thread h(NetworkManager networkmanager) {
        return networkmanager.r;
    }
}
