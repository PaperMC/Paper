package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

@Deprecated
public class EnchantCommand extends VanillaCommand {
    private static final List<String> ENCHANTMENT_NAMES = new ArrayList<String>();

    public EnchantCommand() {
        super("enchant");
        this.description = "Adds enchantments to the item the player is currently holding. Specify 0 for the level to remove an enchantment. Specify force to ignore normal enchantment restrictions";
        this.usageMessage = "/enchant <player> <enchantment> [level|max|0] [force]";
        this.setPermission("bukkit.command.enchant");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        boolean force = false;
        if (args.length > 2) {
            force = args[args.length > 3 ? 3 : 2].equalsIgnoreCase("force");
        }

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("Can't find player " + args[0]);
        } else {
            ItemStack item = player.getItemInHand();
            if (item.getType() == Material.AIR) {
                sender.sendMessage("The player isn't holding an item");
            } else {
                String itemName = item.getType().toString().replaceAll("_", " ");
                itemName = WordUtils.capitalizeFully(itemName);

                Enchantment enchantment = getEnchantment(args[1].toUpperCase());
                if (enchantment == null) {
                    sender.sendMessage(String.format("Enchantment does not exist: %s", args[1]));
                }  else {
                    String enchantmentName = enchantment.getName().replaceAll("_", " ");
                    enchantmentName = WordUtils.capitalizeFully(enchantmentName);

                    if (!force && !enchantment.canEnchantItem(item)) {
                        sender.sendMessage(String.format("%s cannot be applied to %s", enchantmentName, itemName));
                    } else {
                        int level = 1;
                        if (args.length > 2) {
                            Integer integer = getInteger(args[2]);
                            int minLevel = enchantment.getStartLevel();
                            int maxLevel = force ? Short.MAX_VALUE : enchantment.getMaxLevel();

                            if (integer != null) {
                                if (integer == 0) {
                                    item.removeEnchantment(enchantment);
                                    Command.broadcastCommandMessage(sender, String.format("Removed %s on %s's %s", enchantmentName, player.getName(), itemName));
                                    return true;
                                }

                                if (integer < minLevel || integer > maxLevel) {
                                    sender.sendMessage(String.format("Level for enchantment %s must be between %d and %d", enchantmentName, minLevel, maxLevel));
                                    sender.sendMessage("Specify 0 for level to remove an enchantment");
                                    return true;
                                }

                                level = integer;
                            }

                            if ("max".equals(args[2])) {
                                level = maxLevel;
                            }
                        }

                        Map<Enchantment, Integer> enchantments = item.getEnchantments();
                        boolean conflicts = false;

                        if (!force && !enchantments.isEmpty()) { // TODO: Improve this to use a "hasEnchantments" call
                            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                Enchantment enchant = entry.getKey();

                                if (enchant.equals(enchantment)) continue;
                                if (enchant.conflictsWith(enchantment)) {
                                    sender.sendMessage(String.format("Can't apply the enchantment %s on an item with the enchantment %s", enchantmentName, WordUtils.capitalizeFully(enchant.getName().replaceAll("_", " "))));
                                    conflicts = true;
                                    break;
                                }
                            }
                        }

                        if (!conflicts) {
                            item.addUnsafeEnchantment(enchantment, level);

                            Command.broadcastCommandMessage(sender, String.format("Applied %s (Lvl %d) on %s's %s", enchantmentName, level, player.getName(), itemName), false);
                            sender.sendMessage(String.format("Enchanting succeeded, applied %s (Lvl %d) onto your %s", enchantmentName, level, itemName));
                        }
                    }
                }
            }
        }
        return true;
    }

     @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }

        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], ENCHANTMENT_NAMES, new ArrayList<String>(ENCHANTMENT_NAMES.size()));
        }

        if (args.length == 3 || args.length == 4) {
            if (!args[args.length - 2].equalsIgnoreCase("force")) {
                return ImmutableList.of("force");
            }
        }

        return ImmutableList.of();
     }

    private Enchantment getEnchantment(String lookup) {
        Enchantment enchantment = Enchantment.getByName(lookup);

        if (enchantment == null) {
            Integer id = getInteger(lookup);
            if (id != null) {
                enchantment = Enchantment.getById(id);
            }
        }

        return enchantment;
    }

    public static void buildEnchantments() {
        if (!ENCHANTMENT_NAMES.isEmpty()) {
            throw new IllegalStateException("Enchantments have already been built!");
        }

        for (Enchantment enchantment : Enchantment.values()) {
            ENCHANTMENT_NAMES.add(enchantment.getName());
        }

        Collections.sort(ENCHANTMENT_NAMES);
    }
}
