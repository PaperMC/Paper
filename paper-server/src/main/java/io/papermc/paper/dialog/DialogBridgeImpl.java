package io.papermc.paper.dialog;

import io.papermc.paper.dialog.body.PaperItemElement;
import io.papermc.paper.dialog.body.PaperPlainText;
import io.papermc.paper.dialog.inputs.PaperBooleanInput;
import io.papermc.paper.dialog.inputs.PaperNumberSliderInput;
import io.papermc.paper.dialog.inputs.PaperSingleOptionInput;
import io.papermc.paper.dialog.inputs.PaperTextInput;
import io.papermc.paper.dialog.types.PaperConfirmationDialog;
import io.papermc.paper.dialog.types.PaperDialogListDialog;
import io.papermc.paper.dialog.types.PaperMultiActionDialog;
import io.papermc.paper.dialog.types.PaperNoticeDialog;
import io.papermc.paper.dialog.types.PaperServerLinksDialog;
import net.kyori.adventure.text.Component;
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

    @Override
    public InputElement.Text<?> text() {
        return new PaperTextInput();
    }

    @Override
    public InputElement.Checkbox<?> checkbox() {
        return new PaperBooleanInput();
    }

    @Override
    public InputElement.Option option() {
        return new PaperSingleOptionInput.OptionButton("", Component.empty(), false);
    }

    @Override
    public InputElement.SingleOption<?> singleOption() {
        return new PaperSingleOptionInput();
    }

    @Override
    public InputElement.NumberSlider<?> numberSlider() {
        return new PaperNumberSliderInput();
    }
}
