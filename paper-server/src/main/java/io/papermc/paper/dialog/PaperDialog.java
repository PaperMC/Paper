package io.papermc.paper.dialog;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;

public final class PaperDialog extends HolderableBase<Dialog> implements io.papermc.paper.dialog.Dialog {

    public PaperDialog(final Holder<Dialog> holder) {
        super(holder);
    }
}
