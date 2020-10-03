package net.minecraft.server;

import javax.annotation.Nullable;

public class BlockTNT extends Block {

    public static final BlockStateBoolean a = BlockProperties.B;

    public BlockTNT(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) this.getBlockData().set(BlockTNT.a, false));
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata1.a(iblockdata.getBlock())) {
            if (world.isBlockIndirectlyPowered(blockposition)) {
                a(world, blockposition);
                world.a(blockposition, false);
            }

        }
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (world.isBlockIndirectlyPowered(blockposition)) {
            a(world, blockposition);
            world.a(blockposition, false);
        }

    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (!world.s_() && !entityhuman.isCreative() && (Boolean) iblockdata.get(BlockTNT.a)) {
            a(world, blockposition);
        }

        super.a(world, blockposition, iblockdata, entityhuman);
    }

    @Override
    public void wasExploded(World world, BlockPosition blockposition, Explosion explosion) {
        if (!world.isClientSide) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, explosion.getSource());

            entitytntprimed.setFuseTicks((short) (world.random.nextInt(entitytntprimed.getFuseTicks() / 4) + entitytntprimed.getFuseTicks() / 8));
            world.addEntity(entitytntprimed);
        }
    }

    public static void a(World world, BlockPosition blockposition) {
        a(world, blockposition, (EntityLiving) null);
    }

    private static void a(World world, BlockPosition blockposition, @Nullable EntityLiving entityliving) {
        if (!world.isClientSide) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, entityliving);

            world.addEntity(entitytntprimed);
            world.playSound((EntityHuman) null, entitytntprimed.locX(), entitytntprimed.locY(), entitytntprimed.locZ(), SoundEffects.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        ItemStack itemstack = entityhuman.b(enumhand);
        Item item = itemstack.getItem();

        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.interact(iblockdata, world, blockposition, entityhuman, enumhand, movingobjectpositionblock);
        } else {
            a(world, blockposition, (EntityLiving) entityhuman);
            world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 11);
            if (!entityhuman.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.damage(1, entityhuman, (entityhuman1) -> {
                        entityhuman1.broadcastItemBreak(enumhand);
                    });
                } else {
                    itemstack.subtract(1);
                }
            }

            return EnumInteractionResult.a(world.isClientSide);
        }
    }

    @Override
    public void a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, IProjectile iprojectile) {
        if (!world.isClientSide) {
            Entity entity = iprojectile.getShooter();

            if (iprojectile.isBurning()) {
                BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(iprojectile, blockposition, Blocks.AIR.getBlockData()).isCancelled()) {
                    return;
                }
                // CraftBukkit end

                a(world, blockposition, entity instanceof EntityLiving ? (EntityLiving) entity : null);
                world.a(blockposition, false);
            }
        }

    }

    @Override
    public boolean a(Explosion explosion) {
        return false;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockTNT.a);
    }
}
