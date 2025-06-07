package io.papermc.paper.dialog.inputs;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.InputElement;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.input.TextInput;
import java.util.Optional;
import java.util.OptionalInt;

public class PaperTextInput extends PaperInputElementBase<PaperTextInput> implements InputElement.Text<PaperTextInput> {
    int width = 200;
    boolean labelVisible = true;
    String initialValue = "";
    int maxLength = 32;
    MultiLine multiLine;

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public PaperTextInput width(final int width) {
        this.width = width;
        return this;
    }

    @Override
    public boolean labelVisible() {
        return this.labelVisible;
    }

    @Override
    public PaperTextInput labelVisible(final boolean flag) {
        this.labelVisible = flag;
        return this;
    }

    @Override
    public String initialValue() {
        return this.initialValue;
    }

    @Override
    public PaperTextInput initialValue(final String value) {
        this.initialValue = value;
        return this;
    }

    @Override
    public int maxLength() {
        return this.maxLength;
    }

    @Override
    public PaperTextInput maxLength(final int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public MultiLine multiLine() {
        return this.multiLine;
    }

    @Override
    public PaperTextInput multiLine(final MultiLine config) {
        this.multiLine = config;
        return this;
    }

    @Override
    public Input input() {
        return new Input(
            this.key,
            new TextInput(
                this.width,
                PaperAdventure.asVanilla(this.label),
                this.labelVisible,
                this.initialValue,
                this.maxLength,
                Optional.ofNullable(this.multiLine)
                    .map(x -> new TextInput.MultilineOptions(
                        x.maxLines(),
                        x.height()
                    ))
            )
        );
    }

    public record PaperMultiLine(Optional<Integer> maxLines, Optional<Integer> height) implements MultiLine {
        @Override
        public MultiLine maxLines(final int maxLines) {
            return new PaperMultiLine(Optional.of(maxLines), height);
        }

        @Override
        public MultiLine height(final int height) {
            return new PaperMultiLine(maxLines, Optional.of(height));
        }
    }
}
