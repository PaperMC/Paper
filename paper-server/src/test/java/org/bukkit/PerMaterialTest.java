package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import net.minecraft.server.Block;
import net.minecraft.server.BlockFire;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemRecord;
import net.minecraft.server.BlockSand;

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

@RunWith(Parameterized.class)
public class PerMaterialTest extends AbstractTestingBase {
    private static int[] fireValues;

    @BeforeClass
    public static void getFireValues() {
        fireValues = Util.getInternalState(BlockFire.class, Block.FIRE, "a");
    }

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        List<Object[]> list = Lists.newArrayList();
        for (Material material : Material.values()) {
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
            assertThat(material.isSolid(), is(Block.byId[material.getId()].material.isSolid()));
        } else {
            assertFalse(material.isSolid());
        }
    }

    @Test
    public void isEdible() {
        assertThat(material.isEdible(), is(Item.byId[material.getId()] instanceof ItemFood));
    }

    @Test
    public void isRecord() {
        assertThat(material.isRecord(), is(Item.byId[material.getId()] instanceof ItemRecord));
    }

    @Test
    public void maxDurability() {
        if (material == Material.AIR) {
            assertThat((int) material.getMaxDurability(), is(0));
        } else {
            assertThat((int) material.getMaxDurability(), is(Item.byId[material.getId()].getMaxDurability()));
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
            assertThat(material.getMaxStackSize(), is(Item.byId[material.getId()].getMaxStackSize()));
            assertThat(bukkit.getMaxStackSize(), is(material.getMaxStackSize()));
            assertThat(craft.getMaxStackSize(), is(material.getMaxStackSize()));
        }
    }

    @Test
    public void isTransparent() {
        if (material == Material.AIR) {
            assertTrue(material.isTransparent());
        } else if (material.isBlock()) {
            assertThat(material.isTransparent(), is(not(Block.byId[material.getId()].material.blocksLight())));
        } else {
            assertFalse(material.isTransparent());
        }
    }

    @Test
    public void isFlammable() {
        if (material != Material.AIR && material.isBlock()) {
            assertThat(material.isFlammable(), is(Block.byId[material.getId()].material.isBurnable()));
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
            assertThat(material.isOccluding(), is(Block.l(material.getId())));
        } else {
            assertFalse(material.isOccluding());
        }
    }

    @Test
    public void hasGravity() {
        if (material.isBlock()) {
            assertThat(material.hasGravity(), is(Block.byId[material.getId()] instanceof BlockSand));
        } else {
            assertFalse(material.hasGravity());
        }
    }
}
