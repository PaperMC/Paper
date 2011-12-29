package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.Enchantment;
import net.minecraft.server.StatisticList;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

public class CraftItemStackTest {
    @Before
    public void setUp() throws Exception {
        StatisticList.a();
    }

    @Test
    public void testCloneEnchantedItem() throws Exception {
        net.minecraft.server.ItemStack nmsItemStack = new net.minecraft.server.ItemStack(net.minecraft.server.Item.POTION);
        nmsItemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack itemStack = new CraftItemStack(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assert (clone.getType().equals(itemStack.getType()));
        assert (clone.getAmount() == itemStack.getAmount());
        assert (clone.getDurability() == itemStack.getDurability());
        assert (clone.getEnchantments().equals(itemStack.getEnchantments()));
        assert (clone.getTypeId() == itemStack.getTypeId());
        assert (clone.getData().equals(itemStack.getData()));
    }

    @Test
    public void testCloneNullItem() throws Exception {
        net.minecraft.server.ItemStack nmsItemStack = null;
        ItemStack itemStack = new CraftItemStack(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assert (clone.equals(itemStack));
    }
}
