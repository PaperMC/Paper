package io.papermc.paper.world.attribute;

import io.papermc.paper.registry.data.typed.PaperTypedDataAdapter;
import io.papermc.paper.util.MCUtil;
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
    public API getValue(final EnvironmentalAttributeContext context) {
        PaperEnvironmentalAttributeContext.CURRENT_CONTEXT.set((PaperEnvironmentalAttributeContext) context);
        this.attributeSystem.invalidateTickCache(); // Invalidate cache, otherwise it would return the cached value if it was already requested in the same tick
        API value = context.position() == null ? this.getGlobal() : this.adapter.fromVanilla(this.attributeSystem.getValue(this.type.getHandle(), MCUtil.toVec3(context.position())));
        PaperEnvironmentalAttributeContext.CURRENT_CONTEXT.set(PaperEnvironmentalAttributeContext.EMPTY);
        return value;
    }
}
