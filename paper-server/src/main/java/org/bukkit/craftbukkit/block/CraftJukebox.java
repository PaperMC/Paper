package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.BlockJukeBox.TileEntityRecordPlayer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockEntityState<TileEntityRecordPlayer> implements Jukebox {

    public CraftJukebox(final Block block) {
        super(block, TileEntityRecordPlayer.class);
    }

    public CraftJukebox(final Material material, TileEntityRecordPlayer te) {
        super(material, te);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            CraftWorld world = (CraftWorld) this.getWorld();
            Material record = this.getPlaying();
            if (record == Material.AIR) {
                world.getHandle().setTypeAndData(new BlockPosition(this.getX(), this.getY(), this.getZ()),
                    Blocks.JUKEBOX.getBlockData()
                        .set(BlockJukeBox.HAS_RECORD, false), 3);
            } else {
                world.getHandle().setTypeAndData(new BlockPosition(this.getX(), this.getY(), this.getZ()),
                    Blocks.JUKEBOX.getBlockData()
                        .set(BlockJukeBox.HAS_RECORD, true), 3);
            }
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = this.getSnapshot().getRecord();
        if (record.isEmpty()) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
        }

        this.getSnapshot().setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        if (record == Material.AIR) {
            setRawData((byte) 0);
        } else {
            setRawData((byte) 1);
        }
    }

    @Override
    public boolean isPlaying() {
        return getRawData() == 1;
    }

    @Override
    public boolean eject() {
        requirePlaced();
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof TileEntityRecordPlayer)) return false;

        TileEntityRecordPlayer jukebox = (TileEntityRecordPlayer) tileEntity;
        boolean result = !jukebox.getRecord().isEmpty();
        CraftWorld world = (CraftWorld) this.getWorld();
        ((BlockJukeBox) Blocks.JUKEBOX).dropRecord(world.getHandle(), new BlockPosition(getX(), getY(), getZ()), null);
        return result;
    }
}
