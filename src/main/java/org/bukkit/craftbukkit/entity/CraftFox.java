package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityFox;
import org.bukkit.craftbukkit.CraftServer;
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
        return Type.values()[getHandle().dV().ordinal()];
    }

    @Override
    public void setFoxType(Type type) {
        Preconditions.checkArgument(type != null, "type");

        getHandle().a(EntityFox.Type.values()[type.ordinal()]);
    }

    @Override
    public boolean isCrouching() {
        return getHandle().ef();
    }

    @Override
    public void setCrouching(boolean crouching) {
        getHandle().t(crouching);
    }

    @Override
    public boolean isSitting() {
        return getHandle().dW();
    }

    @Override
    public void setSitting(boolean sitting) {
        getHandle().r(sitting);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        getHandle().x(sleeping);
    }
}
