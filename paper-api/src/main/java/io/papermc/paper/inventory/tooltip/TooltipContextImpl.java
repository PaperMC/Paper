package io.papermc.paper.inventory.tooltip;

import org.jspecify.annotations.NullMarked;

@NullMarked
record TooltipContextImpl(boolean isAdvanced, boolean isCreative) implements TooltipContext {

    @Override
    public TooltipContext asCreative() {
        return new TooltipContextImpl(this.isAdvanced, true);
    }

    @Override
    public TooltipContext asAdvanced() {
        return new TooltipContextImpl(true, this.isCreative);
    }
}
