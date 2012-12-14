package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import net.minecraft.server.Block;
import net.minecraft.server.BlockFire;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemRecord;

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
        if (material == Material.AIR) {
            assertThat(material.getMaxStackSize(), is(64 /* Why can't I hold all of these AIR? */));
        } else {
            assertThat(material.getMaxStackSize(), is(Item.byId[material.getId()].getMaxStackSize()));
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
            assertThat(material.isOccluding(), is(Block.i(material.getId())));
        } else {
            assertFalse(material.isOccluding());
        }
    }
}
