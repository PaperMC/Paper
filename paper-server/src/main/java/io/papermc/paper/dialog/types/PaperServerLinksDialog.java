package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.Dialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.MultiActionDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Optional;

public class PaperServerLinksDialog extends PaperDialogBase<PaperServerLinksDialog> implements Dialog.ServerLinks<PaperServerLinksDialog> {
    @Override
    public void openFor(final Player player) {
        var dialog = new MultiActionDialog(
            this.commonDialogData(),
            List.of(),
            Optional.empty(),
            2
        );
        ((CraftPlayer) player).getHandle().openDialog(Holder.direct(dialog));
    }
}
