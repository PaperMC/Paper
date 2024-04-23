package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class CompositeSerialization extends AbstractTestingBase {

    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Test
    public void testSaveRestoreCompositeList() throws InvalidConfigurationException {
        YamlConfiguration out = getConfig();

        List<ItemStack> stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(Material.STONE));
        stacks.add(new ItemStack(Material.SHORT_GRASS));
        stacks.add(new ItemStack(Material.DIRT));
        stacks.add(new ItemStack(Material.COBBLESTONE, 17));
        stacks.add(new ItemStack(Material.OAK_PLANKS, 63));
        stacks.add(new ItemStack(Material.OAK_SAPLING, 1, (short) 1));
        stacks.add(new ItemStack(Material.OAK_LEAVES, 32, (short) 2));

        ItemStack item7 = new ItemStack(Material.IRON_SHOVEL);
        item7.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 1);
        stacks.add(item7);

        ItemStack item8 = new ItemStack(Material.IRON_PICKAXE);
        item8.addUnsafeEnchantment(Enchantment.FEATHER_FALLING, 2);
        item8.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 1);
        item8.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 5);
        item8.addUnsafeEnchantment(Enchantment.RESPIRATION, 4);
        stacks.add(item8);

        ItemStack item9 = new ItemStack(Material.APPLE);
        ItemMeta meta9 = item9.getItemMeta();
        meta9.setDisplayName(ChatColor.RED + "DisplayName");
        meta9.setLocalizedName(ChatColor.AQUA + "LocalizedName");
        meta9.setLore(Arrays.asList(ChatColor.BLUE + "Lore1", ChatColor.DARK_AQUA + "Lore2"));
        item9.setItemMeta(meta9);
        stacks.add(item9);

        out.set("composite-list.abc.def", stacks);
        String yaml = out.saveToString();

        YamlConfiguration in = new YamlConfiguration();
        in.loadFromString(yaml);
        List<?> raw = in.getList("composite-list.abc.def");

        assertThat(stacks, hasSize(raw.size()));

        for (int i = 0; i < raw.size(); i++) {
            assertThat((Object) stacks.get(i), is((Object) raw.get(i)), String.valueOf(i));
        }
    }
}

