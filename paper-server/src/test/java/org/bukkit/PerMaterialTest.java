package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemRecord;
import net.minecraft.world.level.BlockAccessAir;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockFalling;
import net.minecraft.world.level.block.BlockFire;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class PerMaterialTest extends AbstractTestingBase {
    private static Map<Block, Integer> fireValues;

    @BeforeAll
    public static void getFireValues() {
        fireValues = ((BlockFire) Blocks.FIRE).igniteOdds;
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isBlock(Material material) {
        if (material != Material.AIR && material != Material.CAVE_AIR && material != Material.VOID_AIR) {
            assertThat(material.isBlock(), is(not(CraftMagicNumbers.getBlock(material) == null)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isSolid(Material material) {
        if (material == Material.AIR) {
            assertFalse(material.isSolid());
        } else if (material.isBlock()) {
            assertThat(material.isSolid(), is(CraftMagicNumbers.getBlock(material).defaultBlockState().blocksMotion()));
        } else {
            assertFalse(material.isSolid());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = ".LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isEdible(Material material) {
        if (material.isBlock()) {
            assertFalse(material.isEdible());
        } else {
            assertThat(material.isEdible(), is(CraftMagicNumbers.getItem(material).components().has(DataComponents.FOOD)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isRecord(Material material) {
        assertThat(material.isRecord(), is(CraftMagicNumbers.getItem(material) instanceof ItemRecord));
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void maxDurability(Material material) {
        if (INVALIDATED_MATERIALS.contains(material)) return;

        if (material == Material.AIR) {
            assertThat((int) material.getMaxDurability(), is(0));
        } else if (material.isBlock()) {
            Item item = CraftMagicNumbers.getItem(material);
            assertThat((int) material.getMaxDurability(), is(item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void maxStackSize(Material material) {
        if (INVALIDATED_MATERIALS.contains(material)) return;

        final ItemStack bukkit = new ItemStack(material);
        final CraftItemStack craft = CraftItemStack.asCraftCopy(bukkit);
        if (material == Material.AIR) {
            final int MAX_AIR_STACK = 0 /* Why can't I hold all of these AIR? */;
            assertThat(material.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(bukkit.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(craft.getMaxStackSize(), is(MAX_AIR_STACK));
        } else {
            int max = CraftMagicNumbers.getItem(material).components().getOrDefault(DataComponents.MAX_STACK_SIZE, 64);
            assertThat(material.getMaxStackSize(), is(max));
            assertThat(bukkit.getMaxStackSize(), is(max));
            assertThat(craft.getMaxStackSize(), is(max));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isTransparent(Material material) {
        if (material == Material.AIR) {
            assertTrue(material.isTransparent());
        } else if (material.isBlock()) {
            // assertThat(material.isTransparent(), is(not(CraftMagicNumbers.getBlock(material).getBlockData().getMaterial().blocksLight()))); // PAIL: not unit testable anymore (17w50a)
        } else {
            assertFalse(material.isTransparent());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isFlammable(Material material) {
        if (material != Material.AIR && material.isBlock()) {
            assertThat(material.isFlammable(), is(CraftMagicNumbers.getBlock(material).defaultBlockState().ignitedByLava()));
        } else {
            assertFalse(material.isFlammable());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isBurnable(Material material) {
        if (material.isBlock()) {
            Block block = CraftMagicNumbers.getBlock(material);
            assertThat(material.isBurnable(), is(fireValues.containsKey(block) && fireValues.get(block) > 0));
        } else {
            assertFalse(material.isBurnable());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isFuel(Material material) {
        if (material.isItem()) {
            assertThat(material.isFuel(), is(TileEntityFurnace.isFuel(new net.minecraft.world.item.ItemStack(CraftMagicNumbers.getItem(material)))));
        } else {
            assertFalse(material.isFuel());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void isOccluding(Material material) {
        if (material.isBlock()) {
            assertThat(material.isOccluding(), is(CraftMagicNumbers.getBlock(material).defaultBlockState().isRedstoneConductor(BlockAccessAir.INSTANCE, BlockPosition.ZERO)));
        } else {
            assertFalse(material.isOccluding());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void hasGravity(Material material) {
        if (material.isBlock()) {
            assertThat(material.hasGravity(), is(CraftMagicNumbers.getBlock(material) instanceof BlockFalling));
        } else {
            assertFalse(material.hasGravity());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void usesDurability(Material material) {
        if (!material.isBlock()) {
            assertThat(EnchantmentTarget.BREAKABLE.includes(material), is(CraftMagicNumbers.getItem(material).components().getOrDefault(DataComponents.MAX_DAMAGE, 0) > 0));
        } else {
            assertFalse(EnchantmentTarget.BREAKABLE.includes(material));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testDurability(Material material) {
        if (!material.isBlock()) {
            assertThat(material.getMaxDurability(), is((short) (int) CraftMagicNumbers.getItem(material).components().getOrDefault(DataComponents.MAX_DAMAGE, 0)));
        } else {
            assertThat(material.getMaxDurability(), is((short) 0));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlock(Material material) {
        if (material == Material.AIR) {
            assertTrue(material.isBlock());
        } else {
            assertThat(material.isBlock(), is(equalTo(CraftMagicNumbers.getBlock(material) != null)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testAir(Material material) {
        if (material.isBlock()) {
            assertThat(material.isAir(), is(equalTo(CraftMagicNumbers.getBlock(material).defaultBlockState().isAir())));
        } else {
            assertThat(material.isAir(), is(equalTo(false)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testItem(Material material) {
        if (material == Material.AIR) {
            assertTrue(material.isItem());
        } else {
            assertThat(material.isItem(), is(equalTo(CraftMagicNumbers.getItem(material) != null)));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testInteractable(Material material) throws ReflectiveOperationException {
        if (material.isBlock()) {
            Class<?> clazz = CraftMagicNumbers.getBlock(material).getClass();

            boolean hasMethod = hasMethod(clazz, "useWithoutItem", IBlockData.class, net.minecraft.world.level.World.class, BlockPosition.class, EntityHuman.class, MovingObjectPositionBlock.class)
                    || hasMethod(clazz, "useItemOn", net.minecraft.world.item.ItemStack.class, IBlockData.class, net.minecraft.world.level.World.class, BlockPosition.class, EntityHuman.class, EnumHand.class, MovingObjectPositionBlock.class);

            if (!hasMethod && clazz.getSuperclass() != BlockBase.class) {
                clazz = clazz.getSuperclass();

                hasMethod = hasMethod(clazz, "useWithoutItem", IBlockData.class, net.minecraft.world.level.World.class, BlockPosition.class, EntityHuman.class, MovingObjectPositionBlock.class)
                        || hasMethod(clazz, "useItemOn", net.minecraft.world.item.ItemStack.class, IBlockData.class, net.minecraft.world.level.World.class, BlockPosition.class, EntityHuman.class, EnumHand.class, MovingObjectPositionBlock.class);
            }

            assertThat(material.isInteractable(),
                    is(hasMethod));
        } else {
            assertFalse(material.isInteractable());
        }
    }

    private boolean hasMethod(Class<?> clazz, String methodName, Class<?>... params) {
        boolean hasMethod;
        try {
            hasMethod = clazz.getDeclaredMethod(methodName, params) != null;
        } catch (NoSuchMethodException ex) {
            hasMethod = false;
        }

        return hasMethod;
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlockHardness(Material material) {
        if (material.isBlock()) {
            assertThat(material.getHardness(), is(CraftMagicNumbers.getBlock(material).defaultBlockState().destroySpeed));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlastResistance(Material material) {
        if (material.isBlock()) {
            assertThat(material.getBlastResistance(), is(CraftMagicNumbers.getBlock(material).getExplosionResistance()));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testSlipperiness(Material material) {
        if (material.isBlock()) {
            assertThat(material.getSlipperiness(), is(CraftMagicNumbers.getBlock(material).getFriction()));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlockDataCreation(Material material) {
        if (material.isBlock()) {
            assertNotNull(material.createBlockData());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testCraftingRemainingItem(Material material) {
        if (material.isItem()) {
            Item expectedItem = CraftMagicNumbers.getItem(material).getCraftingRemainingItem();
            Material expected = expectedItem == null ? null : CraftMagicNumbers.getMaterial(expectedItem);

            assertThat(material.getCraftingRemainingItem(), is(expected));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testEquipmentSlot(Material material) {
        if (material.isItem()) {
            EquipmentSlot expected = CraftEquipmentSlot.getSlot(EntityInsentient.getEquipmentSlotForItem(CraftItemStack.asNMSCopy(new ItemStack(material))));
            assertThat(material.getEquipmentSlot(), is(expected));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlockDataClass(Material material) {
        if (material.isBlock()) {
            Class<?> expectedClass = material.data;
            if (expectedClass != MaterialData.class) {
                BlockData blockData = Bukkit.createBlockData(material);
                assertTrue(expectedClass.isInstance(blockData), expectedClass + " <> " + blockData.getClass());
            }
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testCreativeCategory(Material material) {
        if (material.isItem()) {
            material.getCreativeCategory();
        }
    }
}
