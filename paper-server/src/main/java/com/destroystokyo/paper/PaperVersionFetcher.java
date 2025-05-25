package com.destroystokyo.paper;

import com.destroystokyo.paper.util.VersionFetcher;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import io.papermc.paper.ServerBuildInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.StreamSupport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.slf4j.Logger;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

@DefaultQualifier(NonNull.class)
public class PaperVersionFetcher implements VersionFetcher {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static final org.apache.logging.log4j.Logger SIMPLE_LOGGER = org.apache.logging.log4j.LogManager.getRootLogger();
    private static final int DISTANCE_ERROR = -1;
    private static final int DISTANCE_UNKNOWN = -2;
    private static final String DOWNLOAD_PAGE = "https://papermc.io/downloads/paper";
    public static final String REPOSITORY = "PaperMC/Paper";

    @Override
    public long getCacheTime() {
        return 720000;
    }

    @Override
    public Component getVersionMessage() {
        final Component updateMessage;
        final ServerBuildInfo build = ServerBuildInfo.buildInfo();
        if (build.buildNumber().isEmpty() && build.gitCommit().isEmpty()) {
            updateMessage = text("You are running a development version without access to version information", color(0xFF5300));
        } else {
            updateMessage = getUpdateStatusMessage(REPOSITORY, build);
        }
        final @Nullable Component history = this.getHistory();

        return history != null ? Component.textOfChildren(updateMessage, Component.newline(), history) : updateMessage;
    }

    public static void getUpdateStatusStartupMessage(final String repo, final ServerBuildInfo build) {
        final net.kyori.adventure.text.logger.slf4j.ComponentLogger COMPONENT_LOGGER = net.kyori.adventure.text.logger.slf4j.ComponentLogger.logger(SIMPLE_LOGGER.getName());
        int distance = DISTANCE_ERROR;

        final OptionalInt buildNumber = build.buildNumber();
        if (buildNumber.isEmpty() && build.gitCommit().isEmpty()) {
            SIMPLE_LOGGER.warn("*** You are running a development version without access to version information ***");
        } else {
            if (buildNumber.isPresent()) {
                distance = fetchDistanceFromSiteApi(build, buildNumber.getAsInt());
            } else {
                final Optional<String> gitBranch = build.gitBranch();
                final Optional<String> gitCommit = build.gitCommit();
                if (gitBranch.isPresent() && gitCommit.isPresent()) {
                    distance = fetchDistanceFromGitHub(repo, gitBranch.get(), gitCommit.get());
                }
            }
            switch (distance) {
                case DISTANCE_ERROR -> SIMPLE_LOGGER.error("*** Error obtaining version information! Can't fetch version info ***");
                case 0 -> {
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("*************************************************************************************", NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("You can safely disregard the previous warning", NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("You're running the latest version!", NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("However, it's still recommended you check the downloads page", NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("In case a new minecraft version has been released", NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text(DOWNLOAD_PAGE, NamedTextColor.GREEN));
                    COMPONENT_LOGGER.info(net.kyori.adventure.text.Component.text("*************************************************************************************", NamedTextColor.GREEN));
                }
                case DISTANCE_UNKNOWN -> SIMPLE_LOGGER.warn("*** You are running an unknown version! Can't fetch version info ***");
                case 5 -> {
                    SIMPLE_LOGGER.error("*** You are " + distance + " builds behind!");
                    SIMPLE_LOGGER.error("*** Please download a new build from " + DOWNLOAD_PAGE + " ***");
                }
                default -> {
                    SIMPLE_LOGGER.warn("*** Currently you are " + distance + " builds behind");
                    SIMPLE_LOGGER.warn("*** It's highly recommended to download a new build from " + DOWNLOAD_PAGE + " ***");
                }
            };
        }
    }

    private static Component getUpdateStatusMessage(final String repo, final ServerBuildInfo build) {
        int distance = DISTANCE_ERROR;

        final OptionalInt buildNumber = build.buildNumber();
        if (buildNumber.isPresent()) {
            distance = fetchDistanceFromSiteApi(build, buildNumber.getAsInt());
        } else {
            final Optional<String> gitBranch = build.gitBranch();
            final Optional<String> gitCommit = build.gitCommit();
            if (gitBranch.isPresent() && gitCommit.isPresent()) {
                distance = fetchDistanceFromGitHub(repo, gitBranch.get(), gitCommit.get());
            }
        }

        return switch (distance) {
            case DISTANCE_ERROR -> text("Error obtaining version information", NamedTextColor.YELLOW);
            case 0 -> text("You are running the latest version", NamedTextColor.GREEN);
            case DISTANCE_UNKNOWN -> text("Unknown version", NamedTextColor.YELLOW);
            default -> text("You are " + distance + " version(s) behind", NamedTextColor.YELLOW)
                .append(Component.newline())
                .append(text("Download the new version at: ")
                    .append(text(DOWNLOAD_PAGE, NamedTextColor.GOLD)
                        .hoverEvent(text("Click to open", NamedTextColor.WHITE))
                        .clickEvent(ClickEvent.openUrl(DOWNLOAD_PAGE))));
        };
    }

    private static int fetchDistanceFromSiteApi(final ServerBuildInfo build, final int jenkinsBuild) {
        try {
            try (final BufferedReader reader = Resources.asCharSource(
                URI.create("https://api.papermc.io/v2/projects/paper/versions/" + build.minecraftVersionId()).toURL(),
                StandardCharsets.UTF_8
            ).openBufferedStream()) {
                final JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                final JsonArray builds = json.getAsJsonArray("builds");
                final int latest = StreamSupport.stream(builds.spliterator(), false)
                    .mapToInt(JsonElement::getAsInt)
                    .max()
                    .orElseThrow();
                return latest - jenkinsBuild;
            } catch (final JsonSyntaxException ex) {
                LOGGER.error("Error parsing json from Paper's downloads API", ex);
                return DISTANCE_ERROR;
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing version", e);
            return DISTANCE_ERROR;
        }
    }

    // Contributed by Techcable <Techcable@outlook.com> in GH-65
    private static int fetchDistanceFromGitHub(final String repo, final String branch, final String hash) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) URI.create("https://api.github.com/repos/%s/compare/%s...%s".formatted(repo, branch, hash)).toURL().openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) return DISTANCE_UNKNOWN; // Unknown commit
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final JsonObject obj = new Gson().fromJson(reader, JsonObject.class);
                final String status = obj.get("status").getAsString();
                return switch (status) {
                    case "identical" -> 0;
                    case "behind" -> obj.get("behind_by").getAsInt();
                    default -> DISTANCE_ERROR;
                };
            } catch (final JsonSyntaxException | NumberFormatException e) {
                LOGGER.error("Error parsing json from GitHub's API", e);
                return DISTANCE_ERROR;
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing version", e);
            return DISTANCE_ERROR;
        }
    }

    private @Nullable Component getHistory() {
        final VersionHistoryManager.@Nullable VersionData data = VersionHistoryManager.INSTANCE.getVersionData();
        if (data == null) {
            return null;
        }

        final @Nullable String oldVersion = data.getOldVersion();
        if (oldVersion == null) {
            return null;
        }

        return text("Previous version: " + oldVersion, NamedTextColor.GRAY, TextDecoration.ITALIC);
    }
}
