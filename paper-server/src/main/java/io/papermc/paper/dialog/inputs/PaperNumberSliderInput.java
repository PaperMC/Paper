package io.papermc.paper.dialog.inputs;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.InputElement;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.minecraft.server.dialog.input.TextInput;

import java.util.Optional;
import java.util.Random;

public class PaperNumberSliderInput extends PaperInputElementBase<PaperNumberSliderInput> implements InputElement.NumberSlider<PaperNumberSliderInput> {
    String labelFormat = "options.generic_value";
    int width = 200;
    float start = 0.0f;
    float end = 10.0f;
    Float step = null;
    Float initial = null;

    @Override
    public String labelFormat() {
        return this.labelFormat;
    }

    @Override
    public PaperNumberSliderInput labelFormat(final String key) {
        this.key = key;
        return this;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public PaperNumberSliderInput width(final int width) {
        this.width = width;
        return this;
    }

    @Override
    public float start() {
        return this.start;
    }

    @Override
    public PaperNumberSliderInput start(final float startValue) {
        this.start = startValue;
        return this;
    }

    @Override
    public float end() {
        return this.end;
    }

    @Override
    public PaperNumberSliderInput end(final float endValue) {
        this.end = endValue;
        return this;
    }

    @Override
    public float step() {
        return this.step;
    }

    @Override
    public PaperNumberSliderInput step(final float stepValue) {
        this.step = stepValue;
        return this;
    }

    @Override
    public float initial() {
        return this.initial;
    }

    @Override
    public PaperNumberSliderInput initial(final float initialValue) {
        this.initial = initialValue;
        return this;
    }

    @Override
    public Input input() {
        return new Input(
            this.key,
            new NumberRangeInput(
                this.width,
                PaperAdventure.asVanilla(this.label),
                this.labelFormat,
                new NumberRangeInput.RangeInfo(
                    this.start,
                    this.end,
                    Optional.ofNullable(this.step),
                    Optional.ofNullable(this.step)
                )
            )
        );
    }
}
