
package org.bukkit.craftbukkit.block;

import org.bukkit.Block;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftSign extends CraftBlockState implements Sign {
    public CraftSign(final CraftWorld world, final int x, final int y, final int z, final int type, final byte data) {
        super(world, x, y, z, type, data);
    }

    public String[] getLines() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
