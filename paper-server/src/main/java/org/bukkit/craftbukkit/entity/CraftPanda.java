package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Panda;

public class CraftPanda extends CraftAnimals implements Panda {

    public CraftPanda(CraftServer server, net.minecraft.world.entity.animal.Panda entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Panda getHandle() {
        return (net.minecraft.world.entity.animal.Panda) this.entity;
    }

    @Override
    public Gene getMainGene() {
        return CraftPanda.fromNms(this.getHandle().getMainGene());
    }

    @Override
    public void setMainGene(Gene gene) {
        this.getHandle().setMainGene(CraftPanda.toNms(gene));
    }

    @Override
    public Gene getHiddenGene() {
        return CraftPanda.fromNms(this.getHandle().getHiddenGene());
    }

    @Override
    public void setHiddenGene(Gene gene) {
        this.getHandle().setHiddenGene(CraftPanda.toNms(gene));
    }

    // Paper start - Panda API
    @Override
    public void setSneezeTicks(int ticks) {
        this.getHandle().setSneezeCounter(ticks);
    }

    @Override
    public int getSneezeTicks() {
        return this.getHandle().getSneezeCounter();
    }

    @Override
    public void setEatingTicks(int ticks) {
        this.getHandle().setEatCounter(ticks);
    }

    @Override
    public int getEatingTicks() {
        return this.getHandle().getEatCounter();
    }

    @Override
    public void setUnhappyTicks(int ticks) {
        this.getHandle().setUnhappyCounter(ticks);
    }

    @Override
    public Gene getCombinedGene() {
        return CraftPanda.fromNms(this.getHandle().getVariant());
    }
    // Paper end - Panda API

    @Override
    public boolean isRolling() {
        return this.getHandle().isRolling();
    }

    @Override
    public void setRolling(boolean flag) {
        this.getHandle().roll(flag);
    }

    @Override
    public boolean isSneezing() {
        return this.getHandle().isSneezing();
    }

    @Override
    public void setSneezing(boolean flag) {
        this.getHandle().sneeze(flag);
    }

    @Override
    public boolean isSitting() {
        return this.getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean flag) {
        this.getHandle().sit(flag);
    }

    @Override
    public boolean isOnBack() {
        return this.getHandle().isOnBack();
    }

    @Override
    public void setOnBack(boolean flag) {
        this.getHandle().setOnBack(flag);
    }

    @Override
    public boolean isEating() {
        return this.getHandle().isEating();
    }

    @Override
    public void setEating(boolean flag) {
        this.getHandle().eat(flag);
    }

    @Override
    public boolean isScared() {
        return this.getHandle().isScared();
    }

    @Override
    public int getUnhappyTicks() {
        return this.getHandle().getUnhappyCounter();
    }

    public static Gene fromNms(net.minecraft.world.entity.animal.Panda.Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return Gene.values()[gene.ordinal()];
    }

    public static net.minecraft.world.entity.animal.Panda.Gene toNms(Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return net.minecraft.world.entity.animal.Panda.Gene.values()[gene.ordinal()];
    }
}
