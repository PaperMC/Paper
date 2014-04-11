package net.minecraft.server;

import java.util.UUID;

import net.minecraft.util.com.mojang.authlib.GameProfile;

public class ItemSkull extends Item {

    private static final String[] b = new String[] { "skeleton", "wither", "zombie", "char", "creeper"};
    public static final String[] a = new String[] { "skeleton", "wither", "zombie", "steve", "creeper"};

    public ItemSkull() {
        this.a(CreativeModeTab.c);
        this.setMaxDurability(0);
        this.a(true);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (l == 0) {
            return false;
        } else if (!world.getType(i, j, k).getMaterial().isBuildable()) {
            return false;
        } else {
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

            if (!world.isStatic) {
                // CraftBukkit start - Handle in ItemBlock
                // world.setTypeAndData(i, j, k, Blocks.SKULL, l, 2);
                if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, Blocks.SKULL, l, clickedX, clickedY, clickedZ)) {
                    return false;
                }
                l = world.getData(i, j, k);
                // CraftBukkit end
                int i1 = 0;

                if (l == 1) {
                    i1 = MathHelper.floor((double) (entityhuman.yaw * 16.0F / 360.0F) + 0.5D) & 15;
                }

                TileEntity tileentity = world.getTileEntity(i, j, k);

                if (tileentity != null && tileentity instanceof TileEntitySkull) {
                    if (itemstack.getData() == 3) {
                        GameProfile gameprofile = null;

                        if (itemstack.hasTag()) {
                            NBTTagCompound nbttagcompound = itemstack.getTag();

                            if (nbttagcompound.hasKeyOfType("SkullOwner", 10)) {
                                gameprofile = GameProfileSerializer.a(nbttagcompound.getCompound("SkullOwner"));
                            } else if (nbttagcompound.hasKeyOfType("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0) {
                                gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
                            }
                        }

                        ((TileEntitySkull) tileentity).setGameProfile(gameprofile);
                    } else {
                        ((TileEntitySkull) tileentity).setSkullType(itemstack.getData());
                    }

                    ((TileEntitySkull) tileentity).setRotation(i1);
                    ((BlockSkull) Blocks.SKULL).a(world, i, j, k, (TileEntitySkull) tileentity);
                }

                --itemstack.count;
            }

            return true;
        }
    }

    public int filterData(int i) {
        return i;
    }

    public String a(ItemStack itemstack) {
        int i = itemstack.getData();

        if (i < 0 || i >= b.length) {
            i = 0;
        }

        return super.getName() + "." + b[i];
    }

    public String n(ItemStack itemstack) {
        if (itemstack.getData() == 3 && itemstack.hasTag()) {
            if (itemstack.getTag().hasKeyOfType("SkullOwner", 10)) {
                return LocaleI18n.get("item.skull.player.name", new Object[] { GameProfileSerializer.a(itemstack.getTag().getCompound("SkullOwner")).getName()});
            }

            if (itemstack.getTag().hasKeyOfType("SkullOwner", 8)) {
                return LocaleI18n.get("item.skull.player.name", new Object[] { itemstack.getTag().getString("SkullOwner")});
            }
        }

        return super.n(itemstack);
    }
}
