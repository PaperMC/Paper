package cool.circuit.paper.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class ItemCreator {

    //Circuit board fork start

    public static ItemStack createItem(final Material material, final String displayName, final String... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItem(final Material material, final Component displayName, final Component... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItem(final Material material, final String displayName, final boolean glowing, final String... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));

            if (glowing) {
                meta.setEnchantmentGlintOverride(true);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItem(final Material material, final Component displayName, final boolean glowing, final Component... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));

            if (glowing) {
                meta.setEnchantmentGlintOverride(true);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    //Circuit Board fork end
}
