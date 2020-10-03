package net.minecraft.server;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

public class ItemBoneMeal extends Item {

    public ItemBoneMeal(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        // CraftBukkit start - extract bonemeal application logic to separate, static method
        return applyBonemeal(itemactioncontext);
    }

    public static EnumInteractionResult applyBonemeal(ItemActionContext itemactioncontext) {
        // CraftBukkit end
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        BlockPosition blockposition1 = blockposition.shift(itemactioncontext.getClickedFace());

        if (a(itemactioncontext.getItemStack(), world, blockposition)) {
            if (!world.isClientSide) {
                world.triggerEffect(2005, blockposition, 0);
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else {
            IBlockData iblockdata = world.getType(blockposition);
            boolean flag = iblockdata.d(world, blockposition, itemactioncontext.getClickedFace());

            if (flag && a(itemactioncontext.getItemStack(), world, blockposition1, itemactioncontext.getClickedFace())) {
                if (!world.isClientSide) {
                    world.triggerEffect(2005, blockposition1, 0);
                }

                return EnumInteractionResult.a(world.isClientSide);
            } else {
                return EnumInteractionResult.PASS;
            }
        }
    }

    public static boolean a(ItemStack itemstack, World world, BlockPosition blockposition) {
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.getBlock() instanceof IBlockFragilePlantElement) {
            IBlockFragilePlantElement iblockfragileplantelement = (IBlockFragilePlantElement) iblockdata.getBlock();

            if (iblockfragileplantelement.a(world, blockposition, iblockdata, world.isClientSide)) {
                if (world instanceof WorldServer) {
                    if (iblockfragileplantelement.a(world, world.random, blockposition, iblockdata)) {
                        iblockfragileplantelement.a((WorldServer) world, world.random, blockposition, iblockdata);
                    }

                    itemstack.subtract(1);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean a(ItemStack itemstack, World world, BlockPosition blockposition, @Nullable EnumDirection enumdirection) {
        if (world.getType(blockposition).a(Blocks.WATER) && world.getFluid(blockposition).e() == 8) {
            if (!(world instanceof WorldServer)) {
                return true;
            } else {
                int i = 0;

                while (i < 128) {
                    BlockPosition blockposition1 = blockposition;
                    IBlockData iblockdata = Blocks.SEAGRASS.getBlockData();
                    int j = 0;

                    while (true) {
                        if (j < i / 16) {
                            blockposition1 = blockposition1.b(ItemBoneMeal.RANDOM.nextInt(3) - 1, (ItemBoneMeal.RANDOM.nextInt(3) - 1) * ItemBoneMeal.RANDOM.nextInt(3) / 2, ItemBoneMeal.RANDOM.nextInt(3) - 1);
                            if (!world.getType(blockposition1).r(world, blockposition1)) {
                                ++j;
                                continue;
                            }
                        } else {
                            Optional<ResourceKey<BiomeBase>> optional = world.i(blockposition1);

                            if (Objects.equals(optional, Optional.of(Biomes.WARM_OCEAN)) || Objects.equals(optional, Optional.of(Biomes.DEEP_WARM_OCEAN))) {
                                if (i == 0 && enumdirection != null && enumdirection.n().d()) {
                                    iblockdata = (IBlockData) ((Block) TagsBlock.WALL_CORALS.a(world.random)).getBlockData().set(BlockCoralFanWallAbstract.a, enumdirection);
                                } else if (ItemBoneMeal.RANDOM.nextInt(4) == 0) {
                                    iblockdata = ((Block) TagsBlock.UNDERWATER_BONEMEALS.a(ItemBoneMeal.RANDOM)).getBlockData();
                                }
                            }

                            if (iblockdata.getBlock().a((Tag) TagsBlock.WALL_CORALS)) {
                                for (int k = 0; !iblockdata.canPlace(world, blockposition1) && k < 4; ++k) {
                                    iblockdata = (IBlockData) iblockdata.set(BlockCoralFanWallAbstract.a, EnumDirection.EnumDirectionLimit.HORIZONTAL.a(ItemBoneMeal.RANDOM));
                                }
                            }

                            if (iblockdata.canPlace(world, blockposition1)) {
                                IBlockData iblockdata1 = world.getType(blockposition1);

                                if (iblockdata1.a(Blocks.WATER) && world.getFluid(blockposition1).e() == 8) {
                                    world.setTypeAndData(blockposition1, iblockdata, 3);
                                } else if (iblockdata1.a(Blocks.SEAGRASS) && ItemBoneMeal.RANDOM.nextInt(10) == 0) {
                                    ((IBlockFragilePlantElement) Blocks.SEAGRASS).a((WorldServer) world, ItemBoneMeal.RANDOM, blockposition1, iblockdata1);
                                }
                            }
                        }

                        ++i;
                        break;
                    }
                }

                itemstack.subtract(1);
                return true;
            }
        } else {
            return false;
        }
    }
}
