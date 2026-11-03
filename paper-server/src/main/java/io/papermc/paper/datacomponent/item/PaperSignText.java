package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.network.chat.CommonComponents;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperSignText(
    net.minecraft.world.level.block.entity.SignText impl
) implements SignText, Handleable<net.minecraft.world.level.block.entity.SignText> {

    @Override
    public net.minecraft.world.level.block.entity.SignText getHandle() {
        return this.impl;
    }

    @Override
    public List<Component> lines() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.impl.getMessages(false));
    }

    @Override
    public DyeColor color() {
        return DyeColor.getByWoolData((byte) this.impl.getColor().getId());
    }

    @Override
    public boolean hasGlowingText() {
        return this.impl.hasGlowingText();
    }

    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.impl.getMessages(false), this.impl.getMessages(true))
            .color(this.color())
            .hasGlowingText(this.hasGlowingText());
    }

    public static List<net.minecraft.network.chat.Component> fillWithBlankLines(final List<net.minecraft.network.chat.Component> lines) {
        while (lines.size() < net.minecraft.world.level.block.entity.SignText.LINES) {
            lines.add(CommonComponents.EMPTY);
        }
        return lines;
    }

    static final class BuilderImpl implements Builder {

        private List<net.minecraft.network.chat.Component> lines;
        private final List<net.minecraft.network.chat.Component> filteredLines;
        private net.minecraft.world.item.DyeColor color = net.minecraft.world.level.block.entity.SignText.EMPTY.getColor();
        private boolean hasGlowingText = net.minecraft.world.level.block.entity.SignText.EMPTY.hasGlowingText();

        BuilderImpl() {
            this(
                net.minecraft.world.level.block.entity.SignText.EMPTY.getMessages(false),
                net.minecraft.world.level.block.entity.SignText.EMPTY.getMessages(true)
            );
        }

        BuilderImpl(final List<net.minecraft.network.chat.Component> lines, final List<net.minecraft.network.chat.Component> filteredLines) {
            this.lines = new ObjectArrayList<>(lines);
            this.filteredLines = new ObjectArrayList<>(filteredLines);
        }

        @Override
        public Builder lines(final List<? extends ComponentLike> lines) {
            Preconditions.checkArgument(
                lines.size() <= net.minecraft.world.level.block.entity.SignText.LINES,
                "Cannot have more than %s lines, had %s",
                net.minecraft.world.level.block.entity.SignText.LINES, lines.size()
            );

            this.lines = fillWithBlankLines(PaperAdventure.asVanilla(ComponentLike.asComponents(lines)));
            return this;
        }

        @Override
        public Builder line(final int index, final ComponentLike line) {
            this.lines.set(index, PaperAdventure.asVanilla(line.asComponent()));
            return this;
        }

        @Override
        public Builder color(final DyeColor color) {
            this.color = net.minecraft.world.item.DyeColor.byId(color.getWoolData());
            return this;
        }

        @Override
        public Builder hasGlowingText(final boolean hasGlowingText) {
            this.hasGlowingText = hasGlowingText;
            return this;
        }

        @Override
        public SignText build() {
            if (this.lines.isEmpty() && this.filteredLines.isEmpty() && this.color == net.minecraft.world.item.DyeColor.BLACK && !this.hasGlowingText) {
                return new PaperSignText(net.minecraft.world.level.block.entity.SignText.EMPTY);
            }

            return new PaperSignText(new net.minecraft.world.level.block.entity.SignText(
                this.lines,
                this.filteredLines.isEmpty() ? this.lines : this.filteredLines,
                this.color,
                this.hasGlowingText
            ));
        }
    }
}
