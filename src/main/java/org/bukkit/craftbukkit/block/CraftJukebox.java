package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityJukeBox;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockEntityState<TileEntityJukeBox> implements Jukebox {

    public CraftJukebox(final Block block) {
        super(block, TileEntityJukeBox.class);
    }

    public CraftJukebox(final Material material, TileEntityJukeBox te) {
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
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, Item.getId(CraftMagicNumbers.getItem((Material) record)));
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
            getHandle().set(BlockJukeBox.HAS_RECORD, false);
        } else {
            getHandle().set(BlockJukeBox.HAS_RECORD, true);
        }
    }

    @Override
    public boolean isPlaying() {
        return getHandle().get(BlockJukeBox.HAS_RECORD);
    }

    @Override
    public boolean eject() {
        requirePlaced();
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof TileEntityJukeBox)) return false;

        TileEntityJukeBox jukebox = (TileEntityJukeBox) tileEntity;
        boolean result = !jukebox.getRecord().isEmpty();
        CraftWorld world = (CraftWorld) this.getWorld();
        ((BlockJukeBox) Blocks.JUKEBOX).dropRecord(world.getHandle(), new BlockPosition(getX(), getY(), getZ()));
        return result;
    }
}
