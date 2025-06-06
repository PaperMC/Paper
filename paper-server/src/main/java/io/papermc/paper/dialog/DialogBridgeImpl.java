package io.papermc.paper.dialog;

import io.papermc.paper.dialog.actions.PaperButtonElement;
import io.papermc.paper.dialog.actions.PaperChangePageAction;
import io.papermc.paper.dialog.actions.PaperCopyToClipboardAction;
import io.papermc.paper.dialog.actions.PaperCustomAction;
import io.papermc.paper.dialog.actions.PaperDynamicCustomAction;
import io.papermc.paper.dialog.actions.PaperInputData;
import io.papermc.paper.dialog.actions.PaperShowDialogAction;
import io.papermc.paper.dialog.actions.PaperOpenURLAction;
import io.papermc.paper.dialog.actions.PaperRunCommandAction;
import io.papermc.paper.dialog.actions.PaperSuggestCommandAction;
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
import net.minecraft.nbt.CompoundTag;

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

    @Override
    public ButtonElement button() {
        return new PaperButtonElement();
    }

    @Override
    public ActionElement.OpenURL openURL() {
        return new PaperOpenURLAction();
    }

    @Override
    public ActionElement.RunCommand runCommand() {
        return new PaperRunCommandAction();
    }

    @Override
    public ActionElement.SuggestCommand suggestCommand() {
        return new PaperSuggestCommandAction();
    }

    @Override
    public ActionElement.ChangePage changePage() {
        return new PaperChangePageAction();
    }

    @Override
    public ActionElement.CopyToClipboard copyToClipboard() {
        return new PaperCopyToClipboardAction();
    }

    @Override
    public ActionElement.ShowDialog showDialog() {
        return new PaperShowDialogAction();
    }

    @Override
    public ActionElement.Custom custom() {
        return new PaperCustomAction();
    }

    @Override
    public ActionElement.DynamicCustom dynamicCustom() {
        return new PaperDynamicCustomAction();
    }

    @Override
    public InputData inputData() {
        return new PaperInputData(new CompoundTag());
    }
}
