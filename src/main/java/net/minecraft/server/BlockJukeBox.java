package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockJukeBox extends Block {

    protected BlockJukeBox(int i, int j) {
        super(i, j, Material.WOOD);
    }

    public int a(int i) {
        return this.textureId + (i == 1 ? 1 : 0);
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        int l = world.getData(i, j, k);

        if (l > 0) {
            // CraftBukkit start - Interact Jukebox
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

            BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            this.f(world, i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public void f(World world, int i, int j, int k, int l) {
        world.a((String) null, i, j, k);
        world.c(i, j, k, 0);
        int i1 = Item.GOLD_RECORD.id + l - 1;
        float f = 0.7F;
        double d0 = (double) (world.l.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
        double d1 = (double) (world.l.nextFloat() * f) + (double) (1.0F - f) * 0.2D + 0.6D;
        double d2 = (double) (world.l.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double) i + d0, (double) j + d1, (double) k + d2, new ItemStack(i1, 1, 0));

        entityitem.c = 10;
        world.a((Entity) entityitem);
    }

    public void a(World world, int i, int j, int k, int l, float f) {
        if (!world.isStatic) {
            if (l > 0) {
                this.f(world, i, j, k, l);
            }

            super.a(world, i, j, k, l, f);
        }
    }
}
