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

    // TODO: inputs

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

    }

    interface Confirmation<B extends Confirmation<B>> extends Dialog<B> {

    }

    interface MultiAction<B extends MultiAction<B>> extends Dialog<B> {

    }

    interface ServerLinks<B extends ServerLinks<B>> extends Dialog<B> {

    }

    interface DialogList<B extends DialogList<B>> extends Dialog<B> {

    }
}
