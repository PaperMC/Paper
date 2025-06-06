package io.papermc.paper.dialog.inputs;

import io.papermc.paper.dialog.InputElement;
import net.minecraft.server.dialog.Input;

public interface InputElementConversion<E extends InputElementConversion<E>> extends InputElement<E> {
    Input input();
}
