package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.server.dialog.action.Action;

public interface ActionElementConversion extends ActionElement {
    Action action();
}
