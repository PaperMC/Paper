package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.craftbukkit.entity.CraftPlayer;
// CraftBukkit end

public class WorldNBTStorage implements IDataManager, PlayerFileData {

    private static final Logger log = Logger.getLogger("Minecraft");
    private final File baseDir;
    private final File playerDir;
    private final File dataDir;
    private final long sessionId = System.currentTimeMillis();
    private final String f;
    private UUID uuid = null; // CraftBukkit

    public WorldNBTStorage(File file1, String s, boolean flag) {
        this.baseDir = new File(file1, s);
        this.baseDir.mkdirs();
        this.playerDir = new File(this.baseDir, "players");
        this.dataDir = new File(this.baseDir, "data");
        this.dataDir.mkdirs();
        this.f = s;
        if (flag) {
            this.playerDir.mkdirs();
        }

        this.h();
    }

    private void h() {
        try {
            File file1 = new File(this.baseDir, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try {
                dataoutputstream.writeLong(this.sessionId);
            } finally {
                dataoutputstream.close();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    public File getDirectory() { // CraftBukkit - protected to public
        return this.baseDir;
    }

    public void checkSession() throws ExceptionWorldConflict { // CraftBukkit - throws ExceptionWorldConflict
        try {
            File file1 = new File(this.baseDir, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

            try {
                if (datainputstream.readLong() != this.sessionId) {
                    throw new ExceptionWorldConflict("The save is being accessed from another location, aborting");
                }
            } finally {
                datainputstream.close();
            }
        } catch (IOException ioexception) {
            throw new ExceptionWorldConflict("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader createChunkLoader(WorldProvider worldprovider) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    public WorldData getWorldData() {
        File file1 = new File(this.baseDir, "level.dat");
        NBTTagCompound nbttagcompound;
        NBTTagCompound nbttagcompound1;

        if (file1.exists()) {
            try {
                nbttagcompound = NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
                nbttagcompound1 = nbttagcompound.getCompound("Data");
                return new WorldData(nbttagcompound1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        file1 = new File(this.baseDir, "level.dat_old");
        if (file1.exists()) {
            try {
                nbttagcompound = NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
                nbttagcompound1 = nbttagcompound.getCompound("Data");
                return new WorldData(nbttagcompound1);
            } catch (Exception exception1) {
                exception1.printStackTrace();
            }
        }

        return null;
    }

    public void saveWorldData(WorldData worlddata, NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = worlddata.a(nbttagcompound);
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.set("Data", nbttagcompound1);

        try {
            File file1 = new File(this.baseDir, "level.dat_new");
            File file2 = new File(this.baseDir, "level.dat_old");
            File file3 = new File(this.baseDir, "level.dat");

            NBTCompressedStreamTools.a(nbttagcompound2, (OutputStream) (new FileOutputStream(file1)));
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

    public void saveWorldData(WorldData worlddata) {
        NBTTagCompound nbttagcompound = worlddata.a();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.set("Data", nbttagcompound);

        try {
            File file1 = new File(this.baseDir, "level.dat_new");
            File file2 = new File(this.baseDir, "level.dat_old");
            File file3 = new File(this.baseDir, "level.dat");

            NBTCompressedStreamTools.a(nbttagcompound1, (OutputStream) (new FileOutputStream(file1)));
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

    public void save(EntityHuman entityhuman) {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entityhuman.d(nbttagcompound);
            File file1 = new File(this.playerDir, entityhuman.name + ".dat.tmp");
            File file2 = new File(this.playerDir, entityhuman.name + ".dat");

            NBTCompressedStreamTools.a(nbttagcompound, (OutputStream) (new FileOutputStream(file1)));
            if (file2.exists()) {
                file2.delete();
            }

            file1.renameTo(file2);
        } catch (Exception exception) {
            log.warning("Failed to save player data for " + entityhuman.name);
        }
    }

    public void load(EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = this.getPlayerData(entityhuman.name);

        if (nbttagcompound != null) {
            // CraftBukkit start
            if (entityhuman instanceof EntityPlayer) {
                CraftPlayer player = (CraftPlayer) entityhuman.bukkitEntity;
                player.setFirstPlayed(new File(playerDir, entityhuman.name + ".dat").lastModified());
            }
            // CraftBukkit end
            entityhuman.e(nbttagcompound);
        }
    }

    public NBTTagCompound getPlayerData(String s) {
        try {
            File file1 = new File(this.playerDir, s + ".dat");

            if (file1.exists()) {
                return NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            log.warning("Failed to load player data for " + s);
        }

        return null;
    }

    public PlayerFileData getPlayerFileData() {
        return this;
    }

    public String[] getSeenPlayers() {
        String[] astring = this.playerDir.list();

        for (int i = 0; i < astring.length; ++i) {
            if (astring[i].endsWith(".dat")) {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }

        return astring;
    }

    public void a() {}

    public File getDataFile(String s) {
        return new File(this.dataDir, s + ".dat");
    }

    public String g() {
        return this.f;
    }

    // CraftBukkit start
    public UUID getUUID() {
        if (uuid != null) return uuid;
        try {
            File file1 = new File(this.baseDir, "uid.dat");
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

    public File getPlayerDir() {
        return playerDir;
    }
    // CraftBukkit end
}
