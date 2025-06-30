package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

/**
 * A plain message body for a dialog.
 * <p>Created via {@link DialogBody#plainMessage(Component, int)}</p>
 */
public sealed interface PlainMessageDialogBody extends DialogBody permits PlainMessageBodyImpl {

    /**
     * The contents of the plain message body.
     *
     * @return the component contents
     */
    @Contract(pure = true)
    Component contents();

    /**
     * The width of the plain message body.
     *
     * @return the width
     */
    @Contract(pure = true)
    int width();
}
