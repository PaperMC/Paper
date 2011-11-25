package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import java.util.UUID; // CraftBukkit

public class PlayerNBTManager implements PlayerFileData, IDataManager {

    private static final Logger a = Logger.getLogger("Minecraft");
    private final File b;
    private final File c;
    private final File d;
    private final long e = System.currentTimeMillis();
    private final String f;
    private UUID uuid = null; // CraftBukkit

    public PlayerNBTManager(File file1, String s, boolean flag) {
        this.b = new File(file1, s);
        this.b.mkdirs();
        this.c = new File(this.b, "players");
        this.d = new File(this.b, "data");
        this.d.mkdirs();
        this.f = s;
        if (flag) {
            this.c.mkdirs();
        }

        this.f();
    }

    private void f() {
        try {
            File file1 = new File(this.b, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try {
                dataoutputstream.writeLong(this.e);
            } finally {
                dataoutputstream.close();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    public File a() { // CraftBukkit - prot to public. Also, hi zml2008.
        return this.b;
    }

    public void b() {
        try {
            File file1 = new File(this.b, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

            try {
                if (datainputstream.readLong() != this.e) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            } finally {
                datainputstream.close();
            }
        } catch (IOException ioexception) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader a(WorldProvider worldprovider) {
        File file1;

        if (worldprovider instanceof WorldProviderHell) {
            file1 = new File(this.b, "DIM-1");
            file1.mkdirs();
            return new ChunkLoader(file1, true);
        } else if (worldprovider instanceof WorldProviderSky) {
            file1 = new File(this.b, "DIM1");
            file1.mkdirs();
            return new ChunkLoader(file1, true);
        } else {
            return new ChunkLoader(this.b, true);
        }
    }

    public WorldData c() {
        File file1 = new File(this.b, "level.dat");
        NBTTagCompound nbttagcompound;
        NBTTagCompound nbttagcompound1;

        if (file1.exists()) {
            try {
                nbttagcompound = CompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
                nbttagcompound1 = nbttagcompound.l("Data");
                return new WorldData(nbttagcompound1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        file1 = new File(this.b, "level.dat_old");
        if (file1.exists()) {
            try {
                nbttagcompound = CompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
                nbttagcompound1 = nbttagcompound.l("Data");
                return new WorldData(nbttagcompound1);
            } catch (Exception exception1) {
                exception1.printStackTrace();
            }
        }

        return null;
    }

    public void a(WorldData worlddata, List list) {
        NBTTagCompound nbttagcompound = worlddata.a(list);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.a("Data", (NBTBase) nbttagcompound);

        try {
            File file1 = new File(this.b, "level.dat_new");
            File file2 = new File(this.b, "level.dat_old");
            File file3 = new File(this.b, "level.dat");

            CompressedStreamTools.a(nbttagcompound1, (OutputStream) (new FileOutputStream(file1)));
            if (file2.exists()) {
                file2.delete();
            }

            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }

            file1.renameTo(file3);
            if (file1.exists()) {
                file1.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(WorldData worlddata) {
        NBTTagCompound nbttagcompound = worlddata.a();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.a("Data", (NBTBase) nbttagcompound);

        try {
            File file1 = new File(this.b, "level.dat_new");
            File file2 = new File(this.b, "level.dat_old");
            File file3 = new File(this.b, "level.dat");

            CompressedStreamTools.a(nbttagcompound1, (OutputStream) (new FileOutputStream(file1)));
            if (file2.exists()) {
                file2.delete();
            }

            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }

            file1.renameTo(file3);
            if (file1.exists()) {
                file1.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(EntityHuman entityhuman) {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entityhuman.d(nbttagcompound);
            File file1 = new File(this.c, "_tmp_.dat");
            File file2 = new File(this.c, entityhuman.name + ".dat");

            CompressedStreamTools.a(nbttagcompound, (OutputStream) (new FileOutputStream(file1)));
            if (file2.exists()) {
                file2.delete();
            }

            file1.renameTo(file2);
        } catch (Exception exception) {
            a.warning("Failed to save player data for " + entityhuman.name);
        }
    }

    public void b(EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = this.a(entityhuman.name);

        if (nbttagcompound != null) {
            entityhuman.e(nbttagcompound);
        }
    }

    public NBTTagCompound a(String s) {
        try {
            File file1 = new File(this.c, s + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            a.warning("Failed to load player data for " + s);
        }

        return null;
    }

    public PlayerFileData d() {
        return this;
    }

    public void e() {}

    public File b(String s) {
        return new File(this.d, s + ".dat");
    }

    // CraftBukkit start
    public UUID getUUID() {
        if (uuid != null) return uuid;
        try {
            File file1 = new File(this.b, "uid.dat");
            if (!file1.exists()) {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(file1));
                uuid = UUID.randomUUID();
                dos.writeLong(uuid.getMostSignificantBits());
                dos.writeLong(uuid.getLeastSignificantBits());
                dos.close();
            }
            else {
                DataInputStream dis = new DataInputStream(new FileInputStream(file1));
                uuid = new UUID(dis.readLong(), dis.readLong());
                dis.close();
            }
            return uuid;
        }
        catch (IOException ex) {
            return null;
        }
    }
    // CraftBukkit end
}
