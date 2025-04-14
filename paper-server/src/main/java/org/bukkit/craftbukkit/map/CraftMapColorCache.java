package org.bukkit.craftbukkit.map;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import net.minecraft.Util;
import org.bukkit.map.MapPalette;

public class CraftMapColorCache implements MapPalette.MapColorCache {

    private static final String MD5_CACHE_HASH = "E88EDD068D12D39934B40E8B6B124C83"; // 248 colors
    private static final File CACHE_FILE = new File("map-color-cache.dat");
    private byte[] cache;
    private final Logger logger;
    private boolean cached = false;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CraftMapColorCache(Logger logger) {
        this.logger = logger;
    }

    private static CraftMapColorCache dryRun() {
        CraftMapColorCache mapColorCache = new CraftMapColorCache(Logger.getGlobal());
        mapColorCache.cache = new byte[256 * 256 * 256];
        mapColorCache.buildCache();
        return mapColorCache;
    }

    // Builds and prints the md5 hash of the cache, this should be run when new map colors are added to update the MD5_CACHE_HASH string
    public static void main(String[] args) {
        CraftMapColorCache craftMapColorCache = CraftMapColorCache.dryRun();
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(craftMapColorCache.cache);
            System.out.println("MD5_CACHE_HASH: " + CraftMapColorCache.bytesToString(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToString(byte[] bytes) {
        char[] chars = "0123456789ABCDEF".toCharArray();

        StringBuilder builder = new StringBuilder();
        for (byte value : bytes) {
            int first = (value & 0xF0) >> 4;
            int second = value & 0x0F;
            builder.append(chars[first]);
            builder.append(chars[second]);
        }

        return builder.toString();
    }

    public CompletableFuture<Void> initCache() {
        Preconditions.checkState(!this.cached && !this.running.getAndSet(true), "Cache is already build or is currently being build");

        this.cache = new byte[256 * 256 * 256]; // Red, Green and Blue have each a range from 0 to 255 each mean we need space for 256 * 256 * 256 values
        if (CraftMapColorCache.CACHE_FILE.exists()) {
            byte[] fileContent;

            try (InputStream inputStream = new InflaterInputStream(new FileInputStream(CraftMapColorCache.CACHE_FILE))) {
                fileContent = inputStream.readAllBytes();
            } catch (IOException e) {
                this.logger.warning("Error while reading map color cache");
                e.printStackTrace();
                return CompletableFuture.completedFuture(null);
            }

            byte[] hash;
            try {
                hash = MessageDigest.getInstance("MD5").digest(fileContent);
            } catch (NoSuchAlgorithmException e) {
                this.logger.warning("Error while hashing map color cache");
                e.printStackTrace();
                return CompletableFuture.completedFuture(null);
            }

            if (!CraftMapColorCache.MD5_CACHE_HASH.equals(CraftMapColorCache.bytesToString(hash))) {
                this.logger.info("Map color cache hash invalid, rebuilding cache in the background");
                return this.buildAndSaveCache();
            } else {
                System.arraycopy(fileContent, 0, this.cache, 0, fileContent.length);
            }

            this.cached = true;
        } else {
            this.logger.info("Map color cache not found, building it in the background");
            return this.buildAndSaveCache();
        }

        return CompletableFuture.completedFuture(null);
    }

    private void buildCache() {
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    Color color = new Color(r, g, b);
                    this.cache[this.toInt(color)] = MapPalette.matchColor(color);
                }
            }
        }
    }

    private CompletableFuture<Void> buildAndSaveCache() {
        return CompletableFuture.runAsync(() -> {
            this.buildCache();

            if (!CraftMapColorCache.CACHE_FILE.exists()) {
                try {
                    if (!CraftMapColorCache.CACHE_FILE.createNewFile()) {
                        this.cached = true;
                        return;
                    }
                } catch (IOException e) {
                    this.logger.warning("Error while building map color cache");
                    e.printStackTrace();
                    this.cached = true;
                    return;
                }
            }

            try (OutputStream outputStream = new DeflaterOutputStream(new FileOutputStream(CraftMapColorCache.CACHE_FILE))) {
                outputStream.write(this.cache);
            } catch (IOException e) {
                this.logger.warning("Error while building map color cache");
                e.printStackTrace();
                this.cached = true;
                return;
            }

            this.cached = true;
            this.logger.info("Map color cache build successfully");
        }, Util.backgroundExecutor());
    }

    private int toInt(Color color) {
        return color.getRGB() & 0xFFFFFF;
    }

    @Override
    public boolean isCached() {
        return this.cached || (!this.running.get() && this.initCache().isDone());
    }

    @Override
    public byte matchColor(Color color) {
        Preconditions.checkState(this.isCached(), "Cache not build yet");

        return this.cache[this.toInt(color)];
    }
}
