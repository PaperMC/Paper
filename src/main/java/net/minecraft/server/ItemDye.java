package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.entity.SheepDyeWoolEvent;
// CraftBukkit end

public class ItemDye extends Item {

    public static final String[] a = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final String[] b = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white"};
    public static final int[] c = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye() {
        this.a(true);
        this.setMaxDurability(0);
        this.a(CreativeModeTab.l);
    }

    public String a(ItemStack itemstack) {
        int i = MathHelper.a(itemstack.getData(), 0, 15);

        return super.getName() + "." + a[i];
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            if (itemstack.getData() == 15) {
                if (a(itemstack, world, i, j, k, entityhuman)) { // CraftBukkit - pass entity for StructureGrowEvent
                    if (!world.isStatic) {
                        world.triggerEffect(2005, i, j, k, 0);
                    }

                    return true;
                }
            } else if (itemstack.getData() == 3) {
                Block block = world.getType(i, j, k);
                int i1 = world.getData(i, j, k);

                if (block == Blocks.LOG && BlockLogAbstract.c(i1) == 3) {
                    if (l == 0) {
                        return false;
                    }

                    if (l == 1) {
                        return false;
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

                    if (world.isEmpty(i, j, k)) {
                        int j1 = Blocks.COCOA.getPlacedData(world, i, j, k, l, f, f1, f2, 0);

                        world.setTypeAndData(i, j, k, Blocks.COCOA, j1, 2);
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean a(ItemStack itemstack, World world, int i, int j, int k) {
        // CraftBukkit start
        return a(itemstack, world, i, j, k, null);
    }

    public static boolean a(ItemStack itemstack, World world, int i, int j, int k, EntityHuman entityhuman) {
        // CraftBukkit end
        Block block = world.getType(i, j, k);

        if (block instanceof IBlockFragilePlantElement) {
            IBlockFragilePlantElement iblockfragileplantelement = (IBlockFragilePlantElement) block;

            if (iblockfragileplantelement.a(world, i, j, k, world.isStatic)) {
                if (!world.isStatic) {
                    if (iblockfragileplantelement.a(world, world.random, i, j, k)) {
                        // CraftBukkit start - Special case BlockSapling and BlockMushroom to use our methods
                        if (block instanceof BlockSapling) {
                            Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                            ((BlockSapling) block).grow(world, i, j, k, world.random, true, player, null);
                        } else if (block instanceof BlockMushroom) {
                            Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                            ((BlockMushroom) block).grow(world, i, j, k, world.random, true, player, null);
                        } else {
                            iblockfragileplantelement.b(world, world.random, i, j, k);
                        }
                        // CraftBukkit end

                    }

                    --itemstack.count;
                }

                return true;
            }
        }

        return false;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            int i = BlockCloth.b(itemstack.getData());

            if (!entitysheep.isSheared() && entitysheep.getColor() != i) {
                // CraftBukkit start
                byte bColor = (byte) i;
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByData(bColor));
                entitysheep.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                i = (byte) event.getColor().getWoolData();
                // CraftBukkit end
                entitysheep.setColor(i);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }
}
