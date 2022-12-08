package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Camel;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCamel extends CraftAbstractHorse implements Camel {

    public CraftCamel(CraftServer server, net.minecraft.world.entity.animal.camel.Camel entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.camel.Camel getHandle() {
        return (net.minecraft.world.entity.animal.camel.Camel) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCamel";
    }

    @Override
    public EntityType getType() {
        return EntityType.CAMEL;
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.CAMEL;
    }

    @Override
    public boolean isDashing() {
        return getHandle().isDashing();
    }

    @Override
    public void setDashing(boolean dashing) {
        getHandle().setDashing(dashing);
    }

    @Override
    public boolean isSitting() {
        return getHandle().isPoseSitting();
    }

    @Override
    public void setSitting(boolean sitting) {
        if (sitting) {
            getHandle().sitDown();
        } else {
            getHandle().standUp();
        }
    }
}
