package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.SkinParts;
import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.MainHand;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
        Preconditions.checkArgument(profile != null, "profile cannot be null");
        this.getHandle().setProfile(((PaperResolvableProfile) profile).getHandle());
    }

    @Override
    public SkinParts.Mutable getSkinParts() {
        return new PaperSkinParts.Mutable(this.getHandle().getEntityData().get(Avatar.DATA_PLAYER_MODE_CUSTOMISATION));
    }

    @Override
    public void setSkinParts(final SkinParts parts) {
        Preconditions.checkArgument(parts != null, "parts cannot be null");
        this.getHandle().getEntityData().set(Avatar.DATA_PLAYER_MODE_CUSTOMISATION, (byte) parts.getRaw());
    }

    @Override
    public boolean isImmovable() {
        return this.getHandle().getImmovable();
    }

    @Override
    public void setImmovable(final boolean immovable) {
        this.getHandle().setImmovable(immovable);
    }

    @Override
    public @Nullable Component getDescription() {
        final var mc = this.getHandle().getDescription();
        return mc == null ? null : PaperAdventure.asAdventure(mc);
    }

    @Override
    public void setDescription(final @Nullable Component description) {
        if (description == null) {
            this.getHandle().setHideDescription(true);
        } else {
            this.getHandle().setDescription(PaperAdventure.asVanilla(description));
            this.getHandle().setHideDescription(false);
        }
    }

    @Override
    public MainHand getMainHand() {
        return this.getHandle().getMainArm() == HumanoidArm.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    @Override
    public void setMainHand(final MainHand hand) {
        Preconditions.checkArgument(hand != null, "hand cannot be null");
        this.getHandle().setMainArm(hand == MainHand.LEFT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
    }
}
