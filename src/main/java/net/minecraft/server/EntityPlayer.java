package net.minecraft.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import java.util.EnumSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.ChunkCompressionThread;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    public NetServerHandler netServerHandler;
    public MinecraftServer server;
    public ItemInWorldManager itemInWorldManager;
    public double d;
    public double e;
    public List chunkCoordIntPairQueue = new LinkedList();
    public Set playerChunkCoordIntPairs = new HashSet();
    private int cf = -99999999;
    private int cg = -99999999;
    private boolean ch = true;
    public int lastSentExp = -99999999; // CraftBukkit - priv to pub
    public int invulnerableTicks = 60; // CraftBukkit - private to public; temporary until we get an API out
    private ItemStack[] ck = new ItemStack[] { null, null, null, null, null};
    private int containerCounter = 0;
    public boolean h;
    public int ping;
    public boolean viewingCredits = false;

    public EntityPlayer(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(world);
        iteminworldmanager.player = this;
        this.itemInWorldManager = iteminworldmanager;
        ChunkCoordinates chunkcoordinates = world.getSpawn();
        int i = chunkcoordinates.x;
        int j = chunkcoordinates.z;
        int k = chunkcoordinates.y;

        if (!world.worldProvider.f) {
            i += this.random.nextInt(20) - 10;
            k = world.f(i, j);
            j += this.random.nextInt(20) - 10;
        }

        this.setPositionRotation((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);
        this.server = minecraftserver;
        this.bP = 0.0F;
        this.name = s;
        this.height = 0.0F;

        // CraftBukkit start
        this.displayName = this.name;
        this.listName = this.name;
    }

    public String displayName;
    public String listName;
    public org.bukkit.Location compassTarget;
    public int newExp = 0;
    public int newLevel = 0;
    public int newTotalExp = 0;
    public boolean keepLevel = false;
    // CraftBukkit end

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("playerGameType")) {
            this.itemInWorldManager.setGameMode(nbttagcompound.getInt("playerGameType"));
        }
        this.getBukkitEntity().readExtraData(nbttagcompound); // CraftBukkit
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.itemInWorldManager.getGameMode());
        this.getBukkitEntity().setExtraData(nbttagcompound); // CraftBukkit
    }

    public void spawnIn(World world) {
        super.spawnIn(world);
        // CraftBukkit - world fallback code, either respawn location or global spawn
        if (world == null) {
            this.dead = false;
            ChunkCoordinates position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBed() != null) {
                    world = cworld.getHandle();
                    position = EntityHuman.getBed(cworld.getHandle(), this.getBed());
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.getSpawn();
            }
            this.world = world;
            this.setPosition(position.x + 0.5, position.y, position.z + 0.5);
        }
        this.dimension = ((WorldServer) this.world).dimension;
        int oldMode = itemInWorldManager.getGameMode();
        this.itemInWorldManager = new ItemInWorldManager((WorldServer) world);
        this.itemInWorldManager.player = this;
        this.itemInWorldManager.setGameMode(oldMode);
        // CraftBukkit end
    }

    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    public ItemStack[] getEquipment() {
        return this.ck;
    }

    protected void r_() {
        this.height = 0.0F;
    }

    public float getHeadHeight() {
        return 1.62F;
    }

    public void y_() {
        this.itemInWorldManager.c();
        --this.invulnerableTicks;
        this.activeContainer.a();

        for (int i = 0; i < 5; ++i) {
            ItemStack itemstack = this.c(i);

            if (itemstack != this.ck[i]) {
                this.server.getTracker(this.dimension).a(this, new Packet5EntityEquipment(this.id, i, itemstack));
                this.ck[i] = itemstack;
            }
        }
    }

    public ItemStack c(int i) {
        return i == 0 ? this.inventory.getItemInHand() : this.inventory.armor[i - 1];
    }

    public void die(DamageSource damagesource) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        for (int i = 0; i < this.inventory.items.length; ++i) {
            if (this.inventory.items[i] != null) {
                loot.add(new CraftItemStack(this.inventory.items[i]));
            }
        }

        for (int i = 0; i < this.inventory.armor.length; ++i) {
            if (this.inventory.armor[i] != null) {
                loot.add(new CraftItemStack(this.inventory.armor[i]));
            }
        }

        PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, damagesource.getLocalizedDeathMessage(this));

        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && deathMessage.length() > 0) {
            this.server.serverConfigurationManager.sendAll(new Packet3Chat(event.getDeathMessage()));
        }

        // CraftBukkit start - we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        for (int i = 0; i < this.inventory.items.length; ++i) {
            this.inventory.items[i] = null;
        }

        for (int i = 0; i < this.inventory.armor.length; ++i) {
            this.inventory.armor[i] = null;
        }

        this.closeInventory();
        // CraftBukkit end
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.invulnerableTicks > 0) {
            return false;
        } else {
            // CraftBukkit - this.b.pvpMode -> this.world.pvpMode
            if (!this.world.pvpMode && damagesource instanceof EntityDamageSource) {
                Entity entity = damagesource.getEntity();

                if (entity instanceof EntityHuman) {
                    return false;
                }

                if (entity instanceof EntityArrow) {
                    EntityArrow entityarrow = (EntityArrow) entity;

                    if (entityarrow.shooter instanceof EntityHuman) {
                        return false;
                    }
                }
            }

            return super.damageEntity(damagesource, i);
        }
    }

    protected boolean z() {
        return this.server.pvpMode;
    }

    public void heal(int i) {
        super.heal(i);
    }

    public void a(boolean flag) {
        super.y_();

        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);

            if (itemstack != null && Item.byId[itemstack.id].n_() && this.netServerHandler.lowPriorityCount() <= 2) {
                Packet packet = ((ItemWorldMapBase) Item.byId[itemstack.id]).c(itemstack, this.world, this);

                if (packet != null) {
                    this.netServerHandler.sendPacket(packet);
                }
            }
        }

        if (flag && !this.chunkCoordIntPairQueue.isEmpty()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) this.chunkCoordIntPairQueue.get(0);

            if (chunkcoordintpair != null) {
                boolean flag1 = false;

                // CraftBukkit - Add check against Chunk Packets in the ChunkCompressionThread.
                if (this.netServerHandler.lowPriorityCount() + ChunkCompressionThread.getPlayerQueueSize(this) < 4) {
                    flag1 = true;
                }

                if (flag1) {
                    WorldServer worldserver = this.server.getWorldServer(this.dimension);

                    this.chunkCoordIntPairQueue.remove(chunkcoordintpair);
                    this.netServerHandler.sendPacket(new Packet51MapChunk(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, 16, worldserver.height, 16, worldserver));
                    List list = worldserver.getTileEntities(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, chunkcoordintpair.x * 16 + 16, worldserver.height, chunkcoordintpair.z * 16 + 16);

                    for (int j = 0; j < list.size(); ++j) {
                        this.a((TileEntity) list.get(j));
                    }
                }
            }
        }

        if (this.J) {
            //if (this.server.propertyManager.getBoolean("allow-nether", true)) { // CraftBukkit
                if (this.activeContainer != this.defaultContainer) {
                    this.closeInventory();
                }

                if (this.vehicle != null) {
                    this.mount(this.vehicle);
                } else {
                    this.K += 0.0125F;
                    if (this.K >= 1.0F) {
                        this.K = 1.0F;
                        this.I = 10;
                        boolean flag2 = false;
                        byte b0;

                        if (this.dimension == -1) {
                            b0 = 0;
                        } else {
                            b0 = -1;
                        }

                        this.server.serverConfigurationManager.changeDimension(this, b0);
                        this.lastSentExp = -1;
                        this.cf = -1;
                        this.cg = -1;
                        this.a((Statistic) AchievementList.x);
                    }
                }

                this.J = false;
            //} // CraftBukkit
        } else {
            if (this.K > 0.0F) {
                this.K -= 0.05F;
            }

            if (this.K < 0.0F) {
                this.K = 0.0F;
            }
        }

        if (this.I > 0) {
            --this.I;
        }

        if (this.getHealth() != this.cf || this.cg != this.foodData.a() || this.foodData.c() == 0.0F != this.ch) {
            this.netServerHandler.sendPacket(new Packet8UpdateHealth(this.getHealth(), this.foodData.a(), this.foodData.c()));
            this.cf = this.getHealth();
            this.cg = this.foodData.a();
            this.ch = this.foodData.c() == 0.0F;
        }

        if (this.expTotal != this.lastSentExp) {
            this.lastSentExp = this.expTotal;
            this.netServerHandler.sendPacket(new Packet43SetExperience(this.exp, this.expTotal, this.expLevel));
        }

        // CraftBukkit start
        if (this.oldLevel == -1) {
            this.oldLevel = this.expLevel;
        }

        if (this.oldLevel != this.expLevel) {
            CraftEventFactory.callPlayerLevelChangeEvent(this.world.getServer().getPlayer((EntityPlayer) this), this.oldLevel, this.expLevel);
            this.oldLevel = this.expLevel;
        }
        // CraftBukkit end
    }

    public void e(int i) {
        if (this.dimension == 1 && i == 1) {
            this.a((Statistic) AchievementList.C);
            this.world.kill(this);
            this.viewingCredits = true;
            this.netServerHandler.sendPacket(new Packet70Bed(4, 0));
        } else {
            this.a((Statistic) AchievementList.B);
            /* CraftBukkit start - removed to fix our handling of The End portals
            ChunkCoordinates chunkcoordinates = this.b.getWorldServer(i).d();

            if (chunkcoordinates != null) {
                this.netServerHandler.a((double) chunkcoordinates.x, (double) chunkcoordinates.y, (double) chunkcoordinates.z, 0.0F, 0.0F);
            }
            // CraftBukkit end */

            this.server.serverConfigurationManager.changeDimension(this, 1);
            this.lastSentExp = -1;
            this.cf = -1;
            this.cg = -1;
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.k();

            if (packet != null) {
                this.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void receive(Entity entity, int i) {
        if (!entity.dead) {
            EntityTracker entitytracker = this.server.getTracker(this.dimension);

            if (entity instanceof EntityItem) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityArrow) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityExperienceOrb) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }
        }

        super.receive(entity, i);
        this.activeContainer.a();
    }

    public void s_() {
        if (!this.t) {
            this.u = -1;
            this.t = true;
            EntityTracker entitytracker = this.server.getTracker(this.dimension);

            entitytracker.a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public void B() {}

    public EnumBedResult a(int i, int j, int k) {
        EnumBedResult enumbedresult = super.a(i, j, k);

        if (enumbedresult == EnumBedResult.OK) {
            EntityTracker entitytracker = this.server.getTracker(this.dimension);
            Packet17EntityLocationAction packet17entitylocationaction = new Packet17EntityLocationAction(this, 0, i, j, k);

            entitytracker.a(this, packet17entitylocationaction);
            this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.netServerHandler.sendPacket(packet17entitylocationaction);
        }

        return enumbedresult;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.isSleeping()) {
            EntityTracker entitytracker = this.server.getTracker(this.dimension);

            entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(this, 3));
        }

        super.a(flag, flag1, flag2);
        if (this.netServerHandler != null) {
            this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        this.setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // mount(null) doesn't really fly for overloaded methods,
        // so this method is needed

        super.setPassengerOf(entity);
        // CraftBukkit end

        this.netServerHandler.sendPacket(new Packet39AttachEntity(this, this.vehicle));
        this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }

    protected void a(double d0, boolean flag) {}

    public void b(double d0, boolean flag) {
        super.a(d0, flag);
    }

    public int nextContainerCounter() { // CraftBukkit - private void -> public int
        this.containerCounter = this.containerCounter % 100 + 1;
        return this.containerCounter; // CraftBukkit
    }

    public void startCrafting(int i, int j, int k) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerWorkbench(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 1, "Crafting", 9));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void startEnchanting(int i, int j, int k) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerEnchantTable(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 4, "Enchanting", 9));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openContainer(IInventory iinventory) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerChest(this.inventory, iinventory));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 0, iinventory.getName(), iinventory.getSize()));
        this.activeContainer = container; // CraftBukkit - Use container passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openFurnace(TileEntityFurnace tileentityfurnace) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerFurnace(this.inventory, tileentityfurnace));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 2, tileentityfurnace.getName(), tileentityfurnace.getSize()));
        this.activeContainer = container; // CraftBukkit - Use container passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openDispenser(TileEntityDispenser tileentitydispenser) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerDispenser(this.inventory, tileentitydispenser));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 3, tileentitydispenser.getName(), tileentitydispenser.getSize()));
        this.activeContainer = container; // CraftBukkit - Use container passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {
        // CraftBukkit start - INVENTORY_OPEN hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerBrewingStand(this.inventory, tileentitybrewingstand));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.containerCounter, 5, tileentitybrewingstand.getName(), tileentitybrewingstand.getSize()));
        this.activeContainer = container; // CraftBukkit - Use container passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (!this.h) {
                this.netServerHandler.sendPacket(new Packet103SetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.b());
    }

    public void a(Container container, List list) {
        this.netServerHandler.sendPacket(new Packet104WindowItems(container.windowId, list));
        this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getCarried()));
        // CraftBukkit start - send a Set Slot to update the crafting result slot
        if (EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
            this.netServerHandler.sendPacket(new Packet103SetSlot(container.windowId, 0, container.getSlot(0).getItem()));
        }
        // CraftBukkit end
    }

    public void setContainerData(Container container, int i, int j) {
        this.netServerHandler.sendPacket(new Packet105CraftProgressBar(container.windowId, i, j));
    }

    public void carriedChanged(ItemStack itemstack) {}

    public void closeInventory() {
        this.netServerHandler.sendPacket(new Packet101CloseWindow(this.activeContainer.windowId));
        this.E();
    }

    public void broadcastCarriedItem() {
        if (!this.h) {
            this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void E() {
        this.activeContainer.a((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(Statistic statistic, int i) {
        if (statistic != null) {
            if (!statistic.f) {
                while (i > 100) {
                    this.netServerHandler.sendPacket(new Packet200Statistic(statistic.e, 100));
                    i -= 100;
                }

                this.netServerHandler.sendPacket(new Packet200Statistic(statistic.e, i));
            }
        }
    }

    public void F() {
        if (this.vehicle != null) {
            this.mount(this.vehicle);
        }

        if (this.passenger != null) {
            this.passenger.mount(this);
        }

        if (this.sleeping) {
            this.a(true, false, false);
        }
    }

    public void t_() {
        this.cf = -99999999;
        this.lastSentExp = -1; // CraftBukkit - Added to reset
    }

    public void a(String s) {
        LocaleLanguage localelanguage = LocaleLanguage.a();
        String s1 = localelanguage.b(s);

        this.netServerHandler.sendPacket(new Packet3Chat(s1));
    }

    protected void H() {
        this.netServerHandler.sendPacket(new Packet38EntityStatus(this.id, (byte) 9));
        super.H();
    }

    public void a(ItemStack itemstack, int i) {
        super.a(itemstack, i);
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem().d(itemstack) == EnumAnimation.b) {
            EntityTracker entitytracker = this.server.getTracker(this.dimension);

            entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(this, 5));
        }
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.netServerHandler.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void c(MobEffect mobeffect) {
        super.c(mobeffect);
        this.netServerHandler.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void d(MobEffect mobeffect) {
        super.d(mobeffect);
        this.netServerHandler.sendPacket(new Packet42RemoveMobEffect(this.id, mobeffect));
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.netServerHandler.a(d0, d1, d2, this.yaw, this.pitch);
    }

    public void c(Entity entity) {
        EntityTracker entitytracker = this.server.getTracker(this.dimension);

        entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(entity, 6));
    }

    public void d(Entity entity) {
        EntityTracker entitytracker = this.server.getTracker(this.dimension);

        entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(entity, 7));
    }

    // CraftBukkit start
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getTime() - (this.world.getTime() % 24000) + this.timeOffset;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.name + " at " + this.locX + "," + this.locY + "," + this.locZ + ")";
    }

    public void reset() {
        float exp = 0;
        if (this.keepLevel) {
            exp = this.exp;
            this.newTotalExp = this.expTotal;
            this.newLevel = this.expLevel;
        }

        this.health = 20;
        this.fireTicks = 0;
        this.fallDistance = 0;
        this.foodData = new FoodMetaData();
        this.expLevel = this.newLevel;
        this.expTotal = this.newTotalExp;
        this.exp = 0;
        this.deathTicks = 0;
        effects.clear();
        this.activeContainer = this.defaultContainer;
        this.lastSentExp = -1;
        if (this.keepLevel) {
            this.exp = exp;
        } else {
            this.giveExp(this.newExp);
        }
        this.keepLevel = false;
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }
    // CraftBukkit end
}
