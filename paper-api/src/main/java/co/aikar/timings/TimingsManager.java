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

import co.aikar.util.LoadingMap;
import com.google.common.collect.EvictingQueue;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
public final class TimingsManager {
    static final Map<TimingIdentifier, TimingHandler> TIMING_MAP = LoadingMap.of(
        new ConcurrentHashMap<>(4096, .5F), TimingHandler::new
    );
    public static final FullServerTickHandler FULL_SERVER_TICK = new FullServerTickHandler();
    public static final TimingHandler TIMINGS_TICK = Timings.ofSafe("Timings Tick", FULL_SERVER_TICK);
    public static final Timing PLUGIN_GROUP_HANDLER = Timings.ofSafe("Plugins");
    public static String url = "https://timings.aikar.co/";
    public static List<String> hiddenConfigs = new ArrayList<String>();
    public static boolean privacy = false;

    static final List<TimingHandler> HANDLERS = new ArrayList<>(1024);
    static final List<TimingHistory.MinuteReport> MINUTE_REPORTS = new ArrayList<>(64);

    static EvictingQueue<TimingHistory> HISTORY = EvictingQueue.create(12);
    static long timingStart = 0;
    static long historyStart = 0;
    static boolean needsFullReset = false;
    static boolean needsRecheckEnabled = false;

    private TimingsManager() {}

    /**
     * Resets all timing data on the next tick
     */
    static void reset() {
        needsFullReset = true;
    }

    /**
     * Ticked every tick by CraftBukkit to count the number of times a timer
     * caused TPS loss.
     */
    static void tick() {
        if (Timings.timingsEnabled) {
            boolean violated = FULL_SERVER_TICK.isViolated();

            for (TimingHandler handler : HANDLERS) {
                if (handler.isSpecial()) {
                    // We manually call this
                    continue;
                }
                handler.processTick(violated);
            }

            TimingHistory.playerTicks += Bukkit.getOnlinePlayers().size();
            TimingHistory.timedTicks++;
            // Generate TPS/Ping/Tick reports every minute
        }
    }
    static void stopServer() {
        Timings.timingsEnabled = false;
        recheckEnabled();
    }
    static void recheckEnabled() {
        synchronized (TIMING_MAP) {
            for (TimingHandler timings : TIMING_MAP.values()) {
                timings.checkEnabled();
            }
        }
        needsRecheckEnabled = false;
    }
    static void resetTimings() {
        if (needsFullReset) {
            // Full resets need to re-check every handlers enabled state
            // Timing map can be modified from async so we must sync on it.
            synchronized (TIMING_MAP) {
                for (TimingHandler timings : TIMING_MAP.values()) {
                    timings.reset(true);
                }
            }
            Bukkit.getLogger().log(Level.INFO, "Timings Reset");
            HISTORY.clear();
            needsFullReset = false;
            needsRecheckEnabled = false;
            timingStart = System.currentTimeMillis();
        } else {
            // Soft resets only need to act on timings that have done something
            // Handlers can only be modified on main thread.
            for (TimingHandler timings : HANDLERS) {
                timings.reset(false);
            }
        }

        HANDLERS.clear();
        MINUTE_REPORTS.clear();

        TimingHistory.resetTicks(true);
        historyStart = System.currentTimeMillis();
    }

    @NotNull
    static TimingHandler getHandler(@Nullable String group, @NotNull String name, @Nullable Timing parent) {
        return TIMING_MAP.get(new TimingIdentifier(group, name, parent));
    }


    /**
     * <p>Due to access restrictions, we need a helper method to get a Command TimingHandler with String group</p>
     *
     * Plugins should never call this
     *
     * @param pluginName Plugin this command is associated with
     * @param command    Command to get timings for
     * @return TimingHandler
     */
    @NotNull
    public static Timing getCommandTiming(@Nullable String pluginName, @NotNull Command command) {
        Plugin plugin = null;
        final Server server = Bukkit.getServer();
        if (!(server == null || pluginName == null ||
            "minecraft".equals(pluginName) || "bukkit".equals(pluginName) ||
            "spigot".equalsIgnoreCase(pluginName) || "paper".equals(pluginName)
        )) {
            plugin = server.getPluginManager().getPlugin(pluginName);
        }
        if (plugin == null) {
            // Plugin is passing custom fallback prefix, try to look up by class loader
            plugin = getPluginByClassloader(command.getClass());
        }
        if (plugin == null) {
            return Timings.ofSafe("Command: " + pluginName + ":" + command.getTimingName());
        }

        return Timings.ofSafe(plugin, "Command: " + pluginName + ":" + command.getTimingName());
    }

    /**
     * Looks up the class loader for the specified class, and if it is a PluginClassLoader, return the
     * Plugin that created this class.
     *
     * @param clazz Class to check
     * @return Plugin if created by a plugin
     */
    @Nullable
    public static Plugin getPluginByClassloader(@Nullable Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        final ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader instanceof PluginClassLoader) {
            PluginClassLoader pluginClassLoader = (PluginClassLoader) classLoader;
            return pluginClassLoader.getPlugin();
        }
        return null;
    }
}
