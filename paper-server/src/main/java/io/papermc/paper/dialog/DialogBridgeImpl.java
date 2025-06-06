package io.papermc.paper.dialog;

import io.papermc.paper.dialog.body.PaperItemElement;
import io.papermc.paper.dialog.body.PaperPlainText;
import io.papermc.paper.dialog.types.PaperNoticeDialog;

public class DialogBridgeImpl implements DialogBridge {
    @Override
    public Dialog.Notice noticeDialog() {
        return new PaperNoticeDialog();
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
