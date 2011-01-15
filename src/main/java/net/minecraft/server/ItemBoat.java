package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
// CraftBukkit end

public class ItemBoat extends Item {

    public ItemBoat(int i) {
        super(i);
        bb = 1;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        float f = 1.0F;
        float f1 = entityplayer.y + (entityplayer.w - entityplayer.y) * f;
        float f2 = entityplayer.x + (entityplayer.v - entityplayer.x) * f;
        double d = entityplayer.m + (entityplayer.p - entityplayer.m) * (double) f;
        double d1 = (entityplayer.n + (entityplayer.q - entityplayer.n) * (double) f + 1.6200000000000001D) - (double) entityplayer.H;
        double d2 = entityplayer.o + (entityplayer.r - entityplayer.o) * (double) f;
        Vec3D vec3d = Vec3D.b(d, d1, d2);
        float f3 = MathHelper.b(-f2 * 0.01745329F - 3.141593F);
        float f4 = MathHelper.a(-f2 * 0.01745329F - 3.141593F);
        float f5 = -MathHelper.b(-f1 * 0.01745329F);
        float f6 = MathHelper.a(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3D vec3d1 = vec3d.c((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);
        MovingObjectPosition movingobjectposition = world.a(vec3d, vec3d1, true);

        if (movingobjectposition == null) {
            return itemstack;
        }
        if (movingobjectposition.a == EnumMovingObjectType.a) {
            int i = movingobjectposition.b;
            int j = movingobjectposition.c;
            int k = movingobjectposition.d;

            if (!world.z) {
                // CraftBukkit start - Boat placement
                CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
                CraftItemStack itemInHand = new CraftItemStack(itemstack);
                CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
                PlayerItemEvent pie = new PlayerItemEvent(Type.PLAYER_ITEM, thePlayer, itemInHand, blockClicked, CraftBlock.notchToBlockFace(movingobjectposition.e));

                ((WorldServer) world).getServer().getPluginManager().callEvent(pie);

                if (pie.isCancelled()) return itemstack;
                // CraftBukkit end

                world.a(((Entity) (new EntityBoat(world, (float) i + 0.5F, (float) j + 1.5F, (float) k + 0.5F))));
            }
            itemstack.a--;
        }
        return itemstack;
    }
}
