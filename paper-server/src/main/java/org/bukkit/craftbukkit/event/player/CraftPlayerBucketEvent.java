package org.bukkit.craftbukkit.event.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jspecify.annotations.Nullable;

public abstract class CraftPlayerBucketEvent extends CraftPlayerEvent implements PlayerBucketEvent {

    private final Block block;
    private final Block blockClicked;
    private final BlockFace blockFace;
    private final Material bucket;
    private final EquipmentSlot hand;
    private @Nullable ItemStack itemStack;

    private boolean cancelled;

    protected CraftPlayerBucketEvent(final Player player, final Block block, final Block blockClicked, final BlockFace blockFace, final Material bucket, final ItemStack itemInHand, final EquipmentSlot hand) {
        super(player);
        this.block = block;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.itemStack = itemInHand;
        if (bucket != null && bucket.isLegacy()) {
            this.bucket = Bukkit.getUnsafe().fromLegacy(new MaterialData(bucket), true);
        } else {
            this.bucket = bucket;
        }
        this.hand = hand;
    }

    @FunctionalInterface
    public interface Factory<EVENT extends PlayerBucketEvent> {
        EVENT create(Player player, Block block, Block clickedBlock, BlockFace clickedFace, Material bucket, ItemStack itemInHand, EquipmentSlot hand);
    }

    public static <EVENT extends PlayerBucketEvent> EVENT create(
        Factory<? extends EVENT> factory, Level level, net.minecraft.world.entity.player.Player player, BlockPos changedPos,
        BlockPos clickedPos, Direction clickedFace, net.minecraft.world.item.ItemStack bucket, net.minecraft.world.item.Item itemInHand,
        InteractionHand hand
    ) {
        return factory.create(
            (Player) player.getBukkitEntity(),
            CraftBlock.at(level, changedPos),
            CraftBlock.at(level, clickedPos),
            CraftBlock.notchToBlockFace(clickedFace),
            CraftItemType.minecraftToBukkit(bucket.getItem()),
            CraftItemStack.asNewCraftStack(itemInHand),
            CraftEquipmentSlot.getHand(hand)
        );
    }

    @Override
    public final Block getBlock() {
        return this.block;
    }

    @Override
    public Block getBlockClicked() {
        return this.blockClicked;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public Material getBucket() {
        return this.bucket;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public @Nullable ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
