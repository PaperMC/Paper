package io.papermc.paper.dialog;

import io.papermc.paper.dialog.body.PaperItemElement;
import io.papermc.paper.dialog.body.PaperPlainText;
import io.papermc.paper.dialog.types.PaperConfirmationDialog;
import io.papermc.paper.dialog.types.PaperDialogListDialog;
import io.papermc.paper.dialog.types.PaperMultiActionDialog;
import io.papermc.paper.dialog.types.PaperNoticeDialog;
import io.papermc.paper.dialog.types.PaperServerLinksDialog;
import net.minecraft.server.dialog.DialogListDialog;

public class DialogBridgeImpl implements DialogBridge {
    @Override
    public Dialog.Notice<?> noticeDialog() {
        return new PaperNoticeDialog();
    }

    @Override
    public Dialog.Confirmation<?> confirmation() {
        return new PaperConfirmationDialog();
    }

    @Override
    public Dialog.MultiAction<?> multiAction() {
        return new PaperMultiActionDialog();
    }

    @Override
    public Dialog.ServerLinks<?> serverLinks() {
        return new PaperServerLinksDialog();
    }

    @Override
    public Dialog.DialogList<?> dialogList() {
        return new PaperDialogListDialog();
    }

    @Override
    public BodyElement.PlainText plainText() {
        return new PaperPlainText();
    }

    @Override
    public BodyElement.Item itemElement() {
        return new PaperItemElement();
    }
}
