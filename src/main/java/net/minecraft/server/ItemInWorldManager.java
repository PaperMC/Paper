package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.block.BlockBreakEvent;
// CraftBukkit end

public class ItemInWorldManager {

    private World b;
    public EntityHuman a;
    private float c;
    public float d = 0.0F; // CraftBukkit private -> public
    private int e = 0;
    private float f = 0.0F;
    private int g;
    private int h;
    private int i;

    public ItemInWorldManager(World world) {
        this.b = world;
    }

    public void a(int i, int j, int k) {
        int l = this.b.getTypeId(i, j, k);

        if (l > 0 && this.d == 0.0F) {
            Block.byId[l].b(this.b, i, j, k, this.a);
        }

        if (l > 0 && Block.byId[l].a(this.a) >= 1.0F) {
            this.c(i, j, k);
        }
    }

    public void a() {
        this.d = 0.0F;
        this.e = 0;
    }

    public void a(int i, int j, int k, int l) {
        if (this.e > 0) {
            --this.e;
        } else {
            if (i == this.g && j == this.h && k == this.i) {
                int i1 = this.b.getTypeId(i, j, k);

                if (i1 == 0) {
                    return;
                }

                Block block = Block.byId[i1];

                this.d += block.a(this.a);
                ++this.f;
                if (this.d >= 1.0F) {
                    this.c(i, j, k);
                    this.d = 0.0F;
                    this.c = 0.0F;
                    this.f = 0.0F;
                    this.e = 5;
                }
            } else {
                this.d = 0.0F;
                this.c = 0.0F;
                this.f = 0.0F;
                this.g = i;
                this.h = j;
                this.i = k;
            }
        }
    }

    public boolean b(int i, int j, int k) {
        Block block = Block.byId[this.b.getTypeId(i, j, k)];
        int l = this.b.getData(i, j, k);
        boolean flag = this.b.e(i, j, k, 0);

        if (block != null && flag) {
            block.a(this.b, i, j, k, l);
        }

        return flag;
    }

    public boolean c(int i, int j, int k) {
        //CraftBukkit start
        if (this.a instanceof EntityPlayer){
            CraftServer server = ((WorldServer) this.b).getServer();
            org.bukkit.block.Block block = ((WorldServer) this.b).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) this.a.getBukkitEntity();

            BlockBreakEvent event = new BlockBreakEvent(block,player);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()){
                return true;
            }
        }
        //CraftBukkit end

        int l = this.b.getTypeId(i, j, k);
        int i1 = this.b.getData(i, j, k);
        boolean flag = this.b(i, j, k);
        ItemStack itemstack = this.a.P();

        if (itemstack != null) {
            itemstack.a(l, i, j, k);
            if (itemstack.count == 0) {
                itemstack.a(this.a);
                this.a.Q();
            }
        }

        if (flag && this.a.b(Block.byId[l])) {
            Block.byId[l].g(this.b, i, j, k, i1);
        }

        return flag;
    }

    public boolean a(EntityHuman entityhuman, World world, ItemStack itemstack) {
        int i = itemstack.count;
        ItemStack itemstack1 = itemstack.a(world, entityhuman);

        if (itemstack1 == itemstack && (itemstack1 == null || itemstack1.count == i)) {
            return false;
        } else {
            entityhuman.inventory.a[entityhuman.inventory.c] = itemstack1;
            if (itemstack1.count == 0) {
                entityhuman.inventory.a[entityhuman.inventory.c] = null;
            }

            return true;
        }
    }

    public boolean a(EntityHuman entityhuman, World world, ItemStack itemstack, int i, int j, int k, int l) {
        int i1 = world.getTypeId(i, j, k);

        return i1 > 0 && Block.byId[i1].a(world, i, j, k, entityhuman) ? true : (itemstack == null ? false : itemstack.a(entityhuman, world, i, j, k, l));
    }
}
