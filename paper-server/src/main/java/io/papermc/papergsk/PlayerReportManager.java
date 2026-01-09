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
 * Manages player reports for admin review
 */
public class PlayerReportManager {
    
    private final File reportsDataFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Map<String, Object>> reports = new ConcurrentHashMap<>();
    
    public PlayerReportManager() {
        File dataFolder = new File(Bukkit.getServer().getWorldContainer(), "papergsk");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.reportsDataFile = new File(dataFolder, "reports.json");
        loadReports();
    }
    
    public String submitReport(UUID reporter, UUID reported, String reason) {
        String reportId = generateReportId();
        
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", reportId);
        report.put("reporter", reporter.toString());
        report.put("reported", reported.toString());
        report.put("reason", reason);
        report.put("timestamp", Instant.now().toString());
        report.put("status", "OPEN");
        
        reports.put(reportId, report);
        saveReports();
        
        Bukkit.getLogger().info("Report " + reportId + " submitted by " + reporter);
        return reportId;
    }
    
    public Map<String, Object> getReport(String reportId) {
        Map<String, Object> report = reports.get(reportId);
        return report != null ? new HashMap<>(report) : null;
    }
    
    public Collection<Map<String, Object>> getReports() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> report : reports.values()) {
            result.add(new HashMap<>(report));
        }
        return result;
    }
    
    public Collection<Map<String, Object>> getOpenReports() {
        List<Map<String, Object>> openReports = new ArrayList<>();
        for (Map<String, Object> report : reports.values()) {
            if ("OPEN".equals(report.get("status"))) {
                openReports.add(new HashMap<>(report));
            }
        }
        return openReports;
    }
    
    public boolean updateReport(String reportId, String newStatus) {
        Map<String, Object> report = reports.get(reportId);
        if (report != null) {
            report.put("status", newStatus);
            saveReports();
            return true;
        }
        return false;
    }
    
    private String generateReportId() {
        return "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void saveReports() {
        try {
            if (!reportsDataFile.getParentFile().exists()) {
                reportsDataFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(reportsDataFile)) {
                gson.toJson(new ArrayList<>(reports.values()), writer);
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save reports: " + e.getMessage());
        }
    }
    
    private void loadReports() {
        if (!reportsDataFile.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(reportsDataFile)) {
            List<Map<String, Object>> reportsList = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>(){}.getType());
            if (reportsList != null) {
                for (Map<String, Object> report : reportsList) {
                    try {
                        String reportId = (String) report.get("reportId");
                        reports.put(reportId, report);
                    } catch (Exception e) {
                        Bukkit.getLogger().warning("Failed to load report: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to load reports: " + e.getMessage());
        }
    }
}
