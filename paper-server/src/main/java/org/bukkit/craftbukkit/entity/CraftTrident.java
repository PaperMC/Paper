package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Trident;

public class CraftTrident extends CraftAbstractArrow implements Trident {

    public CraftTrident(CraftServer server, ThrownTrident entity) {
        super(server, entity);
    }

    @Override
    public ThrownTrident getHandle() {
        return (ThrownTrident) this.entity;
    }

    @Override
    public boolean hasGlint() {
        return this.getHandle().isFoil();
    }

    @Override
    public void setGlint(boolean glint) {
        this.getHandle().setFoil(glint);
    }

    @Override
    public int getLoyaltyLevel() {
        return this.getHandle().getLoyalty();
    }

    @Override
    public void setLoyaltyLevel(int loyaltyLevel) {
        com.google.common.base.Preconditions.checkArgument(loyaltyLevel >= 0 && loyaltyLevel <= 127, "The loyalty level has to be between 0 and 127");
        this.getHandle().setLoyalty((byte) loyaltyLevel);
    }

    @Override
    public boolean hasDealtDamage() {
        return this.getHandle().dealtDamage;
    }

    @Override
    public void setHasDealtDamage(boolean hasDealtDamage) {
        this.getHandle().dealtDamage = hasDealtDamage;
    }
}
