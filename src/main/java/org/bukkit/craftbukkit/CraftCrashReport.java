package org.bukkit.craftbukkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import net.minecraft.server.MinecraftServer;

public class CraftCrashReport implements Callable<Object> {

    public Object call() throws Exception {
        StringWriter value = new StringWriter();
        try {
            value.append("\n   Running: ").append(Bukkit.getName()).append(" version ").append(Bukkit.getVersion()).append(" (Implementing API version ").append(Bukkit.getBukkitVersion()).append(") ").append(String.valueOf(MinecraftServer.getServer().getOnlineMode()));
            value.append("\n   Plugins: {");
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                PluginDescriptionFile description = plugin.getDescription();
                value.append(' ').append(description.getFullName()).append(' ').append(description.getMain()).append(' ').append(Arrays.toString(description.getAuthors().toArray())).append(',');
            }
            value.append("}\n   Warnings: ").append(Bukkit.getWarningState().name());
            value.append("\n   Threads: {");
            for (Map.Entry<Thread, ? extends Object[]> entry : Thread.getAllStackTraces().entrySet()) {
                value.append(' ').append(entry.getKey().getState().name()).append(' ').append(entry.getKey().getName()).append(": ").append(Arrays.toString(entry.getValue())).append(',');
            }
            value.append("}\n   ").append(Bukkit.getScheduler().toString());
        } catch (Throwable t) {
            value.append("\n   Failed to handle CraftCrashReport:\n");
            PrintWriter writer = new PrintWriter(value);
            t.printStackTrace(writer);
            writer.flush();
        }
        return value.toString();
    }

}
