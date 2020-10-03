package net.minecraft.server;

public class ItemEnderEye extends Item {

    public ItemEnderEye(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.a(Blocks.END_PORTAL_FRAME) && !(Boolean) iblockdata.get(BlockEnderPortalFrame.EYE)) {
            if (world.isClientSide) {
                return EnumInteractionResult.SUCCESS;
            } else {
                IBlockData iblockdata1 = (IBlockData) iblockdata.set(BlockEnderPortalFrame.EYE, true);

                Block.a(iblockdata, iblockdata1, world, blockposition);
                world.setTypeAndData(blockposition, iblockdata1, 2);
                world.updateAdjacentComparators(blockposition, Blocks.END_PORTAL_FRAME);
                itemactioncontext.getItemStack().subtract(1);
                world.triggerEffect(1503, blockposition, 0);
                ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = BlockEnderPortalFrame.c().a(world, blockposition);

                if (shapedetector_shapedetectorcollection != null) {
                    BlockPosition blockposition1 = shapedetector_shapedetectorcollection.a().b(-3, 0, -3);

                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            world.setTypeAndData(blockposition1.b(i, 0, j), Blocks.END_PORTAL.getBlockData(), 2);
                        }
                    }

                    world.b(1038, blockposition1.b(1, 0, 1), 0);
                }

                return EnumInteractionResult.CONSUME;
            }
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        MovingObjectPositionBlock movingobjectpositionblock = a(world, entityhuman, RayTrace.FluidCollisionOption.NONE);

        if (movingobjectpositionblock.getType() == MovingObjectPosition.EnumMovingObjectType.BLOCK && world.getType(((MovingObjectPositionBlock) movingobjectpositionblock).getBlockPosition()).a(Blocks.END_PORTAL_FRAME)) {
            return InteractionResultWrapper.pass(itemstack);
        } else {
            entityhuman.c(enumhand);
            if (world instanceof WorldServer) {
                BlockPosition blockposition = ((WorldServer) world).getChunkProvider().getChunkGenerator().findNearestMapFeature((WorldServer) world, StructureGenerator.STRONGHOLD, entityhuman.getChunkCoordinates(), 100, false);

                if (blockposition != null) {
                    EntityEnderSignal entityendersignal = new EntityEnderSignal(world, entityhuman.locX(), entityhuman.e(0.5D), entityhuman.locZ());

                    entityendersignal.setItem(itemstack);
                    entityendersignal.a(blockposition);
                    // CraftBukkit start
                    if (!world.addEntity(entityendersignal)) {
                        return new InteractionResultWrapper(EnumInteractionResult.FAIL, itemstack);
                    }
                    // CraftBukkit end
                    if (entityhuman instanceof EntityPlayer) {
                        CriterionTriggers.m.a((EntityPlayer) entityhuman, blockposition);
                    }

                    world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemEnderEye.RANDOM.nextFloat() * 0.4F + 0.8F));
                    world.a((EntityHuman) null, 1003, entityhuman.getChunkCoordinates(), 0);
                    if (!entityhuman.abilities.canInstantlyBuild) {
                        itemstack.subtract(1);
                    }

                    entityhuman.b(StatisticList.ITEM_USED.b(this));
                    entityhuman.swingHand(enumhand, true);
                    return InteractionResultWrapper.success(itemstack);
                }
            }

            return InteractionResultWrapper.consume(itemstack);
        }
    }
}
