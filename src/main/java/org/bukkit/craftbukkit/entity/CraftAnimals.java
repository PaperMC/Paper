package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import com.google.common.base.Preconditions;
import net.minecraft.server.EntityAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, EntityAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityAnimal getHandle() {
        return (EntityAnimal) entity;
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }

    @Override
    public UUID getBreedCause() {
        return getHandle().breedCause;
    }

    @Override
    public void setBreedCause(UUID uuid) {
        getHandle().breedCause = uuid;
    }

    @Override
    public boolean isLoveMode() {
        return getHandle().isInLove();
    }

    @Override
    public void setLoveModeTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Love mode ticks must be positive or 0");
        getHandle().d(ticks); // PAIL rename setLoveModeTicks
    }

    @Override
    public int getLoveModeTicks() {
        return getHandle().bC; // PAIL rename loveTicks
    }
}
