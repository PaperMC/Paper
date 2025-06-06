package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;

@ApiStatus.Experimental
public interface Dialog<D extends Dialog<D>> {
    void openFor(Player player);

    Component title();
    D title(Component component);

    Component externalTitle();
    D externalTitle(Component component);

    List<BodyElement> bodyElements();
    D bodyElements(List<BodyElement> elements);
    default D bodyElements(BodyElement... elements) {
        return this.bodyElements(List.of(elements));
    }

    List<? extends InputElement<?>> inputElements();
    D inputElements(List<InputElement<?>> elements);
    default D inputElements(InputElement<?>... elements) {
        return this.inputElements(List.of(elements));
    }

    boolean canCloseWithEscape();
    D canCloseWithEscape(boolean flag);

    // note: `pause` flag is irrelevant since Paper runs on multi-player
    // remove this comment later

    static Dialog.Notice<?> notice() {
        return DialogBridge.BRIDGE.noticeDialog();
    }

    static Dialog.Confirmation<?> confirmation() {
        return DialogBridge.BRIDGE.confirmation();
    }

    static Dialog.MultiAction<?> multiAction() {
        return DialogBridge.BRIDGE.multiAction();
    }

    static Dialog.ServerLinks<?> serverLinks() {
        return DialogBridge.BRIDGE.serverLinks();
    }

    static Dialog.DialogList<?> dialogList() {
        return DialogBridge.BRIDGE.dialogList();
    }

    interface Notice<B extends Notice<B>> extends Dialog<B> {
        ButtonElement button();
        B button(ButtonElement button);
    }

    interface Confirmation<B extends Confirmation<B>> extends Dialog<B> {
        ButtonElement yes();
        B yes(ButtonElement button);

        ButtonElement no();
        B no(ButtonElement button);
    }

    interface MultiAction<B extends MultiAction<B>> extends Dialog<B> {
        List<ButtonElement> buttons();
        B buttons(List<ButtonElement> buttonElements);

        int columns();
        B columns(int columns);

        ButtonElement exitButton();
        B exitButton(ButtonElement buttonElement);
    }

    interface ServerLinks<B extends ServerLinks<B>> extends Dialog<B> {
        ButtonElement exitAction();
        B exitAction(ButtonElement buttonElement);

        int columns();
        B columns(int columns);

        int buttonWidth();
        B buttonWidth(int width);
    }

    interface DialogList<B extends DialogList<B>> extends Dialog<B> {
        List<Dialog<?>> dialogs();
        B dialogs(List<Dialog<?>> dialogs);

        ButtonElement exitAction();
        B exitAction(ButtonElement buttonElement);

        int columns();
        B columns(int columns);

        int buttonWidth();
        B buttonWidth(int width);
    }
}
