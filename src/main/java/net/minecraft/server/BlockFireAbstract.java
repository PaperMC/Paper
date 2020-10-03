package net.minecraft.server;

import java.util.Optional;

public abstract class BlockFireAbstract extends Block {

    private final float b;
    protected static final VoxelShape a = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BlockFireAbstract(BlockBase.Info blockbase_info, float f) {
        super(blockbase_info);
        this.b = f;
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return a((IBlockAccess) blockactioncontext.getWorld(), blockactioncontext.getClickPosition());
    }

    public static IBlockData a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();
        IBlockData iblockdata = iblockaccess.getType(blockposition1);

        return BlockSoulFire.c(iblockdata.getBlock()) ? Blocks.SOUL_FIRE.getBlockData() : ((BlockFire) Blocks.FIRE).getPlacedState(iblockaccess, blockposition);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockFireAbstract.a;
    }

    protected abstract boolean e(IBlockData iblockdata);

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (!entity.isFireProof()) {
            entity.setFireTicks(entity.getFireTicks() + 1);
            if (entity.getFireTicks() == 0) {
                entity.setOnFire(8);
            }

            entity.damageEntity(DamageSource.FIRE, this.b);
        }

        super.a(iblockdata, world, blockposition, entity);
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata1.a(iblockdata.getBlock())) {
            if (a(world)) {
                Optional<BlockPortalShape> optional = BlockPortalShape.a((GeneratorAccess) world, blockposition, EnumDirection.EnumAxis.X);

                if (optional.isPresent()) {
                    ((BlockPortalShape) optional.get()).createPortal();
                    return;
                }
            }

            if (!iblockdata.canPlace(world, blockposition)) {
                world.a(blockposition, false);
            }

        }
    }

    private static boolean a(World world) {
        return world.getDimensionKey() == World.OVERWORLD || world.getDimensionKey() == World.THE_NETHER;
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (!world.s_()) {
            world.a((EntityHuman) null, 1009, blockposition, 0);
        }

    }

    public static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = world.getType(blockposition);

        return !iblockdata.isAir() ? false : a((IBlockAccess) world, blockposition).canPlace(world, blockposition) || b(world, blockposition, enumdirection);
    }

    private static boolean b(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        if (!a(world)) {
            return false;
        } else {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = blockposition.i();
            boolean flag = false;
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection1 = aenumdirection[j];

                if (world.getType(blockposition_mutableblockposition.g(blockposition).c(enumdirection1)).a(Blocks.OBSIDIAN)) {
                    flag = true;
                    break;
                }
            }

            return flag && BlockPortalShape.a((GeneratorAccess) world, blockposition, enumdirection.h().n()).isPresent();
        }
    }
}
