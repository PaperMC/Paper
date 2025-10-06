package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.SkinParts;
import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.Component;
import net.minecraft.Optionull;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Pose;
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
        return (net.minecraft.world.entity.decoration.Mannequin) this.entity;
    }

    @Override
    public void setPose(Pose pose, boolean fixed) {
        Preconditions.checkArgument(pose != null, "pose cannot be null");
        net.minecraft.world.entity.Pose internalPose = net.minecraft.world.entity.Pose.values()[pose.ordinal()];
        if (!net.minecraft.world.entity.decoration.Mannequin.VALID_POSES.contains(internalPose)) {
            throw new IllegalArgumentException("Invalid pose '%s', expected one of: %s".formatted(
                pose.name(),
                net.minecraft.world.entity.decoration.Mannequin.VALID_POSES.stream().map(p -> Pose.values()[p.ordinal()]).toList() // name doesn't match
            ));
        }

        this.setPose0(internalPose, fixed);
    }

    @Override
    public io.papermc.paper.datacomponent.item.ResolvableProfile getProfile() {
        return new PaperResolvableProfile(this.getHandle().getProfile());
    }

    @Override
    public void setProfile(final ResolvableProfile profile) {
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
        return Optionull.map(this.getHandle().getDescription(), PaperAdventure::asAdventure);
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
