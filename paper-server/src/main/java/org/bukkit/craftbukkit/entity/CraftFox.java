package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Fox.Type;

public class CraftFox extends CraftAnimals implements Fox {

    public CraftFox(CraftServer server, net.minecraft.world.entity.animal.Fox entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Fox getHandle() {
        return (net.minecraft.world.entity.animal.Fox) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftFox";
    }

    @Override
    public Type getFoxType() {
        return Type.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setFoxType(Type type) {
        Preconditions.checkArgument(type != null, "type");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.Fox.Variant.values()[type.ordinal()]);
    }

    @Override
    public boolean isCrouching() {
        return this.getHandle().isCrouching();
    }

    @Override
    public void setCrouching(boolean crouching) {
        this.getHandle().setIsCrouching(crouching);
    }

    @Override
    public boolean isSitting() {
        return this.getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean sitting) {
        this.getHandle().setSitting(sitting);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.getHandle().setSleeping(sleeping);
    }

    @Override
    public AnimalTamer getFirstTrustedPlayer() {
        UUID uuid = this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = this.getServer().getPlayer(uuid);
        if (player == null) {
            player = this.getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setFirstTrustedPlayer(AnimalTamer player) {
        if (player == null) {
            Preconditions.checkState(this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1).isEmpty(), "Must remove second trusted player first");
        }

        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }

    @Override
    public AnimalTamer getSecondTrustedPlayer() {
        UUID uuid = this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = this.getServer().getPlayer(uuid);
        if (player == null) {
            player = this.getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setSecondTrustedPlayer(AnimalTamer player) {
        if (player != null) {
            Preconditions.checkState(this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0).isPresent(), "Must add first trusted player first");
        }

        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }

    @Override
    public boolean isFaceplanted() {
        return this.getHandle().isFaceplanted();
    }
}
