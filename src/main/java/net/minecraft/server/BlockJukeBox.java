package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockJukeBox extends Block {

    protected BlockJukeBox(int i, int j) {
        super(i, j, Material.c);
    }

    public int a(int i) {
        return bh + (i != 1 ? 0 : 1);
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        int l = world.b(i, j, k);

        if (l > 0) {
            // CraftBukkit start - Interact Jukebox
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);

            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

            if (bie.isCancelled()) return true;
            // CraftBukkit end

            f(world, i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public void f(World world, int i, int j, int k, int l) {
        world.a(((String) (null)), i, j, k);
        world.c(i, j, k, 0);
        int i1 = (Item.aY.ba + l) - 1;
        float f1 = 0.7F;
        double d = (double) (world.l.nextFloat() * f1) + (double) (1.0F - f1) * 0.5D;
        double d1 = (double) (world.l.nextFloat() * f1) + (double) (1.0F - f1) * 0.20000000000000001D + 0.59999999999999998D;
        double d2 = (double) (world.l.nextFloat() * f1) + (double) (1.0F - f1) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double) i + d, (double) j + d1, (double) k + d2, new ItemStack(i1, 1, 0));

        entityitem.c = 10;
        world.a(((Entity) (entityitem)));
    }

    public void a(World world, int i, int j, int k, int l, float f1) {
        if (world.z) {
            return;
        }
        if (l > 0) {
            f(world, i, j, k, l);
        }
        super.a(world, i, j, k, l, f1);
    }
}
