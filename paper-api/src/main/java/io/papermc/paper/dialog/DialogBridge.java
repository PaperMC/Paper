package io.papermc.paper.dialog;

import java.util.ServiceLoader;

public interface DialogBridge {
    DialogBridge BRIDGE = ServiceLoader.load(DialogBridge.class).findFirst().orElseThrow();

    Dialog.Notice noticeDialog();
    BodyElement.PlainText plainText();
    BodyElement.Item itemElement();
}
