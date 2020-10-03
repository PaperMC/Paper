package net.minecraft.server;

import java.util.Iterator;
import javax.annotation.Nullable;

public class BlockWitherSkull extends BlockSkull {

    @Nullable
    private static ShapeDetector c;
    @Nullable
    private static ShapeDetector d;

    protected BlockWitherSkull(BlockBase.Info blockbase_info) {
        super(BlockSkull.Type.WITHER_SKELETON, blockbase_info);
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, @Nullable EntityLiving entityliving, ItemStack itemstack) {
        super.postPlace(world, blockposition, iblockdata, entityliving, itemstack);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntitySkull) {
            a(world, blockposition, (TileEntitySkull) tileentity);
        }

    }

    public static void a(World world, BlockPosition blockposition, TileEntitySkull tileentityskull) {
        if (!world.isClientSide) {
            IBlockData iblockdata = tileentityskull.getBlock();
            boolean flag = iblockdata.a(Blocks.WITHER_SKELETON_SKULL) || iblockdata.a(Blocks.WITHER_SKELETON_WALL_SKULL);

            if (flag && blockposition.getY() >= 0 && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                ShapeDetector shapedetector = c();
                ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = shapedetector.a(world, blockposition);

                if (shapedetector_shapedetectorcollection != null) {
                    for (int i = 0; i < shapedetector.c(); ++i) {
                        for (int j = 0; j < shapedetector.b(); ++j) {
                            ShapeDetectorBlock shapedetectorblock = shapedetector_shapedetectorcollection.a(i, j, 0);

                            world.setTypeAndData(shapedetectorblock.getPosition(), Blocks.AIR.getBlockData(), 2);
                            world.triggerEffect(2001, shapedetectorblock.getPosition(), Block.getCombinedId(shapedetectorblock.a()));
                        }
                    }

                    EntityWither entitywither = (EntityWither) EntityTypes.WITHER.a(world);
                    BlockPosition blockposition1 = shapedetector_shapedetectorcollection.a(1, 2, 0).getPosition();

                    entitywither.setPositionRotation((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.55D, (double) blockposition1.getZ() + 0.5D, shapedetector_shapedetectorcollection.getFacing().n() == EnumDirection.EnumAxis.X ? 0.0F : 90.0F, 0.0F);
                    entitywither.aA = shapedetector_shapedetectorcollection.getFacing().n() == EnumDirection.EnumAxis.X ? 0.0F : 90.0F;
                    entitywither.beginSpawnSequence();
                    Iterator iterator = world.a(EntityPlayer.class, entitywither.getBoundingBox().g(50.0D)).iterator();

                    while (iterator.hasNext()) {
                        EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                        CriterionTriggers.n.a(entityplayer, (Entity) entitywither);
                    }

                    world.addEntity(entitywither);

                    for (int k = 0; k < shapedetector.c(); ++k) {
                        for (int l = 0; l < shapedetector.b(); ++l) {
                            world.update(shapedetector_shapedetectorcollection.a(k, l, 0).getPosition(), Blocks.AIR);
                        }
                    }

                }
            }
        }
    }

    public static boolean b(World world, BlockPosition blockposition, ItemStack itemstack) {
        return itemstack.getItem() == Items.WITHER_SKELETON_SKULL && blockposition.getY() >= 2 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isClientSide ? d().a(world, blockposition) != null : false;
    }

    private static ShapeDetector c() {
        if (BlockWitherSkull.c == null) {
            BlockWitherSkull.c = ShapeDetectorBuilder.a().a("^^^", "###", "~#~").a('#', (shapedetectorblock) -> {
                return shapedetectorblock.a().a((Tag) TagsBlock.WITHER_SUMMON_BASE_BLOCKS);
            }).a('^', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.a(Blocks.WITHER_SKELETON_WALL_SKULL)))).a('~', ShapeDetectorBlock.a(MaterialPredicate.a(Material.AIR))).b();
        }

        return BlockWitherSkull.c;
    }

    private static ShapeDetector d() {
        if (BlockWitherSkull.d == null) {
            BlockWitherSkull.d = ShapeDetectorBuilder.a().a("   ", "###", "~#~").a('#', (shapedetectorblock) -> {
                return shapedetectorblock.a().a((Tag) TagsBlock.WITHER_SUMMON_BASE_BLOCKS);
            }).a('~', ShapeDetectorBlock.a(MaterialPredicate.a(Material.AIR))).b();
        }

        return BlockWitherSkull.d;
    }
}
