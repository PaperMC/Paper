package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CowVariant;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftCow extends CraftAbstractCow implements Cow {

    public CraftCow(CraftServer server, net.minecraft.world.entity.animal.Cow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cow getHandle() {
        return (net.minecraft.world.entity.animal.Cow) this.entity;
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant extends HolderableBase<CowVariant> implements Variant {

        public static Variant minecraftToBukkit(CowVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.COW_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<CowVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.COW_VARIANT);
        }

        public static CowVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<CowVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.COW_VARIANT);
        }

        public CraftVariant(final Holder<CowVariant> holder) {
            super(holder);
        }

        @NotNull
        public net.kyori.adventure.key.Key assetId() {
            return PaperAdventure.asAdventure(this.getHandle().modelAndTexture().asset().id());
        }

        @Override
        public @Nullable Cow.Variant.Model getModel() {
            return fromNms(this.getHandle().modelAndTexture().model());
        }

        public static Cow.Variant.Model fromNms(CowVariant.ModelType modelType) {
            Preconditions.checkArgument(modelType != null, "Model Type may not be null");

            return Cow.Variant.Model.values()[modelType.ordinal()];
        }

        public static CowVariant.ModelType toNms(Cow.Variant.Model model) {
            Preconditions.checkArgument(model != null, "Model may not be null");

            return CowVariant.ModelType.values()[model.ordinal()];
        }
    }
}
