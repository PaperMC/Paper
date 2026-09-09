package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.datacomponent.item.blocktransform.BlockTransformData;
import io.papermc.paper.datacomponent.item.blocktransform.BlockTransformer;
import java.util.List;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperBlockTransformer(
    net.minecraft.core.component.BlockTransformer imlp
) implements BlockTransformer, Handleable<net.minecraft.core.component.BlockTransformer> {

    @Override
    public net.minecraft.core.component.BlockTransformer getHandle() {
        return this.imlp;
    }

    @Override
    public List<BlockTransformData> transforms() {
        return List.of();
    }
}
