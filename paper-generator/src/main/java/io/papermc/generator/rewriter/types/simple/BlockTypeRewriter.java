package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.typewriter.ClassNamed;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class BlockTypeRewriter extends RegistryFieldRewriter<Block> {

    public BlockTypeRewriter() {
        super(Registries.BLOCK, "getBlockType");
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<Block> reference) {
        ClassNamed blockData = BlockStateMapping.getBestSuitedApiClass(reference.value().getClass());
        if (blockData == null) {
            blockData = Types.BLOCK_DATA;
        }

        return "%s<%s>".formatted(Types.BLOCK_TYPE_TYPED.dottedNestedName(), this.importCollector.getShortName(blockData));
    }
}
