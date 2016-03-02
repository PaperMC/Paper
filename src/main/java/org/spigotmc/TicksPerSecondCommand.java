package org.spigotmc;

import com.google.common.base.Joiner;
import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Iterables;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TicksPerSecondCommand extends Command
{

    public TicksPerSecondCommand(String name)
    {
        super( name );
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission( "bukkit.command.tps" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( !testPermission( sender ) )
        {
            return true;
        }

        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        String[] tpsAvg = new String[tps.length];

        for ( int i = 0; i < tps.length; i++) {
            tpsAvg[i] = format( tps[i] );
        }
        sender.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
        if (args.length > 0 && args[0].equals("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            sender.sendMessage(ChatColor.GOLD + "Current Memory Usage: " + ChatColor.GREEN + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");
            if (!hasShownMemoryWarning) {
                sender.sendMessage(ChatColor.RED + "Warning: " + ChatColor.GOLD + " Memory usage on modern garbage collectors is not a stable value and it is perfectly normal to see it reach max. Please do not pay it much attention.");
                hasShownMemoryWarning = true;
            }
        }
        // Paper end

        return true;
    }

    private boolean hasShownMemoryWarning; // Paper
    private static String format(double tps) // Paper - Made static
    {
        return ( ( tps > 18.0 ) ? ChatColor.GREEN : ( tps > 16.0 ) ? ChatColor.YELLOW : ChatColor.RED ).toString()
                + ( ( tps > 21.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 ); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
    }
}
