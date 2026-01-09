package io.papermc.papergsk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages maintenance mode and allowlists
 */
public class MaintenanceManager {
    
    private final File maintenanceDataFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private volatile boolean maintenanceMode = false;
    private final Set<UUID> allowlist = ConcurrentHashMap.newKeySet();
    
    public MaintenanceManager() {
        File dataFolder = new File(Bukkit.getServer().getWorldContainer(), "papergsk");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.maintenanceDataFile = new File(dataFolder, "maintenance.json");
        loadMaintenanceData();
    }
    
    public void setMaintenanceMode(boolean enabled) {
        this.maintenanceMode = enabled;
        saveMaintenanceData();
        Bukkit.getLogger().info("Maintenance mode " + (enabled ? "enabled" : "disabled"));
    }
    
    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }
    
    public void addToAllowlist(UUID playerUuid) {
        allowlist.add(playerUuid);
        saveMaintenanceData();
        Bukkit.getLogger().info("Added " + playerUuid + " to maintenance allowlist");
    }
    
    public void removeFromAllowlist(UUID playerUuid) {
        allowlist.remove(playerUuid);
        saveMaintenanceData();
        Bukkit.getLogger().info("Removed " + playerUuid + " from maintenance allowlist");
    }
    
    public boolean isPlayerAllowed(UUID playerUuid) {
        return allowlist.contains(playerUuid);
    }
    
    public Set<UUID> getAllowlist() {
        return new HashSet<>(allowlist);
    }
    
    private void saveMaintenanceData() {
        try {
            if (!maintenanceDataFile.getParentFile().exists()) {
                maintenanceDataFile.getParentFile().mkdirs();
            }
            Map<String, Object> data = new HashMap<>();
            data.put("maintenanceMode", maintenanceMode);
            data.put("allowlist", new ArrayList<>(allowlist));
            
            try (FileWriter writer = new FileWriter(maintenanceDataFile)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save maintenance data: " + e.getMessage());
        }
    }
    
    private void loadMaintenanceData() {
        if (!maintenanceDataFile.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(maintenanceDataFile)) {
            Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
            if (data != null) {
                Object modeObj = data.get("maintenanceMode");
                if (modeObj instanceof Boolean) {
                    maintenanceMode = (Boolean) modeObj;
                }
                
                List<?> allowlistData = (List<?>) data.get("allowlist");
                if (allowlistData != null) {
                    for (Object uuid : allowlistData) {
                        try {
                            allowlist.add(UUID.fromString((String) uuid));
                        } catch (Exception e) {
                            Bukkit.getLogger().warning("Failed to load allowlist entry: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to load maintenance data: " + e.getMessage());
        }
    }
}
