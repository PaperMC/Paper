package org.bukkit.materials;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Comparator;
import org.bukkit.material.Crops;
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
import org.junit.jupiter.api.Test;

@Deprecated(forRemoval = true)
public class MaterialDataTest {

    @Test
    public void testDoor() {
        @SuppressWarnings("deprecation")
        Door door = new Door();
        assertThat(door.getItemType(), equalTo(Material.LEGACY_WOODEN_DOOR), "Constructed with default door type");
        assertThat(door.isTopHalf(), equalTo(false), "Constructed with default top or bottom");
        assertThat(door.getFacing(), equalTo(BlockFace.WEST), "Constructed with default direction");
        assertThat(door.isOpen(), equalTo(false), "Constructed with default open state");

        Material[] types = new Material[]{Material.LEGACY_WOODEN_DOOR,
            Material.LEGACY_IRON_DOOR_BLOCK, Material.LEGACY_SPRUCE_DOOR,
            Material.LEGACY_BIRCH_DOOR, Material.LEGACY_JUNGLE_DOOR,
            Material.LEGACY_ACACIA_DOOR, Material.LEGACY_DARK_OAK_DOOR};
        BlockFace[] directions = new BlockFace[]{BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH};
        boolean[] openStates = new boolean[]{false, true};
        boolean[] hingeStates = new boolean[]{false, true};
        for (Material type : types) {
            // Test bottom half
            for (BlockFace facing : directions) {
                door = new Door(type, facing);
                assertThat(door.getItemType(), equalTo(type), "Constructed with correct door type");
                assertThat(door.isTopHalf(), equalTo(false), "Constructed with default top or bottom");
                assertThat(door.getFacing(), equalTo(facing), "Constructed with correct direction");
                assertThat(door.isOpen(), equalTo(false), "Constructed with default open state");

                for (boolean openState : openStates) {
                    door = new Door(type, facing, openState);
                    assertThat(door.getItemType(), equalTo(type), "Constructed with correct door type");
                    assertThat(door.isTopHalf(), equalTo(false), "Constructed with default top or bottom");
                    assertThat(door.getFacing(), equalTo(facing), "Constructed with correct direction");
                    assertThat(door.isOpen(), equalTo(openState), "Constructed with correct open state");
                }
            }

            // Test top half
            for (boolean hingeState : hingeStates) {
                door = new Door(type, hingeState);
                assertThat(door.getItemType(), equalTo(type), "Constructed with correct door type");
                assertThat(door.isTopHalf(), equalTo(true), "Constructed with default top or bottom");
                assertThat(door.getHinge(), equalTo(hingeState), "Constructed with correct direction");
            }
        }
    }

    @Test
    public void testWood() {
        Wood wood = new Wood();
        assertThat(wood.getItemType(), equalTo(Material.LEGACY_WOOD), "Constructed with default wood type");
        assertThat(wood.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");

        TreeSpecies[] allSpecies = TreeSpecies.values();
        for (TreeSpecies species : allSpecies) {
            wood = new Wood(species);
            assertThat(wood.getItemType(), equalTo(Material.LEGACY_WOOD), "Constructed with default wood type");
            assertThat(wood.getSpecies(), equalTo(species), "Constructed with correct tree species");
        }

        Material[] types = new Material[]{Material.LEGACY_WOOD, Material.LEGACY_WOOD_DOUBLE_STEP};
        for (Material type : types) {
            wood = new Wood(type);
            assertThat(wood.getItemType(), equalTo(type), "Constructed with correct wood type");
            assertThat(wood.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");

            for (TreeSpecies species : allSpecies) {
                wood = new Wood(type, species);
                assertThat(wood.getItemType(), equalTo(type), "Constructed with correct wood type");
                assertThat(wood.getSpecies(), equalTo(species), "Constructed with correct tree species");
            }
        }
    }

    @Test
    public void testTree() {
        Tree tree = new Tree();
        assertThat(tree.getItemType(), equalTo(Material.LEGACY_LOG), "Constructed with default tree type");
        assertThat(tree.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(tree.getDirection(), equalTo(BlockFace.UP), "Constructed with default direction");

        tree = new Tree(Material.LEGACY_LOG);
        assertThat(tree.getItemType(), equalTo(Material.LEGACY_LOG), "Constructed with correct tree type");
        assertThat(tree.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(tree.getDirection(), equalTo(BlockFace.UP), "Constructed with default direction");

        Material[] types = new Material[]{Material.LEGACY_LOG, Material.LEGACY_LOG_2};
        TreeSpecies[][] allSpecies = new TreeSpecies[][]{
            {TreeSpecies.GENERIC, TreeSpecies.REDWOOD, TreeSpecies.BIRCH, TreeSpecies.JUNGLE},
            {TreeSpecies.ACACIA, TreeSpecies.DARK_OAK}
        };
        BlockFace[] allDirections = new BlockFace[]{BlockFace.UP, BlockFace.WEST, BlockFace.NORTH, BlockFace.SELF};
        for (int t = 0; t < types.length; t++) {
            for (TreeSpecies species : allSpecies[t]) {
                tree = new Tree(types[t], species);
                assertThat(tree.getItemType(), equalTo(types[t]), "Constructed with correct tree type");
                assertThat(tree.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(tree.getDirection(), equalTo(BlockFace.UP), "Constructed with default direction");

                // check item type is fixed automatically for invalid type-species combo
                tree = new Tree(types[types.length - 1 - t], species);
                assertThat(tree.getItemType(), equalTo(types[t]), "Constructed with fixed tree type");
                assertThat(tree.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(tree.getDirection(), equalTo(BlockFace.UP), "Constructed with default direction");
                for (BlockFace dir : allDirections) {
                    tree = new Tree(types[t], species, dir);
                    assertThat(tree.getItemType(), equalTo(types[t]), "Constructed with correct tree type");
                    assertThat(tree.getSpecies(), equalTo(species), "Constructed with correct tree species");
                    assertThat(tree.getDirection(), equalTo(dir), "Constructed with correct direction");
                }
            }
        }
    }

    @Test
    public void testLeaves() {
        Leaves leaves = new Leaves();
        assertThat(leaves.getItemType(), equalTo(Material.LEGACY_LEAVES), "Constructed with default leaf type");
        assertThat(leaves.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(leaves.isDecayable(), equalTo(true), "Constructed with default decayable");
        assertThat(leaves.isDecaying(), equalTo(false), "Constructed with default decaying");

        leaves = new Leaves(Material.LEGACY_LEAVES);
        assertThat(leaves.getItemType(), equalTo(Material.LEGACY_LEAVES), "Constructed with correct leaf type");
        assertThat(leaves.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(leaves.isDecayable(), equalTo(true), "Constructed with default decayable");
        assertThat(leaves.isDecaying(), equalTo(false), "Constructed with default decaying");

        Material[] types = new Material[]{Material.LEGACY_LEAVES, Material.LEGACY_LEAVES_2};
        TreeSpecies[][] allSpecies = new TreeSpecies[][]{
            {TreeSpecies.GENERIC, TreeSpecies.REDWOOD, TreeSpecies.BIRCH, TreeSpecies.JUNGLE},
            {TreeSpecies.ACACIA, TreeSpecies.DARK_OAK}
        };
        boolean[] decayable = new boolean[]{true, false};
        boolean[] decaying = new boolean[]{true, false};
        for (int t = 0; t < types.length; t++) {
            for (TreeSpecies species : allSpecies[t]) {
                leaves = new Leaves(types[t], species);
                assertThat(leaves.getItemType(), equalTo(types[t]), "Constructed with correct leaf type");
                assertThat(leaves.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(leaves.isDecayable(), equalTo(true), "Constructed with default decayable");
                assertThat(leaves.isDecaying(), equalTo(false), "Constructed with default decaying");

                // check item type is fixed automatically for invalid type-species combo
                leaves = new Leaves(types[types.length - 1 - t], species);
                assertThat(leaves.getItemType(), equalTo(types[t]), "Constructed with fixed leaf type");
                assertThat(leaves.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(leaves.isDecayable(), equalTo(true), "Constructed with default decayable");
                assertThat(leaves.isDecaying(), equalTo(false), "Constructed with default decaying");
                for (boolean isDecayable : decayable) {
                    leaves = new Leaves(types[t], species, isDecayable);
                    assertThat(leaves.getItemType(), equalTo(types[t]), "Constructed with correct wood type");
                    assertThat(leaves.getSpecies(), equalTo(species), "Constructed with correct tree species");
                    assertThat(leaves.isDecayable(), equalTo(isDecayable), "Constructed with correct decayable");
                    assertThat(leaves.isDecaying(), equalTo(false), "Constructed with default decaying");
                    for (boolean isDecaying : decaying) {
                        leaves = new Leaves(types[t], species, isDecayable);
                        leaves.setDecaying(isDecaying);
                        assertThat(leaves.getItemType(), equalTo(types[t]), "Constructed with correct wood type");
                        assertThat(leaves.getSpecies(), equalTo(species), "Constructed with correct tree species");
                        assertThat(leaves.isDecayable(), equalTo(isDecaying || isDecayable), "Constructed with correct decayable");
                        assertThat(leaves.isDecaying(), equalTo(isDecaying), "Constructed with correct decaying");
                    }
                }
            }
        }
    }

    @Test
    public void testWoodenStep() {
        WoodenStep woodenStep = new WoodenStep();
        assertThat(woodenStep.getItemType(), equalTo(Material.LEGACY_WOOD_STEP), "Constructed with default step type");
        assertThat(woodenStep.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(woodenStep.isInverted(), equalTo(false), "Constructed with default inversion");

        TreeSpecies[] allSpecies = TreeSpecies.values();
        boolean[] inversion = new boolean[]{true, false};
        for (TreeSpecies species : allSpecies) {
            woodenStep = new WoodenStep(species);
            assertThat(woodenStep.getItemType(), equalTo(Material.LEGACY_WOOD_STEP), "Constructed with default step type");
            assertThat(woodenStep.getSpecies(), equalTo(species), "Constructed with correct tree species");
            assertThat(woodenStep.isInverted(), equalTo(false), "Constructed with default inversion");
            for (boolean isInverted : inversion) {
                woodenStep = new WoodenStep(species, isInverted);
                assertThat(woodenStep.getItemType(), equalTo(Material.LEGACY_WOOD_STEP), "Constructed with default step type");
                assertThat(woodenStep.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(woodenStep.isInverted(), equalTo(isInverted), "Constructed with correct inversion");
            }
        }
    }

    @Test
    public void testSapling() {
        Sapling sapling = new Sapling();
        assertThat(sapling.getItemType(), equalTo(Material.LEGACY_SAPLING), "Constructed with default sapling type");
        assertThat(sapling.getSpecies(), equalTo(TreeSpecies.GENERIC), "Constructed with default tree species");
        assertThat(sapling.isInstantGrowable(), equalTo(false), "Constructed with default growable");

        TreeSpecies[] allSpecies = TreeSpecies.values();
        boolean[] growable = new boolean[]{true, false};
        for (TreeSpecies species : allSpecies) {
            sapling = new Sapling(species);
            assertThat(sapling.getItemType(), equalTo(Material.LEGACY_SAPLING), "Constructed with default sapling type");
            assertThat(sapling.getSpecies(), equalTo(species), "Constructed with correct tree species");
            assertThat(sapling.isInstantGrowable(), equalTo(false), "Constructed with default growable");
            for (boolean isInstantGrowable : growable) {
                sapling = new Sapling(species, isInstantGrowable);
                assertThat(sapling.getItemType(), equalTo(Material.LEGACY_SAPLING), "Constructed with default sapling type");
                assertThat(sapling.getSpecies(), equalTo(species), "Constructed with correct tree species");
                assertThat(sapling.isInstantGrowable(), equalTo(isInstantGrowable), "Constructed with correct growable");
            }
        }
    }

    @Test
    public void testMushroom() {
        Material[] mushroomTypes = new Material[]{Material.LEGACY_HUGE_MUSHROOM_1, Material.LEGACY_HUGE_MUSHROOM_2};
        BlockFace[] setFaces = new BlockFace[]{BlockFace.SELF, BlockFace.UP, BlockFace.NORTH,
            BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
            BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};
        MushroomBlockTexture[] textures = MushroomBlockTexture.values();
        for (Material type : mushroomTypes) {
            Mushroom mushroom = new Mushroom(type);
            assertThat(mushroom.getItemType(), equalTo(type), "Constructed with correct mushroom type");
            assertThat(mushroom.getBlockTexture(), equalTo(MushroomBlockTexture.ALL_PORES), "Constructed with default pores face");

            for (int f = 0; f < setFaces.length; f++) {
                mushroom = new Mushroom(type, setFaces[f]);
                assertThat(mushroom.getItemType(), equalTo(type), "Constructed with correct mushroom type");
                assertThat(mushroom.getBlockTexture(), equalTo(MushroomBlockTexture.getCapByFace(setFaces[f])), "Constructed with correct texture");
            }

            for (MushroomBlockTexture texture : textures) {
                mushroom = new Mushroom(type, texture);
                assertThat(mushroom.getItemType(), equalTo(type), "Constructed with correct mushroom type");
                assertThat(mushroom.getBlockTexture(), equalTo(texture), "Constructed with correct texture");
            }
        }
    }

    @Test
    public void testCrops() {
        Crops crops = new Crops();
        assertThat(crops.getItemType(), equalTo(Material.LEGACY_CROPS), "Constructed with default crops type");
        assertThat(crops.getState(), equalTo(CropState.SEEDED), "Constructed with default crop state");

        CropState[] allStates = CropState.values();
        for (CropState state : allStates) {
            crops = new Crops(state);
            assertThat(crops.getItemType(), equalTo(Material.LEGACY_CROPS), "Constructed with default crops type");
            assertThat(crops.getState(), equalTo(state), "Constructed with correct crop state");
        }

        // The crops which fully implement all crop states
        Material[] allCrops = new Material[]{Material.LEGACY_CROPS, Material.LEGACY_CARROT, Material.LEGACY_POTATO};
        for (Material crop : allCrops) {
            crops = new Crops(crop);
            assertThat(crops.getItemType(), equalTo(crop), "Constructed with correct crops type");
            assertThat(crops.getState(), equalTo(CropState.SEEDED), "Constructed with default crop state");

            for (CropState state : allStates) {
                crops = new Crops(crop, state);
                assertThat(crops.getItemType(), equalTo(crop), "Constructed with correct crops type");
                assertThat(crops.getState(), equalTo(state), "Constructed with correct crop state");
            }
        }

        // Beetroot are crops too, but they only have four states
        // Setting different crop states for beetroot will return the following when retrieved back
        CropState[] beetrootStates = new CropState[]{CropState.SEEDED, CropState.SEEDED, CropState.SMALL, CropState.SMALL, CropState.TALL, CropState.TALL, CropState.RIPE, CropState.RIPE};
        assertThat(beetrootStates.length, equalTo(allStates.length), "Beetroot state translations match size");
        crops = new Crops(Material.LEGACY_BEETROOT_BLOCK);
        assertThat(crops.getItemType(), equalTo(Material.LEGACY_BEETROOT_BLOCK), "Constructed with correct crops type");
        assertThat(crops.getState(), equalTo(CropState.SEEDED), "Constructed with default crop state");
        for (int s = 0; s < beetrootStates.length; s++) {
            crops = new Crops(Material.LEGACY_BEETROOT_BLOCK, allStates[s]);
            assertThat(crops.getItemType(), equalTo(Material.LEGACY_BEETROOT_BLOCK), "Constructed with correct crops type");
            assertThat(crops.getState(), equalTo(beetrootStates[s]), "Constructed with correct crop state");
        }

        // In case you want to treat NetherWarts as Crops, although they really aren't
        crops = new Crops(Material.LEGACY_NETHER_WARTS);
        NetherWarts warts = new NetherWarts();
        assertThat(crops.getItemType(), equalTo(warts.getItemType()), "Constructed with correct crops type");
        assertThat(crops.getState(), equalTo(CropState.SEEDED), "Constructed with default crop state");
        assertThat(warts.getState(), equalTo(NetherWartsState.SEEDED), "Constructed with default wart state");
        allStates = new CropState[]{CropState.SEEDED, CropState.SMALL, CropState.TALL, CropState.RIPE};
        NetherWartsState[] allWartStates = NetherWartsState.values();
        assertThat(allWartStates.length, equalTo(allStates.length), "Nether Warts state translations match size");
        for (int s = 0; s < allStates.length; s++) {
            crops = new Crops(Material.LEGACY_NETHER_WARTS, allStates[s]);
            warts = new NetherWarts(allWartStates[s]);
            assertThat(crops.getItemType(), equalTo(warts.getItemType()), "Constructed with correct crops type");
            assertThat(crops.getState(), equalTo(allStates[s]), "Constructed with correct crop state");
            assertThat(warts.getState(), equalTo(allWartStates[s]), "Constructed with correct wart state");
        }
    }

    @Test
    public void testDiode() {
        Diode diode = new Diode();
        assertThat(diode.getItemType(), equalTo(Material.LEGACY_DIODE_BLOCK_ON), "Constructed with backward compatible diode state");
        assertThat(diode.isPowered(), equalTo(true), "Constructed with backward compatible powered");
        assertThat(diode.getDelay(), equalTo(1), "Constructed with default delay");
        assertThat(diode.getFacing(), equalTo(BlockFace.NORTH), "Constructed with default direction");

        BlockFace[] directions = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        int[] delays = new int[]{1, 2, 3, 4};
        boolean[] states = new boolean[]{false, true};
        for (BlockFace direction : directions) {
            diode = new Diode(direction);
            assertThat(diode.getItemType(), equalTo(Material.LEGACY_DIODE_BLOCK_OFF), "Constructed with default diode state");
            assertThat(diode.isPowered(), equalTo(false), "Constructed with default powered");
            assertThat(diode.getDelay(), equalTo(1), "Constructed with default delay");
            assertThat(diode.getFacing(), equalTo(direction), "Constructed with correct direction");
            for (int delay : delays) {
                diode = new Diode(direction, delay);
                assertThat(diode.getItemType(), equalTo(Material.LEGACY_DIODE_BLOCK_OFF), "Constructed with default diode state");
                assertThat(diode.isPowered(), equalTo(false), "Constructed with default powered");
                assertThat(diode.getDelay(), equalTo(delay), "Constructed with correct delay");
                assertThat(diode.getFacing(), equalTo(direction), "Constructed with correct direction");
                for (boolean state : states) {
                    diode = new Diode(direction, delay, state);
                    assertThat(diode.getItemType(), equalTo(state ? Material.LEGACY_DIODE_BLOCK_ON : Material.LEGACY_DIODE_BLOCK_OFF), "Constructed with correct diode state");
                    assertThat(diode.isPowered(), equalTo(state), "Constructed with default powered");
                    assertThat(diode.getDelay(), equalTo(delay), "Constructed with correct delay");
                    assertThat(diode.getFacing(), equalTo(direction), "Constructed with correct direction");
                }
            }
        }
    }

    @Test
    public void testComparator() {
        Comparator comparator = new Comparator();
        assertThat(comparator.getItemType(), equalTo(Material.LEGACY_REDSTONE_COMPARATOR_OFF), "Constructed with default comparator state");
        assertThat(comparator.isPowered(), equalTo(false), "Constructed with default powered");
        assertThat(comparator.isBeingPowered(), equalTo(false), "Constructed with default being powered");
        assertThat(comparator.isSubtractionMode(), equalTo(false), "Constructed with default mode");
        assertThat(comparator.getFacing(), equalTo(BlockFace.NORTH), "Constructed with default direction");

        BlockFace[] directions = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        boolean[] modes = new boolean[]{false, true};
        boolean[] states = new boolean[]{false, true};
        for (BlockFace direction : directions) {
            comparator = new Comparator(direction);
            assertThat(comparator.getItemType(), equalTo(Material.LEGACY_REDSTONE_COMPARATOR_OFF), "Constructed with default comparator state");
            assertThat(comparator.isPowered(), equalTo(false), "Constructed with default powered");
            assertThat(comparator.isBeingPowered(), equalTo(false), "Constructed with default being powered");
            assertThat(comparator.isSubtractionMode(), equalTo(false), "Constructed with default mode");
            assertThat(comparator.getFacing(), equalTo(direction), "Constructed with correct direction");
            for (boolean mode : modes) {
                comparator = new Comparator(direction, mode);
                assertThat(comparator.getItemType(), equalTo(Material.LEGACY_REDSTONE_COMPARATOR_OFF), "Constructed with default comparator state");
                assertThat(comparator.isPowered(), equalTo(false), "Constructed with default powered");
                assertThat(comparator.isBeingPowered(), equalTo(false), "Constructed with default being powered");
                assertThat(comparator.isSubtractionMode(), equalTo(mode), "Constructed with correct mode");
                assertThat(comparator.getFacing(), equalTo(direction), "Constructed with correct direction");
                for (boolean state : states) {
                    comparator = new Comparator(direction, mode, state);
                    assertThat(comparator.getItemType(), equalTo(state ? Material.LEGACY_REDSTONE_COMPARATOR_ON : Material.LEGACY_REDSTONE_COMPARATOR_OFF), "Constructed with correct comparator state");
                    assertThat(comparator.isPowered(), equalTo(state), "Constructed with correct powered");
                    assertThat(comparator.isBeingPowered(), equalTo(false), "Constructed with default being powered");
                    assertThat(comparator.isSubtractionMode(), equalTo(mode), "Constructed with correct mode");
                    assertThat(comparator.getFacing(), equalTo(direction), "Constructed with correct direction");

                    // Check if the game sets the fourth bit, that block data is still interpreted correctly
                    comparator.setData((byte) ((comparator.getData() & 0x7) | 0x8));
                    assertThat(comparator.getItemType(), equalTo(state ? Material.LEGACY_REDSTONE_COMPARATOR_ON : Material.LEGACY_REDSTONE_COMPARATOR_OFF), "Constructed with correct comparator state");
                    assertThat(comparator.isPowered(), equalTo(state), "Constructed with correct powered");
                    assertThat(comparator.isBeingPowered(), equalTo(true), "Constructed with correct being powered");
                    assertThat(comparator.isSubtractionMode(), equalTo(mode), "Constructed with correct mode");
                    assertThat(comparator.getFacing(), equalTo(direction), "Constructed with correct direction");
                }
            }
        }
    }

    @Test
    public void testHopper() {
        Hopper hopper = new Hopper();
        assertThat(hopper.getItemType(), equalTo(Material.LEGACY_HOPPER), "Constructed with default hopper type");
        assertThat(hopper.isActive(), equalTo(true), "Constructed with default active state");
        assertThat(hopper.isPowered(), equalTo(false), "Constructed with default powered state");
        assertThat(hopper.getFacing(), equalTo(BlockFace.DOWN), "Constructed with default direction");

        BlockFace[] directions = new BlockFace[]{BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
        boolean[] activeStates = new boolean[]{true, false};
        for (BlockFace direction : directions) {
            hopper = new Hopper(direction);
            assertThat(hopper.getItemType(), equalTo(Material.LEGACY_HOPPER), "Constructed with default hopper type");
            assertThat(hopper.isActive(), equalTo(true), "Constructed with default active state");
            assertThat(hopper.isPowered(), equalTo(false), "Constructed with correct powered state");
            assertThat(hopper.getFacing(), equalTo(direction), "Constructed with correct direction");
            for (boolean isActive : activeStates) {
                hopper = new Hopper(direction, isActive);
                assertThat(hopper.getItemType(), equalTo(Material.LEGACY_HOPPER), "Constructed with default hopper type");
                assertThat(hopper.isActive(), equalTo(isActive), "Constructed with correct active state");
                assertThat(hopper.isPowered(), equalTo(!isActive), "Constructed with correct powered state");
                assertThat(hopper.getFacing(), equalTo(direction), "Constructed with correct direction");
            }
        }
    }
}
