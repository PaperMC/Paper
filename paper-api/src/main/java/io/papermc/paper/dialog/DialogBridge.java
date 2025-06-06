package io.papermc.paper.dialog;

import java.util.ServiceLoader;

public interface DialogBridge {
    DialogBridge BRIDGE = ServiceLoader.load(DialogBridge.class).findFirst().orElseThrow();

    Dialog.Notice<?> noticeDialog();
    Dialog.Confirmation<?> confirmation();
    Dialog.MultiAction<?> multiAction();
    Dialog.ServerLinks<?> serverLinks();
    Dialog.DialogList<?> dialogList();

    BodyElement.PlainText plainText();
    BodyElement.Item itemElement();

    InputElement.Text<?> text();
    InputElement.Checkbox<?> checkbox();
    InputElement.Option option();
    InputElement.SingleOption<?> singleOption();
    InputElement.NumberSlider<?> numberSlider();

    ButtonElement button();
    ActionElement.OpenURL openURL();
    ActionElement.RunCommand runCommand();
    ActionElement.SuggestCommand suggestCommand();
    ActionElement.ChangePage changePage();
    ActionElement.CopyToClipboard copyToClipboard();
    ActionElement.ShowDialog showDialog();
    ActionElement.Custom custom();
}
