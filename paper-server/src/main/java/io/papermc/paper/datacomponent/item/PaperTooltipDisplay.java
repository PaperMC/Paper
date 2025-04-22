package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperTooltipDisplay(
    net.minecraft.world.item.component.TooltipDisplay impl
) implements TooltipDisplay, Handleable<net.minecraft.world.item.component.TooltipDisplay> {

    @Override
    public net.minecraft.world.item.component.TooltipDisplay getHandle() {
        return this.impl;
    }

    @Override
    public boolean hideTooltip() {
        return this.impl.hideTooltip();
    }

    @Override
    public Set<DataComponentType> hiddenComponents() {
        return this.impl.hiddenComponents().stream()
            .map(PaperDataComponentType::minecraftToBukkit)
            .collect(Collectors.toCollection(ReferenceLinkedOpenHashSet::new));
    }

    static final class BuilderImpl implements Builder {

        private final Set<DataComponentType> hiddenComponents = new ReferenceLinkedOpenHashSet<>();
        private boolean hideTooltip;

        @Override
        public Builder hideTooltip(final boolean hide) {
            this.hideTooltip = hide;
            return this;
        }

        @Override
        public Builder addHiddenComponents(final DataComponentType... components) {
            this.hiddenComponents.addAll(Arrays.asList(components));
            return this;
        }

        @Override
        public Builder hiddenComponents(final Set<DataComponentType> components) {
            this.hiddenComponents.addAll(components);
            return this;
        }

        @Override
        public TooltipDisplay build() {
            return new PaperTooltipDisplay(new net.minecraft.world.item.component.TooltipDisplay(
                this.hideTooltip,
                this.hiddenComponents.stream().map(PaperDataComponentType::bukkitToMinecraft).collect(Collectors.toCollection(ReferenceLinkedOpenHashSet::new))
            ));
        }
    }
}
