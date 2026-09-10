package io.papermc.paper.registry.data;

import io.papermc.paper.datacomponent.item.blocktransformer.BlockTransformData;
import io.papermc.paper.datacomponent.item.blocktransformer.PaperBlockTransformData;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.BlockTransformer;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperBlockTransformerRegistryEntry implements BlockTransformerRegistryEntry {

    protected @Nullable List<BlockTransformer.BlockTransformData> transforms;

    public PaperBlockTransformerRegistryEntry(final Conversions conversions, final @Nullable BlockTransformer internal) {
        if (internal == null) {
            return;
        }
        this.transforms = new ArrayList<>(internal.transforms());
    }

    @Override
    public List<BlockTransformData> transforms() {
        return asConfigured(this.transforms, "transforms").stream().map(PaperBlockTransformData::new).map(BlockTransformData.class::cast).toList();
    }

    public static final class PaperBuilder extends PaperBlockTransformerRegistryEntry implements Builder, PaperRegistryBuilder<BlockTransformer, io.papermc.paper.datacomponent.item.blocktransformer.BlockTransformer> {

        public PaperBuilder(final Conversions conversions, final @Nullable BlockTransformer internal) {
            super(conversions, internal);
        }

        @Override
        public Builder addTransform(final BlockTransformData transform) {
            if (this.transforms == null) {
                this.transforms = new ArrayList<>();
            }
            this.transforms.add(PaperBlockTransformData.toVanilla(asArgument(transform, "transform")));
            return this;
        }

        @Override
        public Builder addTransforms(final List<BlockTransformData> transforms) {
            for (final BlockTransformData transform : asArgument(transforms, "transforms")) {
                this.addTransform(transform);
            }
            return this;
        }

        @Override
        public BlockTransformer build() {
            return new BlockTransformer(List.copyOf(asConfigured(this.transforms, "transforms")));
        }
    }
}
