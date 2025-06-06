package io.papermc.paper.dialog.inputs;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.InputElement;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.input.BooleanInput;
import net.minecraft.server.dialog.input.SingleOptionInput;
import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class PaperSingleOptionInput extends PaperInputElementBase<PaperSingleOptionInput> implements InputElement.SingleOption<PaperSingleOptionInput> {
    boolean labelVisible = true;
    int width = 200;
    List<Option> options = List.of();

    @Override
    public boolean labelVisible() {
        return this.labelVisible;
    }

    @Override
    public PaperSingleOptionInput labelVisible(final boolean flag) {
        this.labelVisible = flag;
        return this;
    }

    @Override
    public int width() {
        return 0;
    }

    @Override
    public PaperSingleOptionInput width(final int width) {
        this.width = width;
        return this;
    }

    @Override
    public List<Option> options() {
        return this.options;
    }

    @Override
    public PaperSingleOptionInput options(final List<Option> options) {
        this.options = options;
        return this;
    }

    @Override
    public Input input() {
        return new Input(
            this.key,
            new SingleOptionInput(
                this.width,
                this.options.stream()
                    .map(x -> new SingleOptionInput.Entry(x.id(), Optional.of(PaperAdventure.asVanilla(x.display())), x.initial()))
                    .toList(),
                PaperAdventure.asVanilla(this.label),
                this.labelVisible
            )
        );
    }

    public record OptionButton(
        String id,
        Component display,
        boolean initial
    ) implements Option {
        @Override
        public Option id(final String id) {
            return new OptionButton(id, display, initial);
        }

        @Override
        public Option display(final Component display) {
            return new OptionButton(id, display, initial);
        }

        @Override
        public Option initial(final boolean initial) {
            return new OptionButton(id, display, initial);
        }
    }
}
