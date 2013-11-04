package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import net.minecraft.server.BlockFalling;
import net.minecraft.server.BlockFire;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemRecord;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.support.Util;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;
import net.minecraft.server.Blocks;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

@RunWith(Parameterized.class)
public class PerMaterialTest extends AbstractTestingBase {
    private static int[] fireValues;

    @BeforeClass
    public static void getFireValues() {
        fireValues = Util.getInternalState(BlockFire.class, Blocks.FIRE, "a");
    }

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        List<Object[]> list = Lists.newArrayList();
        for (Material material : Material.values()) {
            if (INVALIDATED_MATERIALS.contains(material)) {
                continue;
            }

            list.add(new Object[] {material});
        }
        return list;
    }

    @Parameter public Material material;

    @Test
    public void isSolid() {
        if (material == Material.AIR) {
            assertFalse(material.isSolid());
        } else if (material.isBlock()) {
            assertThat(material.isSolid(), is(CraftMagicNumbers.getBlock(material).getMaterial().isSolid()));
        } else {
            assertFalse(material.isSolid());
        }
    }

    @Test
    public void isEdible() {
        assertThat(material.isEdible(), is(CraftMagicNumbers.getItem(material) instanceof ItemFood));
    }

    @Test
    public void isRecord() {
        assertThat(material.isRecord(), is(CraftMagicNumbers.getItem(material) instanceof ItemRecord));
    }

    @Test
    public void maxDurability() {
        if (material == Material.AIR) {
            assertThat((int) material.getMaxDurability(), is(0));
        } else if (material.isBlock()){
            Item item = CraftMagicNumbers.getItem(material);
            assertThat((int) material.getMaxDurability(), is(item.getMaxDurability()));
        }
    }

    @Test
    public void maxStackSize() {
        final ItemStack bukkit = new ItemStack(material);
        final CraftItemStack craft = CraftItemStack.asCraftCopy(bukkit);
        if (material == Material.AIR) {
            final int MAX_AIR_STACK = 0 /* Why can't I hold all of these AIR? */;
            assertThat(material.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(bukkit.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(craft.getMaxStackSize(), is(MAX_AIR_STACK));
        } else {
            assertThat(material.getMaxStackSize(), is(CraftMagicNumbers.getItem(material).getMaxStackSize()));
            assertThat(bukkit.getMaxStackSize(), is(material.getMaxStackSize()));
            assertThat(craft.getMaxStackSize(), is(material.getMaxStackSize()));
        }
    }

    @Test
    public void isTransparent() {
        if (material == Material.AIR) {
            assertTrue(material.isTransparent());
        } else if (material.isBlock()) {
            assertThat(material.isTransparent(), is(not(CraftMagicNumbers.getBlock(material).getMaterial().blocksLight())));
        } else {
            assertFalse(material.isTransparent());
        }
    }

    @Test
    public void isFlammable() {
        if (material != Material.AIR && material.isBlock()) {
            assertThat(material.isFlammable(), is(CraftMagicNumbers.getBlock(material).getMaterial().isBurnable()));
        } else {
            assertFalse(material.isFlammable());
        }
    }

    @Test
    public void isBurnable() {
        if (material.isBlock()) {
            assertThat(material.isBurnable(), is(fireValues[material.getId()] > 0));
        } else {
            assertFalse(material.isBurnable());
        }
    }

    @Test
    public void isOccluding() {
        if (material.isBlock()) {
            assertThat(material.isOccluding(), is(CraftMagicNumbers.getBlock(material).r()));
        } else {
            assertFalse(material.isOccluding());
        }
    }

    @Test
    public void hasGravity() {
        if (material.isBlock()) {
            assertThat(material.hasGravity(), is(CraftMagicNumbers.getBlock(material) instanceof BlockFalling));
        } else {
            assertFalse(material.hasGravity());
        }
    }
}
