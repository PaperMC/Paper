package net.minecraft.server;

import org.bukkit.event.block.BlockFromToEvent; // CraftBukkit

public class BlockDragonEgg extends BlockFalling {

    protected static final VoxelShape a = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public BlockDragonEgg(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockDragonEgg.a;
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        this.d(iblockdata, world, blockposition);
        return EnumInteractionResult.a(world.isClientSide);
    }

    @Override
    public void attack(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman) {
        this.d(iblockdata, world, blockposition);
    }

    private void d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        for (int i = 0; i < 1000; ++i) {
            BlockPosition blockposition1 = blockposition.b(world.random.nextInt(16) - world.random.nextInt(16), world.random.nextInt(8) - world.random.nextInt(8), world.random.nextInt(16) - world.random.nextInt(16));

            if (world.getType(blockposition1).isAir()) {
                // CraftBukkit start
                org.bukkit.block.Block from = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                org.bukkit.block.Block to = world.getWorld().getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
                BlockFromToEvent event = new BlockFromToEvent(from, to);
                org.bukkit.Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                blockposition1 = new BlockPosition(event.getToBlock().getX(), event.getToBlock().getY(), event.getToBlock().getZ());
                // CraftBukkit end
                if (world.isClientSide) {
                    for (int j = 0; j < 128; ++j) {
                        double d0 = world.random.nextDouble();
                        float f = (world.random.nextFloat() - 0.5F) * 0.2F;
                        float f1 = (world.random.nextFloat() - 0.5F) * 0.2F;
                        float f2 = (world.random.nextFloat() - 0.5F) * 0.2F;
                        double d1 = MathHelper.d(d0, (double) blockposition1.getX(), (double) blockposition.getX()) + (world.random.nextDouble() - 0.5D) + 0.5D;
                        double d2 = MathHelper.d(d0, (double) blockposition1.getY(), (double) blockposition.getY()) + world.random.nextDouble() - 0.5D;
                        double d3 = MathHelper.d(d0, (double) blockposition1.getZ(), (double) blockposition.getZ()) + (world.random.nextDouble() - 0.5D) + 0.5D;

                        world.addParticle(Particles.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2);
                    }
                } else {
                    world.setTypeAndData(blockposition1, iblockdata, 2);
                    world.a(blockposition, false);
                }

                return;
            }
        }

    }

    @Override
    protected int c() {
        return 5;
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
