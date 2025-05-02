package com.destroystokyo.paper;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.plugin.Plugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 *
 * Check out https://bstats.org/ to learn more about bStats!
 */
public class Metrics {

    // Executor service for requests
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // The version of this bStats class
    public static final int B_STATS_VERSION = 1;

    // The url to which the data is sent
    private static final String URL = "https://bstats.org/submitData/server-implementation";

    // Should failed requests be logged?
    private static boolean logFailedRequests = false;

    // The logger for the failed requests
    private static Logger logger = Logger.getLogger("bStats");

    // The name of the server software
    private final String name;

    // The uuid of the server
    private final String serverUUID;

    // A list with all custom charts
    private final List<CustomChart> charts = new ArrayList<>();

    /**
     * Class constructor.
     */
    public Metrics(String name, String serverUUID, boolean logFailedRequests, Logger logger) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.serverUUID = Objects.requireNonNull(serverUUID, "serverUUID cannot be null");
        Metrics.logFailedRequests = logFailedRequests;
        Metrics.logger = Objects.requireNonNull(logger, "logger cannot be null");

        // Start submitting the data
        startSubmitting();
    }

    /**
     * Adds a custom chart.
     */
    public void addCustomChart(CustomChart chart) {
        charts.add(Objects.requireNonNull(chart, "chart cannot be null"));
    }

    private void startSubmitting() {
        // Add shutdown hook to properly cleanup scheduler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        final Runnable submitTask = () -> {
            if (!MinecraftServer.getServer().hasStopped()) {
                submitData();
            }
        };

        // Randomize initial delay as before
        long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
        long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
        scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
    }

    private JSONObject getPluginData() {
        JSONObject data = new JSONObject();
        data.put("pluginName", name);

        JSONArray customCharts = new JSONArray();
        for (CustomChart customChart : charts) {
            try {
                JSONObject chart = customChart.getRequestJsonObject();
                if (chart != null) {
                    customCharts.add(chart);
                }
            } catch (Exception e) {
                if (logFailedRequests) {
                    logger.log(Level.WARNING, "Failed to get data for chart " + customChart.chartId, e);
                }
            }
        }
        data.put("customCharts", customCharts);

        return data;
    }

    private JSONObject getServerData() {
        JSONObject data = new JSONObject();
        data.put("serverUUID", serverUUID);

        data.put("osName", System.getProperty("os.name"));
        data.put("osArch", System.getProperty("os.arch"));
        data.put("osVersion", System.getProperty("os.version"));
        data.put("coreCount", Runtime.getRuntime().availableProcessors());

        return data;
    }

    private void submitData() {
        try {
            JSONObject data = getServerData();
            JSONArray pluginData = new JSONArray();
            pluginData.add(getPluginData());
            data.put("plugins", pluginData);

            sendData(data);
        } catch (Exception e) {
            if (logFailedRequests) {
                logger.log(Level.WARNING, "Could not submit stats of " + name, e);
            }
        }
    }

    private static void sendData(JSONObject data) throws Exception {
        Objects.requireNonNull(data, "data cannot be null");
        
        byte[] compressedData;
        try {
            compressedData = compress(data.toString());
        } catch (IOException e) {
            throw new IOException("Failed to compress data", e);
        }

        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();
        try {
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(compressedData);
            }

            // Ensure the connection is completed
            try (InputStream in = connection.getInputStream()) {
                while (in.read() != -1) {}
            }
        } finally {
            connection.disconnect();
        }
    }

    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.finish();
            return outputStream.toByteArray();
        }
    }

    public static abstract class CustomChart {
        final String chartId;

        CustomChart(String chartId) {
            this.chartId = Objects.requireNonNull(chartId, "chartId cannot be null");
        }

        private JSONObject getRequestJsonObject() {
            JSONObject chart = new JSONObject();
            chart.put("chartId", chartId);
            try {
                JSONObject data = getChartData();
                if (data == null) {
                    return null;
                }
                chart.put("data", data);
            } catch (Exception e) {
                if (logFailedRequests) {
                    logger.log(Level.WARNING, "Failed to get data for custom chart with id " + chartId, e);
                }
                return null;
            }
            return chart;
        }

        protected abstract JSONObject getChartData() throws Exception;
    }

    // [All the specific chart implementations (SimplePie, AdvancedPie, etc.) remain the same]
    // ... (rest of the chart classes remain unchanged)

    public static class PaperMetrics {
        public static void startMetrics() {
            File configFile = new File(new File((File) MinecraftServer.getServer().options.valueOf("plugins"), "bStats"), "config.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            if (!config.isSet("serverUuid")) {
                config.addDefault("enabled", true);
                config.addDefault("serverUuid", UUID.randomUUID().toString());
                config.addDefault("logFailedRequests", false);

                config.options().header(
                    "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                    "To honor their work, you should not disable it.\n" +
                    "This has nearly no effect on the server performance!\n" +
                    "Check out https://bStats.org/ to learn more :)"
                ).copyDefaults(true);

                try {
                    configFile.getParentFile().mkdirs();
                    File tmpFile = new File(configFile.getParentFile(), configFile.getName() + ".tmp");
                    config.save(tmpFile);
                    Files.move(tmpFile.toPath(), configFile.toPath(), 
                             StandardCopyOption.REPLACE_EXISTING, 
                             StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to save bStats config", e);
                }
            }

            if (config.getBoolean("enabled", true)) {
                Metrics metrics = new Metrics("Paper", config.getString("serverUuid"), 
                    config.getBoolean("logFailedRequests", false), Bukkit.getLogger());

                // [All the existing metric charts setup remains the same]
                // ... (rest of the metric setup remains unchanged)
            }
        }
    }
}
