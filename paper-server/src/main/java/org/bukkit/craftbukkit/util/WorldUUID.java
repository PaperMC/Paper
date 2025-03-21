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

    public static UUID getOrCreate(File worldDir) {
        File fileId = new File(worldDir, "uid.dat");
        if (fileId.exists()) {
            try (DataInputStream inputStream = new DataInputStream(new FileInputStream(fileId))) {
                return new UUID(inputStream.readLong(), inputStream.readLong());
            } catch (IOException ex) {
                LOGGER.warn("Failed to read {}, generating new random UUID", fileId, ex);
            }
        }

        UUID uuid = UUID.randomUUID();
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(fileId))) {
            outputStream.writeLong(uuid.getMostSignificantBits());
            outputStream.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            LOGGER.warn("Failed to write {}", fileId, ex);
        }
        return uuid;
    }
}
