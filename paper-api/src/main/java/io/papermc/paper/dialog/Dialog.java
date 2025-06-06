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

    List<BodyElement> bodyElements();
    D bodyElements(List<BodyElement> elements);
    default D bodyElements(BodyElement... elements) {
        return this.bodyElements(List.of(elements));
    }

    static Dialog.Notice notice() {
        return DialogBridge.BRIDGE.noticeDialog();
    }

    interface Notice extends Dialog<Notice> {

    }

    interface Confirmation extends Dialog<Confirmation> {

    }

    interface MultiAction extends Dialog<MultiAction> {

    }

    interface ServerLinks extends Dialog<ServerLinks> {

    }

    interface DialogList extends Dialog<DialogList> {

    }
}
