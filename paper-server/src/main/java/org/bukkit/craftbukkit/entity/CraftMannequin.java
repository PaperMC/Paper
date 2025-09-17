package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.SkinParts;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.minecraft.world.entity.Avatar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Mannequin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

@NullMarked
public class CraftMannequin extends CraftLivingEntity implements Mannequin {
    public CraftMannequin(final CraftServer server, final net.minecraft.world.entity.decoration.Mannequin entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.decoration.Mannequin getHandle() {
        return (net.minecraft.world.entity.decoration.Mannequin) super.getHandle();
    }

    @Override
    public io.papermc.paper.datacomponent.item.@Nullable ResolvableProfile getResolvableProfile() {
        return new PaperResolvableProfile(this.getHandle().getProfile());
    }

    @Override
    public void setResolvableProfile(final ResolvableProfile profile) {
        Objects.requireNonNull(profile, "profile");
        this.getHandle().setProfile(((PaperResolvableProfile) profile).getHandle());
    }

    @Override
    public SkinParts.Mutable getSkinParts() {
        return new PaperSkinParts.Mutable(this.getHandle().getEntityData().get(Avatar.DATA_PLAYER_MODE_CUSTOMISATION));
    }

    @Override
    public void setSkinParts(final SkinParts parts) {
        Objects.requireNonNull(parts, "parts");
        this.getHandle().getEntityData().set(Avatar.DATA_PLAYER_MODE_CUSTOMISATION, (byte) parts.getRaw());
    }
}
