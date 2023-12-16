package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.EntityRabbit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, EntityRabbit entity) {
        super(server, entity);
    }

    @Override
    public EntityRabbit getHandle() {
        return (EntityRabbit) entity;
    }

    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + getRabbitType() + "}";
    }

    @Override
    public Type getRabbitType() {
        return Type.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setRabbitType(Type type) {
        getHandle().setVariant(EntityRabbit.Variant.values()[type.ordinal()]);
    }
}
