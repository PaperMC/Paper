package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockRedstoneLamp extends Block {

    private final boolean a;

    public BlockRedstoneLamp(boolean flag) {
        super(Material.BUILDABLE_GLASS);
        this.a = flag;
        if (flag) {
            this.a(1.0F);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (!world.isStatic) {
            if (this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
                world.a(i, j, k, this, 4);
            } else if (!this.a && world.isBlockIndirectlyPowered(i, j, k)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end

                world.setTypeAndData(i, j, k, Blocks.REDSTONE_LAMP_ON, 0, 2);
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (!world.isStatic) {
            if (this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
                world.a(i, j, k, this, 4);
            } else if (!this.a && world.isBlockIndirectlyPowered(i, j, k)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end

                world.setTypeAndData(i, j, k, Blocks.REDSTONE_LAMP_ON, 0, 2);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic && this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
            // CraftBukkit start
            if (CraftEventFactory.callRedstoneChange(world, i, j, k, 15, 0).getNewCurrent() != 0) {
                return;
            }
            // CraftBukkit end

            world.setTypeAndData(i, j, k, Blocks.REDSTONE_LAMP_OFF, 0, 2);
        }
    }

    public Item getDropType(int i, Random random, int j) {
        return Item.getItemOf(Blocks.REDSTONE_LAMP_OFF);
    }

    protected ItemStack j(int i) {
        return new ItemStack(Blocks.REDSTONE_LAMP_OFF);
    }
}
