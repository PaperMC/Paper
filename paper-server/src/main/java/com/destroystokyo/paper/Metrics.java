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
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
 * Check out https://bstats.org/ to learn more about bStats . (as a code owner or a developer)
 */
public class Metrics {

    // Executor service for requests
    // We use an executor service because the Bukkit scheduler is affected by server lags
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
     *
     * @param name              The name of the server software.
     * @param serverUUID        The uuid of the server.
     * @param logFailedRequests Whether failed requests should be logged or not.
     * @param logger            The logger for the failed requests.
     */
    public Metrics(String name, String serverUUID, boolean logFailedRequests, Logger logger) {
        this.name = name;
        this.serverUUID = serverUUID;
        Metrics.logFailedRequests = logFailedRequests;
        Metrics.logger = logger;

        // Start submitting the data
        startSubmitting();
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(CustomChart chart) {
        if (chart == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
        }
        charts.add(chart);
    }

    /**
     * Starts the Scheduler which submits our data every 30 minutes.
     */
    private void startSubmitting() {
        final Runnable submitTask = () -> {
            if (!MinecraftServer.getServer().hasStopped()) {
                submitData();
            }
        };

        // Many servers tend to restart at a fixed time at xx:00 which causes an uneven distribution of requests on the
        // bStats backend. To circumvent this problem, we introduce some randomness into the initial and second delay.
        // WARNING: You must not modify any part of this Metrics class, including the submit delay or frequency!
        // WARNING: Modifying this code will get your plugin banned on bStats. Just don't do it!
        long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
        long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
        scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the plugin specific data.
     *
     * @return The plugin specific data.
     */
    private JSONObject getPluginData() {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("serverUUID", serverUUID);
        JSONArray chartData = new JSONArray();
        for (CustomChart chart : charts) {
            JSONObject chartJson = chart.getRequestJsonObject();
            if (chartJson != null) {
                chartData.add(chartJson);
            }
        }
        data.put("customCharts", chartData);
        return data;
    }

    /**
     * Gets the server specific data.
     *
     * @return The server specific data.
     */
    private JSONObject getServerData() {
        JSONObject data = new JSONObject();
        data.put("serverUUID", serverUUID);
        data.put("bukkitVersion", Bukkit.getVersion());
        data.put("bukkitName", Bukkit.getName());
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("osName", System.getProperty("os.name"));
        data.put("osArch", System.getProperty("os.arch"));
        data.put("osVersion", System.getProperty("os.version"));
        data.put("coreCount", Runtime.getRuntime().availableProcessors());
        return data;
    }

    /**
     * Collects the data and sends it afterwards.
     */
    private void submitData() {
        JSONObject data = getServerData();
        data.put("plugins", Collections.singletonList(getPluginData()));
        try {
            sendData(data);
        } catch (Exception e) {
            if (logFailedRequests) {
                logger.log(Level.WARNING, "Could not submit bStats metrics data", e);
            }
        }
    }

    /**
     * Sends the data to the bStats server.
     *
     * @param data The data to send.
     * @throws Exception If the request failed.
     */
    private void sendData(JSONObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.addRequestProperty("User-Agent", "bStats/" + B_STATS_VERSION);
        connection.setDoOutput(true);
        // The bStats API expects the data to be compressed using GZIP
        byte[] compressedData = compress(data.toString());
        connection.addRequestProperty("Content-Encoding", "gzip");
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.write(compressedData);
            out.flush();
        }
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Invalid response from bStats server: " + responseCode);
        }
    }

    /**
     * Gzips the given String.
     *
     * @param str The string to gzip.
     * @return The gzipped String.
     * @throws IOException If the compression failed.
     */
    private byte[] compress(final String str) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("String to compress cannot be null!");
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(byteStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        // Important: GZIPOutputStream must be finished to ensure all data is written
        // This is usually handled by closing, but let's be explicit
        // gzip.close(); // Not needed with try-with-resources, but left for clarity
        return byteStream.toByteArray();
    }

    /**
     * Represents a custom chart.
     */
    public abstract static class CustomChart {
        private final String chartId;

        public CustomChart(String chartId) {
            if (chartId == null || chartId.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
            this.chartId = chartId;
        }

        protected JSONObject getRequestJsonObject() {
            JSONObject chart = new JSONObject();
            chart.put("chartId", chartId);
            JSONObject data = getChartData();
            if (data == null) {
                // If the data is null, we don't send the chart.
                return null;
            }
            chart.put("data", data);
            return chart;
        }

        protected abstract JSONObject getChartData();
    }

    /**
     * Represents a custom simple pie.
     */
    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() {
            JSONObject data = new JSONObject();
            String value;
            try {
                value = callable.call();
            } catch (Exception e) {
                return null;
            }
            if (value == null || value.isEmpty()) {
                return null;
            }
            data.put("value", value);
            return data;
        }
    }

    /**
     * Represents a custom advanced pie.
     */
    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() {
            JSONObject data = new JSONObject();
            Map<String, Integer> map;
            try {
                map = callable.call();
            } catch (Exception e) {
                return null;
            }
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
            return data;
        }
    }

    /**
     * Represents a custom drilldown pie.
     */
    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() {
            JSONObject data = new JSONObject();
            Map<String, Map<String, Integer>> map;
            try {
                map = callable.call();
            } catch (Exception e) {
                return null;
            }
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
                JSONObject value = new JSONObject();
                for (Map.Entry<String, Integer> valueEntry : entry.getValue().entrySet()) {
                    value.put(valueEntry.getKey(), valueEntry.getValue());
                }
                data.put(entry.getKey(), value);
            }
            return data;
        }
    }

    /**
     * Represents a custom single line chart.
     */
    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() {
            JSONObject data = new JSONObject();
            Integer value;
            try {
                value = callable.call();
            } catch (Exception e) {
                return null;
            }
            if (value == null) {
                return null;
            }
            data.put("value", value);
            return data;
        }
    }

    /**
     * Represents a custom multi line chart.
     */
    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JSONObject getChartData() {
            JSONObject data = new JSONObject();
            Map<String, Integer> map;
            try {
                map = callable.call();
            } catch (Exception e) {
                return null;
            }
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
            return data;
        }
    }

    // Example usage for PaperMC (registering custom charts)
    public static void registerPaperCharts(Metrics metrics) {
        if (metrics != null) {
            metrics.addCustomChart(new Metrics.SimplePie("online_mode", () -> Bukkit.getOnlineMode() ? "online" : "offline"));
            final String paperVersion;
            final String implVersion = org.bukkit.craftbukkit.Main.class.getPackage().getImplementationVersion();
            if (implVersion != null) {
                final String buildOrHash = implVersion.substring(implVersion.lastIndexOf('-') + 1);
                paperVersion = String.format("git-Paper-%s-%s", Bukkit.getServer().getMinecraftVersion(), buildOrHash);
            } else {
                paperVersion = "unknown";
            }
            metrics.addCustomChart(new Metrics.SimplePie("paper_version", () -> paperVersion));

            metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                String javaVersion = System.getProperty("java.version");
                Map<String, Integer> entry = new HashMap<>();
                entry.put(javaVersion, 1);

                // http://openjdk.java.net/jeps/223
                // Java decided to change their versioning scheme and in doing so modified the java.version system
                // property to return $major[.$minor][.$secuity][-ea], as opposed to 1.$major.0_$identifier
                // we can handle pre-9 by checking if the "major" is equal to "1", otherwise, 9+
                String majorVersion = javaVersion.split("\\.")[0];
                String release;

                int indexOf = javaVersion.lastIndexOf('.');

                if (majorVersion.equals("1")) {
                    release = "Java " + javaVersion.substring(0, indexOf);
                } else {
                    // of course, it really wouldn't be all that simple if they didn't add a quirk, now would it
                    // valid strings for the major may potentially include values such as -ea to deannotate a pre release (:
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

                // count legacy plugins
                int legacy = 0;
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    if (CraftMagicNumbers.isLegacy(plugin.getDescription())) {
                        legacy++;
                    }
                }

                // insert real value as lower dimension
                Map<String, Integer> entry = new HashMap<>();
                entry.put(String.valueOf(legacy), 1);

                // create buckets as higher dimension
                if (legacy == 0) {
                    map.put("0 \uD83D\uDE0E", entry); // :sunglasses:
                } else if (legacy <= 5) {
                    map.put("1-5", entry);
                } else if (legacy <= 10) {
                    map.put("6-10", entry);
                } else if (legacy <= 25) {
                    map.put("11-25", entry);
                } else if (legacy <= 50) {
                    map.put("26-50", entry);
                } else {
                    map.put("50+ \uD83D\uDE2D", entry); // :cry:
                }

                return map;
            }));
        }
    }
}
