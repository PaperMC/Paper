package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.HangingSign;

public class CraftHangingSign extends CraftSign<HangingSignBlockEntity> implements HangingSign {

    public CraftHangingSign(World world, HangingSignBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftHangingSign(CraftHangingSign state, Location location) {
        super(state, location);
    }

    @Override
    public CraftHangingSign copy() {
        return new CraftHangingSign(this, null);
    }

    @Override
    public CraftHangingSign copy(Location location) {
        return new CraftHangingSign(this, location);
    }
}
