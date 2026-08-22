package org.bukkit.craftbukkit.event.entity;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPlayerDeathEvent extends CraftEntityDeathEvent implements PlayerDeathEvent {

    private int newExp = 0;
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean showDeathMessages;
    private Component deathMessage;
    private Component deathScreenMessageOverride = null;
    private boolean doExpDrop;
    private boolean keepLevel = false;
    private boolean keepInventory = false;
    @Deprecated
    private final List<ItemStack> itemsToKeep = new ArrayList<>();

    // todo collapse
    public CraftPlayerDeathEvent(final Player player, final DamageSource damageSource, final List<ItemStack> drops, final int droppedExp, final int newExp, final @Nullable Component deathMessage, final boolean showDeathMessages) {
        this(player, damageSource, drops, droppedExp, newExp, 0, 0, deathMessage, showDeathMessages);
    }

    public CraftPlayerDeathEvent(final Player player, final DamageSource damageSource, final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final @Nullable Component deathMessage, final boolean showDeathMessages) {
        this(player, damageSource, drops, droppedExp, newExp, newTotalExp, newLevel, deathMessage, showDeathMessages, true);
    }

    public CraftPlayerDeathEvent(final Player player, final DamageSource damageSource, final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final @Nullable Component deathMessage, final boolean showDeathMessages, final boolean doExpDrop) {
        super(player, damageSource, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
        this.showDeathMessages = showDeathMessages;
        this.doExpDrop = doExpDrop;
    }

    @Override
    public Player getEntity() {
        return (Player) this.entity;
    }

    @Override
    public boolean getShowDeathMessages() {
        return showDeathMessages;
    }

    @Override
    public void setShowDeathMessages(final boolean displayDeathMessage) {
        this.showDeathMessages = displayDeathMessage;
    }

    @Override
    public int getNewExp() {
        return this.newExp;
    }

    @Override
    public void setNewExp(final int exp) {
        this.newExp = exp;
    }

    @Override
    public int getNewLevel() {
        return this.newLevel;
    }

    @Override
    public void setNewLevel(final int level) {
        this.newLevel = level;
    }

    @Override
    public int getNewTotalExp() {
        return this.newTotalExp;
    }

    @Override
    public void setNewTotalExp(final int totalExp) {
        this.newTotalExp = totalExp;
    }

    @Override
    public @Nullable Component deathMessage() {
        return this.deathMessage;
    }

    @Override
    public void deathMessage(final @Nullable Component deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    @Deprecated
    public @Nullable String getDeathMessage() {
        return LegacyComponentSerializer.legacySection().serializeOrNull(this.deathMessage);
    }

    @Override
    @Deprecated
    public void setDeathMessage(final @Nullable String deathMessage) {
        this.deathMessage = LegacyComponentSerializer.legacySection().deserializeOrNull(deathMessage);
    }

    @Override
    public @Nullable Component deathScreenMessageOverride() {
        return this.deathScreenMessageOverride;
    }

   @Override
   public void deathScreenMessageOverride(final @Nullable Component deathScreenMessageOverride) {
        this.deathScreenMessageOverride = deathScreenMessageOverride;
    }

    @Override
    public boolean shouldDropExperience() {
        return this.doExpDrop;
    }

    @Override
    public void setShouldDropExperience(final boolean doExpDrop) {
        this.doExpDrop = doExpDrop;
    }

    @Override
    public boolean getKeepLevel() {
        return this.keepLevel;
    }

    @Override
    public void setKeepLevel(final boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    @Override
    public boolean getKeepInventory() {
        return this.keepInventory;
    }

    @Override
    public void setKeepInventory(final boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    @Override
    public List<ItemStack> getItemsToKeep() {
        return this.itemsToKeep;
    }
}
