package io.papermc.paper.world.attribute;

import io.papermc.paper.math.Position;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import io.papermc.paper.util.MCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;

public class PaperEnvironmentalAttribute<API, NMS> implements EnvironmentalAttribute<API> {

    private final EnvironmentAttributeSystem attributeSystem;
    private final PaperEnvironmentalAttributeType<API, NMS> type;
    private final PaperTypedDataAdapter<API, NMS> adapter;

    public PaperEnvironmentalAttribute(final EnvironmentAttributeSystem attributeSystem, final PaperEnvironmentalAttributeType<API, NMS> type) {
        this.attributeSystem = attributeSystem;
        this.type = type;
        this.adapter = type.getAdapter();
    }

    @Override
    public API getGlobal() {
        return this.adapter.fromVanilla(this.attributeSystem.getDimensionValue(this.type.getHandle()));
    }

    @Override
    public API getPositioned(final Position position) {
        return this.adapter.fromVanilla(this.attributeSystem.getValue(this.type.getHandle(), MCUtil.toVec3(position)));
    }

    public API getPositioned(final BlockPos pos) {
        return this.adapter.fromVanilla(this.attributeSystem.getValue(this.type.getHandle(), pos));
    }

    @Override
    public API getValue(final EnvironmentalAttributeContext context) {
        if (context.equals(PaperEnvironmentalAttributeContext.EMPTY)) {
            // No field is set, return the global value to prevent invalidating cache
            return this.getGlobal();
        }

        Position position = context.position();
        if (position != null && context.time() == null && context.rainLevel() == null && context.thunderLevel() == null) {
            // Only position is set, return cached positioned value
            return this.getPositioned(position);
        }

        PaperEnvironmentalAttributeContext.CURRENT_CONTEXT.set((PaperEnvironmentalAttributeContext) context);
        try {
            this.attributeSystem.invalidateTickCache(); // Invalidate cache, otherwise it would return the cached value if it was already requested in the same tick
            return position == null ? this.getGlobal() : this.adapter.fromVanilla(this.attributeSystem.getValue(this.type.getHandle(), MCUtil.toVec3(position)));
        } finally {
            PaperEnvironmentalAttributeContext.CURRENT_CONTEXT.set(PaperEnvironmentalAttributeContext.EMPTY);
        }
    }
}
