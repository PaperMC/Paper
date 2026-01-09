package io.papermc.papergsk;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight anti-cheat logging system for suspicious players
 */
public class SuspiciousPlayerLogger {
    
    private final Map<UUID, List<Map<String, Object>>> playerAnomalies = new ConcurrentHashMap<>();
    private final Set<UUID> flaggedPlayers = ConcurrentHashMap.newKeySet();
    
    public SuspiciousPlayerLogger() {
    }
    
    public void flagPlayer(UUID playerUuid) {
        flaggedPlayers.add(playerUuid);
    }
    
    public void flagPlayer(UUID playerUuid, String reason) {
        flaggedPlayers.add(playerUuid);
    }
    
    public void unflagPlayer(UUID playerUuid) {
        flaggedPlayers.remove(playerUuid);
    }
    
    public boolean isFlagged(UUID playerUuid) {
        return flaggedPlayers.contains(playerUuid);
    }
    
    public void logAnomaly(UUID playerUuid, String anomalyType, String details) {
        if (!isFlagged(playerUuid)) {
            return;
        }
        
        Map<String, Object> anomaly = new HashMap<>();
        anomaly.put("type", anomalyType);
        anomaly.put("details", details);
        anomaly.put("timestamp", Instant.now().toString());
        
        playerAnomalies.computeIfAbsent(playerUuid, k -> Collections.synchronizedList(new ArrayList<>()))
            .add(anomaly);
    }
    
    public List<Map<String, Object>> getAnomalies(UUID playerUuid) {
        return new ArrayList<>(playerAnomalies.getOrDefault(playerUuid, new ArrayList<>()));
    }
    
    public Set<UUID> getFlaggedPlayers() {
        return new HashSet<>(flaggedPlayers);
    }
}
