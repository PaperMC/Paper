package org.spigotmc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.text.DecimalFormat;

import static net.kyori.adventure.text.Component.text;

public class TicksPerSecondCommand extends Command {

    private boolean hasShownMemoryWarning;
    private static final ThreadLocal<DecimalFormat> ONE_DECIMAL_PLACES = ThreadLocal.withInitial(() -> new DecimalFormat("########0.0"));

    public TicksPerSecondCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps [mem]";
        this.setPermission("bukkit.command.tps");
    }

    private static final Component WARN_MSG = text()
        .append(text("Warning: ", NamedTextColor.RED))
        .append(text("Memory usage on modern garbage collectors is not a stable value and it is perfectly normal to see it reach max. Please do not pay it much attention.", NamedTextColor.GOLD))
        .build();

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        // 1. Daten holen (WICHTIG: getAverageTickTime() gibt ein double zurück, kein Array!)
        double[] tps = org.bukkit.Bukkit.getTPS();
        double mspt = org.bukkit.Bukkit.getAverageTickTime();

        // 2. TPS formatieren und in ein Array packen
        Component[] tpsAvg = new Component[tps.length];
        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = formatTps(tps[i]);
        }

        // 3. MSPT farbig codieren (50ms = 20 TPS. Alles unter 30ms ist super, über 45ms ist schlecht)
        TextColor msptColor = (mspt <= 30.0) ? NamedTextColor.GREEN : (mspt <= 45.0) ? NamedTextColor.YELLOW : NamedTextColor.RED;
        Component msptComponent = text(ONE_DECIMAL_PLACES.get().format(mspt) + "ms", msptColor);

        // 4. Schöne Ausgabe mit Alpes-Branding bauen
        TextComponent.Builder builder = text()
            .append(text("⛰️ ", NamedTextColor.AQUA))
            .append(text("Performance", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
            .append(Component.newline());

        builder.append(text("TPS (1m, 5m, 15m): ", NamedTextColor.GRAY))
            .append(Component.join(JoinConfiguration.commas(true), tpsAvg))
            .append(Component.newline());

        builder.append(text("MSPT (Avg): ", NamedTextColor.GRAY))
            .append(msptComponent);

        sender.sendMessage(builder.asComponent());

        // 5. Memory Check (wie vorher, nur etwas aufgeräumter formatiert)
        if (args.length > 0 && args[0].equalsIgnoreCase("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
            long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long usedMem = totalMem - freeMem;

            sender.sendMessage(text()
                .append(Component.newline())
                .append(text("Memory: ", NamedTextColor.GRAY))
                .append(text(usedMem + "MB / " + totalMem + "MB", NamedTextColor.GREEN))
                .append(text(" (Max: " + maxMem + "MB)", NamedTextColor.DARK_GRAY))
            );

            if (!this.hasShownMemoryWarning) {
                sender.sendMessage(WARN_MSG);
                this.hasShownMemoryWarning = true;
            }
        }

        return true;
    }

    // Formatierung für TPS mit coolen Hover-Effekten
    private static Component formatTps(double tps) {
        TextColor color = (tps > 18.0) ? NamedTextColor.GREEN : (tps > 16.0) ? NamedTextColor.YELLOW : NamedTextColor.RED;
        String amount = ONE_DECIMAL_PLACES.get().format(tps);
        if (tps > 20.0) amount += "*"; // Paper behavior (zeigt ein * bei über 20 TPS)

        // Wenn man im Spiel mit der Maus über die Zahl fährt, wird der exakte Wert angezeigt
        return text(amount, color).hoverEvent(
            text("Exact: " + tps, NamedTextColor.GRAY)
        );
    }
}
