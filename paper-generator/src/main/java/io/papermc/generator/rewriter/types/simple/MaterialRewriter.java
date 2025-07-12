package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.preset.model.EnumValue;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import org.bukkit.block.data.BlockData;

import static io.papermc.generator.utils.Formatting.asCode;

@Deprecated(forRemoval = true)
public class MaterialRewriter {

    // blocks

    public static class Blocks extends EnumRegistryRewriter<Block> {

        public Blocks() {
            super(Registries.BLOCK, false);
        }

        @Override
        protected Iterable<Holder.Reference<Block>> getValues() {
            return BuiltInRegistries.BLOCK.listElements().filter(reference -> !reference.value().equals(net.minecraft.world.level.block.Blocks.AIR))
                .sorted(Formatting.HOLDER_ORDER)::iterator;
        }

        @Override
        protected EnumValue.Builder rewriteEnumValue(Holder.Reference<Block> reference) {
            EnumValue.Builder value = super.rewriteEnumValue(reference);
            Block block = reference.value();
            if (BlockStateMapping.MAPPING.containsKey(block.getClass())) {
                // some block can also be represented as item in that enum
                // doing a double job
                Optional<Item> equivalentItem = BuiltInRegistries.ITEM.getOptional(reference.key().location());

                if (equivalentItem.isEmpty() && block instanceof WallSignBlock) {
                    // wall sign block stack size is 16 for some reason like the sign item?
                    // but that rule doesn't work for the wall hanging sign block??
                    equivalentItem = Optional.of(block.asItem());
                }

                Class<?> blockData = BlockStateMapping.getBestSuitedApiClass(block.getClass());
                if (blockData == null) {
                    blockData = BlockData.class;
                }
                if (equivalentItem.isPresent() && equivalentItem.get().getDefaultMaxStackSize() != Item.DEFAULT_MAX_STACK_SIZE) {
                    return value.arguments(Integer.toString(-1), Integer.toString(equivalentItem.get().getDefaultMaxStackSize()), this.importCollector.getShortName(blockData).concat(".class"));
                }
                return value.arguments(Integer.toString(-1), this.importCollector.getShortName(blockData).concat(".class"));
            }
            return value.argument(Integer.toString(-1)); // id not needed for non legacy material
        }
    }

    /* todo test is broken
    public static class IsTransparent extends SwitchCaseRewriter {

        public IsTransparent() {
            super(false);
        }

        @Override
        protected Iterable<String> getCases() {
            return BuiltInRegistries.BLOCK.holders().filter(reference -> reference.value().defaultBlockState().useShapeForLightOcclusion())
            .map(reference -> reference.key().location().getPath().toUpperCase(Locale.ENGLISH)).sorted(Formatting.ALPHABETIC_KEY_ORDER)::iterator;
        }
    }*/

    // items

    public static class Items extends EnumRegistryRewriter<Item> {

        public Items() {
            super(Registries.ITEM, false);
        }

        @Override
        protected Iterable<Holder.Reference<Item>> getValues() {
            return BuiltInRegistries.ITEM.listElements().filter(reference -> BuiltInRegistries.BLOCK.getOptional(reference.key().location()).isEmpty() || reference.value().equals(net.minecraft.world.item.Items.AIR))
                .sorted(Formatting.HOLDER_ORDER)::iterator;
        }

        @Override
        protected EnumValue.Builder rewriteEnumValue(Holder.Reference<Item> reference) {
            EnumValue.Builder value = super.rewriteEnumValue(reference);
            Item item = reference.value();
            int maxStackSize = item.getDefaultMaxStackSize();
            if (maxStackSize != Item.DEFAULT_MAX_STACK_SIZE) {
                return value.arguments(asCode(-1, maxStackSize));
            }

            return value.argument(Integer.toString(-1)); // id not needed for non legacy material
        }
    }
}
