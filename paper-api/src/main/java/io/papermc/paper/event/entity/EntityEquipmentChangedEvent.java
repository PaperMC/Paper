package io.papermc.paper.event.entity;

import java.util.Collections;
import java.util.Map;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/// Called whenever a change to an entity's equipment has been detected. This event is called after effects from
/// attribute modifiers and enchantments have been updated.
///
/// Examples of actions that can trigger this event:
///
///   - An entity being added to a world.
///   - A player logging in.
///   - The durability of an equipment item changing.
///   - A dispenser equipping an item onto an entity.
///   - An entity picking up an armor or weapon item from the ground.
///   - A player changing their equipped armor.
///   - A player changes their currently held item.
@NullMarked
public class EntityEquipmentChangedEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Map<EquipmentSlot, EquipmentChange> equipmentChanges;

    @ApiStatus.Internal
    public EntityEquipmentChangedEvent(final LivingEntity entity, final Map<EquipmentSlot, EquipmentChange> equipmentChanges) {
        super(entity);

        this.equipmentChanges = equipmentChanges;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /// Gets a map of changed slots to their respective equipment changes.
    ///
    /// @return the equipment changes map
    public @Unmodifiable Map<EquipmentSlot, EquipmentChange> getEquipmentChanges() {
        return Collections.unmodifiableMap(this.equipmentChanges);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /// Represents a change in equipment for a single equipment slot.
    @ApiStatus.NonExtendable
    public interface EquipmentChange {

        /// Gets the existing item that is being replaced.
        ///
        /// @return the existing item
        @Contract(pure = true, value = "-> new")
        ItemStack oldItem();

        /// Gets the new item that is replacing the existing item.
        ///
        /// @return the new item
        @Contract(pure = true, value = "-> new")
        ItemStack newItem();
    }
}
