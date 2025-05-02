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
 * <p>
 * Check out https://bstats.org/ to learn more about bStats!
 */
@SuppressWarnings("unchecked")
public class Metrics {
    private static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bstats.org/submitData/server-implementation";
    private static boolean logFailedRequests;
    private static Logger logger;

    private final String name;
    private final String serverUUID;
    private final List<CustomChart> charts = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Metrics(String name, String serverUUID, boolean logFailedRequests, Logger logger) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.serverUUID = Objects.requireNonNull(serverUUID, "serverUUID cannot be null");
        Metrics.logFailedRequests = logFailedRequests;
        Metrics.logger = Objects.requireNonNull(logger, "logger cannot be null");
        startSubmitting();
    }

    public void addCustomChart(CustomChart chart) {
        charts.add(Objects.requireNonNull(chart, "chart cannot be null"));
    }

    private void startSubmitting() {
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
        byte[] compressedData = compress(data.toString());

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

            try (InputStream in = connection.getInputStream()) {
                while (in.read() != -1) {}
            }
        } finally {
            connection.disconnect();
        }
    }

    private static byte[] compress(final String str) throws IOException {
        if (str == null) return null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.finish();
            return outputStream.toByteArray();
        }
    }

    public abstract static class CustomChart {
        final String chartId;

        CustomChart(String chartId) {
            this.chartId = Objects.requireNonNull(chartId, "chartId cannot be null");
        }

        private JSONObject getRequestJsonObject() {
            JSONObject chart = new JSONObject();
            chart.put("chartId", chartId);
            try {
                JSONObject data = getChartData();
                if (data != null) {
                    chart.put("data", data);
                }
            } catch (Exception e) {
                if (logFailedRequests) {
                    logger.log(Level.WARNING, "Failed to get data for custom chart with id " + chartId, e);
                }
            }
            return chart;
        }

        protected abstract JSONObject getChartData() throws Exception;
    }

    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            String value = callable.call();
            if (value == null || value.isEmpty()) return null;
            data.put("value", value);
            return data;
        }
    }

    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) return null;

            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) continue;
                allSkipped = false;
                values.put(entry.getKey(), entry.getValue());
            }
            if (allSkipped) return null;

            data.put("values", values);
            return data;
        }
    }

    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        public JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, Map<String, Integer>> map = callable.call();
            if (map == null || map.isEmpty()) return null;

            boolean reallyAllSkipped = true;
            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                JSONObject value = new JSONObject();
                boolean allSkipped = true;
                for (Map.Entry<String, Integer> valueEntry : entryValues.getValue().entrySet()) {
                    value.put(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    values.put(entryValues.getKey(), value);
                }
            }
            if (reallyAllSkipped) return null;

            data.put("values", values);
            return data;
        }
    }

    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            int value = callable.call();
            if (value == 0) return null;
            data.put("value", value);
            return data;
        }
    }

    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) return null;

            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) continue;
                allSkipped = false;
                values.put(entry.getKey(), entry.getValue());
            }
            if (allSkipped) return null;

            data.put("values", values);
            return data;
        }
    }

    public static class SimpleBarChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, Integer> map = callable.call();
            if (map == null || map.isEmpty()) return null;

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                JSONArray categoryValues = new JSONArray();
                categoryValues.add(entry.getValue());
                values.put(entry.getKey(), categoryValues);
            }
            data.put("values", values);
            return data;
        }
    }

    public static class AdvancedBarChart extends CustomChart {
        private final Callable<Map<String, int[]>> callable;

        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject data = new JSONObject();
            JSONObject values = new JSONObject();
            Map<String, int[]> map = callable.call();
            if (map == null || map.isEmpty()) return null;

            boolean allSkipped = true;
            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) continue;
                allSkipped = false;
                JSONArray categoryValues = new JSONArray();
                for (int categoryValue : entry.getValue()) {
                    categoryValues.add(categoryValue);
                }
                values.put(entry.getKey(), categoryValues);
            }
            if (allSkipped) return null;

            data.put("values", values);
            return data;
        }
    }

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

                metrics.addCustomChart(new Metrics.SimplePie("minecraft_version", () -> {
                    String minecraftVersion = Bukkit.getVersion();
                    minecraftVersion = minecraftVersion.substring(minecraftVersion.indexOf("MC: ") + 4, minecraftVersion.length() - 1);
                    return minecraftVersion;
                }));

                metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
                metrics.addCustomChart(new Metrics.SimplePie("online_mode", () -> Bukkit.getOnlineMode() ? "online" : "offline"));
                
                final String paperVersion;
                final String implVersion = org.bukkit.craftbukkit.Main.class.getPackage().getImplementationVersion();
                if (implVersion != null) {
                    final String buildOrHash = implVersion.substring(implVersion.lastIndexOf('-') + 1);
                    paperVersion = "git-Paper-%s-%s".formatted(Bukkit.getServer().getMinecraftVersion(), buildOrHash);
                } else {
                    paperVersion = "unknown";
                }
                metrics.addCustomChart(new Metrics.SimplePie("paper_version", () -> paperVersion));

                metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
                    Map<String, Map<String, Integer>> map = new HashMap<>();
                    String javaVersion = System.getProperty("java.version");
                    Map<String, Integer> entry = new HashMap<>();
                    entry.put(javaVersion, 1);

                    String majorVersion = javaVersion.split("\\.")[0];
                    String release;

                    int indexOf = javaVersion.lastIndexOf('.');

                    if (majorVersion.equals("1")) {
                        release = "Java " + javaVersion.substring(0, indexOf);
                    } else {
                        Matcher versionMatcher = Pattern.compile("\\d+").matcher(majorVersion);
                        if (versionMatcher.find()) {
                            majorVersion = versionMatcher.group(0);
                        }
                        release = "Java " + majorVersion;
                    }
                    map.put(release, entry);

                    return map;
                }));

                metrics.addCustomChart(new Metrics.DrilldownPie("legacy_plugins", () -> {
                    Map<String, Map<String, Integer>> map = new HashMap<>();
                    int legacy = 0;
                    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                        if (CraftMagicNumbers.isLegacy(plugin.getDescription())) {
                            legacy++;
                        }
                    }

                    Map<String, Integer> entry = new HashMap<>();
                    entry.put(String.valueOf(legacy), 1);

                    if (legacy == 0) {
                        map.put("0 \uD83D\uDE0E", entry);
                    } else if (legacy <= 5) {
                        map.put("1-5", entry);
                    } else if (legacy <= 10) {
                        map.put("6-10", entry);
                    } else if (legacy <= 25) {
                        map.put("11-25", entry);
                    } else if (legacy <= 50) {
                        map.put("26-50", entry);
                    } else {
                        map.put("50+ \uD83D\uDE2D", entry);
                    }

                    return map;
                }));
            }
        }
    }
}
