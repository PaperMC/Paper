package io.papermc.paper.dialog;

import java.util.ServiceLoader;

public interface DialogBuilderBridge {
    DialogBuilderBridge BRIDGE = ServiceLoader.load(DialogBuilderBridge.class).findFirst().orElseThrow();

    Dialog.Notice noticeDialog();
    BodyElement.PlainText plainText();
    BodyElement.Item itemElement();
}
