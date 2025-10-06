package org.bukkit.craftbukkit.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.inventory.CraftInventoryJukebox;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.JukeboxInventory;

public class CraftJukebox extends CraftBlockEntityState<JukeboxBlockEntity> implements Jukebox {

    public CraftJukebox(World world, JukeboxBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftJukebox(CraftJukebox state, Location location) {
        super(state, location);
    }

    @Override
    public JukeboxInventory getSnapshotInventory() {
        return new CraftInventoryJukebox(this.getSnapshot());
    }

    @Override
    public JukeboxInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryJukebox(this.getBlockEntity());
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            this.getWorldHandle().setBlock(this.getPosition(), this.data, Block.UPDATE_ALL);

            BlockEntity blockEntity = this.getBlockEntityFromWorld();
            if (blockEntity instanceof JukeboxBlockEntity jukebox) {
                jukebox.setTheItem(jukebox.getTheItem());
            }
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        return this.getRecord().getType();
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftItemType.bukkitToMinecraft(record) == null) {
            record = Material.AIR;
        }

        this.setRecord(new org.bukkit.inventory.ItemStack(record));
    }

    @Override
    public boolean hasRecord() {
        return this.data.getValueOrElse(JukeboxBlock.HAS_RECORD, false) && !this.getPlaying().isAir();
    }

    @Override
    public org.bukkit.inventory.ItemStack getRecord() {
        ItemStack record = this.getSnapshot().getTheItem();
        return CraftItemStack.asBukkitCopy(record);
    }

    @Override
    public void setRecord(org.bukkit.inventory.ItemStack record) {
        ItemStack nms = CraftItemStack.asNMSCopy(record);

        JukeboxBlockEntity snapshot = this.getSnapshot();
        snapshot.setSongItemWithoutPlaying(nms, snapshot.getSongPlayer().getTicksSinceSongStarted());

        this.data = this.data.trySetValue(JukeboxBlock.HAS_RECORD, !nms.isEmpty());
    }

    @Override
    public boolean isPlaying() {
        this.requirePlaced();

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        return blockEntity instanceof JukeboxBlockEntity jukebox && jukebox.getSongPlayer().isPlaying();
    }

    @Override
    public boolean startPlaying() {
        this.requirePlaced();

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        if (!(blockEntity instanceof JukeboxBlockEntity jukebox)) {
            return false;
        }

        ItemStack record = jukebox.getTheItem();
        if (record.isEmpty() || this.isPlaying()) {
            return false;
        }

        jukebox.tryForcePlaySong();
        return true;
    }

    @Override
    public void stopPlaying() {
        this.requirePlaced();

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        if (!(blockEntity instanceof JukeboxBlockEntity jukebox)) {
            return;
        }

        jukebox.getSongPlayer().stop(blockEntity.getLevel(), blockEntity.getBlockState());
    }

    @Override
    public boolean eject() {
        this.ensureNoWorldGeneration();

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        if (!(blockEntity instanceof JukeboxBlockEntity)) return false;

        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) blockEntity;
        boolean result = !jukebox.getTheItem().isEmpty();
        jukebox.popOutTheItem();
        return result;
    }

    @Override
    public CraftJukebox copy() {
        return new CraftJukebox(this, null);
    }

    @Override
    public CraftJukebox copy(Location location) {
        return new CraftJukebox(this, location);
    }
}
