package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Fox;

public class CraftFox extends CraftAnimals implements Fox {

    public CraftFox(CraftServer server, net.minecraft.world.entity.animal.Fox entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Fox getHandle() {
        return (net.minecraft.world.entity.animal.Fox) this.entity;
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

    private AnimalTamer getTrustedPlayer(EntityDataAccessor<Optional<EntityReference<LivingEntity>>> entityDataKey) {
        return this.getHandle().getEntityData().get(entityDataKey)
            .map(EntityReference::getUUID)
            .map(uuid -> {
                AnimalTamer player = this.getServer().getPlayer(uuid);
                if (player == null) {
                    player = this.getServer().getOfflinePlayer(uuid);
                }
                return player;
            })
            .orElse(null);
    }

    @Override
    public AnimalTamer getFirstTrustedPlayer() {
        return this.getTrustedPlayer(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0);
    }

    @Override
    public void setFirstTrustedPlayer(AnimalTamer player) {
        if (player == null) {
            Preconditions.checkState(this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1).isEmpty(), "Must remove second trusted player first");
        }

        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0, player == null ? Optional.empty() : Optional.of(EntityReference.of(player.getUniqueId())));
    }

    @Override
    public AnimalTamer getSecondTrustedPlayer() {
        return this.getTrustedPlayer(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1);
    }

    @Override
    public void setSecondTrustedPlayer(AnimalTamer player) {
        if (player != null) {
            Preconditions.checkState(this.getHandle().getEntityData().get(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_0).isPresent(), "Must add first trusted player first");
        }

        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.Fox.DATA_TRUSTED_ID_1, player == null ? Optional.empty() : Optional.of(EntityReference.of(player.getUniqueId())));
    }

    @Override
    public boolean isFaceplanted() {
        return this.getHandle().isFaceplanted();
    }

    // Paper start - Add more fox behavior API
    @Override
    public void setInterested(boolean interested) {
        this.getHandle().setIsInterested(interested);
    }

    @Override
    public boolean isInterested() {
        return this.getHandle().isInterested();
    }

    @Override
    public void setLeaping(boolean leaping) {
        this.getHandle().setIsPouncing(leaping);
    }

    @Override
    public boolean isLeaping() {
        return this.getHandle().isPouncing();
    }

    @Override
    public void setDefending(boolean defending) {
        this.getHandle().setDefending(defending);
    }

    @Override
    public boolean isDefending() {
        return this.getHandle().isDefending();
    }

    @Override
    public void setFaceplanted(boolean faceplanted) {
        this.getHandle().setFaceplanted(faceplanted);
    }
    // Paper end - Add more fox behavior API
}
