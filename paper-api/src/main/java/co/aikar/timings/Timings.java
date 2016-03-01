/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import com.google.common.base.Preconditions;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
@SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"})
public final class Timings {

    final static List<CommandSender> requestingReport = Lists.newArrayList();
    private static final int MAX_HISTORY_FRAMES = 12;
    public static final Timing NULL_HANDLER = new NullTimingHandler();
    static boolean timingsEnabled = false;
    static boolean verboseEnabled = false;
    private static int historyInterval = -1;
    private static int historyLength = -1;
    private static boolean warnedAboutDeprecationOnEnable;

    private Timings() {}

    /**
     * Returns a Timing for a plugin corresponding to a name.
     *
     * @param plugin Plugin to own the Timing
     * @param name   Name of Timing
     * @return Handler
     */
    @NotNull
    public static Timing of(@NotNull Plugin plugin, @NotNull String name) {
        Timing pluginHandler = null;
        if (plugin != null) {
            pluginHandler = ofSafe(plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
        }
        return of(plugin, name, pluginHandler);
    }

    /**
     * <p>Returns a handler that has a groupHandler timer handler. Parent timers should not have their
     * start/stop methods called directly, as the children will call it for you.</p>
     *
     * Parent Timers are used to group multiple subsections together and get a summary of them combined
     * Parent Handler can not be changed after first call
     *
     * @param plugin       Plugin to own the Timing
     * @param name         Name of Timing
     * @param groupHandler Parent handler to mirror .start/stop calls to
     * @return Timing Handler
     */
    @NotNull
    public static Timing of(@NotNull Plugin plugin, @NotNull String name, @Nullable Timing groupHandler) {
        Preconditions.checkNotNull(plugin, "Plugin can not be null");
        Bukkit.getLogger().warning(String.format("Plugin '%s' is creating timing '%s' - this is deprecated behavior, please report it to the authors: %s", plugin.getName(), name, String.join(", ", plugin.getDescription().getAuthors())));
        return TimingsManager.getHandler(plugin.getName(), name, groupHandler);
    }

    /**
     * Returns a Timing object after starting it, useful for Java7 try-with-resources.
     *
     * try (Timing ignored = Timings.ofStart(plugin, someName)) {
     * // timed section
     * }
     *
     * @param plugin Plugin to own the Timing
     * @param name   Name of Timing
     * @return Timing Handler
     */
    @NotNull
    public static Timing ofStart(@NotNull Plugin plugin, @NotNull String name) {
        return ofStart(plugin, name, null);
    }

    /**
     * Returns a Timing object after starting it, useful for Java7 try-with-resources.
     *
     * try (Timing ignored = Timings.ofStart(plugin, someName, groupHandler)) {
     * // timed section
     * }
     *
     * @param plugin       Plugin to own the Timing
     * @param name         Name of Timing
     * @param groupHandler Parent handler to mirror .start/stop calls to
     * @return Timing Handler
     */
    @NotNull
    public static Timing ofStart(@NotNull Plugin plugin, @NotNull String name, @Nullable Timing groupHandler) {
        Timing timing = of(plugin, name, groupHandler);
        timing.startTiming();
        return timing;
    }

    /**
     * Gets whether or not the Spigot Timings system is enabled
     *
     * @return Enabled or not
     */
    public static boolean isTimingsEnabled() {
        return timingsEnabled;
    }

    /**
     * <p>Sets whether or not the Spigot Timings system should be enabled</p>
     *
     * Calling this will reset timing data.
     *
     * @param enabled Should timings be reported
     */
    public static void setTimingsEnabled(boolean enabled) {
        if (enabled && !warnedAboutDeprecationOnEnable) {
            Bukkit.getLogger().severe(PlainTextComponentSerializer.plainText().serialize(deprecationMessage()));
            warnedAboutDeprecationOnEnable = true;
        }
    }

    public static Component deprecationMessage() {
        return Component.text()
            .color(TextColor.color(0xffc93a))
            .append(Component.text("[!] The timings profiler is in no-op mode and will be fully removed in a later update."))
            .append(Component.newline())
            .append(Component.text("    We recommend migrating to the spark profiler."))
            .append(Component.newline())
            .append(
                Component.text("    For more information please visit: ")
                    .append(
                        Component.text()
                            .content("https://github.com/PaperMC/Paper/discussions/10565")
                            .clickEvent(ClickEvent.openUrl("https://github.com/PaperMC/Paper/discussions/10565")))
            )
            .build();
    }

    /**
     * <p>Sets whether or not the Timings should monitor at Verbose level.</p>
     *
     * <p>When Verbose is disabled, high-frequency timings will not be available.</p>
     *
     * @return Enabled or not
     */
    public static boolean isVerboseTimingsEnabled() {
        return verboseEnabled;
    }

    /**
     * <p>Sets whether or not the Timings should monitor at Verbose level.</p>
     *
     * When Verbose is disabled, high-frequency timings will not be available.
     * Calling this will reset timing data.
     *
     * @param enabled Should high-frequency timings be reported
     */
    public static void setVerboseTimingsEnabled(boolean enabled) {
        verboseEnabled = enabled;
        TimingsManager.needsRecheckEnabled = true;
    }

    /**
     * <p>Gets the interval between Timing History report generation.</p>
     *
     * Defaults to 5 minutes (6000 ticks)
     *
     * @return Interval in ticks
     */
    public static int getHistoryInterval() {
        return historyInterval;
    }

    /**
     * <p>Sets the interval between Timing History report generations.</p>
     *
     * <p>Defaults to 5 minutes (6000 ticks)</p>
     *
     * This will recheck your history length, so lowering this value will lower your
     * history length if you need more than 60 history windows.
     *
     * @param interval Interval in ticks
     */
    public static void setHistoryInterval(int interval) {
        historyInterval = Math.max(20*60, interval);
        // Recheck the history length with the new Interval
        if (historyLength != -1) {
            setHistoryLength(historyLength);
        }
    }

    /**
     * Gets how long in ticks Timings history is kept for the server.
     *
     * Defaults to 1 hour (72000 ticks)
     *
     * @return Duration in Ticks
     */
    public static int getHistoryLength() {
        return historyLength;
    }

    /**
     * Sets how long Timing History reports are kept for the server.
     *
     * Defaults to 1 hours(72000 ticks)
     *
     * This value is capped at a maximum of getHistoryInterval() * MAX_HISTORY_FRAMES (12)
     *
     * Will not reset Timing Data but may truncate old history if the new length is less than old length.
     *
     * @param length Duration in ticks
     */
    public static void setHistoryLength(int length) {
        // Cap at 12 History Frames, 1 hour at 5 minute frames.
        int maxLength = historyInterval * MAX_HISTORY_FRAMES;
        // For special cases of servers with special permission to bypass the max.
        // This max helps keep data file sizes reasonable for processing on Aikar's Timing parser side.
        // Setting this will not help you bypass the max unless Aikar has added an exception on the API side.
        if (System.getProperty("timings.bypassMax") != null) {
            maxLength = Integer.MAX_VALUE;
        }
        historyLength = Math.max(Math.min(maxLength, length), historyInterval);
        Queue<TimingHistory> oldQueue = TimingsManager.HISTORY;
        int frames = (getHistoryLength() / getHistoryInterval());
        if (length > maxLength) {
            Bukkit.getLogger().log(Level.WARNING, "Timings Length too high. Requested " + length + ", max is " + maxLength + ". To get longer history, you must increase your interval. Set Interval to " + Math.ceil(length / MAX_HISTORY_FRAMES) + " to achieve this length.");
        }
        TimingsManager.HISTORY = EvictingQueue.create(frames);
        TimingsManager.HISTORY.addAll(oldQueue);
    }

    /**
     * Resets all Timing Data
     */
    public static void reset() {
        TimingsManager.reset();
    }

    /**
     * Generates a report and sends it to the specified command sender.
     *
     * If sender is null, ConsoleCommandSender will be used.
     * @param sender The sender to send to, or null to use the ConsoleCommandSender
     */
    public static void generateReport(@Nullable CommandSender sender) {
        if (sender == null) {
            sender = Bukkit.getConsoleSender();
        }
        requestingReport.add(sender);
    }

    /**
     * Generates a report and sends it to the specified listener.
     * Use with {@link org.bukkit.command.BufferedCommandSender} to get full response when done!
     * @param sender The listener to send responses too.
     */
    public static void generateReport(@NotNull TimingsReportListener sender) {
        Preconditions.checkNotNull(sender);
        requestingReport.add(sender);
    }

    /*
    =================
    Protected API: These are for internal use only in Bukkit/CraftBukkit
    These do not have isPrimaryThread() checks in the startTiming/stopTiming
    =================
    */
    @NotNull
    static TimingHandler ofSafe(@NotNull String name) {
        return ofSafe(null, name, null);
    }

    @NotNull
    static Timing ofSafe(@Nullable Plugin plugin, @NotNull String name) {
        Timing pluginHandler = null;
        if (plugin != null) {
            pluginHandler = ofSafe(plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
        }
        return ofSafe(plugin != null ? plugin.getName() : "Minecraft - Invalid Plugin", name, pluginHandler);
    }

    @NotNull
    static TimingHandler ofSafe(@NotNull String name, @Nullable Timing groupHandler) {
        return ofSafe(null, name, groupHandler);
    }

    @NotNull
    static TimingHandler ofSafe(@Nullable String groupName, @NotNull String name, @Nullable Timing groupHandler) {
        return TimingsManager.getHandler(groupName, name, groupHandler);
    }
}

