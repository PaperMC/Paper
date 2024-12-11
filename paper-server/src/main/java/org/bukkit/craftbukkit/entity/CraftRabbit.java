package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, net.minecraft.world.entity.animal.Rabbit entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Rabbit getHandle() {
        return (net.minecraft.world.entity.animal.Rabbit) this.entity;
    }

    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + this.getRabbitType() + "}";
    }

    @Override
    public Type getRabbitType() {
        return Type.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setRabbitType(Type type) {
        this.getHandle().setVariant(net.minecraft.world.entity.animal.Rabbit.Variant.values()[type.ordinal()]);
    }
}
