package org.bukkit.craftbukkit.block;

import net.minecraft.server.*;
import net.minecraft.server.BlockJukeBox.TileEntityRecordPlayer;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockState implements Jukebox {
    private final CraftWorld world;
    private final TileEntityRecordPlayer jukebox;

    public CraftJukebox(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        jukebox = (TileEntityRecordPlayer) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftJukebox(final Material material, TileEntityRecordPlayer te) {
        super(material);
        world = null;
        jukebox = te;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = jukebox.getRecord();
        if (record == null) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
            jukebox.setRecord(null);
        } else {
            jukebox.setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        }
        if (!isPlaced()) {
            return;
        }
        jukebox.update();
        if (record == Material.AIR) {
            world.getHandle().setTypeAndData(new BlockPosition(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getBlockData()
                    .set(BlockJukeBox.HAS_RECORD, false), 3);
        } else {
            world.getHandle().setTypeAndData(new BlockPosition(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getBlockData()
                    .set(BlockJukeBox.HAS_RECORD, true), 3);
        }
        world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getId());
    }

    public boolean isPlaying() {
        return getRawData() == 1;
    }

    public boolean eject() {
        requirePlaced();
        boolean result = isPlaying();
        ((BlockJukeBox) Blocks.JUKEBOX).dropRecord(world.getHandle(), new BlockPosition(getX(), getY(), getZ()), null);
        return result;
    }

    @Override
    public TileEntityRecordPlayer getTileEntity() {
        return jukebox;
    }
}
