package net.minecraft.server;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;

import java.io.IOException; // CraftBukkit

public class NetworkManager implements INetworkManager {

    public static AtomicInteger a = new AtomicInteger();
    public static AtomicInteger b = new AtomicInteger();
    private Object h = new Object();
    public Socket socket; // CraftBukkit - private -> public
    private final SocketAddress j;
    private volatile DataInputStream input;
    private volatile DataOutputStream output;
    private volatile boolean m = true;
    private volatile boolean n = false;
    private java.util.Queue inboundQueue = new java.util.concurrent.ConcurrentLinkedQueue(); // CraftBukkit - Concurrent linked queue
    private List highPriorityQueue = Collections.synchronizedList(new ArrayList());
    private List lowPriorityQueue = Collections.synchronizedList(new ArrayList());
    private NetHandler packetListener;
    private boolean s = false;
    private Thread t;
    private Thread u;
    private String v = "";
    private Object[] w;
    private int x = 0;
    private int y = 0;
    public static int[] c = new int[256];
    public static int[] d = new int[256];
    public int e = 0;
    boolean f = false;
    boolean g = false;
    private SecretKey z = null;
    private PrivateKey A = null;
    private int lowPriorityQueueDelay = 50;

    public NetworkManager(Socket socket, String s, NetHandler nethandler, PrivateKey privatekey) throws IOException { // CraftBukkit - throws IOException
        this.A = privatekey;
        this.socket = socket;
        this.j = socket.getRemoteSocketAddress();
        this.packetListener = nethandler;

        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);
        } catch (SocketException socketexception) {
            System.err.println(socketexception.getMessage());
        }

        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
        this.u = new NetworkReaderThread(this, s + " read thread");
        this.t = new NetworkWriterThread(this, s + " write thread");
        this.u.start();
        this.t.start();
    }

    public void a(NetHandler nethandler) {
        this.packetListener = nethandler;
    }

    public void queue(Packet packet) {
        if (!this.s) {
            Object object = this.h;

            synchronized (this.h) {
                this.y += packet.a() + 1;
                if (packet.lowPriority) {
                    this.lowPriorityQueue.add(packet);
                } else {
                    this.highPriorityQueue.add(packet);
                }
            }
        }
    }

    private boolean h() {
        boolean flag = false;

        try {
            Packet packet;
            int i;
            int[] aint;

            if (this.e == 0 || System.currentTimeMillis() - ((Packet) this.highPriorityQueue.get(0)).timestamp >= (long) this.e) {
                packet = this.a(false);
                if (packet != null) {
                    Packet.a(packet, this.output);
                    if (packet instanceof Packet252KeyResponse && !this.g) {
                        if (!this.packetListener.a()) {
                            this.z = ((Packet252KeyResponse) packet).d();
                        }

                        this.k();
                    }

                    aint = d;
                    i = packet.k();
                    aint[i] += packet.a() + 1;
                    flag = true;
                }
            }

            // CraftBukkit - don't allow low priority packet to be sent unless it was placed in the queue before the first packet on the high priority queue TODO: is this still right?
            if ((flag || this.lowPriorityQueueDelay-- <= 0) && !this.lowPriorityQueue.isEmpty() && (this.highPriorityQueue.isEmpty() || ((Packet) this.highPriorityQueue.get(0)).timestamp > ((Packet) this.lowPriorityQueue.get(0)).timestamp)) {
                packet = this.a(true);
                if (packet != null) {
                    Packet.a(packet, this.output);
                    aint = d;
                    i = packet.k();
                    aint[i] += packet.a() + 1;
                    this.lowPriorityQueueDelay = 0;
                    flag = true;
                }
            }

            return flag;
        } catch (Exception exception) {
            if (!this.n) {
                this.a(exception);
            }

            return false;
        }
    }

    private Packet a(boolean flag) {
        Packet packet = null;
        List list = flag ? this.lowPriorityQueue : this.highPriorityQueue;
        Object object = this.h;

        synchronized (this.h) {
            while (!list.isEmpty() && packet == null) {
                packet = (Packet) list.remove(0);
                this.y -= packet.a() + 1;
                if (this.a(packet, flag)) {
                    packet = null;
                }
            }

            return packet;
        }
    }

    private boolean a(Packet packet, boolean flag) {
        if (!packet.e()) {
            return false;
        } else {
            List list = flag ? this.lowPriorityQueue : this.highPriorityQueue;
            Iterator iterator = list.iterator();

            Packet packet1;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                packet1 = (Packet) iterator.next();
            } while (packet1.k() != packet.k());

            return packet.a(packet1);
        }
    }

    public void a() {
        if (this.u != null) {
            this.u.interrupt();
        }

        if (this.t != null) {
            this.t.interrupt();
        }
    }

    private boolean i() {
        boolean flag = false;

        try {
            Packet packet = Packet.a(this.input, this.packetListener.a(), this.socket);

            if (packet != null) {
                if (packet instanceof Packet252KeyResponse && !this.f) {
                    if (this.packetListener.a()) {
                        this.z = ((Packet252KeyResponse) packet).a(this.A);
                    }

                    this.j();
                }

                int[] aint = c;
                int i = packet.k();

                aint[i] += packet.a() + 1;
                if (!this.s) {
                    if (packet.a_() && this.packetListener.b()) {
                        this.x = 0;
                        packet.handle(this.packetListener);
                    } else {
                        this.inboundQueue.add(packet);
                    }
                }

                flag = true;
            } else {
                this.a("disconnect.endOfStream", new Object[0]);
            }

            return flag;
        } catch (Exception exception) {
            if (!this.n) {
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
        if (this.m) {
            this.n = true;
            this.v = s;
            this.w = aobject;
            this.m = false;
            (new NetworkMasterThread(this)).start();

            try {
                this.input.close();
                this.input = null;
                this.output.close();
                this.output = null;
                this.socket.close();
                this.socket = null;
            } catch (Throwable throwable) {
                ;
            }
        }
    }

    public void b() {
        if (this.y > 2097152) {
            this.a("disconnect.overflow", new Object[0]);
        }

        if (this.inboundQueue.isEmpty()) {
            if (this.x++ == 1200) {
                this.a("disconnect.timeout", new Object[0]);
            }
        } else {
            this.x = 0;
        }

        int i = 1000;

        while (!this.inboundQueue.isEmpty() && i-- >= 0) {
            Packet packet = (Packet) this.inboundQueue.poll(); // CraftBukkit - remove -> poll

            // CraftBukkit start
            if (this.packetListener instanceof NetLoginHandler ? ((NetLoginHandler) this.packetListener).c : ((NetServerHandler) this.packetListener).disconnected) {
                continue;
            }
            // CraftBukkit end

            packet.handle(this.packetListener);
        }

        this.a();
        if (this.n && this.inboundQueue.isEmpty()) {
            this.packetListener.a(this.v, this.w);
        }
    }

    public SocketAddress getSocketAddress() {
        return this.j;
    }

    public void d() {
        if (!this.s) {
            this.a();
            this.s = true;
            this.u.interrupt();
            (new NetworkMonitorThread(this)).start();
        }
    }

    private void j() throws IOException { // CraftBukkit - throws IOException
        this.f = true;
        this.input = new DataInputStream(MinecraftEncryption.a(this.z, this.socket.getInputStream()));
    }

    private void k() throws IOException { // CraftBukkit - throws IOException
        this.output.flush();
        this.g = true;
        this.output = new DataOutputStream(new BufferedOutputStream(MinecraftEncryption.a(this.z, this.socket.getOutputStream()), 5120));
    }

    public int e() {
        return this.lowPriorityQueue.size();
    }

    public Socket getSocket() {
        return this.socket;
    }

    static boolean a(NetworkManager networkmanager) {
        return networkmanager.m;
    }

    static boolean b(NetworkManager networkmanager) {
        return networkmanager.s;
    }

    static boolean c(NetworkManager networkmanager) {
        return networkmanager.i();
    }

    static boolean d(NetworkManager networkmanager) {
        return networkmanager.h();
    }

    static DataOutputStream e(NetworkManager networkmanager) {
        return networkmanager.output;
    }

    static boolean f(NetworkManager networkmanager) {
        return networkmanager.n;
    }

    static void a(NetworkManager networkmanager, Exception exception) {
        networkmanager.a(exception);
    }

    static Thread g(NetworkManager networkmanager) {
        return networkmanager.u;
    }

    static Thread h(NetworkManager networkmanager) {
        return networkmanager.t;
    }
}
