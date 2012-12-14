package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import net.minecraft.server.Enchantment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class CraftItemStackTest extends AbstractTestingBase {

    @Test
    public void testCloneEnchantedItem() throws Exception {
        net.minecraft.server.ItemStack nmsItemStack = new net.minecraft.server.ItemStack(net.minecraft.server.Item.POTION);
        nmsItemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack itemStack = new CraftItemStack(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone.getType(), is(itemStack.getType()));
        assertThat(clone.getAmount(), is(itemStack.getAmount()));
        assertThat(clone.getDurability(), is(itemStack.getDurability()));
        assertThat(clone.getEnchantments(), is(itemStack.getEnchantments()));
        assertThat(clone.getTypeId(), is(itemStack.getTypeId()));
        assertThat(clone.getData(), is(itemStack.getData()));
        assertThat(clone, is(itemStack));
    }

    @Test
    public void testCloneNullItem() throws Exception {
        net.minecraft.server.ItemStack nmsItemStack = null;
        ItemStack itemStack = new CraftItemStack(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone, is(itemStack));
    }
}
