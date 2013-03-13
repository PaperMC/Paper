package org.bukkit.craftbukkit.block;

import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.TileEntityRecordPlayer;

public class CraftJukebox extends CraftBlockState implements Jukebox {
    private final CraftWorld world;
    private final TileEntityRecordPlayer jukebox;

    public CraftJukebox(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        jukebox = (TileEntityRecordPlayer) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Material getPlaying() {
        ItemStack record = jukebox.getRecord();
        if (record == null) {
            return Material.AIR;
        }
        return Material.getMaterial(record.id);
    }

    public void setPlaying(Material record) {
        if (record == null || Item.byId[record.getId()] == null) {
            record = Material.AIR;
            jukebox.setRecord(null);
        } else {
            jukebox.setRecord(new ItemStack(Item.byId[record.getId()], 1));
        }
        jukebox.update();
        if (record == Material.AIR) {
            world.getHandle().setData(getX(), getY(), getZ(), 0, 3);
        } else {
            world.getHandle().setData(getX(), getY(), getZ(), 1, 3);
        }
        world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getId());
    }

    public boolean isPlaying() {
        return getRawData() == 1;
    }

    public boolean eject() {
        boolean result = isPlaying();
        ((BlockJukeBox) net.minecraft.server.Block.JUKEBOX).dropRecord(world.getHandle(), getX(), getY(), getZ());
        return result;
    }
}
