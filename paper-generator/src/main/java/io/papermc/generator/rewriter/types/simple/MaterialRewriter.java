package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.preset.model.EnumValue;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.data.BlockData;

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
                Class<?> blockData = BlockStateMapping.getBestSuitedApiClass(block.getClass());
                if (blockData == null) {
                    blockData = BlockData.class;
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
            return BuiltInRegistries.BLOCK.listElements().filter(reference -> reference.value().defaultBlockState().useShapeForLightOcclusion())
                .map(reference -> reference.key().identifier().getPath().toUpperCase(Locale.ENGLISH)).sorted(Formatting.alphabeticKeyOrder(Function.identity()))::iterator;
        }
    }*/

    // items

    public static class Items extends EnumRegistryRewriter<Item> {

        public Items() {
            super(Registries.ITEM, false);
        }

        @Override
        protected Iterable<Holder.Reference<Item>> getValues() {
            return BuiltInRegistries.ITEM.listElements().filter(reference -> BuiltInRegistries.BLOCK.getOptional(reference.key().identifier()).isEmpty() || reference.value().equals(net.minecraft.world.item.Items.AIR))
                .sorted(Formatting.HOLDER_ORDER)::iterator;
        }

        @Override
        protected EnumValue.Builder rewriteEnumValue(Holder.Reference<Item> reference) {
            return super.rewriteEnumValue(reference).argument(Integer.toString(-1)); // id not needed for non legacy material
        }
    }
}
