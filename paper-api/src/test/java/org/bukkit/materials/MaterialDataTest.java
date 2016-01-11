package org.bukkit.materials;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import org.junit.Test;

public class MaterialDataTest {

    @Test
    public void testDoor()
    {
        @SuppressWarnings("deprecation")
        Door door = new Door();
        assertThat("Constructed with default door type",door.getItemType(),equalTo(Material.WOODEN_DOOR));
        assertThat("Constructed with default top or bottom",door.isTopHalf(),equalTo(false));
        assertThat("Constructed with default direction",door.getFacing(),equalTo(BlockFace.WEST));
        assertThat("Constructed with default open state",door.isOpen(),equalTo(false));
        
        Material[] types = new Material[] { Material.WOODEN_DOOR,
                Material.IRON_DOOR_BLOCK, Material.SPRUCE_DOOR,
                Material.BIRCH_DOOR, Material.JUNGLE_DOOR,
                Material.ACACIA_DOOR, Material.DARK_OAK_DOOR };
        BlockFace[] directions = new BlockFace[] { BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH };
        boolean[] openStates = new boolean[] {false, true};
        boolean[] hingeStates = new boolean[] {false, true};
        for(Material type : types)
        {
            // Test bottom half
            for(BlockFace facing : directions)
            {
                door = new Door(type,facing);
                assertThat("Constructed with correct door type",door.getItemType(),equalTo(type));
                assertThat("Constructed with default top or bottom",door.isTopHalf(),equalTo(false));
                assertThat("Constructed with correct direction",door.getFacing(),equalTo(facing));
                assertThat("Constructed with default open state",door.isOpen(),equalTo(false));

                for(boolean openState : openStates)
                {
                    door = new Door(type,facing,openState);
                    assertThat("Constructed with correct door type",door.getItemType(),equalTo(type));
                    assertThat("Constructed with default top or bottom",door.isTopHalf(),equalTo(false));
                    assertThat("Constructed with correct direction",door.getFacing(),equalTo(facing));
                    assertThat("Constructed with correct open state",door.isOpen(),equalTo(openState));
                }
            }

            // Test top half
            for(boolean hingeState : hingeStates)
            {
                door = new Door(type,hingeState);
                assertThat("Constructed with correct door type",door.getItemType(),equalTo(type));
                assertThat("Constructed with default top or bottom",door.isTopHalf(),equalTo(true));
                assertThat("Constructed with correct direction",door.getHinge(),equalTo(hingeState));
            }
        }
    }
}
