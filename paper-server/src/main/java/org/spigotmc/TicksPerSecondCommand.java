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
    // Paper start
    private static final net.kyori.adventure.text.Component WARN_MSG = net.kyori.adventure.text.Component.text()
        .append(net.kyori.adventure.text.Component.text("Warning: ", net.kyori.adventure.text.format.NamedTextColor.RED))
        .append(net.kyori.adventure.text.Component.text("Memory usage on modern garbage collectors is not a stable value and it is perfectly normal to see it reach max. Please do not pay it much attention.", net.kyori.adventure.text.format.NamedTextColor.GOLD))
        .build();
    // Paper end

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( !this.testPermission( sender ) )
        {
            return true;
        }

        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        net.kyori.adventure.text.Component[] tpsAvg = new net.kyori.adventure.text.Component[tps.length];

        for ( int i = 0; i < tps.length; i++) {
            tpsAvg[i] = TicksPerSecondCommand.format( tps[i] );
        }

        net.kyori.adventure.text.TextComponent.Builder builder = net.kyori.adventure.text.Component.text();
        builder.append(net.kyori.adventure.text.Component.text("TPS from last 1m, 5m, 15m: ", net.kyori.adventure.text.format.NamedTextColor.GOLD));
        builder.append(net.kyori.adventure.text.Component.join(net.kyori.adventure.text.JoinConfiguration.commas(true), tpsAvg));
        sender.sendMessage(builder.asComponent());
        if (args.length > 0 && args[0].equals("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            sender.sendMessage(net.kyori.adventure.text.Component.text()
                .append(net.kyori.adventure.text.Component.text("Current Memory Usage: ", net.kyori.adventure.text.format.NamedTextColor.GOLD))
                .append(net.kyori.adventure.text.Component.text(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)", net.kyori.adventure.text.format.NamedTextColor.GREEN))
            );
            if (!this.hasShownMemoryWarning) {
                sender.sendMessage(WARN_MSG);
                this.hasShownMemoryWarning = true;
            }
        }
        // Paper end

        return true;
    }

    private boolean hasShownMemoryWarning; // Paper
    private static net.kyori.adventure.text.Component format(double tps) // Paper - Made static
    {
        // Paper
        net.kyori.adventure.text.format.TextColor color = ( ( tps > 18.0 ) ? net.kyori.adventure.text.format.NamedTextColor.GREEN : ( tps > 16.0 ) ? net.kyori.adventure.text.format.NamedTextColor.YELLOW : net.kyori.adventure.text.format.NamedTextColor.RED );
        String amount = Math.min(Math.round(tps * 100.0) / 100.0, 20.0) + (tps > 21.0  ? "*" : ""); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
        return net.kyori.adventure.text.Component.text(amount, color);
        // Paper end
    }
}
