package ch.alexinf.alpes.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends Command {

    public EnchantCommand(@NotNull String name) {
        super(name);
        this.description = "Verzaubert einen Gegenstand (ignoriert Vanilla-Limits).";
        this.usageMessage = "/" + name + " [spieler] <verzauberung> [stufe]";
        this.setPermission("alpes.command.enchant");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;

        if (args.length < 1) {
            sender.sendMessage(Component.text("Benutzung: /" + currentAlias + " [spieler] <verzauberung> [stufe]", NamedTextColor.RED));
            return true;
        }

        // 1. Ziel bestimmen (Spieler oder Sender)
        Player target;
        int argOffset = 0;
        if (args.length >= 2 && Bukkit.getPlayerExact(args[0]) != null) {
            target = Bukkit.getPlayerExact(args[0]);
            argOffset = 1;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Du musst ein Spieler sein, um diesen Befehl ohne Spielernamen zu nutzen!", NamedTextColor.RED));
                return true;
            }
            target = (Player) sender;
        }

        // 2. Verzauberung holen
        if (args.length <= argOffset) {
            sender.sendMessage(Component.text("Bitte gib eine Verzauberung an.", NamedTextColor.RED));
            return true;
        }

        String enchantName = args[argOffset].toLowerCase();
        // Wir suchen in der Registry, um auch Custom Enchantments zu unterstützen, falls ihr welche habt
        Enchantment enchant = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName));

        if (enchant == null) {
            sender.sendMessage(Component.text("Unbekannte Verzauberung: " + enchantName, NamedTextColor.RED));
            return true;
        }

        // 3. Level bestimmen (Standard: 1, aber wir erlauben ALLES)
        int level = 1;
        if (args.length > argOffset + 1) {
            try {
                level = Integer.parseInt(args[argOffset + 1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Das Level muss eine Zahl sein!", NamedTextColor.RED));
                return true;
            }
        }

        // 4. Gegenstand prüfen
        ItemStack item = target.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            sender.sendMessage(Component.text("Du musst einen Gegenstand in der Hand halten!", NamedTextColor.RED));
            return true;
        }

        // 5. Verzauberung anwenden (addUnsafeEnchantment ignoriert Vanilla-Regeln wie "Schärfe auf Spitzhacke" oder Level-Limits!)
        item.addUnsafeEnchantment(enchant, level);

        // 6. Coole Erfolgsmeldung
        String enchantDisplayName = enchant.getKey().getKey(); // Oder enchant.displayName() je nach Version

        Component message = Component.text(target.getName(), NamedTextColor.AQUA)
            .append(Component.text(" hat ", NamedTextColor.WHITE))
            .append(Component.text(enchantDisplayName + " " + level, NamedTextColor.AQUA))
            .append(Component.text(" erhalten!"));

        sender.sendMessage(message);

        // Easter Egg Nachricht, wenn das Vanilla-Limit gebrochen wurde
        if (level > enchant.getMaxLevel()) {
            target.sendMessage(Component.text("⚠️ Warnung: Vanilla-Limit für " + enchantDisplayName + " wurde gebrochen!", NamedTextColor.RED));
        }

        return true
            ;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Vorschlag von Spielernamen
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2 || (args.length == 3 && Bukkit.getPlayerExact(args[0]) != null)) {
            // Vorschlag von Verzauberungen
            int enchantIndex = (args.length == 3 && Bukkit.getPlayerExact(args[0]) != null) ? 1 : 0;
            String currentArg = args[args.length - 1].toLowerCase();

            for (Enchantment enchant : Registry.ENCHANTMENT) {
                if (enchant.getKey().getKey().toLowerCase().startsWith(currentArg)) {
                    completions.add(enchant.getKey().getKey());
                }
            }
        } else if (args.length == 3 || (args.length == 4 && Bukkit.getPlayerExact(args[0]) != null)) {
            // Vorschlag von Levels: Wir zeigen nur die VANILLA-Max-Levels an, damit es sauber aussieht!
            // Der Admin kann sie aber trotzdem manuell überschreiben.
            int enchantIndex = (args.length == 4) ? 1 : 0;
            String enchantName = args[enchantIndex].toLowerCase();
            Enchantment enchant = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName));

            if (enchant != null) {
                for (int i = 1; i <= enchant.getMaxLevel(); i++) {
                    completions.add(String.valueOf(i));
                }
            }
        }

        return completions;
    }
}
