package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.datacomponent.item.blocktransform.BlockTransformData;
import io.papermc.paper.datacomponent.item.blocktransform.DropStrategy;
import io.papermc.paper.datacomponent.item.blocktransform.TransformParticle;
import io.papermc.paper.datacomponent.item.blocktransform.TransformType;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.Handleable;
import org.checkerframework.checker.index.qual.NonNegative;

public record PaperBlockTransformData(
    net.minecraft.core.component.BlockTransformer.BlockTransformData imlp
) implements BlockTransformData, Handleable<BlockTransformer.BlockTransformData> {

    @Override
    public net.minecraft.core.component.BlockTransformer.BlockTransformData getHandle() {
        return this.imlp;
    }

    @Override
    public BlockPredicate predicate() {
        final BlockStateProvider blockStateProvider = this.imlp().blockStateProvider().value();
        blockStateProvider.
        BlockPredicate.predicate()
            .blocks(nms.blocks().map(blocks -> PaperRegistrySets.convertToApi(RegistryKey.BLOCK, blocks)).orElse(null)).build()
        return null;
    }

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.imlp().sound().value().location());
    }

    @Override
    public TransformParticle particle() {
        return TransformParticle.valueOf(this.imlp().particle().toString());
    }

    @Override
    public List<BlockFace> disallowedFaces() {
        return this.imlp().disallowedFaces().stream().map(CraftBlock::notchToBlockFace).toList();
    }

    @Override
    public DropStrategy dropStrategy() {
        return DropStrategy.valueOf(this.imlp().dropStrategy().toString());
    }

    @Override
    public boolean updateFromNeighbors() {
        return this.imlp().updateFromNeighbors();
    }

    @Override
    public TransformType transformType() {
        return TransformType.valueOf(this.imlp().transformType().toString());
    }

    @Override
    public boolean consumeOnUse() {
        return this.imlp().consumeOnUse();
    }

    @Override
    public @NonNegative int itemDamagePerUse() {
        return this.imlp().itemDamagePerUse();
    }
}
