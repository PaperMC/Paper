package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.EntityFox;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;

public class CraftFox extends CraftAnimals implements Fox {

    public CraftFox(CraftServer server, EntityFox entity) {
        super(server, entity);
    }

    @Override
    public EntityFox getHandle() {
        return (EntityFox) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.FOX;
    }

    @Override
    public String toString() {
        return "CraftFox";
    }

    @Override
    public Type getFoxType() {
        return Type.values()[getHandle().getFoxType().ordinal()];
    }

    @Override
    public void setFoxType(Type type) {
        Preconditions.checkArgument(type != null, "type");

        getHandle().setFoxType(EntityFox.Type.values()[type.ordinal()]);
    }

    @Override
    public boolean isCrouching() {
        return getHandle().isCrouching();
    }

    @Override
    public void setCrouching(boolean crouching) {
        getHandle().setCrouching(crouching);
    }

    @Override
    public boolean isSitting() {
        return getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean sitting) {
        getHandle().setSitting(sitting);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        getHandle().setSleeping(sleeping);
    }

    @Override
    public AnimalTamer getFirstTrustedPlayer() {
        UUID uuid = getHandle().getDataWatcher().get(EntityFox.by).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = getServer().getPlayer(uuid);
        if (player == null) {
            player = getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setFirstTrustedPlayer(AnimalTamer player) {
        if (player == null && getHandle().getDataWatcher().get(EntityFox.bz).isPresent()) {
            throw new IllegalStateException("Must remove second trusted player first");
        }

        getHandle().getDataWatcher().set(EntityFox.by, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }

    @Override
    public AnimalTamer getSecondTrustedPlayer() {
        UUID uuid = getHandle().getDataWatcher().get(EntityFox.bz).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = getServer().getPlayer(uuid);
        if (player == null) {
            player = getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setSecondTrustedPlayer(AnimalTamer player) {
        if (player != null && !getHandle().getDataWatcher().get(EntityFox.by).isPresent()) {
            throw new IllegalStateException("Must add first trusted player first");
        }

        getHandle().getDataWatcher().set(EntityFox.bz, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }
}
