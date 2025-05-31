package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.animal.Animal;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Animals;
import org.bukkit.inventory.ItemStack;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, Animal entity) {
        super(server, entity);
    }

    @Override
    public Animal getHandle() {
        return (Animal) this.entity;
    }

    @Override
    public UUID getBreedCause() {
        EntityReference<ServerPlayer> reference = this.getHandle().loveCause;
        if (reference == null) {
            return null;
        }

        return reference.getUUID();
    }

    @Override
    public void setBreedCause(UUID uuid) {
        this.getHandle().loveCause = new EntityReference<>(uuid);
    }

    @Override
    public boolean isLoveMode() {
        return this.getHandle().isInLove();
    }

    @Override
    public void setLoveModeTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Love mode ticks must be positive or 0");
        this.getHandle().setInLoveTime(ticks);
    }

    @Override
    public int getLoveModeTicks() {
        return this.getHandle().inLove;
    }

    @Override
    public boolean isBreedItem(ItemStack itemStack) {
        return this.getHandle().isFood(CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public boolean isBreedItem(Material material) {
        return this.isBreedItem(new ItemStack(material));
    }
}
