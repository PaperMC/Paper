package org.bukkit.craftbukkit.inventory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import net.minecraft.world.item.enchantment.Enchantments;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class NMSCraftItemStackTest extends AbstractTestingBase {

    @Test
    public void testCloneEnchantedItem() throws Exception {
        net.minecraft.world.item.ItemStack nmsItemStack = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.POTION);
        nmsItemStack.enchant(Enchantments.SHARPNESS, 1);
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone.getType(), is(itemStack.getType()));
        assertThat(clone.getAmount(), is(itemStack.getAmount()));
        assertThat(clone.getDurability(), is(itemStack.getDurability()));
        assertThat(clone.getEnchantments(), is(itemStack.getEnchantments()));
        assertThat(clone.getData(), is(itemStack.getData()));
        assertThat(clone, is(itemStack));
    }

    @Test
    public void testCloneNullItem() throws Exception {
        net.minecraft.world.item.ItemStack nmsItemStack = null;
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone, is(itemStack));
    }
}
