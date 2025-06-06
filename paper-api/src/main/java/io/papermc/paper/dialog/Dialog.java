package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface Dialog<D extends Dialog<D>> {
    void openFor(Player player);

    Component title();
    D title(Component component);

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
