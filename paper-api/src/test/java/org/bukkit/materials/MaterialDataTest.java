package org.bukkit.materials;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Crops;
import org.bukkit.material.Comparator;
import org.bukkit.material.Diode;
import org.bukkit.material.Door;
import org.bukkit.material.Hopper;
import org.bukkit.material.Leaves;
import org.bukkit.material.Mushroom;
import org.bukkit.material.NetherWarts;
import org.bukkit.material.Sapling;
import org.bukkit.material.Tree;
import org.bukkit.material.Wood;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.types.MushroomBlockTexture;
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

    @Test
    public void testMushroom() {
        Material[] mushroomTypes = new Material[] { Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2 };
        BlockFace[] setFaces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.NORTH,
                BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
                BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST };
        MushroomBlockTexture[] textures = MushroomBlockTexture.values();
        for (Material type : mushroomTypes) {
            Mushroom mushroom = new Mushroom(type);
            assertThat("Constructed with correct mushroom type", mushroom.getItemType(), equalTo(type));
            assertThat("Constructed with default pores face", mushroom.getBlockTexture(), equalTo(MushroomBlockTexture.ALL_PORES));

            for (int f = 0; f < setFaces.length; f++) {
                mushroom = new Mushroom(type, setFaces[f]);
                assertThat("Constructed with correct mushroom type", mushroom.getItemType(), equalTo(type));
                assertThat("Constructed with correct texture", mushroom.getBlockTexture(), equalTo(MushroomBlockTexture.getCapByFace(setFaces[f])));
            }

            for (MushroomBlockTexture texture : textures) {
                mushroom = new Mushroom(type, texture);
                assertThat("Constructed with correct mushroom type", mushroom.getItemType(), equalTo(type));
                assertThat("Constructed with correct texture", mushroom.getBlockTexture(), equalTo(texture));
            }
        }
    }

    @Test
    public void testCrops() {
        Crops crops = new Crops();
        assertThat("Constructed with default crops type", crops.getItemType(), equalTo(Material.CROPS));
        assertThat("Constructed with default crop state", crops.getState(), equalTo(CropState.SEEDED));

        CropState[] allStates = CropState.values();
        for (CropState state : allStates) {
            crops = new Crops(state);
            assertThat("Constructed with default crops type", crops.getItemType(), equalTo(Material.CROPS));
            assertThat("Constructed with correct crop state", crops.getState(), equalTo(state));
        }

        // The crops which fully implement all crop states
        Material[] allCrops = new Material[] {Material.CROPS, Material.CARROT, Material.POTATO};
        for (Material crop : allCrops) {
            crops = new Crops(crop);
            assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(crop));
            assertThat("Constructed with default crop state", crops.getState(), equalTo(CropState.SEEDED));

            for (CropState state : allStates) {
                crops = new Crops(crop, state);
                assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(crop));
                assertThat("Constructed with correct crop state", crops.getState(), equalTo(state));
            }
        }

        // Beetroot are crops too, but they only have four states
        // Setting different crop states for beetroot will return the following when retrieved back
        CropState[] beetrootStates = new CropState[] {CropState.SEEDED, CropState.SEEDED, CropState.SMALL, CropState.SMALL, CropState.TALL, CropState.TALL, CropState.RIPE, CropState.RIPE};
        assertThat("Beetroot state translations match size", beetrootStates.length, equalTo(allStates.length));
        crops = new Crops(Material.BEETROOT_BLOCK);
        assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(Material.BEETROOT_BLOCK));
        assertThat("Constructed with default crop state", crops.getState(), equalTo(CropState.SEEDED));
        for (int s = 0; s < beetrootStates.length; s++) {
            crops = new Crops(Material.BEETROOT_BLOCK, allStates[s]);
            assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(Material.BEETROOT_BLOCK));
            assertThat("Constructed with correct crop state", crops.getState(), equalTo(beetrootStates[s]));
        }

        // In case you want to treat NetherWarts as Crops, although they really aren't
        crops = new Crops(Material.NETHER_WARTS);
        NetherWarts warts = new NetherWarts();
        assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(warts.getItemType()));
        assertThat("Constructed with default crop state", crops.getState(), equalTo(CropState.SEEDED));
        assertThat("Constructed with default wart state", warts.getState(), equalTo(NetherWartsState.SEEDED));
        allStates = new CropState[] {CropState.SEEDED, CropState.SMALL, CropState.TALL, CropState.RIPE};
        NetherWartsState[] allWartStates = NetherWartsState.values();
        assertThat("Nether Warts state translations match size", allWartStates.length, equalTo(allStates.length));
        for (int s = 0; s < allStates.length; s++) {
            crops = new Crops(Material.NETHER_WARTS, allStates[s]);
            warts = new NetherWarts(allWartStates[s]);
            assertThat("Constructed with correct crops type", crops.getItemType(), equalTo(warts.getItemType()));
            assertThat("Constructed with correct crop state", crops.getState(), equalTo(allStates[s]));
            assertThat("Constructed with correct wart state", warts.getState(), equalTo(allWartStates[s]));
        }
    }

    @Test
    public void testDiode() {
        Diode diode = new Diode();
        assertThat("Constructed with backward compatible diode state", diode.getItemType(), equalTo(Material.DIODE_BLOCK_ON));
        assertThat("Constructed with backward compatible powered", diode.isPowered(), equalTo(true));
        assertThat("Constructed with default delay", diode.getDelay(), equalTo(1));
        assertThat("Constructed with default direction", diode.getFacing(), equalTo(BlockFace.NORTH));

        BlockFace[] directions = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        int[] delays = new int[] {1, 2, 3, 4};
        boolean[] states = new boolean[] {false, true};
        for (BlockFace direction : directions) {
            diode = new Diode(direction);
            assertThat("Constructed with default diode state", diode.getItemType(), equalTo(Material.DIODE_BLOCK_OFF));
            assertThat("Constructed with default powered", diode.isPowered(), equalTo(false));
            assertThat("Constructed with default delay", diode.getDelay(), equalTo(1));
            assertThat("Constructed with correct direction", diode.getFacing(), equalTo(direction));
            for (int delay : delays) {
                diode = new Diode(direction, delay);
                assertThat("Constructed with default diode state", diode.getItemType(), equalTo(Material.DIODE_BLOCK_OFF));
                assertThat("Constructed with default powered", diode.isPowered(), equalTo(false));
                assertThat("Constructed with correct delay", diode.getDelay(), equalTo(delay));
                assertThat("Constructed with correct direction", diode.getFacing(), equalTo(direction));
                for (boolean state : states) {
                    diode = new Diode(direction, delay, state);
                    assertThat("Constructed with correct diode state", diode.getItemType(), equalTo(state ? Material.DIODE_BLOCK_ON : Material.DIODE_BLOCK_OFF));
                    assertThat("Constructed with default powered", diode.isPowered(), equalTo(state));
                    assertThat("Constructed with correct delay", diode.getDelay(), equalTo(delay));
                    assertThat("Constructed with correct direction", diode.getFacing(), equalTo(direction));
                }
            }
        }
    }

    @Test
    public void testComparator() {
        Comparator comparator = new Comparator();
        assertThat("Constructed with default comparator state", comparator.getItemType(), equalTo(Material.REDSTONE_COMPARATOR_OFF));
        assertThat("Constructed with default powered", comparator.isPowered(), equalTo(false));
        assertThat("Constructed with default being powered", comparator.isBeingPowered(), equalTo(false));
        assertThat("Constructed with default mode", comparator.isSubtractionMode(), equalTo(false));
        assertThat("Constructed with default direction", comparator.getFacing(), equalTo(BlockFace.NORTH));

        BlockFace[] directions = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        boolean[] modes = new boolean[] {false, true};
        boolean[] states = new boolean[] {false, true};
        for (BlockFace direction : directions) {
            comparator = new Comparator(direction);
            assertThat("Constructed with default comparator state", comparator.getItemType(), equalTo(Material.REDSTONE_COMPARATOR_OFF));
            assertThat("Constructed with default powered", comparator.isPowered(), equalTo(false));
            assertThat("Constructed with default being powered", comparator.isBeingPowered(), equalTo(false));
            assertThat("Constructed with default mode", comparator.isSubtractionMode(), equalTo(false));
            assertThat("Constructed with correct direction", comparator.getFacing(), equalTo(direction));
            for (boolean mode : modes) {
                comparator = new Comparator(direction, mode);
                assertThat("Constructed with default comparator state", comparator.getItemType(), equalTo(Material.REDSTONE_COMPARATOR_OFF));
                assertThat("Constructed with default powered", comparator.isPowered(), equalTo(false));
                assertThat("Constructed with default being powered", comparator.isBeingPowered(), equalTo(false));
                assertThat("Constructed with correct mode", comparator.isSubtractionMode(), equalTo(mode));
                assertThat("Constructed with correct direction", comparator.getFacing(), equalTo(direction));
                for (boolean state : states) {
                    comparator = new Comparator(direction, mode, state);
                    assertThat("Constructed with correct comparator state", comparator.getItemType(), equalTo(state ? Material.REDSTONE_COMPARATOR_ON : Material.REDSTONE_COMPARATOR_OFF));
                    assertThat("Constructed with correct powered", comparator.isPowered(), equalTo(state));
                    assertThat("Constructed with default being powered", comparator.isBeingPowered(), equalTo(false));
                    assertThat("Constructed with correct mode", comparator.isSubtractionMode(), equalTo(mode));
                    assertThat("Constructed with correct direction", comparator.getFacing(), equalTo(direction));

                    // Check if the game sets the fourth bit, that block data is still interpreted correctly
                    comparator.setData((byte)((comparator.getData() & 0x7) | 0x8));
                    assertThat("Constructed with correct comparator state", comparator.getItemType(), equalTo(state ? Material.REDSTONE_COMPARATOR_ON : Material.REDSTONE_COMPARATOR_OFF));
                    assertThat("Constructed with correct powered", comparator.isPowered(), equalTo(state));
                    assertThat("Constructed with correct being powered", comparator.isBeingPowered(), equalTo(true));
                    assertThat("Constructed with correct mode", comparator.isSubtractionMode(), equalTo(mode));
                    assertThat("Constructed with correct direction", comparator.getFacing(), equalTo(direction));
                }
            }
        }
    }

    @Test
    public void testHopper() {
        Hopper hopper = new Hopper();
        assertThat("Constructed with default hopper type", hopper.getItemType(), equalTo(Material.HOPPER));
        assertThat("Constructed with default active state", hopper.isActive(), equalTo(true));
        assertThat("Constructed with default powered state", hopper.isPowered(), equalTo(false));
        assertThat("Constructed with default direction", hopper.getFacing(), equalTo(BlockFace.DOWN));

        BlockFace[] directions = new BlockFace[] {BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
        boolean[] activeStates = new boolean[] {true, false};
        for (BlockFace direction : directions) {
            hopper = new Hopper(direction);
            assertThat("Constructed with default hopper type", hopper.getItemType(), equalTo(Material.HOPPER));
            assertThat("Constructed with default active state", hopper.isActive(), equalTo(true));
            assertThat("Constructed with correct powered state", hopper.isPowered(), equalTo(false));
            assertThat("Constructed with correct direction", hopper.getFacing(), equalTo(direction));
            for(boolean isActive : activeStates) {
                hopper = new Hopper(direction, isActive);
                assertThat("Constructed with default hopper type", hopper.getItemType(), equalTo(Material.HOPPER));
                assertThat("Constructed with correct active state", hopper.isActive(), equalTo(isActive));
                assertThat("Constructed with correct powered state", hopper.isPowered(), equalTo(!isActive));
                assertThat("Constructed with correct direction", hopper.getFacing(), equalTo(direction));
            }
        }
    }
}
