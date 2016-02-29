package org.bukkit.materials;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import org.bukkit.material.Leaves;
import org.bukkit.material.Sapling;
import org.bukkit.material.Tree;
import org.bukkit.material.Wood;
import org.bukkit.material.WoodenStep;
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

    @Test
    public void testWood() {
        Wood wood = new Wood();
        assertThat("Constructed with default wood type", wood.getItemType(), equalTo(Material.WOOD));
        assertThat("Constructed with default tree species", wood.getSpecies(), equalTo(TreeSpecies.GENERIC));

        TreeSpecies[] allSpecies = TreeSpecies.values();
        for (TreeSpecies species : allSpecies) {
            wood = new Wood(species);
            assertThat("Constructed with default wood type", wood.getItemType(), equalTo(Material.WOOD));
            assertThat("Constructed with correct tree species", wood.getSpecies(), equalTo(species));
        }

        Material[] types = new Material[]{Material.WOOD, Material.WOOD_DOUBLE_STEP};
        for (Material type : types) {
            wood = new Wood(type);
            assertThat("Constructed with correct wood type", wood.getItemType(), equalTo(type));
            assertThat("Constructed with default tree species", wood.getSpecies(), equalTo(TreeSpecies.GENERIC));

            for (TreeSpecies species : allSpecies) {
                wood = new Wood(type, species);
                assertThat("Constructed with correct wood type", wood.getItemType(), equalTo(type));
                assertThat("Constructed with correct tree species", wood.getSpecies(), equalTo(species));
            }
        }
    }

    @Test
    public void testTree() {
        Tree tree = new Tree();
        assertThat("Constructed with default tree type", tree.getItemType(), equalTo(Material.LOG));
        assertThat("Constructed with default tree species", tree.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default direction", tree.getDirection(), equalTo(BlockFace.UP));

        tree = new Tree(Material.LOG);
        assertThat("Constructed with correct tree type", tree.getItemType(), equalTo(Material.LOG));
        assertThat("Constructed with default tree species", tree.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default direction", tree.getDirection(), equalTo(BlockFace.UP));

        Material[] types = new Material[]{Material.LOG, Material.LOG_2};
        TreeSpecies[][] allSpecies = new TreeSpecies[][]{
            {TreeSpecies.GENERIC, TreeSpecies.REDWOOD, TreeSpecies.BIRCH, TreeSpecies.JUNGLE},
            {TreeSpecies.ACACIA, TreeSpecies.DARK_OAK}
        };
        BlockFace[] allDirections = new BlockFace[]{BlockFace.UP, BlockFace.WEST, BlockFace.NORTH, BlockFace.SELF};
        for (int t = 0; t < types.length; t++) {
            for (TreeSpecies species : allSpecies[t]) {
                tree = new Tree(types[t], species);
                assertThat("Constructed with correct tree type", tree.getItemType(), equalTo(types[t]));
                assertThat("Constructed with correct tree species", tree.getSpecies(), equalTo(species));
                assertThat("Constructed with default direction", tree.getDirection(), equalTo(BlockFace.UP));

                // check item type is fixed automatically for invalid type-species combo
                tree = new Tree(types[types.length - 1 - t], species);
                assertThat("Constructed with fixed tree type", tree.getItemType(), equalTo(types[t]));
                assertThat("Constructed with correct tree species", tree.getSpecies(), equalTo(species));
                assertThat("Constructed with default direction", tree.getDirection(), equalTo(BlockFace.UP));
                for (BlockFace dir : allDirections) {
                    tree = new Tree(types[t], species, dir);
                    assertThat("Constructed with correct tree type", tree.getItemType(), equalTo(types[t]));
                    assertThat("Constructed with correct tree species", tree.getSpecies(), equalTo(species));
                    assertThat("Constructed with correct direction", tree.getDirection(), equalTo(dir));
                }
            }
        }
    }

    @Test
    public void testLeaves() {
        Leaves leaves = new Leaves();
        assertThat("Constructed with default leaf type", leaves.getItemType(), equalTo(Material.LEAVES));
        assertThat("Constructed with default tree species", leaves.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default decayable", leaves.isDecayable(), equalTo(true));
        assertThat("Constructed with default decaying", leaves.isDecaying(), equalTo(false));

        leaves = new Leaves(Material.LEAVES);
        assertThat("Constructed with correct leaf type", leaves.getItemType(), equalTo(Material.LEAVES));
        assertThat("Constructed with default tree species", leaves.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default decayable", leaves.isDecayable(), equalTo(true));
        assertThat("Constructed with default decaying", leaves.isDecaying(), equalTo(false));

        Material[] types = new Material[]{Material.LEAVES, Material.LEAVES_2};
        TreeSpecies[][] allSpecies = new TreeSpecies[][]{
            {TreeSpecies.GENERIC, TreeSpecies.REDWOOD, TreeSpecies.BIRCH, TreeSpecies.JUNGLE},
            {TreeSpecies.ACACIA, TreeSpecies.DARK_OAK}
        };
        boolean[] decayable = new boolean[]{true, false};
        boolean[] decaying = new boolean[]{true, false};
        for (int t = 0; t < types.length; t++) {
            for (TreeSpecies species : allSpecies[t]) {
                leaves = new Leaves(types[t], species);
                assertThat("Constructed with correct leaf type", leaves.getItemType(), equalTo(types[t]));
                assertThat("Constructed with correct tree species", leaves.getSpecies(), equalTo(species));
                assertThat("Constructed with default decayable", leaves.isDecayable(), equalTo(true));
                assertThat("Constructed with default decaying", leaves.isDecaying(), equalTo(false));

                // check item type is fixed automatically for invalid type-species combo
                leaves = new Leaves(types[types.length - 1 - t], species);
                assertThat("Constructed with fixed leaf type", leaves.getItemType(), equalTo(types[t]));
                assertThat("Constructed with correct tree species", leaves.getSpecies(), equalTo(species));
                assertThat("Constructed with default decayable", leaves.isDecayable(), equalTo(true));
                assertThat("Constructed with default decaying", leaves.isDecaying(), equalTo(false));
                for (boolean isDecayable : decayable) {
                    leaves = new Leaves(types[t], species, isDecayable);
                    assertThat("Constructed with correct wood type", leaves.getItemType(), equalTo(types[t]));
                    assertThat("Constructed with correct tree species", leaves.getSpecies(), equalTo(species));
                    assertThat("Constructed with correct decayable", leaves.isDecayable(), equalTo(isDecayable));
                    assertThat("Constructed with default decaying", leaves.isDecaying(), equalTo(false));
                    for (boolean isDecaying : decaying) {
                        leaves = new Leaves(types[t], species, isDecayable);
                        leaves.setDecaying(isDecaying);
                        assertThat("Constructed with correct wood type", leaves.getItemType(), equalTo(types[t]));
                        assertThat("Constructed with correct tree species", leaves.getSpecies(), equalTo(species));
                        assertThat("Constructed with correct decayable", leaves.isDecayable(), equalTo(isDecaying || isDecayable));
                        assertThat("Constructed with correct decaying", leaves.isDecaying(), equalTo(isDecaying));
                    }
                }
            }
        }
    }

    @Test
    public void testWoodenStep() {
        WoodenStep woodenStep = new WoodenStep();
        assertThat("Constructed with default step type", woodenStep.getItemType(), equalTo(Material.WOOD_STEP));
        assertThat("Constructed with default tree species", woodenStep.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default inversion", woodenStep.isInverted(), equalTo(false));

        TreeSpecies[] allSpecies = TreeSpecies.values();
        boolean[] inversion = new boolean[]{true, false};
        for (TreeSpecies species : allSpecies) {
            woodenStep = new WoodenStep(species);
            assertThat("Constructed with default step type", woodenStep.getItemType(), equalTo(Material.WOOD_STEP));
            assertThat("Constructed with correct tree species", woodenStep.getSpecies(), equalTo(species));
            assertThat("Constructed with default inversion", woodenStep.isInverted(), equalTo(false));
            for (boolean isInverted : inversion) {
                woodenStep = new WoodenStep(species, isInverted);
                assertThat("Constructed with default step type", woodenStep.getItemType(), equalTo(Material.WOOD_STEP));
                assertThat("Constructed with correct tree species", woodenStep.getSpecies(), equalTo(species));
                assertThat("Constructed with correct inversion", woodenStep.isInverted(), equalTo(isInverted));
            }
        }
    }

    @Test
    public void testSapling() {
        Sapling sapling = new Sapling();
        assertThat("Constructed with default sapling type", sapling.getItemType(), equalTo(Material.SAPLING));
        assertThat("Constructed with default tree species", sapling.getSpecies(), equalTo(TreeSpecies.GENERIC));
        assertThat("Constructed with default growable", sapling.isInstantGrowable(), equalTo(false));

        TreeSpecies[] allSpecies = TreeSpecies.values();
        boolean[] growable = new boolean[]{true, false};
        for (TreeSpecies species : allSpecies) {
            sapling = new Sapling(species);
            assertThat("Constructed with default sapling type", sapling.getItemType(), equalTo(Material.SAPLING));
            assertThat("Constructed with correct tree species", sapling.getSpecies(), equalTo(species));
            assertThat("Constructed with default growable", sapling.isInstantGrowable(), equalTo(false));
            for (boolean isInstantGrowable : growable) {
                sapling = new Sapling(species, isInstantGrowable);
                assertThat("Constructed with default sapling type", sapling.getItemType(), equalTo(Material.SAPLING));
                assertThat("Constructed with correct tree species", sapling.getSpecies(), equalTo(species));
                assertThat("Constructed with correct growable", sapling.isInstantGrowable(), equalTo(isInstantGrowable));
            }
        }
    }
}
