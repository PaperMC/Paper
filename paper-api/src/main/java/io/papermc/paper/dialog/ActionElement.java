package io.papermc.paper.dialog;

import org.jetbrains.annotations.ApiStatus;
import java.net.URI;

@ApiStatus.Experimental
public interface ActionElement {
    static OpenURL openURL(URI url) {
        return DialogBridge.BRIDGE.openURL()
            .url(url);
    }

    static RunCommand runCommand(String command) {
        return DialogBridge.BRIDGE.runCommand()
            .command(command);
    }

    static SuggestCommand suggestCommand(String command) {
        return DialogBridge.BRIDGE.suggestCommand()
            .command(command);
    }

    static ChangePage changePage(int page) {
        return DialogBridge.BRIDGE.changePage()
            .page(page);
    }

    static CopyToClipboard copyToClipboard(String value) {
        return DialogBridge.BRIDGE.copyToClipboard()
            .value(value);
    }

    static ShowDialog showDialog(Dialog<?> dialog) {
        return DialogBridge.BRIDGE.showDialog()
            .dialog(dialog);
    }

    interface OpenURL extends ActionElement {
        URI url();
        OpenURL url(URI url);
    }

    interface RunCommand extends ActionElement {
        String command();
        RunCommand command(String command);
    }

    interface SuggestCommand extends ActionElement {
        String command();
        SuggestCommand command(String command);
    }

    interface ChangePage extends ActionElement {
        int page();
        ChangePage page(int page);
    }

    interface CopyToClipboard extends ActionElement {
        String value();
        CopyToClipboard value(String value);
    }

    interface ShowDialog extends ActionElement {
        Dialog<?> dialog();
        ShowDialog dialog(Dialog<?> dialog);
    }
}
