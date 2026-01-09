package io.papermc.papergsk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages player bans and appeals
 */
public class BanManager {
    
    private final File banDataFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<UUID, Map<String, Object>> banRecords = new ConcurrentHashMap<>();
    
    public BanManager() {
        File dataFolder = new File(Bukkit.getServer().getWorldContainer(), "papergsk");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.banDataFile = new File(dataFolder, "bans.json");
        loadBans();
    }
    
    public String banPlayer(UUID playerUuid, String reason) {
        String appealId = generateAppealId();
        
        Map<String, Object> banRecord = new HashMap<>();
        banRecord.put("uuid", playerUuid.toString());
        banRecord.put("reason", reason);
        banRecord.put("timestamp", Instant.now().toString());
        banRecord.put("appealId", appealId);
        
        banRecords.put(playerUuid, banRecord);
        saveBans();
        
        Bukkit.getLogger().info("Banned player " + playerUuid + " with appeal ID " + appealId);
        return appealId;
    }
    
    public boolean unbanPlayer(String identifier) {
        // Try to find by UUID
        try {
            UUID uuid = UUID.fromString(identifier);
            if (banRecords.remove(uuid) != null) {
                saveBans();
                return true;
            }
        } catch (IllegalArgumentException e) {
            // Not a valid UUID, try as appeal ID
        }
        
        // Try to find by appeal ID
        for (Iterator<Map.Entry<UUID, Map<String, Object>>> it = banRecords.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Map<String, Object>> entry = it.next();
            if (identifier.equals(entry.getValue().get("appealId"))) {
                it.remove();
                saveBans();
                Bukkit.getLogger().info("Unbanned player with appeal ID " + identifier);
                return true;
            }
        }
        
        return false;
    }
    
    public Map<String, Object> getBanRecord(String identifier) {
        // Try UUID
        try {
            UUID uuid = UUID.fromString(identifier);
            Map<String, Object> record = banRecords.get(uuid);
            if (record != null) return new HashMap<>(record);
        } catch (IllegalArgumentException e) {
            // Not a valid UUID, try as appeal ID
        }
        
        // Try appeal ID
        for (Map<String, Object> record : banRecords.values()) {
            if (identifier.equals(record.get("appealId"))) {
                return new HashMap<>(record);
            }
        }
        
        return null;
    }
    
    public boolean isBanned(UUID playerUuid) {
        return banRecords.containsKey(playerUuid);
    }
    
    private String generateAppealId() {
        return "APPEAL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void saveBans() {
        try {
            if (!banDataFile.getParentFile().exists()) {
                banDataFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(banDataFile)) {
                gson.toJson(new ArrayList<>(banRecords.values()), writer);
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save bans: " + e.getMessage());
        }
    }
    
    private void loadBans() {
        if (!banDataFile.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(banDataFile)) {
            List<Map<String, Object>> records = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>(){}.getType());
            if (records != null) {
                for (Map<String, Object> record : records) {
                    try {
                        UUID uuid = UUID.fromString((String) record.get("uuid"));
                        banRecords.put(uuid, record);
                    } catch (Exception e) {
                        Bukkit.getLogger().warning("Failed to load ban record: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to load bans: " + e.getMessage());
        }
    }
}
