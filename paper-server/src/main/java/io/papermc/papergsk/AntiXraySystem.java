package io.papermc.papergsk;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced anti-xray system that detects suspicious mining patterns
 * and logs players who are likely xray cheating
 */
public class AntiXraySystem {
    
    private final Map<String, PlayerMiningData> miningData = new ConcurrentHashMap<>();
    private final Set<String> suspiciousMiners = ConcurrentHashMap.newKeySet();
    private final SuspiciousPlayerLogger antiCheatLogger;
    
    // Ore materials that are commonly x-rayed
    private static final Set<Material> VALUABLE_ORES = new HashSet<>(Arrays.asList(
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.EMERALD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.GOLD_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.LAPIS_ORE,
        Material.DEEPSLATE_LAPIS_ORE
    ));
    
    private static final int SUSPICIOUS_THRESHOLD = 8; // Find 8+ diamonds in 30 seconds
    private static final int TIME_WINDOW_MS = 30000; // 30 second window
    
    public AntiXraySystem(SuspiciousPlayerLogger antiCheatLogger) {
        this.antiCheatLogger = antiCheatLogger;
        // Audit timer disabled - cleanup happens on-demand
    }
    
    /**
     * Record when a player mines a valuable ore
     */
    public void recordOreBreak(Player player, Block block) {
        String playerName = player.getName();
        PlayerMiningData data = miningData.computeIfAbsent(playerName, k -> new PlayerMiningData());
        
        if (VALUABLE_ORES.contains(block.getType())) {
            data.recordOreMined(block.getType());
            
            // Check if player is suspicious
            if (data.isSuspicious()) {
                flagSuspiciousPlayer(player, data);
            }
        }
    }
    
    /**
     * Check if a player is suspicious based on mining patterns
     */
    private void flagSuspiciousPlayer(Player player, PlayerMiningData data) {
        String playerName = player.getName();
        
        if (!suspiciousMiners.contains(playerName)) {
            suspiciousMiners.add(playerName);
            antiCheatLogger.flagPlayer(player.getUniqueId(), "Xray detection: " + data.getSuspicionReason());
            
            Bukkit.getLogger().warning("[AntiXray] Suspicious mining pattern detected for " + playerName + 
                ": " + data.getSuspicionReason());
            Bukkit.getLogger().warning("[AntiXray] Diamonds: " + data.diamondCount + 
                ", Emeralds: " + data.emeraldCount + ", Lapis: " + data.lapisCount + 
                " in last 30 seconds");
        }
    }
    
    /**
     * Get suspicious miners list
     */
    public Set<String> getSuspiciousMiners() {
        return new HashSet<>(suspiciousMiners);
    }
    
    /**
     * Clear suspicious flag for a player
     */
    public void clearSuspicion(String playerName) {
        suspiciousMiners.remove(playerName);
        miningData.remove(playerName);
        Bukkit.getLogger().info("[AntiXray] Cleared suspicion for " + playerName);
    }
    
    /**
     * Start periodic audit of mining data - Disabled for safety
     */
    @Deprecated
    private void startAuditTimer() {
        // Disabled - cleanup happens on-demand
    }
    
    /**
     * Get mining report for a player
     */
    public String getMiningReport(Player player) {
        PlayerMiningData data = miningData.get(player.getName());
        if (data == null) {
            return "No mining data recorded for " + player.getName();
        }
        
        return String.format(
            "Mining Report for %s:\n  Diamonds: %d\n  Emeralds: %d\n  Gold: %d\n  Lapis: %d\n  Suspicious: %s",
            player.getName(), data.diamondCount, data.emeraldCount, 
            data.goldCount, data.lapisCount, data.isSuspicious()
        );
    }
    
    /**
     * Inner class to track player mining data
     */
    private static class PlayerMiningData {
        int diamondCount = 0;
        int emeraldCount = 0;
        int goldCount = 0;
        int lapisCount = 0;
        
        List<Long> diamondTimestamps = Collections.synchronizedList(new ArrayList<>());
        List<Long> emeraldTimestamps = Collections.synchronizedList(new ArrayList<>());
        List<Long> lapisTimestamps = Collections.synchronizedList(new ArrayList<>());
        
        void recordOreMined(Material ore) {
            long now = System.currentTimeMillis();
            
            switch (ore) {
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    diamondCount++;
                    diamondTimestamps.add(now);
                    break;
                case EMERALD_ORE:
                case DEEPSLATE_EMERALD_ORE:
                    emeraldCount++;
                    emeraldTimestamps.add(now);
                    break;
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    goldCount++;
                    break;
                case LAPIS_ORE:
                case DEEPSLATE_LAPIS_ORE:
                    lapisCount++;
                    lapisTimestamps.add(now);
                    break;
            }
        }
        
        boolean isSuspicious() {
            long now = System.currentTimeMillis();
            
            // Check for 8+ diamonds in 30 seconds
            long recentDiamonds = diamondTimestamps.stream()
                .filter(t -> now - t < TIME_WINDOW_MS)
                .count();
            
            if (recentDiamonds >= SUSPICIOUS_THRESHOLD) {
                return true;
            }
            
            // Check for 6+ emeralds in 30 seconds
            long recentEmeralds = emeraldTimestamps.stream()
                .filter(t -> now - t < TIME_WINDOW_MS)
                .count();
            
            return recentEmeralds >= 6;
        }
        
        String getSuspicionReason() {
            long now = System.currentTimeMillis();
            
            long recentDiamonds = diamondTimestamps.stream()
                .filter(t -> now - t < TIME_WINDOW_MS)
                .count();
            
            long recentEmeralds = emeraldTimestamps.stream()
                .filter(t -> now - t < TIME_WINDOW_MS)
                .count();
            
            if (recentDiamonds >= SUSPICIOUS_THRESHOLD) {
                return "Found " + recentDiamonds + " diamonds in 30 seconds";
            }
            
            if (recentEmeralds >= 6) {
                return "Found " + recentEmeralds + " emeralds in 30 seconds";
            }
            
            return "Suspicious mining pattern";
        }
        
        void cleanOldData(long now) {
            diamondTimestamps.removeIf(t -> now - t > TIME_WINDOW_MS);
            emeraldTimestamps.removeIf(t -> now - t > TIME_WINDOW_MS);
            lapisTimestamps.removeIf(t -> now - t > TIME_WINDOW_MS);
        }
    }
}
