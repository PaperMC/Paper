package org.spigotmc;

import net.minecraft.server.MinecraftServer;
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
        if ( !this.testPermission( sender ) )
        {
            return true;
        }

        StringBuilder sb = new StringBuilder( ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " );
        for ( double tps : MinecraftServer.getServer().recentTps )
        {
            sb.append( this.format( tps ) );
            sb.append( ", " );
        }
        sender.sendMessage( sb.substring( 0, sb.length() - 2 ) );
        sender.sendMessage(ChatColor.GOLD + "Current Memory Usage: " + ChatColor.GREEN + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: "
                + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");

        return true;
    }

    private String format(double tps)
    {
        return ( ( tps > 18.0 ) ? ChatColor.GREEN : ( tps > 16.0 ) ? ChatColor.YELLOW : ChatColor.RED ).toString()
                + ( ( tps > 20.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 );
    }
}
