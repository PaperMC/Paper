package org.bukkit.craftbukkit.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class WorldUUID {

    private static final Logger LOGGER = LogUtils.getLogger();

    private WorldUUID() {
    }

    public static UUID getOrCreate(File baseDir) {
        File uid = new File(baseDir, "uid.dat");
        if (uid.exists()) {
            try (DataInputStream inputStream = new DataInputStream(new FileInputStream(uid))) {
                return new UUID(inputStream.readLong(), inputStream.readLong());
            } catch (IOException ex) {
                LOGGER.warn("Failed to read {}, generating new random UUID", uid, ex);
            }
        }

        UUID uuid = UUID.randomUUID();
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(uid))) {
            outputStream.writeLong(uuid.getMostSignificantBits());
            outputStream.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            LOGGER.warn("Failed to write {}", uid, ex);
        }
        return uuid;
    }
}
