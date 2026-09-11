package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
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
    public List<Component> messages() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.impl.getMessages(false));
    }

    @Override
    public List<Component> filteredMessages() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.impl.getMessages(true));
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
        return new BuilderImpl(this.impl.getMessages(false))
            .color(DyeColor.getByWoolData((byte) this.impl.getColor().getId()))
            .hasGlowingText(this.impl.hasGlowingText());
    }

    static final class BuilderImpl implements Builder {

        private List<net.minecraft.network.chat.Component> messages = net.minecraft.world.level.block.entity.SignText.EMPTY.getMessages(false);
        private List<net.minecraft.network.chat.Component> filteredMessages = net.minecraft.world.level.block.entity.SignText.EMPTY.getMessages(true);
        private net.minecraft.world.item.DyeColor color = net.minecraft.world.level.block.entity.SignText.EMPTY.getColor();
        private boolean hasGlowingText = net.minecraft.world.level.block.entity.SignText.EMPTY.hasGlowingText();

        BuilderImpl() {
        }

        BuilderImpl(List<net.minecraft.network.chat.Component> messages) {
            this.messages = messages;
            this.filteredMessages = messages;
        }

        private static void validateLineCount(final int current, final int add) {
            final int newSize = current + add;
            Preconditions.checkArgument(
                newSize <= net.minecraft.world.level.block.entity.SignText.LINES,
                "Cannot have more than %s lines, had %s",
                net.minecraft.world.level.block.entity.SignText.LINES,
                newSize
            );
        }

        @Override
        public Builder messages(final List<? extends ComponentLike> messages) {
            validateLineCount(0, messages.size());
            this.messages = PaperAdventure.asVanilla(new ArrayList<>(ComponentLike.asComponents(messages)));
            return this;
        }

        @Override
        public Builder addMessage(final ComponentLike message) {
            validateLineCount(this.messages.size(), 1);
            this.messages.add(PaperAdventure.asVanilla(message.asComponent()));
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
            if (this.messages.isEmpty()) {
                return new PaperSignText(net.minecraft.world.level.block.entity.SignText.EMPTY);
            }

            return new PaperSignText(new net.minecraft.world.level.block.entity.SignText(
                this.messages,
                (this.filteredMessages.isEmpty()) ? this.messages : this.filteredMessages,
                this.color,
                this.hasGlowingText
            ));
        }
    }
}
