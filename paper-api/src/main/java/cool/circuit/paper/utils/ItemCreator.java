package cool.circuit.paper.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class ItemCreator {

    //Circuit board fork start

    public static ItemStack createItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItem(Material material, Component displayName, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItem(Material material, String displayName, boolean glowing, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

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

    public static ItemStack createItem(Material material, Component displayName, boolean glowing, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

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
