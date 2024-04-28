package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;

public record PaperItemLore(
    net.minecraft.world.item.component.ItemLore impl
) implements ItemLore, Handleable<net.minecraft.world.item.component.ItemLore> {

    @Override
    public net.minecraft.world.item.component.ItemLore getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<Component> lines() {
        return MCUtil.transformUnmodifiable(this.impl.lines(), PaperAdventure::asAdventure);
    }

    @Override
    public @Unmodifiable List<Component> styledLines() {
        return MCUtil.transformUnmodifiable(this.impl.styledLines(), PaperAdventure::asAdventure);
    }

    static final class BuilderImpl implements ItemLore.Builder {

        private List<Component> lines = new ObjectArrayList<>();

        private static void validateLineCount(final int current, final int add) {
            final int newSize = current + add;
            Preconditions.checkArgument(
                newSize <= net.minecraft.world.item.component.ItemLore.MAX_LINES,
                "Cannot have more than %s lines, had %s",
                net.minecraft.world.item.component.ItemLore.MAX_LINES,
                newSize
            );
        }

        @Override
        public ItemLore.Builder lines(final List<? extends ComponentLike> lines) {
            validateLineCount(0, lines.size());
            this.lines = new ArrayList<>(ComponentLike.asComponents(lines));
            return this;
        }

        @Override
        public ItemLore.Builder addLine(final ComponentLike line) {
            validateLineCount(this.lines.size(), 1);
            this.lines.add(line.asComponent());
            return this;
        }

        @Override
        public ItemLore.Builder addLines(final List<? extends ComponentLike> lines) {
            validateLineCount(this.lines.size(), lines.size());
            this.lines.addAll(ComponentLike.asComponents(lines));
            return this;
        }

        @Override
        public ItemLore build() {
            if (this.lines.isEmpty()) {
                return new PaperItemLore(net.minecraft.world.item.component.ItemLore.EMPTY);
            }

            return new PaperItemLore(new net.minecraft.world.item.component.ItemLore(PaperAdventure.asVanilla(this.lines))); // asVanilla does a list clone
        }
    }
}
