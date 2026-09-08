package org.bukkit.craftbukkit.event.inventory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.Positive;

import static io.papermc.paper.util.BoundChecker.requirePositive;

public class CraftEnchantItemEvent extends CraftInventoryEvent implements EnchantItemEvent {

    private final Player enchanter;
    private final Block table;
    private ItemStack item;
    private int cost;
    private final Map<Enchantment, Integer> enchants;
    private final Enchantment enchantmentHint;
    private final int levelHint;
    private final int button;

    private boolean cancelled;

    public CraftEnchantItemEvent(
        final Player enchanter,
        final InventoryView view,
        final Block table,
        final ItemStack item,
        final int cost,
        final Map<Enchantment, Integer> enchants,
        final Enchantment enchantmentHint,
        final int levelHint,
        final int button
    ) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.cost = cost;
        this.enchants = enchants;
        this.enchantmentHint = enchantmentHint;
        this.levelHint = levelHint;
        this.button = button;
    }

    public CraftEnchantItemEvent(
        final net.minecraft.world.entity.player.Player enchanter,
        final EnchantmentMenu menu,
        final ContainerLevelAccess access,
        final net.minecraft.world.item.ItemStack item,
        final int cost,
        final List<EnchantmentInstance> enchants,
        final Holder<net.minecraft.world.item.enchantment.Enchantment> enchantmentHint,
        final int levelHint,
        final int button
    ) {
        this(
            (Player) enchanter.getBukkitEntity(),
            menu.getBukkitView(),
            access.getLocation().getBlock(),
            CraftItemStack.asCraftMirror(item),
            cost,
            enchants.stream().collect(Collectors.toMap(
                enchant -> CraftEnchantment.minecraftHolderToBukkit(enchant.enchantment()),
                EnchantmentInstance::level
            )),
            CraftEnchantment.minecraftHolderToBukkit(enchantmentHint),
            levelHint,
            button
        );
    }

    @Override
    public Player getPlayer() {
        return this.enchanter;
    }

    @Override
    public Block getBlock() {
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
    public @Positive int getExpLevelCost() {
        return this.cost;
    }

    @Override
    public void setExpLevelCost(final @Positive int level) {
        this.cost = requirePositive(level, "level");
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
