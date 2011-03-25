package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class ItemInWorldManager {

    private World b;
    public EntityHuman a;
    private float c = 0.0F;
    private int d;
    private int e = 0;
    private float f = 0.0F;
    private int g;
    private int h;
    private int i;
    private int j;
    private boolean k;
    private int l;
    private int m;
    private int n;
    private int o;

    public ItemInWorldManager(World world) {
        this.b = world;
    }

    public void a() {
        ++this.j;
        if (this.k) {
            int i = this.j - this.o;
            int j = this.b.getTypeId(this.l, this.m, this.n);

            if (j != 0) {
                Block block = Block.byId[j];
                float f = block.a(this.a) * (float) (i + 1);

                if (f >= 1.0F) {
                    this.k = false;
                    this.d(this.l, this.m, this.n);
                }
            } else {
                this.k = false;
            }
        }
    }

    public void a(int i, int j, int k) {
        this.d = this.j;
        int l = this.b.getTypeId(i, j, k);

        // CraftBukkit start
        // Swings at air do *NOT* exist.
        if (l <= 0) {
            return;
        }

        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.a, Action.LEFT_CLICK_BLOCK , i, j, k, -1, this.a.inventory.b());

        if (event.useInteractedBlock() == Event.Result.DENY) {
            // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
            if (l == Block.WOODEN_DOOR.id) {
                // For some reason *BOTH* the bottom/top part have to be marked updated.
                boolean bottom = (this.b.getData(i, j, k) & 8) == 0;
                ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j, k, this.b)));
                ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, this.b)));
            }
        } else {
            Block.byId[l].b(this.b, i, j, k, this.a);
        }

        // Handle hitting a block
        float toolDamage = Block.byId[l].a(this.a);
        if (event.useItemInHand() == Event.Result.DENY) {
            // If we 'insta destroyed' then the client needs to be informed.
            if (toolDamage > 1.0f) {
                ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j, k, this.b)));
            }
            return;
        }
        BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.a, i, j, k, this.a.inventory.b(), toolDamage >= 1.0f);

        if (blockEvent.isCancelled()) {
            return;
        }

        if (blockEvent.getInstaBreak()) {
            toolDamage = 2.0f;
        }

        if (toolDamage >= 1.0F) {
            // CraftBukkit end
            this.d(i, j, k);
        } else {
            this.g = i;
            this.h = j;
            this.i = k;
        }
    }

    public void b(int i, int j, int k) {
        if (i == this.g && j == this.h && k == this.i) {
            int l = this.j - this.d;
            int i1 = this.b.getTypeId(i, j, k);

            if (i1 != 0) {
                Block block = Block.byId[i1];
                float f = block.a(this.a) * (float) (l + 1);

                if (f >= 1.0F) {
                    this.d(i, j, k);
                } else if (!this.k) {
                    this.k = true;
                    this.l = i;
                    this.m = j;
                    this.n = k;
                    this.o = this.d;
                }
            }
        // CraftBukkit start -- force blockreset to client
        } else {
            ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j, k, this.b)));
            // CraftBukkit end
        }

        this.c = 0.0F;
        this.e = 0;
    }

    public boolean c(int i, int j, int k) {
        Block block = Block.byId[this.b.getTypeId(i, j, k)];
        int l = this.b.getData(i, j, k);
        boolean flag = this.b.e(i, j, k, 0);

        if (block != null && flag) {
            block.b(this.b, i, j, k, l);
        }

        return flag;
    }

    public boolean d(int i, int j, int k) {
        // CraftBukkit start
        if (this.a instanceof EntityPlayer) {
            CraftServer server = ((WorldServer) this.b).getServer();
            org.bukkit.block.Block block = ((WorldServer) this.b).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) this.a.getBukkitEntity();

            BlockBreakEvent event = new BlockBreakEvent(block,player);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
        }
        // CraftBukkit end

        int l = this.b.getTypeId(i, j, k);
        int i1 = this.b.getData(i, j, k);
        boolean flag = this.c(i, j, k);
        ItemStack itemstack = this.a.z();

        if (itemstack != null) {
            itemstack.a(l, i, j, k);
            if (itemstack.count == 0) {
                itemstack.a(this.a);
                this.a.A();
            }
        }

        if (flag && this.a.b(Block.byId[l])) {
            Block.byId[l].a_(this.b, i, j, k, i1);
            ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j, k, this.b)));
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

        // CraftBukkit start - Interact
        boolean result = false;
        if (i1 > 0) {
            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, i, j, k, l, itemstack);
            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (i1 == Block.WOODEN_DOOR.id) {
                    boolean bottom = (world.getData(i, j, k) & 8) == 0;
                    ((EntityPlayer) entityhuman).a.b((Packet) (new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, world)));
                }
            } else {
                result = Block.byId[i1].a(world, i, j, k, entityhuman);
                if (itemstack != null && !result) {
                    result = itemstack.a(entityhuman, world, i, j, k, l);
                }
            }

            // If we have 'true' and no explicit deny *or* an explicit allow -- run the item part of the hook
            if (itemstack != null && ((!result && event.useItemInHand() != Event.Result.DENY) || event.useItemInHand() == Event.Result.ALLOW)) {
                this.a(entityhuman, world, itemstack);
            }
        }
        return result;
    }
    // CraftBukkit end
}
