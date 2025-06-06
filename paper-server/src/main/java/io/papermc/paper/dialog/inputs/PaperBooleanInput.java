package io.papermc.paper.dialog.inputs;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.InputElement;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.input.BooleanInput;

public class PaperBooleanInput extends PaperInputElementBase<PaperBooleanInput> implements InputElement.Checkbox<PaperBooleanInput> {
    boolean initialValue = false;
    String onTrue = "true";
    String onFalse = "false";

    @Override
    public boolean initialValue() {
        return this.initialValue;
    }

    @Override
    public PaperBooleanInput initialValue(final boolean flag) {
        return this;
    }

    @Override
    public String onTrue() {
        return this.onTrue;
    }

    @Override
    public PaperBooleanInput onTrue(final String value) {
        return this;
    }

    @Override
    public String onFalse() {
        return this.onFalse;
    }

    @Override
    public PaperBooleanInput onFalse(final String value) {
        return this;
    }

    @Override
    public Input input() {
        return new Input(
            this.key,
            new BooleanInput(
                PaperAdventure.asVanilla(this.label),
                this.initialValue,
                this.onTrue,
                this.onFalse
            )
        );
    }
}
