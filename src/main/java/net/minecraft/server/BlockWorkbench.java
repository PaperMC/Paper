package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockWorkbench extends Block {

    protected BlockWorkbench(int i) {
        super(i, Material.WOOD);
        this.textureId = 59;
    }

    public int a(int i) {
        return i == 1 ? this.textureId - 16 : (i == 0 ? Block.WOOD.a(0) : (i != 2 && i != 4 ? this.textureId : this.textureId + 1));
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            // CraftBukkit start - Interact Workbench
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

            entityhuman.a(i, j, k);
            return true;
        }
    }
}
