package io.papermc.paper.adventure;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.util.FormattedCharSequence;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.Nullable;

public final class AdventureComponent implements net.minecraft.network.chat.Component {
    final Component adventure;
    private net.minecraft.network.chat.@MonotonicNonNull Component vanilla;

    public AdventureComponent(final Component adventure) {
        this.adventure = adventure;
    }

    public net.minecraft.network.chat.Component deepConverted() {
        net.minecraft.network.chat.Component vanilla = this.vanilla;
        if (vanilla == null) {
            vanilla = PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(this.adventure);
            this.vanilla = vanilla;
        }
        return vanilla;
    }

    public net.minecraft.network.chat.@Nullable Component deepConvertedIfPresent() {
        return this.vanilla;
    }

    @Override
    public Style getStyle() {
        return this.deepConverted().getStyle();
    }

    @Override
    public ComponentContents getContents() {
        if (this.adventure instanceof TextComponent) {
            return PlainTextContents.create(((TextComponent) this.adventure).content());
        } else {
            return this.deepConverted().getContents();
        }
    }

    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(this.adventure);
    }

    @Override
    public List<net.minecraft.network.chat.Component> getSiblings() {
        return this.deepConverted().getSiblings();
    }

    @Override
    public MutableComponent plainCopy() {
        return this.deepConverted().plainCopy();
    }

    @Override
    public MutableComponent copy() {
        return this.deepConverted().copy();
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return this.deepConverted().getVisualOrderText();
    }

    public Component adventure$component() {
        return this.adventure;
    }

    @Override
    public int hashCode() {
        return this.deepConverted().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return this.deepConverted().equals(obj);
    }
}
