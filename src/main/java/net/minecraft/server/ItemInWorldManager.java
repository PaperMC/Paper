package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class ItemInWorldManager {

    public World world;
    public EntityHuman player;
    private int c = -1;
    private float d = 0.0F;
    private int lastDigTick;
    private int f;
    private int g;
    private int h;
    private int currentTick;
    private boolean j;
    private int k;
    private int l;
    private int m;
    private int n;

    public ItemInWorldManager(World world) {
        this.world = world;
    }

    // CraftBukkit start - keep this for backwards compatibility
    public ItemInWorldManager(WorldServer world) {
        this.world = world;
    }
    // CraftBukkit end

    public void setGameMode(int i) {
        this.c = i;
        if (i == 0) {
            this.player.abilities.canFly = false;
            this.player.abilities.isFlying = false;
            this.player.abilities.canInstantlyBuild = false;
            this.player.abilities.isInvulnerable = false;
        } else {
            this.player.abilities.canFly = true;
            this.player.abilities.canInstantlyBuild = true;
            this.player.abilities.isInvulnerable = true;
        }
    }

    public int getGameMode() {
        return this.c;
    }

    public boolean b() {
        return this.c == 1;
    }

    public void b(int i) {
        if (this.c == -1) {
            this.c = i;
        }

        this.setGameMode(this.c);
    }

    public void c() {
        this.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
        if (this.j) {
            int i = this.currentTick - this.n;
            int j = this.world.getTypeId(this.k, this.l, this.m);

            if (j != 0) {
                Block block = Block.byId[j];
                float f = block.getDamage(this.player) * (float) (i + 1);

                if (f >= 1.0F) {
                    this.j = false;
                    this.c(this.k, this.l, this.m);
                }
            } else {
                this.j = false;
            }
        }
    }

    public void dig(int i, int j, int k, int l) {
        // this.world.douseFire((EntityHuman) null, i, j, k, l); // CraftBukkit - moved down
        // CraftBukkit start
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, l, this.player.inventory.getItemInHand());

        if (this.b()) {
            if (event.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
                return;
            }
            this.c(i, j, k);
        } else {
            this.lastDigTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
            int i1 = this.world.getTypeId(i, j, k);

            // Swings at air do *NOT* exist.
            if (i1 <= 0) {
                return;
            }

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
                this.world.douseFire((EntityHuman) null, i, j, k, l);
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
                // Let the client know the block still exists
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
                return;
            }

            if (blockEvent.getInstaBreak()) {
                toolDamage = 2.0f;
            }

            if (toolDamage >= 1.0F) {
                // CraftBukkit end
                this.c(i, j, k);
            } else {
                this.f = i;
                this.g = j;
                this.h = k;
            }
        }
    }

    public void a(int i, int j, int k) {
        if (i == this.f && j == this.g && k == this.h) {
            this.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
            int l = this.currentTick - this.lastDigTick;
            int i1 = this.world.getTypeId(i, j, k);

            if (i1 != 0) {
                Block block = Block.byId[i1];
                float f = block.getDamage(this.player) * (float) (l + 1);

                if (f >= 0.7F) {
                    this.c(i, j, k);
                } else if (!this.j) {
                    this.j = true;
                    this.k = i;
                    this.l = j;
                    this.m = k;
                    this.n = this.lastDigTick;
                }
            }
        // CraftBukkit start - force blockreset to client
        } else {
            ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
            // CraftBukkit end
        }

        this.d = 0.0F;
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
            org.bukkit.block.Block block = this.world.getWorld().getBlockAt(i, j, k);

            BlockBreakEvent event = new BlockBreakEvent(block, (org.bukkit.entity.Player) this.player.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
                return false;
            }
        }
        // CraftBukkit end

        int l = this.world.getTypeId(i, j, k);
        int i1 = this.world.getData(i, j, k);

        this.world.a(this.player, 2001, i, j, k, l + this.world.getData(i, j, k) * 256);
        boolean flag = this.b(i, j, k);

        if (this.b()) {
            ((EntityPlayer) this.player).netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.world));
        } else {
            ItemStack itemstack = this.player.P();
            boolean flag1 = this.player.b(Block.byId[l]);

            if (itemstack != null) {
                itemstack.a(l, i, j, k, this.player);
                if (itemstack.count == 0) {
                    itemstack.a(this.player);
                    this.player.Q();
                }
            }

            if (flag && flag1) {
                Block.byId[l].a(this.world, this.player, i, j, k, i1);
            }
        }

        return flag;
    }

    public boolean useItem(EntityHuman entityhuman, World world, ItemStack itemstack) {
        int i = itemstack.count;
        int j = itemstack.getData();
        ItemStack itemstack1 = itemstack.a(world, entityhuman);

        if (itemstack1 == itemstack && (itemstack1 == null || itemstack1.count == i) && (itemstack1 == null || itemstack1.l() <= 0)) {
            return false;
        } else {
            entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = itemstack1;
            if (this.b()) {
                itemstack1.count = i;
                itemstack1.setData(j);
            }

            if (itemstack1.count == 0) {
                entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
            }

            return true;
        }
    }

    // TODO: Review this code, it changed in 1.8 but I'm not sure if we need to update or not
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
                int j1 = itemstack.getData();
                int k1 = itemstack.count;

                result = itemstack.placeItem(entityhuman, world, i, j, k, l);

                // The item count should not decrement in Creative mode.
                if (this.b()) {
                    itemstack.setData(j1);
                    itemstack.count = k1;
                }
            }

            // If we have 'true' and no explicit deny *or* an explicit allow -- run the item part of the hook
            if (itemstack != null && ((!result && event.useItemInHand() != Event.Result.DENY) || event.useItemInHand() == Event.Result.ALLOW)) {
                this.useItem(entityhuman, world, itemstack);
            }
        }
        return result;
        // CraftBukkit end
    }

    public void a(WorldServer worldserver) {
        this.world = worldserver;
    }
}
