package org.spigotmc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.text.DecimalFormat;

import static net.kyori.adventure.text.Component.text;

public class TicksPerSecondCommand extends Command {

    private boolean hasShownMemoryWarning; // Paper
    private static final ThreadLocal<DecimalFormat> ONE_DECIMAL_PLACES = ThreadLocal.withInitial(() -> {
        return new DecimalFormat("########0.0");
    });

    public TicksPerSecondCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission("bukkit.command.tps");
    }

    // Paper start
    private static final Component WARN_MSG = text()
        .append(text("Warning: ", NamedTextColor.RED))
        .append(text("Memory usage on modern garbage collectors is not a stable value and it is perfectly normal to see it reach max. Please do not pay it much attention.", NamedTextColor.GOLD))
        .build();
    // Paper end

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        Component[] tpsAvg = new Component[tps.length];

        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = TicksPerSecondCommand.format(tps[i]);
        }

        TextComponent.Builder builder = text();
        builder.append(text("TPS from last 1m, 5m, 15m: ", NamedTextColor.GOLD));
        builder.append(Component.join(JoinConfiguration.commas(true), tpsAvg));
        sender.sendMessage(builder.asComponent());
        if (args.length > 0 && args[0].equals("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            sender.sendMessage(text()
                .append(text("Current Memory Usage: ", NamedTextColor.GOLD))
                .append(text(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)", NamedTextColor.GREEN))
            );
            if (!this.hasShownMemoryWarning) {
                sender.sendMessage(WARN_MSG);
                this.hasShownMemoryWarning = true;
            }
        }
        // Paper end

        return true;
    }

    private static Component format(double tps) { // Paper - Made static
        // Paper start
        TextColor color = ((tps > 18.0) ? NamedTextColor.GREEN : (tps > 16.0) ? NamedTextColor.YELLOW : NamedTextColor.RED);
        String amount = ONE_DECIMAL_PLACES.get().format(tps); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
        return text(amount, color);
        // Paper end
    }
}
