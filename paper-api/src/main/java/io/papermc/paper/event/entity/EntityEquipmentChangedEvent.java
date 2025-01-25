package io.papermc.paper.event.entity;

import java.util.Collections;
import java.util.Map;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Called whenever a change to an entity's equipment has been detected. This event is called after effects from
 * attribute modifiers and enchantments have been updated.
 * <p>
 * Examples of actions that can trigger this event:
 * <ul>
 *     <li>An entity being added to a world.</li>
 *     <li>A player logging in.</li>
 *     <li>The durability of an equipment item changing.</li>
 *     <li>A dispenser equipping an item onto an entity.</li>
 *     <li>An entity picking up an armor or weapon item from the ground.</li>
 *     <li>A player changing their equipped armor.</li>
 *     <li>A player changes their currently held item.</li>
 * </ul>
 */
@NullMarked
public class EntityEquipmentChangedEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Map<EquipmentSlot, EquipmentChange> equipmentChanges;

    @ApiStatus.Internal
    public EntityEquipmentChangedEvent(final LivingEntity entity, final Map<EquipmentSlot, EquipmentChange> equipmentChanges) {
        super(entity);

        this.equipmentChanges = Collections.unmodifiableMap(equipmentChanges);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets a map of changed slots to their respective equipment changes.
     *
     * @return the equipment changes map
     */
    @Unmodifiable
    public Map<EquipmentSlot, EquipmentChange> getEquipmentChanges() {
        return equipmentChanges;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents a change in equipment for a single equipment slot.
     *
     * @param oldItem the existing item that is being replaced
     * @param newItem the new item that is replacing the existing item
     */
    public record EquipmentChange(ItemStack oldItem, ItemStack newItem) {

    }

}
