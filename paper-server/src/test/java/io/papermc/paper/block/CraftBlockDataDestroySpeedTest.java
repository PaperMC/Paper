package io.papermc.paper.block;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

/**
 * CraftBlockData's {@link org.bukkit.craftbukkit.block.data.CraftBlockData#getDestroySpeed(ItemStack, boolean)}
 * uses a reimplementation of AttributeValue without any map to avoid attribute instance allocation and mutation
 * for 0 gain.
 * <p>
 * This test is responsible for ensuring that said logic emits the expected destroy speed under heavy attribute
 * modifier use.
 */
@AllFeatures
public class CraftBlockDataDestroySpeedTest {

    @Test
    public void testCorrectEnchantmentDestroySpeedComputation() {
        // Construct fake enchantment that has *all and multiple of* operations
        final Enchantment speedEnchantment = speedEnchantment();
        final BlockState blockStateToMine = Blocks.STONE.defaultBlockState();

        final ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutable.set(Holder.direct(speedEnchantment), 1);

        final net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.DIAMOND_PICKAXE);
        itemStack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());

        // Compute expected value by running the entire attribute instance chain
        final AttributeInstance dummyInstance = new AttributeInstance(Attributes.MINING_EFFICIENCY, $ -> {
        });
        EnchantmentHelper.forEachModifier(itemStack, EquipmentSlot.MAINHAND, (attributeHolder, attributeModifier) -> {
            if (attributeHolder.is(Attributes.MINING_EFFICIENCY)) dummyInstance.addTransientModifier(attributeModifier);
        });

        final double toolSpeed = itemStack.getDestroySpeed(blockStateToMine);
        final double expectedSpeed = toolSpeed <= 1.0F ? toolSpeed : toolSpeed + dummyInstance.getValue();

        // API stack + computation
        final CraftItemStack craftMirror = CraftItemStack.asCraftMirror(itemStack);
        final CraftBlockData data = CraftBlockData.createData(blockStateToMine);
        final float actualSpeed = data.getDestroySpeed(craftMirror, true);

        Assertions.assertEquals(expectedSpeed, actualSpeed, Vector.getEpsilon());
    }

    /**
     * Complex enchantment that holds attribute modifiers for the mining efficiency.
     * The enchantment holds 2 of each operation to also ensure that such behaviour works correctly.
     *
     * @return the enchantment.
     */
    private static @NotNull Enchantment speedEnchantment() {
        return new Enchantment(
            Component.empty(),
            new Enchantment.EnchantmentDefinition(
                HolderSet.empty(),
                Optional.empty(),
                0, 0,
                Enchantment.constantCost(0),
                Enchantment.constantCost(0),
                0,
                List.of(EquipmentSlotGroup.ANY)
            ),
            HolderSet.empty(),
            DataComponentMap.builder()
                .set(EnchantmentEffectComponents.ATTRIBUTES, List.of(
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "base1"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.constant(1),
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "base2"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.perLevel(3),
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "base-mul1"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.perLevel(7),
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "base-mul2"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.constant(10),
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "total-mul1"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.constant(.2f),
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    ),
                    new EnchantmentAttributeEffect(
                        fromNamespaceAndPath("paper", "total-mul2"),
                        Attributes.MINING_EFFICIENCY,
                        LevelBasedValue.constant(-.5F),
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
                ))
                .build()
        );
    }

}
