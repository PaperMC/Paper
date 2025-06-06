package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ApiStatus.Experimental
public interface InputElement<E extends InputElement<E>> {
    String key();
    E key(String key);

    Component label();
    E label(Component component);

    static Text<?> text(String key, Component label) {
        return DialogBridge.BRIDGE.text()
            .key(key)
            .label(label);
    }

    static Checkbox<?> checkbox(String key, Component label) {
        return DialogBridge.BRIDGE.checkbox()
            .key(key)
            .label(label);
    }

    static SingleOption<?> singleOption(String key, Component label) {
        return DialogBridge.BRIDGE.singleOption()
            .key(key)
            .label(label);
    }

    static Option option() {
        return DialogBridge.BRIDGE.option();
    }

    static NumberSlider<?> numberSlider(String key, Component label) {
        return DialogBridge.BRIDGE.numberSlider()
            .key(key)
            .label(label);
    }

    interface Text<B extends Text<B>> extends InputElement<B> {
        int width();
        B width(int width);

        boolean labelVisible();
        B labelVisible(boolean flag);

        String initialValue();
        B initialValue(String value);

        int maxLength();
        B maxLength(int maxLength);

        // TODO: handle multi-line text box
    }

    interface Checkbox<B extends Checkbox<B>> extends InputElement<B> {
        boolean initialValue();
        B initialValue(boolean flag);

        String onTrue();
        B onTrue(String value);

        String onFalse();
        B onFalse(String value);
    }

    interface SingleOption<B extends SingleOption<B>> extends InputElement<B> {
        boolean labelVisible();
        B labelVisible(boolean flag);

        int width();
        B width(int width);

        List<Option> options();
        B options(List<Option> options);
        default B options(Option... options) {
            return this.options(List.of(options));
        }
    }

    interface Option {
        String id();
        Option id(String id);

        Component display();
        Option display(Component component);

        boolean initial();
        Option initial(boolean flag);
    }

    interface NumberSlider<B extends NumberSlider<B>> extends InputElement<B> {
        String labelFormat();
        B labelFormat(String key);

        int width();
        B width(int width);

        float start();
        B start(float startValue);

        float end();
        B end(float endValue);

        float step();
        B step(float stepValue);

        float initial();
        B initial(float initialValue);
    }
}
