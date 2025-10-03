package org.bukkit.craftbukkit;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.util.OldEnumHolderable;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.Art;

public class CraftArt extends OldEnumHolderable<Art, PaintingVariant> implements Art {

    private static int count = 0;

    public static Art minecraftToBukkit(PaintingVariant minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.PAINTING_VARIANT);
    }

    public static Art minecraftHolderToBukkit(Holder<PaintingVariant> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.PAINTING_VARIANT);
    }

    public static PaintingVariant bukkitToMinecraft(Art bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<PaintingVariant> bukkitToMinecraftHolder(Art bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftArt(Holder<PaintingVariant> paintingVariant) {
        super(paintingVariant, count++);
    }

    @Override
    public int getBlockWidth() {
        return this.getHandle().width();
    }

    @Override
    public int getBlockHeight() {
        return this.getHandle().height();
    }

    @Override
    public Component title() {
        return this.getHandle().title().map(PaperAdventure::asAdventure).orElse(null);
    }

    @Override
    public net.kyori.adventure.text.Component author() {
        return this.getHandle().author().map(PaperAdventure::asAdventure).orElse(null);
    }

    @Override
    public net.kyori.adventure.key.Key assetId() {
        return PaperAdventure.asAdventure(this.getHandle().assetId());
    }

    @Override
    public int getId() {
        return CraftRegistry.getMinecraftRegistry(Registries.PAINTING_VARIANT).getId(this.getHandle());
    }
}
