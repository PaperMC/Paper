package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

public record PaperUnbreakable(
    net.minecraft.world.item.component.Unbreakable impl
) implements Unbreakable, Handleable<net.minecraft.world.item.component.Unbreakable> {

    @Override
    public boolean showInTooltip() {
        return this.impl.showInTooltip();
    }

    @Override
    public Unbreakable showInTooltip(final boolean showInTooltip) {
        return new PaperUnbreakable(this.impl.withTooltip(showInTooltip));
    }

    @Override
    public net.minecraft.world.item.component.Unbreakable getHandle() {
        return this.impl;
    }

    static final class BuilderImpl implements Unbreakable.Builder {

        private boolean showInTooltip = true;

        @Override
        public Unbreakable.Builder showInTooltip(final boolean showInTooltip) {
            this.showInTooltip = showInTooltip;
            return this;
        }

        @Override
        public Unbreakable build() {
            return new PaperUnbreakable(new net.minecraft.world.item.component.Unbreakable(this.showInTooltip));
        }
    }
}
