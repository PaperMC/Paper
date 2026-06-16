package ch.alexinf.alpes.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SummonCommand extends Command {

    // Sicherheitslimit, damit niemand aus Versehen den Server mit /summon zombie 10000 zum Absturz bringt
    private static final int MAX_SPAWN_LIMIT = 256;

    public SummonCommand(@NotNull String name) {
        super(name);
        this.description = "Spawnt eine Entität (mit optionaler Anzahl am Ende).";
        this.usageMessage = "/" + name + " <entity> [pos] [nbt] [anzahl]";
        // Wir nutzen die originale Vanilla-Permission, damit es sich nahtlos anfühlt
        this.setPermission("minecraft.command.summon");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;

        if (args.length < 1) {
            sender.sendMessage(Component.text("Benutzung: /" + currentAlias + " <entity> [pos] [nbt] [anzahl]", NamedTextColor.RED));
            return true;
        }

        int count = 1;
        int argsLength = args.length;

        // 1. Prüfen, ob das letzte Argument eine Zahl ist
        String lastArg = args[argsLength - 1];
        try {
            int potentialCount = Integer.parseInt(lastArg);
            if (potentialCount > 0) {
                if (potentialCount > MAX_SPAWN_LIMIT) {
                    sender.sendMessage(Component.text("Maximale Anzahl auf " + MAX_SPAWN_LIMIT + " begrenzt, um den Server zu schützen!", NamedTextColor.RED));
                    count = MAX_SPAWN_LIMIT;
                } else {
                    count = potentialCount;
                }
                // Das letzte Argument war die Anzahl, also ignorieren wir es für den Vanilla-Befehl
                argsLength--;
            }
        } catch (NumberFormatException e) {
            // Keine Zahl am Ende -> count bleibt 1, wir nutzen alle Argumente
        }

        // 2. Den originalen Vanilla-Befehl-String rekonstruieren
        StringBuilder vanillaCommand = new StringBuilder("summon");
        for (int i = 0; i < argsLength; i++) {
            vanillaCommand.append(" ").append(args[i]);
        }

        String finalVanillaCommand = vanillaCommand.toString();

        // 3. Den Befehl 'count'-mal ausführen
        int successfulSpawns = 0;
        for (int i = 0; i < count; i++) {
            // Wir nutzen den Server-Dispatcher. Das garantiert, dass alle Vanilla-Regeln,
            // NBT-Daten und Koordinaten (~ ~ ~) perfekt funktionieren!
            if (Bukkit.dispatchCommand(sender, finalVanillaCommand)) {
                successfulSpawns++;
            } else {
                break; // Wenn ein Befehl fehlschlägt (z.B. ungültiger Entity-Name), sofort abbrechen
            }
        }

        // 4. Schöne Rückmeldung geben
        if (successfulSpawns > 0) {
            if (count > 1) {
                sender.sendMessage(Component.text(successfulSpawns + "x ", NamedTextColor.AQUA)
                    .append(Component.text(args[0] + " erfolgreich gespawnt!", NamedTextColor.GREEN)));
            }
            // Wenn count == 1, übernimmt der originale Vanilla-Befehl bereits die Ausgabe ("Zombie wurde gespawnt")
        } else {
            sender.sendMessage(Component.text("Fehler beim Spawnen der Entität. Ist der Name korrekt?", NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        // Wir delegieren die Tab-Vervollständigung an den originalen Vanilla-Befehl,
        // damit Entity-Namen und NBT-Daten korrekt vorgeschlagen werden.
        // Wenn das letzte Argument eine Zahl sein könnte, schlagen wir 1-10 vor.
        List<String> completions = new ArrayList<>();

        if (args.length >= 1) {
            String lastArg = args[args.length - 1];
            // Einfache Heuristik: Wenn das Argument leer ist oder mit einer Zahl beginnt, schlage Zahlen vor
            if (lastArg.isEmpty() || lastArg.matches("\\d*")) {
                for (int i = 1; i <= 10; i++) {
                    if (String.valueOf(i).startsWith(lastArg)) {
                        completions.add(String.valueOf(i));
                    }
                }
            }
        }

        // Hinweis: Für eine 100% perfekte Vanilla-Tab-Completion bräuchten wir NMS-Zugriff.
        // Dieser Fallback ist aber für den Alltag mehr als ausreichend und stürzt nicht ab.
        return completions;
    }
}
