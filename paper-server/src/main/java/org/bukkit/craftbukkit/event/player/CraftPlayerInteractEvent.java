package org.bukkit.craftbukkit.event.player;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

import static org.bukkit.craftbukkit.inventory.CraftItemStack.emptyToNull;

public class CraftPlayerInteractEvent extends CraftPlayerEvent implements PlayerInteractEvent {

    private final Action action;
    private final @Nullable ItemStack item;
    private final @Nullable Block blockClicked;
    private final BlockFace blockFace;
    private final @Nullable EquipmentSlot hand;
    private final @Nullable Vector clickedPosition;
    private Result useItemInHand;
    private Result useClickedBlock;

    public CraftPlayerInteractEvent(
        final Player player,
        final Action action,
        final @Nullable ItemStack item,
        final @Nullable Block clickedBlock,
        final BlockFace clickedFace,
        final @Nullable EquipmentSlot hand,
        final @Nullable Vector clickedPosition
    ) {
        super(player);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.hand = hand;
        this.clickedPosition = clickedPosition;

        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
    }

    public CraftPlayerInteractEvent(
        final net.minecraft.world.entity.player.Player player,
        final Action action,
        final net.minecraft.world.item.ItemStack item,
        final @Nullable BlockPos pos,
        final @Nullable Direction face,
        final @Nullable InteractionHand hand,
        final @Nullable Vec3 clickedPos
    ) {
        this(
            (Player) player.getBukkitEntity(),
            action,
            emptyToNull(item, CraftItemStack::asCraftMirror),
            Optionull.map(pos, p -> CraftBlock.at(player.level(), p)),
            CraftBlock.notchToBlockFace(face),
            Optionull.map(hand, CraftEquipmentSlot::getHand),
            Optionull.map(clickedPos, CraftVector::toBukkit)
        );
    }

    @Override
    public Action getAction() {
        return this.action;
    }

    @Override
    public boolean isCancelled() {
        return this.useInteractedBlock() == Result.DENY;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.setUseInteractedBlock(cancel ? Result.DENY : this.useInteractedBlock() == Result.DENY ? Result.DEFAULT : this.useInteractedBlock());
        this.setUseItemInHand(cancel ? Result.DENY : this.useItemInHand() == Result.DENY ? Result.DEFAULT : this.useItemInHand());
    }

    @Override
    public @Nullable ItemStack getItem() {
        return this.item;
    }

    @Override
    public Material getMaterial() {
        if (!this.hasItem()) {
            return Material.AIR;
        }

        return this.item.getType();
    }

    @Override
    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    @Override
    public boolean hasItem() {
        return this.item != null;
    }

    @Override
    public boolean isBlockInHand() {
        if (!this.hasItem()) {
            return false;
        }

        return this.item.getType().isBlock();
    }

    @Override
    public @Nullable Block getClickedBlock() {
        return this.blockClicked;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public @Nullable EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public @Nullable Vector getClickedPosition() {
        if (this.clickedPosition == null) {
            return null;
        }
        return this.clickedPosition.clone();
    }

    @Override
    public @Nullable Location getInteractionPoint() {
        if (this.blockClicked == null || this.clickedPosition == null) {
            return null;
        }
        return this.blockClicked.getLocation().add(this.clickedPosition);
    }

    @Override
    public Result useInteractedBlock() {
        return this.useClickedBlock;
    }

    @Override
    public void setUseInteractedBlock(final Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    @Override
    public Result useItemInHand() {
        return this.useItemInHand;
    }

    @Override
    public void setUseItemInHand(final Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerInteractEvent.getHandlerList();
    }
}
