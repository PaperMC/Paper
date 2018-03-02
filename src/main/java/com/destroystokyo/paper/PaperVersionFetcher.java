package com.destroystokyo.paper;

import com.destroystokyo.paper.util.VersionFetcher;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaperVersionFetcher implements VersionFetcher {
    private static final java.util.regex.Pattern VER_PATTERN = java.util.regex.Pattern.compile("^([0-9\\.]*)\\-.*R"); // R is an anchor, will always give '-R' at end
    private static final String GITHUB_BRANCH_NAME = "master";
    private static @Nullable String mcVer;

    @Override
    public long getCacheTime() {
        return 720000;
    }

    @Nonnull
    @Override
    public String getVersionMessage(@Nonnull String serverVersion) {
        String[] parts = serverVersion.substring("git-Paper-".length()).split("[-\\s]");
        String updateMessage = getUpdateStatusMessage("PaperMC/Paper", GITHUB_BRANCH_NAME, parts[0]);
        String history = getHistory();

        return history != null ? history + "\n" + updateMessage : updateMessage;
    }

    private static @Nullable String getMinecraftVersion() {
        if (mcVer == null) {
            java.util.regex.Matcher matcher = VER_PATTERN.matcher(org.bukkit.Bukkit.getBukkitVersion());
            if (matcher.find()) {
                String result = matcher.group();
                mcVer = result.substring(0, result.length() - 2); // strip 'R' anchor and trailing '-'
            } else {
                org.bukkit.Bukkit.getLogger().warning("Unable to match version to pattern! Report to PaperMC!");
                org.bukkit.Bukkit.getLogger().warning("Pattern: " + VER_PATTERN.toString());
                org.bukkit.Bukkit.getLogger().warning("Version: " + org.bukkit.Bukkit.getBukkitVersion());
            }
        }

        return mcVer;
    }

    private static String getUpdateStatusMessage(@Nonnull String repo, @Nonnull String branch, @Nonnull String versionInfo) {
        int distance;
        try {
            int jenkinsBuild = Integer.parseInt(versionInfo);
            distance = fetchDistanceFromSiteApi(jenkinsBuild, getMinecraftVersion());
        } catch (NumberFormatException ignored) {
            versionInfo = versionInfo.replace("\"", "");
            distance = fetchDistanceFromGitHub(repo, branch, versionInfo);
        }

        switch (distance) {
            case -1:
                return "Error obtaining version information";
            case 0:
                return "You are running the latest version";
            case -2:
                return "Unknown version";
            default:
                return "You are " + distance + " version(s) behind";
        }
    }

    private static int fetchDistanceFromSiteApi(int jenkinsBuild, @Nullable String siteApiVersion) {
        if (siteApiVersion == null) { return -1; }
        try {
            try (BufferedReader reader = Resources.asCharSource(
                new URL("https://papermc.io/api/v1/paper/" + siteApiVersion + "/latest"),
                Charsets.UTF_8
            ).openBufferedStream()) {
                JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                int latest = json.get("build").getAsInt();
                return latest - jenkinsBuild;
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Contributed by Techcable <Techcable@outlook.com> in GH-65
    private static int fetchDistanceFromGitHub(@Nonnull String repo, @Nonnull String branch, @Nonnull String hash) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/" + repo + "/compare/" + branch + "..." + hash).openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) return -2; // Unknown commit
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8))) {
                JsonObject obj = new Gson().fromJson(reader, JsonObject.class);
                String status = obj.get("status").getAsString();
                switch (status) {
                    case "identical":
                        return 0;
                    case "behind":
                        return obj.get("behind_by").getAsInt();
                    default:
                        return -1;
                }
            } catch (JsonSyntaxException | NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Nullable
    private String getHistory() {
        final VersionHistoryManager.VersionData data = VersionHistoryManager.INSTANCE.getVersionData();
        if (data == null) {
            return null;
        }

        final String oldVersion = data.getOldVersion();
        if (oldVersion == null) {
            return null;
        }

        return "Previous version: " + oldVersion;
    }
}
