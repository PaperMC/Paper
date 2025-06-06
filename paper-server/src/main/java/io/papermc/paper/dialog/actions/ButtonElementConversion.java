package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ButtonElement;
import net.minecraft.server.dialog.ActionButton;

public interface ButtonElementConversion extends ButtonElement {
    ActionButton button();
}
