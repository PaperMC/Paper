package org.bukkit.craftbukkit.event.inventory;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.Positive;

public class CraftEnchantItemEvent extends CraftInventoryEvent implements EnchantItemEvent {

    private final Player enchanter;
    private final Block table;
    private ItemStack item;
    private int level;
    private final Map<Enchantment, Integer> enchants;
    private final Enchantment enchantmentHint;
    private final int levelHint;
    private final int button;

    private boolean cancelled;

    public CraftEnchantItemEvent(final Player enchanter, final InventoryView view, final Block table, final ItemStack item, final int level, final Map<Enchantment, Integer> enchants, final Enchantment enchantmentHint, final int levelHint, final int button) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap<>(enchants);
        this.enchantmentHint = enchantmentHint;
        this.levelHint = levelHint;
        this.button = button;
    }

    @Override
    public Player getEnchanter() {
        return this.enchanter;
    }

    @Override
    public Block getEnchantBlock() {
        return this.table;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void setItem(final ItemStack item) {
        this.item = item;
    }

    @Override
    public int getExpLevelCost() {
        return this.level;
    }

    @Override
    public void setExpLevelCost(final @Positive int level) {
        Preconditions.checkArgument(level > 0, "The cost must be greater than 0!");

        this.level = level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantsToAdd() {
        return this.enchants;
    }

    @Override
    public Enchantment getEnchantmentHint() {
        return this.enchantmentHint;
    }

    @Override
    public int getLevelHint() {
        return this.levelHint;
    }

    @Override
    public int whichButton() {
        return this.button;
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
        return EnchantItemEvent.getHandlerList();
    }
}
