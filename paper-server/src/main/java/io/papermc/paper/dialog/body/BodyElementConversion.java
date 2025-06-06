package io.papermc.paper.dialog.body;

import io.papermc.paper.dialog.BodyElement;
import net.minecraft.server.dialog.body.DialogBody;

public interface BodyElementConversion extends BodyElement {
    DialogBody dialogBody();
}
