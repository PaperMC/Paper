package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import org.bukkit.craftbukkit.CraftInventoryArmoredSaddledMount;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.inventory.ArmoredSaddledMountInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftAbstractNautilus extends CraftTameableAnimal implements org.bukkit.entity.AbstractNautilus {
    public CraftAbstractNautilus(final CraftServer server, final AbstractNautilus entity) {
        super(server, entity);
    }

    @Override
    public AbstractNautilus getHandle() {
        return (AbstractNautilus) this.entity;
    }

    @Override
    public ArmoredSaddledMountInventory getInventory() {
        return new CraftInventoryArmoredSaddledMount(
            this.getHandle().inventory,
            this.getHandle().createEquipmentSlotContainer(EquipmentSlot.BODY),
            this.getHandle().createEquipmentSlotContainer(EquipmentSlot.SADDLE)
        );
    }
}
