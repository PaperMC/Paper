package io.papermc.paper.dialog.types;

import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface DialogElementConversion<D extends io.papermc.paper.dialog.Dialog<D>> extends io.papermc.paper.dialog.Dialog<D>  {
    Dialog getMcDialog();

    default void openFor(Player player) {
        ((CraftPlayer) player).getHandle().openDialog(Holder.direct(this.getMcDialog()));
    }
}
