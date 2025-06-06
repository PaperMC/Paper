package io.papermc.paper.dialog;

public class DialogBuilderBridgeImpl implements DialogBuilderBridge {
    @Override
    public Dialog.Notice noticeDialog() {
        return new PaperNoticeDialog();
    }
}
