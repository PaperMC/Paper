package io.papermc.paper.event.entity;

import java.util.Collections;
import java.util.Map;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Unmodifiable;

public class PaperEntityEquipmentChangedEvent extends CraftEntityEvent implements EntityEquipmentChangedEvent {

    private final Map<EquipmentSlot, EquipmentChange> equipmentChanges;

    public PaperEntityEquipmentChangedEvent(final LivingEntity entity, final Map<EquipmentSlot, EquipmentChange> equipmentChanges) {
        super(entity);
        this.equipmentChanges = Collections.unmodifiableMap(equipmentChanges);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public @Unmodifiable Map<EquipmentSlot, EquipmentChange> getEquipmentChanges() {
        return this.equipmentChanges;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityEquipmentChangedEvent.getHandlerList();
    }
}
