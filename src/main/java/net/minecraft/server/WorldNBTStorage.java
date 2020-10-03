package net.minecraft.server;

import com.mojang.datafixers.DataFixer;
import java.io.File;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.io.FileInputStream;
import java.io.InputStream;
import org.bukkit.craftbukkit.entity.CraftPlayer;
// CraftBukkit end

public class WorldNBTStorage {

    private static final Logger LOGGER = LogManager.getLogger();
    private final File playerDir;
    protected final DataFixer a;

    public WorldNBTStorage(Convertable.ConversionSession convertable_conversionsession, DataFixer datafixer) {
        this.a = datafixer;
        this.playerDir = convertable_conversionsession.getWorldFolder(SavedFile.PLAYERDATA).toFile();
        this.playerDir.mkdirs();
    }

    public void save(EntityHuman entityhuman) {
        try {
            NBTTagCompound nbttagcompound = entityhuman.save(new NBTTagCompound());
            File file = File.createTempFile(entityhuman.getUniqueIDString() + "-", ".dat", this.playerDir);

            NBTCompressedStreamTools.a(nbttagcompound, file);
            File file1 = new File(this.playerDir, entityhuman.getUniqueIDString() + ".dat");
            File file2 = new File(this.playerDir, entityhuman.getUniqueIDString() + ".dat_old");

            SystemUtils.a(file1, file, file2);
        } catch (Exception exception) {
            WorldNBTStorage.LOGGER.warn("Failed to save player data for {}", entityhuman.getDisplayName().getString());
        }

    }

    @Nullable
    public NBTTagCompound load(EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = null;

        try {
            File file = new File(this.playerDir, entityhuman.getUniqueIDString() + ".dat");

            if (file.exists() && file.isFile()) {
                nbttagcompound = NBTCompressedStreamTools.a(file);
            }
        } catch (Exception exception) {
            WorldNBTStorage.LOGGER.warn("Failed to load player data for {}", entityhuman.getDisplayName().getString());
        }

        if (nbttagcompound != null) {
            // CraftBukkit start
            if (entityhuman instanceof EntityPlayer) {
                CraftPlayer player = (CraftPlayer) entityhuman.getBukkitEntity();
                // Only update first played if it is older than the one we have
                long modified = new File(this.playerDir, entityhuman.getUniqueID().toString() + ".dat").lastModified();
                if (modified < player.getFirstPlayed()) {
                    player.setFirstPlayed(modified);
                }
            }
            // CraftBukkit end
            int i = nbttagcompound.hasKeyOfType("DataVersion", 3) ? nbttagcompound.getInt("DataVersion") : -1;

            entityhuman.load(GameProfileSerializer.a(this.a, DataFixTypes.PLAYER, nbttagcompound, i));
        }

        return nbttagcompound;
    }

    // CraftBukkit start
    public NBTTagCompound getPlayerData(String s) {
        try {
            File file1 = new File(this.playerDir, s + ".dat");

            if (file1.exists()) {
                return NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load player data for " + s);
        }

        return null;
    }
    // CraftBukkit end

    public String[] getSeenPlayers() {
        String[] astring = this.playerDir.list();

        if (astring == null) {
            astring = new String[0];
        }

        for (int i = 0; i < astring.length; ++i) {
            if (astring[i].endsWith(".dat")) {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }

        return astring;
    }

    // CraftBukkit start
    public File getPlayerDir() {
        return playerDir;
    }
    // CraftBukkit end
}
