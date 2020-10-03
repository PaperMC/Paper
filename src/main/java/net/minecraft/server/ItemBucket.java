package net.minecraft.server;

import javax.annotation.Nullable;
// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.DummyGeneratorAccess;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
// CraftBukkit end

public class ItemBucket extends Item {

    public final FluidType fluidType;

    public ItemBucket(FluidType fluidtype, Item.Info item_info) {
        super(item_info);
        this.fluidType = fluidtype;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        MovingObjectPositionBlock movingobjectpositionblock = a(world, entityhuman, this.fluidType == FluidTypes.EMPTY ? RayTrace.FluidCollisionOption.SOURCE_ONLY : RayTrace.FluidCollisionOption.NONE);

        if (movingobjectpositionblock.getType() == MovingObjectPosition.EnumMovingObjectType.MISS) {
            return InteractionResultWrapper.pass(itemstack);
        } else if (movingobjectpositionblock.getType() != MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            return InteractionResultWrapper.pass(itemstack);
        } else {
            MovingObjectPositionBlock movingobjectpositionblock1 = (MovingObjectPositionBlock) movingobjectpositionblock;
            BlockPosition blockposition = movingobjectpositionblock1.getBlockPosition();
            EnumDirection enumdirection = movingobjectpositionblock1.getDirection();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (world.a(entityhuman, blockposition) && entityhuman.a(blockposition1, enumdirection, itemstack)) {
                IBlockData iblockdata;

                if (this.fluidType == FluidTypes.EMPTY) {
                    iblockdata = world.getType(blockposition);
                    if (iblockdata.getBlock() instanceof IFluidSource) {
                        // CraftBukkit start
                        FluidType dummyFluid = ((IFluidSource) iblockdata.getBlock()).removeFluid(DummyGeneratorAccess.INSTANCE, blockposition, iblockdata);
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent((WorldServer) world, entityhuman, blockposition, blockposition, movingobjectpositionblock.getDirection(), itemstack, dummyFluid.a());

                        if (event.isCancelled()) {
                            ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutBlockChange(world, blockposition)); // SPIGOT-5163 (see PlayerInteractManager)
                            ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory(); // SPIGOT-4541
                            return new InteractionResultWrapper(EnumInteractionResult.FAIL, itemstack);
                        }
                        // CraftBukkit end
                        FluidType fluidtype = ((IFluidSource) iblockdata.getBlock()).removeFluid(world, blockposition, iblockdata);

                        if (fluidtype != FluidTypes.EMPTY) {
                            entityhuman.b(StatisticList.ITEM_USED.b(this));
                            entityhuman.playSound(fluidtype.a((Tag) TagsFluid.LAVA) ? SoundEffects.ITEM_BUCKET_FILL_LAVA : SoundEffects.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack itemstack1 = ItemLiquidUtil.a(itemstack, entityhuman, CraftItemStack.asNMSCopy(event.getItemStack())); // CraftBukkit

                            if (!world.isClientSide) {
                                CriterionTriggers.j.a((EntityPlayer) entityhuman, new ItemStack(fluidtype.a()));
                            }

                            return InteractionResultWrapper.a(itemstack1, world.s_());
                        }
                    }

                    return InteractionResultWrapper.fail(itemstack);
                } else {
                    iblockdata = world.getType(blockposition);
                    BlockPosition blockposition2 = iblockdata.getBlock() instanceof IFluidContainer && this.fluidType == FluidTypes.WATER ? blockposition : blockposition1;

                    if (this.a(entityhuman, world, blockposition2, movingobjectpositionblock1, movingobjectpositionblock1.getDirection(), blockposition, itemstack)) { // CraftBukkit
                        this.a(world, itemstack, blockposition2);
                        if (entityhuman instanceof EntityPlayer) {
                            CriterionTriggers.y.a((EntityPlayer) entityhuman, blockposition2, itemstack);
                        }

                        entityhuman.b(StatisticList.ITEM_USED.b(this));
                        return InteractionResultWrapper.a(this.a(itemstack, entityhuman), world.s_());
                    } else {
                        return InteractionResultWrapper.fail(itemstack);
                    }
                }
            } else {
                return InteractionResultWrapper.fail(itemstack);
            }
        }
    }

    protected ItemStack a(ItemStack itemstack, EntityHuman entityhuman) {
        return !entityhuman.abilities.canInstantlyBuild ? new ItemStack(Items.BUCKET) : itemstack;
    }

    public void a(World world, ItemStack itemstack, BlockPosition blockposition) {}

    public boolean a(@Nullable EntityHuman entityhuman, World world, BlockPosition blockposition, @Nullable MovingObjectPositionBlock movingobjectpositionblock) {
        return a(entityhuman, world, blockposition, movingobjectpositionblock, null, null, null);
    }

    public boolean a(EntityHuman entityhuman, World world, BlockPosition blockposition, @Nullable MovingObjectPositionBlock movingobjectpositionblock, EnumDirection enumdirection, BlockPosition clicked, ItemStack itemstack) {
        // CraftBukkit end
        if (!(this.fluidType instanceof FluidTypeFlowing)) {
            return false;
        } else {
            IBlockData iblockdata = world.getType(blockposition);
            Block block = iblockdata.getBlock();
            Material material = iblockdata.getMaterial();
            boolean flag = iblockdata.a(this.fluidType);
            boolean flag1 = iblockdata.isAir() || flag || block instanceof IFluidContainer && ((IFluidContainer) block).canPlace(world, blockposition, iblockdata, this.fluidType);

            // CraftBukkit start
            if (flag1 && entityhuman != null) {
                PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent((WorldServer) world, entityhuman, blockposition, clicked, enumdirection, itemstack);
                if (event.isCancelled()) {
                    ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutBlockChange(world, blockposition)); // SPIGOT-4238: needed when looking through entity
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory(); // SPIGOT-4541
                    return false;
                }
            }
            // CraftBukkit end
            if (!flag1) {
                return movingobjectpositionblock != null && this.a(entityhuman, world, movingobjectpositionblock.getBlockPosition().shift(movingobjectpositionblock.getDirection()), (MovingObjectPositionBlock) null, enumdirection, clicked, itemstack); // CraftBukkit
            } else if (world.getDimensionManager().isNether() && this.fluidType.a((Tag) TagsFluid.WATER)) {
                int i = blockposition.getX();
                int j = blockposition.getY();
                int k = blockposition.getZ();

                world.playSound(entityhuman, blockposition, SoundEffects.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    world.addParticle(Particles.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof IFluidContainer && this.fluidType == FluidTypes.WATER) {
                ((IFluidContainer) block).place(world, blockposition, iblockdata, ((FluidTypeFlowing) this.fluidType).a(false));
                this.a(entityhuman, (GeneratorAccess) world, blockposition);
                return true;
            } else {
                if (!world.isClientSide && flag && !material.isLiquid()) {
                    world.b(blockposition, true);
                }

                if (!world.setTypeAndData(blockposition, this.fluidType.h().getBlockData(), 11) && !iblockdata.getFluid().isSource()) {
                    return false;
                } else {
                    this.a(entityhuman, (GeneratorAccess) world, blockposition);
                    return true;
                }
            }
        }
    }

    protected void a(@Nullable EntityHuman entityhuman, GeneratorAccess generatoraccess, BlockPosition blockposition) {
        SoundEffect soundeffect = this.fluidType.a((Tag) TagsFluid.LAVA) ? SoundEffects.ITEM_BUCKET_EMPTY_LAVA : SoundEffects.ITEM_BUCKET_EMPTY;

        generatoraccess.playSound(entityhuman, blockposition, soundeffect, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
