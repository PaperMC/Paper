package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;

public interface ButtonElement {
    static ButtonElement button() {
        return DialogBridge.BRIDGE.button();
    }

    Component label();
    ButtonElement label(Component text);

    Component tooltip();
    ButtonElement tooltip(Component tooltip);

    int width();
    ButtonElement width(int width);

    ActionElement action();
    ButtonElement action(ActionElement action);
}
