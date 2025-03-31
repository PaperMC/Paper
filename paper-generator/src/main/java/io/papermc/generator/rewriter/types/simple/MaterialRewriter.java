package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.preset.model.EnumConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;

@Deprecated(forRemoval = true)
public class MaterialRewriter {

    // blocks

    public static class Blocks extends EnumRegistryRewriter<Block> {

        public Blocks() {
            super(Registries.BLOCK);
        }

        @Override
        protected Iterable<Holder.Reference<Block>> getValues() {
            return BuiltInRegistries.BLOCK.listElements().filter(reference -> !reference.value().equals(net.minecraft.world.level.block.Blocks.AIR))
                .sorted(Formatting.HOLDER_ORDER)::iterator;
        }

        @Override
        protected void rewriteConstant(EnumConstant.Builder builder, Holder.Reference<Block> reference) {
            Block block = reference.value();
            List<String> args = new ArrayList<>(3);
            args.add(Integer.toString(-1)); // legacy id (not needed for non legacy material)

            if (BlockStateMapping.getOrCreate().containsKey(block.getClass())) {
                // some block can also be represented as item in that enum
                // doing a double job
                Optional<Item> equivalentItem = BuiltInRegistries.ITEM.getOptional(reference.key().location());

                if (equivalentItem.isEmpty() && block instanceof WallSignBlock) {
                    // wall sign block stack size is 16 for some reason like the sign item?
                    // but that rule doesn't work for the wall hanging sign block??
                    equivalentItem = Optional.of(block.asItem());
                }

                ClassNamed blockData = BlockStateMapping.getBestSuitedApiClass(block.getClass());
                if (blockData == null) {
                    blockData = Types.BLOCK_DATA;
                }
                if (equivalentItem.isPresent() && equivalentItem.get().getDefaultMaxStackSize() != Item.DEFAULT_MAX_STACK_SIZE) {
                    args.add(Integer.toString(equivalentItem.get().getDefaultMaxStackSize())); // max stack size
                }
                args.add(this.importCollector.getShortName(blockData).concat(".class")); // block data class
            }
            builder.arguments(args);
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
            super(Registries.ITEM);
        }

        @Override
        protected Iterable<Holder.Reference<Item>> getValues() {
            return BuiltInRegistries.ITEM.listElements().filter(reference -> BuiltInRegistries.BLOCK.getOptional(reference.key().location()).isEmpty() || reference.value().equals(net.minecraft.world.item.Items.AIR))
                .sorted(Formatting.HOLDER_ORDER)::iterator;
        }

        @Override
        protected void rewriteConstant(EnumConstant.Builder builder, Holder.Reference<Item> reference) {
            Item item = reference.value();
            List<String> args = new ArrayList<>(2);
            args.add(Integer.toString(-1)); // legacy id (not needed for non legacy material)

            int maxStackSize = item.getDefaultMaxStackSize();
            if (maxStackSize != Item.DEFAULT_MAX_STACK_SIZE) {
                args.add(Integer.toString(maxStackSize));
            }

            builder.arguments(args);
        }
    }
}
