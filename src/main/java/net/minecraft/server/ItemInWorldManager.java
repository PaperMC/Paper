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

    private WorldServer world;
    public EntityHuman player;
    private float c = 0.0F;
    private int d;
    private int e;
    private int f;
    private int g;
    private int currentTick;
    private boolean i;
    private int j;
    private int k;
    private int l;
    private int m;

    public ItemInWorldManager(WorldServer worldserver) {
        this.world = worldserver;
    }

    public void a() {
        this.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
        if (this.i) {
            int i = this.currentTick - this.m;
            int j = this.world.getTypeId(this.j, this.k, this.l);

            if (j != 0) {
                Block block = Block.byId[j];
                float f = block.getDamage(this.player) * (float) (i + 1);

                if (f >= 1.0F) {
                    this.i = false;
                    this.c(this.j, this.k, this.l);
                }
            } else {
                this.i = false;
            }
        }
    }

    public void dig(int i, int j, int k, int l) {
        this.d = (int) (System.currentTimeMillis() / 50); // CraftBukkit
        int i1 = this.world.getTypeId(i, j, k);

        // CraftBukkit start
        // Swings at air do *NOT* exist.
        if (i1 <= 0) {
            return;
        }

        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK , i, j, k, l, this.player.inventory.getItemInHand());

        if (event.useInteractedBlock() == Event.Result.DENY) {
            // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
            if (i1 == Block.WOODEN_DOOR.id) {
                // For some reason *BOTH* the bottom/top part have to be marked updated.
                boolean bottom = (this.world.getData(i, j, k) & 8) == 0;
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, this.world));
            } else if (i1 == Block.TRAP_DOOR.id) {
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
            }
        } else {
            Block.byId[i1].b(this.world, i, j, k, this.player);
            // Allow fire punching to be blocked
            this.world.a((EntityHuman) null, i, j, k, l);
        }

        // Handle hitting a block
        float toolDamage = Block.byId[i1].getDamage(this.player);
        if (event.useItemInHand() == Event.Result.DENY) {
            // If we 'insta destroyed' then the client needs to be informed.
            if (toolDamage > 1.0f) {
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
            }
            return;
        }
        BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, i, j, k, this.player.inventory.getItemInHand(), toolDamage >= 1.0f);

        if (blockEvent.isCancelled()) {
            return;
        }

        if (blockEvent.getInstaBreak()) {
            toolDamage = 2.0f;
        }

        if (toolDamage >= 1.0F) {
            // CraftBukkit end
            this.c(i, j, k);
        } else {
            this.e = i;
            this.f = j;
            this.g = k;
        }
    }

    public void a(int i, int j, int k) {
        if (i == this.e && j == this.f && k == this.g) {
            this.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
            int l = this.currentTick - this.d;
            int i1 = this.world.getTypeId(i, j, k);

            if (i1 != 0) {
                Block block = Block.byId[i1];
                float f = block.getDamage(this.player) * (float) (l + 1);

                if (f >= 0.7F) {
                    this.c(i, j, k);
                } else if (!this.i) {
                    this.i = true;
                    this.j = i;
                    this.k = j;
                    this.l = k;
                    this.m = this.d;
                }
            }
        // CraftBukkit start - force blockreset to client
        } else {
            ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
            // CraftBukkit end
        }

        this.c = 0.0F;
    }

    public boolean b(int i, int j, int k) {
        Block block = Block.byId[this.world.getTypeId(i, j, k)];
        int l = this.world.getData(i, j, k);
        boolean flag = this.world.setTypeId(i, j, k, 0);

        if (block != null && flag) {
            block.postBreak(this.world, i, j, k, l);
        }

        return flag;
    }

    public boolean c(int i, int j, int k) {
        // CraftBukkit start
        if (this.player instanceof EntityPlayer) {
            CraftServer server = ((WorldServer) this.world).getServer();
            org.bukkit.block.Block block = ((WorldServer) this.world).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) this.player.getBukkitEntity();

            BlockBreakEvent event = new BlockBreakEvent(block,player);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
        }
        // CraftBukkit end

        int l = this.world.getTypeId(i, j, k);
        int i1 = this.world.getData(i, j, k);

        this.world.a(this.player, 2001, i, j, k, l + this.world.getData(i, j, k) * 256);
        boolean flag = this.b(i, j, k);
        ItemStack itemstack = this.player.F();

        if (itemstack != null) {
            itemstack.a(l, i, j, k, this.player);
            if (itemstack.count == 0) {
                itemstack.a(this.player);
                this.player.G();
            }
        }

        if (flag && this.player.b(Block.byId[l])) {
            Block.byId[l].a(this.world, this.player, i, j, k, i1);
            ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
        }

        return flag;
    }

    public boolean useItem(EntityHuman entityhuman, World world, ItemStack itemstack) {
        int i = itemstack.count;
        ItemStack itemstack1 = itemstack.a(world, entityhuman);

        if (itemstack1 == itemstack && (itemstack1 == null || itemstack1.count == i)) {
            return false;
        } else {
            entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = itemstack1;
            if (itemstack1.count == 0) {
                entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
            }

            return true;
        }
    }

    public boolean interact(EntityHuman entityhuman, World world, ItemStack itemstack, int i, int j, int k, int l) {
        int i1 = world.getTypeId(i, j, k);

        // CraftBukkit start - Interact
        boolean result = false;
        if (i1 > 0) {
            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, i, j, k, l, itemstack);
            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (i1 == Block.WOODEN_DOOR.id) {
                    boolean bottom = (world.getData(i, j, k) & 8) == 0;
                    ((EntityPlayer) entityhuman).netServerHandler.sendPacket(new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, world));
                }
                result = (event.useItemInHand() != Event.Result.ALLOW);
            } else {
                result = Block.byId[i1].interact(world, i, j, k, entityhuman);
            }

            if (itemstack != null && !result) {
                result = itemstack.placeItem(entityhuman, world, i, j, k, l);
            }

            // If we have 'true' and no explicit deny *or* an explicit allow -- run the item part of the hook
            if (itemstack != null && ((!result && event.useItemInHand() != Event.Result.DENY) || event.useItemInHand() == Event.Result.ALLOW)) {
                this.useItem(entityhuman, world, itemstack);
            }
        }
        return result;
        // CraftBukkit end
    }
}
