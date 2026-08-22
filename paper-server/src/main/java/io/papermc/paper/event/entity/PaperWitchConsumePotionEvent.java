package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.WitchConsumePotionEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Witch;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperWitchConsumePotionEvent extends CraftEntityEvent implements WitchConsumePotionEvent {

    private @Nullable ItemStack potion;
    private boolean cancelled;

    public PaperWitchConsumePotionEvent(final Witch witch, final @Nullable ItemStack potion) {
        super(witch);
        this.potion = potion;
    }

    @Override
    public Witch getEntity() {
        return (Witch) this.entity;
    }

    @Override
    public @Nullable ItemStack getPotion() {
        return this.potion;
    }

    @Override
    public void setPotion(final @Nullable ItemStack potion) {
        this.potion = potion != null ? potion.clone() : null;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled || this.potion == null;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return WitchConsumePotionEvent.getHandlerList();
    }
}
