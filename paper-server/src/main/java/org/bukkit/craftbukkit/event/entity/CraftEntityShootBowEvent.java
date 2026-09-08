package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftEntityShootBowEvent extends CraftEntityEvent implements EntityShootBowEvent {

    private final ItemStack bow;
    private final @Nullable ItemStack consumable;
    private Entity projectile;
    private final EquipmentSlot hand;
    private final float force;
    private boolean consumeItem;

    private boolean cancelled;

    public CraftEntityShootBowEvent(final LivingEntity shooter, final ItemStack bow, final @Nullable ItemStack consumable, final Entity projectile, final EquipmentSlot hand, final float force, final boolean consumeItem) {
        super(shooter);
        this.bow = bow;
        this.consumable = consumable;
        this.projectile = projectile;
        this.hand = hand;
        this.force = force;
        this.consumeItem = consumeItem;
    }

    public CraftEntityShootBowEvent(
        final net.minecraft.world.entity.LivingEntity shooter,
        final net.minecraft.world.item.ItemStack bow,
        final net.minecraft.world.item.@Nullable ItemStack consumable,
        final Projectile projectile,
        final InteractionHand hand,
        final float force,
        final boolean consumeItem
    ) {
        this(
            shooter.getBukkitEntity(),
            CraftItemStack.asCraftMirror(bow),
            Optionull.map(consumable, CraftItemStack::asCraftMirror),
            projectile.getBukkitEntity(),
            CraftEquipmentSlot.getHand(hand),
            force,
            consumeItem
        );
    }


    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public ItemStack getBow() {
        return this.bow;
    }

    @Override
    public @Nullable ItemStack getConsumable() {
        return this.consumable;
    }

    @Override
    public Entity getProjectile() {
        return this.projectile;
    }

    @Override
    public void setProjectile(final Entity projectile) {
        this.projectile = projectile;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public float getForce() {
        return this.force;
    }

    @Override
    public boolean shouldConsumeItem() {
        return this.consumeItem;
    }

    @Override
    @Deprecated(since = "1.20.5")
    public void setConsumeItem(final boolean consumeItem) {
        this.consumeItem = consumeItem;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityShootBowEvent.getHandlerList();
    }
}
