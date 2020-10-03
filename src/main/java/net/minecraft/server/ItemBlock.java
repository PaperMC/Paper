package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

public class ItemBlock extends Item {

    @Deprecated
    private final Block a;

    public ItemBlock(Block block, Item.Info item_info) {
        super(item_info);
        this.a = block;
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        EnumInteractionResult enuminteractionresult = this.a(new BlockActionContext(itemactioncontext));

        return !enuminteractionresult.a() && this.isFood() ? this.a(itemactioncontext.getWorld(), itemactioncontext.getEntity(), itemactioncontext.getHand()).a() : enuminteractionresult;
    }

    public EnumInteractionResult a(BlockActionContext blockactioncontext) {
        if (!blockactioncontext.b()) {
            return EnumInteractionResult.FAIL;
        } else {
            BlockActionContext blockactioncontext1 = this.b(blockactioncontext);

            if (blockactioncontext1 == null) {
                return EnumInteractionResult.FAIL;
            } else {
                IBlockData iblockdata = this.c(blockactioncontext1);

                if (iblockdata == null) {
                    return EnumInteractionResult.FAIL;
                } else if (!this.a(blockactioncontext1, iblockdata)) {
                    return EnumInteractionResult.FAIL;
                } else {
                    BlockPosition blockposition = blockactioncontext1.getClickPosition();
                    World world = blockactioncontext1.getWorld();
                    EntityHuman entityhuman = blockactioncontext1.getEntity();
                    ItemStack itemstack = blockactioncontext1.getItemStack();
                    IBlockData iblockdata1 = world.getType(blockposition);
                    Block block = iblockdata1.getBlock();

                    if (block == iblockdata.getBlock()) {
                        iblockdata1 = this.a(blockposition, world, itemstack, iblockdata1);
                        this.a(blockposition, world, entityhuman, itemstack, iblockdata1);
                        block.postPlace(world, blockposition, iblockdata1, entityhuman, itemstack);
                        if (entityhuman instanceof EntityPlayer) {
                            CriterionTriggers.y.a((EntityPlayer) entityhuman, blockposition, itemstack);
                        }
                    }

                    SoundEffectType soundeffecttype = iblockdata1.getStepSound();

                    world.playSound(entityhuman, blockposition, this.a(iblockdata1), SoundCategory.BLOCKS, (soundeffecttype.a() + 1.0F) / 2.0F, soundeffecttype.b() * 0.8F);
                    if (entityhuman == null || !entityhuman.abilities.canInstantlyBuild) {
                        itemstack.subtract(1);
                    }

                    return EnumInteractionResult.a(world.isClientSide);
                }
            }
        }
    }

    protected SoundEffect a(IBlockData iblockdata) {
        return iblockdata.getStepSound().e();
    }

    @Nullable
    public BlockActionContext b(BlockActionContext blockactioncontext) {
        return blockactioncontext;
    }

    protected boolean a(BlockPosition blockposition, World world, @Nullable EntityHuman entityhuman, ItemStack itemstack, IBlockData iblockdata) {
        return a(world, entityhuman, blockposition, itemstack);
    }

    @Nullable
    protected IBlockData c(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = this.getBlock().getPlacedState(blockactioncontext);

        return iblockdata != null && this.b(blockactioncontext, iblockdata) ? iblockdata : null;
    }

    private IBlockData a(BlockPosition blockposition, World world, ItemStack itemstack, IBlockData iblockdata) {
        IBlockData iblockdata1 = iblockdata;
        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockStateTag");
            BlockStateList<Block, IBlockData> blockstatelist = iblockdata.getBlock().getStates();
            Iterator iterator = nbttagcompound1.getKeys().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                IBlockState<?> iblockstate = blockstatelist.a(s);

                if (iblockstate != null) {
                    String s1 = nbttagcompound1.get(s).asString();

                    iblockdata1 = a(iblockdata1, iblockstate, s1);
                }
            }
        }

        if (iblockdata1 != iblockdata) {
            world.setTypeAndData(blockposition, iblockdata1, 2);
        }

        return iblockdata1;
    }

    private static <T extends Comparable<T>> IBlockData a(IBlockData iblockdata, IBlockState<T> iblockstate, String s) {
        return (IBlockData) iblockstate.b(s).map((comparable) -> {
            return (IBlockData) iblockdata.set(iblockstate, comparable);
        }).orElse(iblockdata);
    }

    protected boolean b(BlockActionContext blockactioncontext, IBlockData iblockdata) {
        EntityHuman entityhuman = blockactioncontext.getEntity();
        VoxelShapeCollision voxelshapecollision = entityhuman == null ? VoxelShapeCollision.a() : VoxelShapeCollision.a((Entity) entityhuman);

        return (!this.isCheckCollisions() || iblockdata.canPlace(blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) && blockactioncontext.getWorld().a(iblockdata, blockactioncontext.getClickPosition(), voxelshapecollision);
    }

    protected boolean isCheckCollisions() {
        return true;
    }

    protected boolean a(BlockActionContext blockactioncontext, IBlockData iblockdata) {
        return blockactioncontext.getWorld().setTypeAndData(blockactioncontext.getClickPosition(), iblockdata, 11);
    }

    public static boolean a(World world, @Nullable EntityHuman entityhuman, BlockPosition blockposition, ItemStack itemstack) {
        MinecraftServer minecraftserver = world.getMinecraftServer();

        if (minecraftserver == null) {
            return false;
        } else {
            NBTTagCompound nbttagcompound = itemstack.b("BlockEntityTag");

            if (nbttagcompound != null) {
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity != null) {
                    if (!world.isClientSide && tileentity.isFilteredNBT() && (entityhuman == null || !entityhuman.isCreativeAndOp())) {
                        return false;
                    }

                    NBTTagCompound nbttagcompound1 = tileentity.save(new NBTTagCompound());
                    NBTTagCompound nbttagcompound2 = nbttagcompound1.clone();

                    nbttagcompound1.a(nbttagcompound);
                    nbttagcompound1.setInt("x", blockposition.getX());
                    nbttagcompound1.setInt("y", blockposition.getY());
                    nbttagcompound1.setInt("z", blockposition.getZ());
                    if (!nbttagcompound1.equals(nbttagcompound2)) {
                        tileentity.load(world.getType(blockposition), nbttagcompound1);
                        tileentity.update();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public String getName() {
        return this.getBlock().i();
    }

    @Override
    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.a(creativemodetab)) {
            this.getBlock().a(creativemodetab, nonnulllist);
        }

    }

    public Block getBlock() {
        return this.a;
    }

    public void a(Map<Block, Item> map, Item item) {
        map.put(this.getBlock(), item);
    }
}
