package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class PlayerInteractManager {

    public World world;
    public EntityPlayer player;
    private EnumGamemode gamemode;
    private boolean d;
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
    private int o;

    public PlayerInteractManager(World world) {
        this.gamemode = EnumGamemode.NONE;
        this.o = -1;
        this.world = world;
    }

    public void setGameMode(EnumGamemode enumgamemode) {
        this.gamemode = enumgamemode;
        enumgamemode.a(this.player.abilities);
        this.player.updateAbilities();
    }

    public EnumGamemode getGameMode() {
        return this.gamemode;
    }

    public boolean isCreative() {
        return this.gamemode.d();
    }

    public void b(EnumGamemode enumgamemode) {
        if (this.gamemode == EnumGamemode.NONE) {
            this.gamemode = enumgamemode;
        }

        this.setGameMode(this.gamemode);
    }

    public void a() {
        this.currentTick = MinecraftServer.currentTick; // CraftBukkit
        float f;
        int i;

        if (this.j) {
            int j = this.currentTick - this.n;
            Block block = this.world.getType(this.k, this.l, this.m);

            if (block.getMaterial() == Material.AIR) {
                this.j = false;
            } else {
                f = block.getDamage(this.player, this.player.world, this.k, this.l, this.m) * (float) (j + 1);
                i = (int) (f * 10.0F);
                if (i != this.o) {
                    this.world.d(this.player.getId(), this.k, this.l, this.m, i);
                    this.o = i;
                }

                if (f >= 1.0F) {
                    this.j = false;
                    this.breakBlock(this.k, this.l, this.m);
                }
            }
        } else if (this.d) {
            Block block1 = this.world.getType(this.f, this.g, this.h);

            if (block1.getMaterial() == Material.AIR) {
                this.world.d(this.player.getId(), this.f, this.g, this.h, -1);
                this.o = -1;
                this.d = false;
            } else {
                int k = this.currentTick - this.lastDigTick;

                f = block1.getDamage(this.player, this.player.world, this.f, this.g, this.h) * (float) (k + 1);
                i = (int) (f * 10.0F);
                if (i != this.o) {
                    this.world.d(this.player.getId(), this.f, this.g, this.h, i);
                    this.o = i;
                }
            }
        }
    }

    public void dig(int i, int j, int k, int l) {
        // CraftBukkit start
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, l, this.player.inventory.getItemInHand());
        if (!this.gamemode.isAdventure() || this.player.d(i, j, k)) {
            if (event.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                // Update any tile entity data for this block
                TileEntity tileentity = this.world.getTileEntity(i, j, k);
                if (tileentity != null) {
                    this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                }
                return;
            }
            // CraftBukkit end
            if (this.isCreative()) {
                if (!this.world.douseFire((EntityHuman) null, i, j, k, l)) {
                    this.breakBlock(i, j, k);
                }
            } else {
                // this.world.douseFire((EntityHuman) null, i, j, k, l); // CraftBukkit - Moved down
                this.lastDigTick = this.currentTick;
                float f = 1.0F;
                Block block = this.world.getType(i, j, k);
                // CraftBukkit start - Swings at air do *NOT* exist.
                if (event.useInteractedBlock() == Event.Result.DENY) {
                    // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                    if (block == Blocks.WOODEN_DOOR) {
                        // For some reason *BOTH* the bottom/top part have to be marked updated.
                        boolean bottom = (this.world.getData(i, j, k) & 8) == 0;
                        ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                        ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j + (bottom ? 1 : -1), k, this.world));
                    } else if (block == Blocks.TRAP_DOOR) {
                        ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                    }
                } else if (block.getMaterial() != Material.AIR) {
                    block.attack(this.world, i, j, k, this.player);
                    f = block.getDamage(this.player, this.player.world, i, j, k);
                    // Allow fire punching to be blocked
                    this.world.douseFire((EntityHuman) null, i, j, k, l);
                }

                if (event.useItemInHand() == Event.Result.DENY) {
                    // If we 'insta destroyed' then the client needs to be informed.
                    if (f > 1.0f) {
                        ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                    }
                    return;
                }
                org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, i, j, k, this.player.inventory.getItemInHand(), f >= 1.0f);

                if (blockEvent.isCancelled()) {
                    // Let the client know the block still exists
                    ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                    return;
                }

                if (blockEvent.getInstaBreak()) {
                    f = 2.0f;
                }
                // CraftBukkit end

                if (block.getMaterial() != Material.AIR && f >= 1.0F) {
                    this.breakBlock(i, j, k);
                } else {
                    this.d = true;
                    this.f = i;
                    this.g = j;
                    this.h = k;
                    int i1 = (int) (f * 10.0F);

                    this.world.d(this.player.getId(), i, j, k, i1);
                    this.o = i1;
                }
            }
        }
    }

    public void a(int i, int j, int k) {
        if (i == this.f && j == this.g && k == this.h) {
            this.currentTick = MinecraftServer.currentTick; // CraftBukkit
            int l = this.currentTick - this.lastDigTick;
            Block block = this.world.getType(i, j, k);

            if (block.getMaterial() != Material.AIR) {
                float f = block.getDamage(this.player, this.player.world, i, j, k) * (float) (l + 1);

                if (f >= 0.7F) {
                    this.d = false;
                    this.world.d(this.player.getId(), i, j, k, -1);
                    this.breakBlock(i, j, k);
                } else if (!this.j) {
                    this.d = false;
                    this.j = true;
                    this.k = i;
                    this.l = j;
                    this.m = k;
                    this.n = this.lastDigTick;
                }
            }
        // CraftBukkit start - Force block reset to client
        } else {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
            // CraftBukkit end
        }
    }

    public void c(int i, int j, int k) {
        this.d = false;
        this.world.d(this.player.getId(), this.f, this.g, this.h, -1);
    }

    private boolean d(int i, int j, int k) {
        Block block = this.world.getType(i, j, k);
        int l = this.world.getData(i, j, k);

        block.a(this.world, i, j, k, l, this.player);
        boolean flag = this.world.setAir(i, j, k);

        if (flag) {
            block.postBreak(this.world, i, j, k, l);
        }

        return flag;
    }

    public boolean breakBlock(int i, int j, int k) {
        // CraftBukkit start
        BlockBreakEvent event = null;

        if (this.player instanceof EntityPlayer) {
            org.bukkit.block.Block block = this.world.getWorld().getBlockAt(i, j, k);

            // Tell client the block is gone immediately then process events
            if (world.getTileEntity(i, j, k) == null) {
                PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(i, j, k, this.world);
                packet.block = Blocks.AIR;
                packet.data = 0;
                ((EntityPlayer) this.player).playerConnection.sendPacket(packet);
            }

            event = new BlockBreakEvent(block, this.player.getBukkitEntity());

            // Adventure mode pre-cancel
            event.setCancelled(this.gamemode.isAdventure() && !this.player.d(i, j, k));

            // Sword + Creative mode pre-cancel
            event.setCancelled(event.isCancelled() || (this.gamemode.d() && this.player.be() != null && this.player.be().getItem() instanceof ItemSword));

            // Calculate default block experience
            Block nmsBlock = this.world.getType(i, j, k);

            if (nmsBlock != null && !event.isCancelled() && !this.isCreative() && this.player.a(nmsBlock)) {
                // Copied from block.a(world, entityhuman, int, int, int, int)
                if (!(nmsBlock.E() && EnchantmentManager.hasSilkTouchEnchantment(this.player))) {
                    int data = block.getData();
                    int bonusLevel = EnchantmentManager.getBonusBlockLootEnchantmentLevel(this.player);

                    event.setExpToDrop(nmsBlock.getExpDrop(this.world, data, bonusLevel));
                }
            }

            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayer) this.player).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
                // Update any tile entity data for this block
                TileEntity tileentity = this.world.getTileEntity(i, j, k);
                if (tileentity != null) {
                    this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                }
                return false;
            }
        }

        if (false && this.gamemode.isAdventure() && !this.player.d(i, j, k)) { // Never trigger
            // CraftBukkit end
            return false;
        } else if (false && this.gamemode.d() && this.player.be() != null && this.player.be().getItem() instanceof ItemSword) { // CraftBukkit - never trigger
            return false;
        } else {
            Block block = this.world.getType(i, j, k);
            if (block == Blocks.AIR) return false; // CraftBukkit - A plugin set block to air without cancelling
            int l = this.world.getData(i, j, k);

            // CraftBukkit start - Special case skulls, their item data comes from a tile entity
            if (block == Blocks.SKULL && !this.isCreative()) {
                block.dropNaturally(world, i, j, k, l, 1.0F, 0);
                return this.d(i, j, k);
            }
            // CraftBukkit end

            this.world.a(this.player, 2001, i, j, k, Block.b(block) + (this.world.getData(i, j, k) << 12));
            boolean flag = this.d(i, j, k);

            if (this.isCreative()) {
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, this.world));
            } else {
                ItemStack itemstack = this.player.bD();
                boolean flag1 = this.player.a(block);

                if (itemstack != null) {
                    itemstack.a(this.world, block, i, j, k, this.player);
                    if (itemstack.count == 0) {
                        this.player.bE();
                    }
                }

                if (flag && flag1) {
                    block.a(this.world, this.player, i, j, k, l);
                }
            }

            // CraftBukkit start - Drop event experience
            if (flag && event != null) {
                block.dropExperience(this.world, i, j, k, event.getExpToDrop());
            }
            // CraftBukkit end

            return flag;
        }
    }

    public boolean useItem(EntityHuman entityhuman, World world, ItemStack itemstack) {
        int i = itemstack.count;
        int j = itemstack.getData();
        ItemStack itemstack1 = itemstack.a(world, entityhuman);

        if (itemstack1 == itemstack && (itemstack1 == null || itemstack1.count == i && itemstack1.n() <= 0 && itemstack1.getData() == j)) {
            return false;
        } else {
            entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = itemstack1;
            if (this.isCreative()) {
                itemstack1.count = i;
                if (itemstack1.g()) {
                    itemstack1.setData(j);
                }
            }

            if (itemstack1.count == 0) {
                entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
            }

            if (!entityhuman.bw()) {
                ((EntityPlayer) entityhuman).updateInventory(entityhuman.defaultContainer);
            }

            return true;
        }
    }

    public boolean interact(EntityHuman entityhuman, World world, ItemStack itemstack, int i, int j, int k, int l, float f, float f1, float f2) {
        /* CraftBukkit start - whole method
        if ((!entityhuman.isSneaking() || entityhuman.be() == null) && world.getType(i, j, k).interact(world, i, j, k, entityhuman, l, f, f1, f2)) {
            return true;
        } else if (itemstack == null) {
            return false;
        } else if (this.isCreative()) {
            int i1 = itemstack.getData();
            int j1 = itemstack.count;
            boolean flag = itemstack.placeItem(entityhuman, world, i, j, k, l, f, f1, f2);

            itemstack.setData(i1);
            itemstack.count = j1;
            return flag;
        } else {
            return itemstack.placeItem(entityhuman, world, i, j, k, l, f, f1, f2);
        }
        // Interract event */
        Block block = world.getType(i, j, k);
        boolean result = false;
        if (block != Blocks.AIR) {
            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, i, j, k, l, itemstack);
            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (block == Blocks.WOODEN_DOOR) {
                    boolean bottom = (world.getData(i, j, k) & 8) == 0;
                    ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j + (bottom ? 1 : -1), k, world));
                }
                result = (event.useItemInHand() != Event.Result.ALLOW);
            } else if (!entityhuman.isSneaking() || itemstack == null) {
                result = block.interact(world, i, j, k, entityhuman, l, f, f1, f2);
            }

            if (itemstack != null && !result) {
                int j1 = itemstack.getData();
                int k1 = itemstack.count;

                result = itemstack.placeItem(entityhuman, world, i, j, k, l, f, f1, f2);

                // The item count should not decrement in Creative mode.
                if (this.isCreative()) {
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
