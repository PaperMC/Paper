package io.papermc.paper.dialog.inputs;

import io.papermc.paper.dialog.InputElement;
import net.kyori.adventure.text.Component;

public abstract class PaperInputElementBase<E extends PaperInputElementBase<E>> implements InputElementConversion<E> {
    String key = "no_key";
    Component label = Component.empty();

    @Override
    public String key() {
        return this.key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E key(final String key) {
        this.key = key;
        return (E) this;
    }

    @Override
    public Component label() {
        return this.label;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E label(final Component component) {
        this.label = component;
        return (E) this;
    }
}
