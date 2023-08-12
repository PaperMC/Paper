package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.EntityPanda;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Panda.Gene;

public class CraftPanda extends CraftAnimals implements Panda {

    public CraftPanda(CraftServer server, EntityPanda entity) {
        super(server, entity);
    }

    @Override
    public EntityPanda getHandle() {
        return (EntityPanda) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPanda";
    }

    @Override
    public Gene getMainGene() {
        return fromNms(getHandle().getMainGene());
    }

    @Override
    public void setMainGene(Gene gene) {
        getHandle().setMainGene(toNms(gene));
    }

    @Override
    public Gene getHiddenGene() {
        return fromNms(getHandle().getHiddenGene());
    }

    @Override
    public void setHiddenGene(Gene gene) {
        getHandle().setHiddenGene(toNms(gene));
    }

    @Override
    public boolean isRolling() {
        return getHandle().isRolling();
    }

    @Override
    public void setRolling(boolean flag) {
        getHandle().roll(flag);
    }

    @Override
    public boolean isSneezing() {
        return getHandle().isSneezing();
    }

    @Override
    public void setSneezing(boolean flag) {
        getHandle().sneeze(flag);
    }

    @Override
    public boolean isSitting() {
        return getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean flag) {
        getHandle().sit(flag);
    }

    @Override
    public boolean isOnBack() {
        return getHandle().isOnBack();
    }

    @Override
    public void setOnBack(boolean flag) {
        getHandle().setOnBack(flag);
    }

    @Override
    public boolean isEating() {
        return getHandle().isEating();
    }

    @Override
    public void setEating(boolean flag) {
        getHandle().eat(flag);
    }

    @Override
    public boolean isScared() {
        return getHandle().isScared();
    }

    @Override
    public int getUnhappyTicks() {
        return getHandle().getUnhappyCounter();
    }

    public static Gene fromNms(EntityPanda.Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return Gene.values()[gene.ordinal()];
    }

    public static EntityPanda.Gene toNms(Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return EntityPanda.Gene.values()[gene.ordinal()];
    }
}
