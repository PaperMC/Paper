package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemSkull extends Item {

    private static final String[] a = new String[] { "skeleton", "wither", "zombie", "char", "creeper"};
    private static final int[] b = new int[] { 224, 225, 226, 227, 228};

    public ItemSkull(int i) {
        super(i);
        this.a(CreativeModeTab.c);
        this.setMaxDurability(0);
        this.a(true);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l == 0) {
            return false;
        } else if (!world.getMaterial(i, j, k).isBuildable()) {
            return false;
        } else {
            int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }

            if (!entityhuman.a(i, j, k, l, itemstack)) {
                return false;
            } else if (!Block.SKULL.canPlace(world, i, j, k)) {
                return false;
            } else {
                CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                world.setTypeIdAndData(i, j, k, Block.SKULL.id, l);
                int i1 = 0;

                if (l == 1) {
                    i1 = MathHelper.floor((double) (entityhuman.yaw * 16.0F / 360.0F) + 0.5D) & 15;
                }

                TileEntity tileentity = world.getTileEntity(i, j, k);

                if (tileentity != null && tileentity instanceof TileEntitySkull) {
                    String s = "";

                    if (itemstack.hasTag() && itemstack.getTag().hasKey("SkullOwner")) {
                        s = itemstack.getTag().getString("SkullOwner");
                    }

                    ((TileEntitySkull) tileentity).a(itemstack.getData(), s);
                    ((TileEntitySkull) tileentity).a(i1);
                    ((BlockSkull) Block.SKULL).a(world, i, j, k, (TileEntitySkull) tileentity);
                }

                // CraftBukkit start
                org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }
                // CraftBukkit end

                --itemstack.count;
                return true;
            }
        }
    }

    public int filterData(int i) {
        return i;
    }

    public String c_(ItemStack itemstack) {
        int i = itemstack.getData();

        if (i < 0 || i >= a.length) {
            i = 0;
        }

        return super.getName() + "." + a[i];
    }

    public String j(ItemStack itemstack) {
        return itemstack.getData() == 3 && itemstack.hasTag() && itemstack.getTag().hasKey("SkullOwner") ? LocaleI18n.get("item.skull.player.name", new Object[] { itemstack.getTag().getString("SkullOwner")}) : super.j(itemstack);
    }
}
