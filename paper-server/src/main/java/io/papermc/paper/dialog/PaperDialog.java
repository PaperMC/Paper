package io.papermc.paper.dialog;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;

public final class PaperDialog extends HolderableBase<net.minecraft.server.dialog.Dialog> implements Dialog {

    public static net.minecraft.server.dialog.Dialog bukkitToMinecraft(final Dialog bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.server.dialog.Dialog> bukkitToMinecraftHolder(final Dialog bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static Dialog minecraftToBukkit(final net.minecraft.server.dialog.Dialog minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.DIALOG);
    }

    public static Dialog minecraftHolderToBukkit(final Holder<net.minecraft.server.dialog.Dialog> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.DIALOG);
    }

    public PaperDialog(final Holder<net.minecraft.server.dialog.Dialog> holder) {
        super(holder);
    }
}
