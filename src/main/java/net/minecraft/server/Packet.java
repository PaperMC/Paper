package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.craftbukkit.inventory.CraftItemStack; // CraftBukkit

public abstract class Packet {

    public static IntHashMap l = new IntHashMap();
    private static Map a = new HashMap();
    private static Set b = new HashSet();
    private static Set c = new HashSet();
    protected IConsoleLogManager m;
    public final long timestamp = MinecraftServer.aq();
    public static long o;
    public static long p;
    public static long q;
    public static long r;
    public boolean lowPriority;
    // CraftBukkit start - Calculate packet ID once - used a bunch of times
    private int packetID;

    public Packet() {
        packetID = ((Integer) a.get(this.getClass())).intValue();
    }
    // CraftBukkit end

    static void a(int i, boolean flag, boolean flag1, Class oclass) {
        if (l.b(i)) {
            throw new IllegalArgumentException("Duplicate packet id:" + i);
        } else if (a.containsKey(oclass)) {
            throw new IllegalArgumentException("Duplicate packet class:" + oclass);
        } else {
            l.a(i, oclass);
            a.put(oclass, Integer.valueOf(i));
            if (flag) {
                b.add(Integer.valueOf(i));
            }

            if (flag1) {
                c.add(Integer.valueOf(i));
            }
        }
    }

    public static Packet a(IConsoleLogManager iconsolelogmanager, int i) {
        try {
            Class oclass = (Class) l.get(i);

            return oclass == null ? null : (Packet) oclass.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
            iconsolelogmanager.severe("Skipping packet with id " + i);
            return null;
        }
    }

    public static void a(DataOutput dataoutput, byte[] abyte) throws IOException { // CraftBukkit - throws IOException
        dataoutput.writeShort(abyte.length);
        dataoutput.write(abyte);
    }

    public static byte[] b(DataInput datainput) throws IOException { // CraftBukkit - throws IOException
        short short1 = datainput.readShort();

        if (short1 < 0) {
            throw new IOException("Key was smaller than nothing!  Weird key!");
        } else {
            byte[] abyte = new byte[short1];

            datainput.readFully(abyte);
            return abyte;
        }
    }

    public final int n() {
        return packetID; // ((Integer) a.get(this.getClass())).intValue(); // CraftBukkit
    }

    public static Packet a(IConsoleLogManager iconsolelogmanager, DataInput datainput, boolean flag, Socket socket) throws IOException { // CraftBukkit - throws IOException
        boolean flag1 = false;
        Packet packet = null;
        int i = socket.getSoTimeout();

        int j;

        try {
            j = datainput.readUnsignedByte();
            if (flag && !c.contains(Integer.valueOf(j)) || !flag && !b.contains(Integer.valueOf(j))) {
                throw new IOException("Bad packet id " + j);
            }

            packet = a(iconsolelogmanager, j);
            if (packet == null) {
                throw new IOException("Bad packet id " + j);
            }

            packet.m = iconsolelogmanager;
            if (packet instanceof Packet254GetInfo) {
                socket.setSoTimeout(1500);
            }

            packet.a(datainput);
            ++o;
            p += (long) packet.a();
        } catch (EOFException eofexception) {
            //iconsolelogmanager.severe("Reached end of stream for " + socket.getInetAddress()); // CraftBukkit - remove unnecessary logging
            return null;
        }

        // CraftBukkit start
        catch (java.net.SocketTimeoutException exception) {
            iconsolelogmanager.info("Read timed out");
            return null;
        } catch (java.net.SocketException exception) {
            iconsolelogmanager.info("Connection reset");
            return null;
        }
        // CraftBukkit end

        PacketCounter.a(j, (long) packet.a());
        ++o;
        p += (long) packet.a();
        socket.setSoTimeout(i);
        return packet;
    }

    public static void a(Packet packet, DataOutput dataoutput) throws IOException { // CraftBukkit - throws IOException
        dataoutput.write(packet.n());
        packet.a(dataoutput);
        ++q;
        r += (long) packet.a();
    }

    public static void a(String s, DataOutput dataoutput) throws IOException { // CraftBukkit - throws IOException
        if (s.length() > 32767) {
            throw new IOException("String too big");
        } else {
            dataoutput.writeShort(s.length());
            dataoutput.writeChars(s);
        }
    }

    public static String a(DataInput datainput, int i) throws IOException { // CraftBukkit - throws IOException
        short short1 = datainput.readShort();

        if (short1 > i) {
            throw new IOException("Received string length longer than maximum allowed (" + short1 + " > " + i + ")");
        } else if (short1 < 0) {
            throw new IOException("Received string length is less than zero! Weird string!");
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            for (int j = 0; j < short1; ++j) {
                stringbuilder.append(datainput.readChar());
            }

            return stringbuilder.toString();
        }
    }

    public abstract void a(DataInput datainput) throws IOException; // CraftBukkit - throws IOException

    public abstract void a(DataOutput dataoutput) throws IOException; // CraftBukkit - throws IOException

    public abstract void handle(Connection connection);

    public abstract int a();

    public boolean e() {
        return false;
    }

    public boolean a(Packet packet) {
        return false;
    }

    public boolean a_() {
        return this instanceof Packet3Chat && !((Packet3Chat) this).message.startsWith("/"); // CraftBukkit - async chat
    }

    public String toString() {
        String s = this.getClass().getSimpleName();

        return s;
    }

    public static ItemStack c(DataInput datainput) throws IOException { // CraftBukkit - throws IOException
        ItemStack itemstack = null;
        short short1 = datainput.readShort();

        if (short1 >= 0) {
            byte b0 = datainput.readByte();
            short short2 = datainput.readShort();

            itemstack = new ItemStack(short1, b0, short2);
            itemstack.tag = d(datainput);
            // CraftBukkit start
            if (itemstack.tag != null) {
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
            }
            // CraftBukkit end
        }

        return itemstack;
    }

    public static void a(ItemStack itemstack, DataOutput dataoutput) throws IOException { // CraftBukkit - throws IOException
        if (itemstack == null || itemstack.getItem() == null) { // CraftBukkit - NPE fix itemstack.getItem()
            dataoutput.writeShort(-1);
        } else {
            dataoutput.writeShort(itemstack.id);
            dataoutput.writeByte(itemstack.count);
            dataoutput.writeShort(itemstack.getData());
            NBTTagCompound nbttagcompound = null;

            if (itemstack.getItem().usesDurability() || itemstack.getItem().s()) {
                nbttagcompound = itemstack.tag;
            }

            a(nbttagcompound, dataoutput);
        }
    }

    public static NBTTagCompound d(DataInput datainput) throws IOException { // CraftBukkit - throws IOException
        short short1 = datainput.readShort();

        if (short1 < 0) {
            return null;
        } else {
            byte[] abyte = new byte[short1];

            datainput.readFully(abyte);
            return NBTCompressedStreamTools.a(abyte);
        }
    }

    protected static void a(NBTTagCompound nbttagcompound, DataOutput dataoutput) throws IOException { // CraftBukkit - throws IOException
        if (nbttagcompound == null) {
            dataoutput.writeShort(-1);
        } else {
            byte[] abyte = NBTCompressedStreamTools.a(nbttagcompound);

            dataoutput.writeShort((short) abyte.length);
            dataoutput.write(abyte);
        }
    }

    static {
        a(0, true, true, Packet0KeepAlive.class);
        a(1, true, true, Packet1Login.class);
        a(2, false, true, Packet2Handshake.class);
        a(3, true, true, Packet3Chat.class);
        a(4, true, false, Packet4UpdateTime.class);
        a(5, true, false, Packet5EntityEquipment.class);
        a(6, true, false, Packet6SpawnPosition.class);
        a(7, false, true, Packet7UseEntity.class);
        a(8, true, false, Packet8UpdateHealth.class);
        a(9, true, true, Packet9Respawn.class);
        a(10, true, true, Packet10Flying.class);
        a(11, true, true, Packet11PlayerPosition.class);
        a(12, true, true, Packet12PlayerLook.class);
        a(13, true, true, Packet13PlayerLookMove.class);
        a(14, false, true, Packet14BlockDig.class);
        a(15, false, true, Packet15Place.class);
        a(16, true, true, Packet16BlockItemSwitch.class);
        a(17, true, false, Packet17EntityLocationAction.class);
        a(18, true, true, Packet18ArmAnimation.class);
        a(19, false, true, Packet19EntityAction.class);
        a(20, true, false, Packet20NamedEntitySpawn.class);
        a(22, true, false, Packet22Collect.class);
        a(23, true, false, Packet23VehicleSpawn.class);
        a(24, true, false, Packet24MobSpawn.class);
        a(25, true, false, Packet25EntityPainting.class);
        a(26, true, false, Packet26AddExpOrb.class);
        a(27, false, true, Packet27PlayerInput.class);
        a(28, true, false, Packet28EntityVelocity.class);
        a(29, true, false, Packet29DestroyEntity.class);
        a(30, true, false, Packet30Entity.class);
        a(31, true, false, Packet31RelEntityMove.class);
        a(32, true, false, Packet32EntityLook.class);
        a(33, true, false, Packet33RelEntityMoveLook.class);
        a(34, true, false, Packet34EntityTeleport.class);
        a(35, true, false, Packet35EntityHeadRotation.class);
        a(38, true, false, Packet38EntityStatus.class);
        a(39, true, false, Packet39AttachEntity.class);
        a(40, true, false, Packet40EntityMetadata.class);
        a(41, true, false, Packet41MobEffect.class);
        a(42, true, false, Packet42RemoveMobEffect.class);
        a(43, true, false, Packet43SetExperience.class);
        a(44, true, false, Packet44UpdateAttributes.class);
        a(51, true, false, Packet51MapChunk.class);
        a(52, true, false, Packet52MultiBlockChange.class);
        a(53, true, false, Packet53BlockChange.class);
        a(54, true, false, Packet54PlayNoteBlock.class);
        a(55, true, false, Packet55BlockBreakAnimation.class);
        a(56, true, false, Packet56MapChunkBulk.class);
        a(60, true, false, Packet60Explosion.class);
        a(61, true, false, Packet61WorldEvent.class);
        a(62, true, false, Packet62NamedSoundEffect.class);
        a(63, true, false, Packet63WorldParticles.class);
        a(70, true, false, Packet70Bed.class);
        a(71, true, false, Packet71Weather.class);
        a(100, true, false, Packet100OpenWindow.class);
        a(101, true, true, Packet101CloseWindow.class);
        a(102, false, true, Packet102WindowClick.class);
        a(103, true, false, Packet103SetSlot.class);
        a(104, true, false, Packet104WindowItems.class);
        a(105, true, false, Packet105CraftProgressBar.class);
        a(106, true, true, Packet106Transaction.class);
        a(107, true, true, Packet107SetCreativeSlot.class);
        a(108, false, true, Packet108ButtonClick.class);
        a(130, true, true, Packet130UpdateSign.class);
        a(131, true, false, Packet131ItemData.class);
        a(132, true, false, Packet132TileEntityData.class);
        a(133, true, false, Packet133OpenTileEntity.class);
        a(200, true, false, Packet200Statistic.class);
        a(201, true, false, Packet201PlayerInfo.class);
        a(202, true, true, Packet202Abilities.class);
        a(203, true, true, Packet203TabComplete.class);
        a(204, false, true, Packet204LocaleAndViewDistance.class);
        a(205, false, true, Packet205ClientCommand.class);
        a(206, true, false, Packet206SetScoreboardObjective.class);
        a(207, true, false, Packet207SetScoreboardScore.class);
        a(208, true, false, Packet208SetScoreboardDisplayObjective.class);
        a(209, true, false, Packet209SetScoreboardTeam.class);
        a(250, true, true, Packet250CustomPayload.class);
        a(252, true, true, Packet252KeyResponse.class);
        a(253, true, false, Packet253KeyRequest.class);
        a(254, false, true, Packet254GetInfo.class);
        a(255, true, true, Packet255KickDisconnect.class);
    }
}
