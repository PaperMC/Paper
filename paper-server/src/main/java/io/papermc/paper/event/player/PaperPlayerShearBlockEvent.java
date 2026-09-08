package io.papermc.paper.event.player;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerShearBlockEvent extends CraftPlayerEvent implements PlayerShearBlockEvent {

    private final Block block;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private final List<ItemStack> drops;

    private boolean cancelled;

    public PaperPlayerShearBlockEvent(final Player player, final Block block, final ItemStack item, final EquipmentSlot hand, final List<ItemStack> drops) {
        super(player);
        this.block = block;
        this.item = item;
        this.hand = hand;
        this.drops = drops;
    }

    public PaperPlayerShearBlockEvent(
        final net.minecraft.world.entity.player.Player player,
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack item,
        final InteractionHand hand,
        final List<net.minecraft.world.item.ItemStack> drops
    ) {
        this(
            (Player) player.getBukkitEntity(),
            CraftBlock.at(level, pos),
            CraftItemStack.asCraftMirror(item),
            CraftEquipmentSlot.getHand(hand),
            MCUtil.mutableTransform(drops, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy)
        );
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public List<ItemStack> getDrops() {
        return this.drops;
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
        return PlayerShearBlockEvent.getHandlerList();
    }
}
