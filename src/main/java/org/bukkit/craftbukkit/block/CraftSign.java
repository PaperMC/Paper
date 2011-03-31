package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntitySign;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftSign extends CraftBlockState implements Sign {
    private final CraftWorld world;
    private final TileEntitySign sign;

    public CraftSign(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        sign = (TileEntitySign)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public String[] getLines() {
        return sign.a;
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        return sign.a[index];
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        sign.a[index] = line;
    }

    @Override
    public boolean update(boolean force) {
        boolean result = super.update(force);

        if (result) {
            sign.i();
        }

        return result;
    }
}
