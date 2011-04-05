package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// Craftbukkit start
import java.net.SocketException;
import java.net.SocketTimeoutException;
// Craftbukkit end

public abstract class Packet {

    private static Map a = new HashMap();
    private static Map b = new HashMap();
    public final long j = System.currentTimeMillis();
    public boolean k = false;
    private static HashMap c;
    private static int d;

    public Packet() {}

    static void a(int i, Class oclass) {
        if (a.containsKey(Integer.valueOf(i))) {
            throw new IllegalArgumentException("Duplicate packet id:" + i);
        } else if (b.containsKey(oclass)) {
            throw new IllegalArgumentException("Duplicate packet class:" + oclass);
        } else {
            a.put(Integer.valueOf(i), oclass);
            b.put(oclass, Integer.valueOf(i));
        }
    }

    // CraftBukkit start
    private static Map<Integer,Class<?>> e = new HashMap<Integer,Class<?>>();
    private static Map<Class<?>,Integer> f = new HashMap<Class<?>,Integer>();

    static void b(int i, Class<?> oclass) {
        if (e.containsKey(Integer.valueOf(i))) {
            throw new IllegalArgumentException("Duplicate packet id:" + i);
        } else if (f.containsKey(oclass)) {
            throw new IllegalArgumentException("Duplicate packet class:" + oclass);
        } else {
            e.put(Integer.valueOf(i), oclass);
            f.put(oclass, Integer.valueOf(i));
        }
    }
    // CraftBukkit end

    public static Packet a(int i) {
        try {
            Class oclass = (Class) a.get(Integer.valueOf(i));

            return oclass == null ? null : (Packet) oclass.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Skipping packet with id " + i);
            return null;
        }
    }

    public final int b() {
        // CraftBukkit b->f
        return ((Integer) f.get(this.getClass())).intValue();
    }

    // CraftBukkit
    public static Packet b(DataInputStream datainputstream) throws IOException {
        boolean flag = false;
        Packet packet = null;

        int i;

        try {
            i = datainputstream.read();
            if (i == -1) {
                return null;
            }

            packet = a(i);
            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            }

            packet.a(datainputstream);
        } catch (EOFException eofexception) {
            System.out.println("Reached end of stream");
            return null;
        }

        // Craftbukkit start
        catch (SocketTimeoutException exception) {
            System.out.println("Read timed out");
            return null;
        } catch (SocketException exception) {
            System.out.println("Connection reset");
            return null;
        }
        // Craftbukkit end

        PacketCounter packetcounter = (PacketCounter) c.get(Integer.valueOf(i));

        if (packetcounter == null) {
            packetcounter = new PacketCounter((EmptyClass1) null);
            c.put(Integer.valueOf(i), packetcounter);
        }

        packetcounter.a(packet.a());
        ++d;
        if (d % 1000 == 0) {
            ;
        }

        return packet;
    }

    // CraftBukkit
    public static void a(Packet packet, DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.write(packet.b());
        packet.a(dataoutputstream);
    }

    public abstract void a(DataInputStream datainputstream);

    public abstract void a(DataOutputStream dataoutputstream);

    public abstract void a(NetHandler nethandler);

    public abstract int a();

    static {
        // CraftBukkit start -- accepted packets from CLIENT
        a(0, Packet0KeepAlive.class);
        a(1, Packet1Login.class);
        a(2, Packet2Handshake.class);
        a(3, Packet3Chat.class);
        //a(4, Packet4UpdateTime.class);
        //a(5, Packet5EntityEquipment.class);
        //a(6, Packet6SpawnPosition.class);
        a(7, Packet7UseEntity.class);
        //a(8, Packet8UpdateHealth.class);
        a(9, Packet9Respawn.class);
        a(10, Packet10Flying.class);
        a(11, Packet11PlayerPosition.class);
        a(12, Packet12PlayerLook.class);
        a(13, Packet13PlayerLookMove.class);
        a(14, Packet14BlockDig.class);
        a(15, Packet15Place.class);
        a(16, Packet16BlockItemSwitch.class);
        //a(17, Packet17.class);
        a(18, Packet18ArmAnimation.class);
        a(19, Packet19EntityAction.class);
        //a(20, Packet20NamedEntitySpawn.class);
        //a(21, Packet21PickupSpawn.class);
        //a(22, Packet22Collect.class);
        //a(23, Packet23VehicleSpawn.class);
        //a(24, Packet24MobSpawn.class);
        //a(25, Packet25EntityPainting.class);
        a(27, Packet27.class);
        //a(28, Packet28EntityVelocity.class);
        //a(29, Packet29DestroyEntity.class);
        //a(30, Packet30Entity.class);
        //a(31, Packet31RelEntityMove.class);
        //a(32, Packet32EntityLook.class);
        //a(33, Packet33RelEntityMoveLook.class);
        //a(34, Packet34EntityTeleport.class);
        //a(38, Packet38EntityStatus.class);
        //a(39, Packet39AttachEntity.class);
        //a(40, Packet40EntityMetadata.class);
        //a(50, Packet50PreChunk.class);
        //a(51, Packet51MapChunk.class);
        //a(52, Packet52MultiBlockChange.class);
        //a(53, Packet53BlockChange.class);
        //a(54, Packet54PlayNoteBlock.class);
        //a(60, Packet60Explosion.class);
        //a(100, Packet100OpenWindow.class);
        a(101, Packet101CloseWindow.class);
        a(102, Packet102WindowClick.class);
        //a(103, Packet103SetSlot.class);
        //a(104, Packet104WindowItems.class);
        //a(105, Packet105CraftProgressBar.class);
        a(106, Packet106Transaction.class);
        a(130, Packet130UpdateSign.class);
        a(255, Packet255KickDisconnect.class);

        // CraftBukkit packets send from SERVER
        b(0, Packet0KeepAlive.class);
        b(1, Packet1Login.class);
        b(2, Packet2Handshake.class);
        b(3, Packet3Chat.class);
        b(4, Packet4UpdateTime.class);
        b(5, Packet5EntityEquipment.class);
        b(6, Packet6SpawnPosition.class);
        //b(7, Packet7UseEntity.class);
        b(8, Packet8UpdateHealth.class);
        b(9, Packet9Respawn.class);
        //b(10, Packet10Flying.class);
        //b(11, Packet11PlayerPosition.class);
        //b(12, Packet12PlayerLook.class);
        b(13, Packet13PlayerLookMove.class);
        //b(14, Packet14BlockDig.class);
        //b(15, Packet15Place.class);
        //b(16, Packet16BlockItemSwitch.class);
        b(17, Packet17.class);
        b(18, Packet18ArmAnimation.class);
        //b(19, Packet19EntityAction.class);
        b(20, Packet20NamedEntitySpawn.class);
        b(21, Packet21PickupSpawn.class);
        b(22, Packet22Collect.class);
        b(23, Packet23VehicleSpawn.class);
        b(24, Packet24MobSpawn.class);
        b(25, Packet25EntityPainting.class);
        //b(27, Packet27.class);
        b(28, Packet28EntityVelocity.class);
        b(29, Packet29DestroyEntity.class);
        //b(30, Packet30Entity.class);
        b(31, Packet31RelEntityMove.class);
        b(32, Packet32EntityLook.class);
        b(33, Packet33RelEntityMoveLook.class);
        b(34, Packet34EntityTeleport.class);
        b(38, Packet38EntityStatus.class);
        b(39, Packet39AttachEntity.class);
        b(40, Packet40EntityMetadata.class);
        b(50, Packet50PreChunk.class);
        b(51, Packet51MapChunk.class);
        b(52, Packet52MultiBlockChange.class);
        b(53, Packet53BlockChange.class);
        b(54, Packet54PlayNoteBlock.class);
        b(60, Packet60Explosion.class);
        b(70, Packet70Bed.class);
        b(100, Packet100OpenWindow.class);
        b(101, Packet101CloseWindow.class);
        //b(102, Packet102WindowClick.class);
        b(103, Packet103SetSlot.class);
        b(104, Packet104WindowItems.class);
        b(105, Packet105CraftProgressBar.class);
        b(106, Packet106Transaction.class);
        b(130, Packet130UpdateSign.class);
        b(255, Packet255KickDisconnect.class);
        // CraftBukkit end

        c = new HashMap();
        d = 0;
    }
}
