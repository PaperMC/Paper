package org.bukkit.craftbukkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class CraftCrashReport implements Supplier<String> {

    @Override
    public String get() {
        StringWriter value = new StringWriter();
        try {
            value.append("\n   Running: ").append(Bukkit.getName()).append(" version ").append(Bukkit.getVersion()).append(" (Implementing API version ").append(Bukkit.getBukkitVersion()).append(") ").append(String.valueOf(MinecraftServer.getServer().getOnlineMode()));
            value.append("\n   Plugins: {");
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                PluginDescriptionFile description = plugin.getDescription();
                boolean legacy = CraftMagicNumbers.isLegacy(description);
                value.append(' ').append(description.getFullName()).append(legacy ? "*" : "").append(' ').append(description.getMain()).append(' ').append(Arrays.toString(description.getAuthors().toArray())).append(',');
            }
            value.append("}\n   Warnings: ").append(Bukkit.getWarningState().name());
            value.append("\n   Reload Count: ").append(String.valueOf(MinecraftServer.getServer().server.reloadCount));
            value.append("\n   Threads: {");
            for (Map.Entry<Thread, ? extends Object[]> entry : Thread.getAllStackTraces().entrySet()) {
                value.append(' ').append(entry.getKey().getState().name()).append(' ').append(entry.getKey().getName()).append(": ").append(Arrays.toString(entry.getValue())).append(',');
            }
            value.append("}\n   ").append(Bukkit.getScheduler().toString());
            value.append("\n   Force Loaded Chunks: {");
            for (World world : Bukkit.getWorlds()) {
                value.append(' ').append(world.getName()).append(": {");
                for (Map.Entry<Plugin, Collection<Chunk>> entry : world.getPluginChunkTickets().entrySet()) {
                    value.append(' ').append(entry.getKey().getDescription().getFullName()).append(": ").append(Integer.toString(entry.getValue().size())).append(',');
                }
                value.append("},");
            }
            value.append("}");
        } catch (Throwable t) {
            value.append("\n   Failed to handle CraftCrashReport:\n");
            PrintWriter writer = new PrintWriter(value);
            t.printStackTrace(writer);
            writer.flush();
        }
        return value.toString();
    }

}
