package net.minecraft.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    private LocaleLanguage locale = new LocaleLanguage("en_US");
    public PlayerConnection playerConnection;
    public MinecraftServer server;
    public PlayerInteractManager playerInteractManager;
    public double d;
    public double e;
    public final List chunkCoordIntPairQueue = new LinkedList();
    public final List removeQueue = new LinkedList();
    private int cm = -99999999;
    private int cn = -99999999;
    private boolean co = true;
    public int lastSentExp = -99999999; // CraftBukkit - private -> public
    public int invulnerableTicks = 60; // CraftBukkit - private -> public
    private int cr = 0;
    private int cs = 0;
    private boolean ct = true;
    private int containerCounter = 0;
    public boolean h;
    public int ping;
    public boolean viewingCredits = false;
    // CraftBukkit start
    public String displayName;
    public String listName;
    public org.bukkit.Location compassTarget;
    public int newExp = 0;
    public int newLevel = 0;
    public int newTotalExp = 0;
    public boolean keepLevel = false;
    // CraftBukkit end

    public EntityPlayer(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager playerinteractmanager) {
        super(world);
        playerinteractmanager.player = this;
        this.playerInteractManager = playerinteractmanager;
        this.cr = minecraftserver.getPlayerList().o();
        ChunkCoordinates chunkcoordinates = world.getSpawn();
        int i = chunkcoordinates.x;
        int j = chunkcoordinates.z;
        int k = chunkcoordinates.y;

        if (!world.worldProvider.f && world.getWorldData().getGameType() != EnumGamemode.ADVENTURE) {
            int l = Math.max(5, minecraftserver.getSpawnProtection() - 6);

            i += this.random.nextInt(l * 2) - l;
            j += this.random.nextInt(l * 2) - l;
            k = world.i(i, j);
        }

        this.server = minecraftserver;
        this.Y = 0.0F;
        this.name = s;
        this.height = 0.0F;
        this.setPositionRotation((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);

        while (!world.getCubes(this, this.boundingBox).isEmpty()) {
            this.setPosition(this.locX, this.locY + 1.0D, this.locZ);
        }

        // CraftBukkit start
        this.displayName = this.name;
        this.listName = this.name;
        this.canPickUpLoot = true;
        // CraftBukkit end
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("playerGameType")) {
            this.playerInteractManager.setGameMode(EnumGamemode.a(nbttagcompound.getInt("playerGameType")));
        }
        this.getBukkitEntity().readExtraData(nbttagcompound); // CraftBukkit
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().a());
        this.getBukkitEntity().setExtraData(nbttagcompound); // CraftBukkit
    }

    // CraftBukkit start - world fallback code, either respawn location or global spawn
    public void spawnIn(World world) {
        super.spawnIn(world);
        if (world == null) {
            this.dead = false;
            ChunkCoordinates position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBed() != null) {
                    world = cworld.getHandle();
                    position = EntityHuman.getBed(cworld.getHandle(), this.getBed(), false);
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
        this.playerInteractManager.a((WorldServer) world);
    }
    // CraftBukkit end

    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    protected void e_() {
        this.height = 0.0F;
    }

    public float getHeadHeight() {
        return 1.62F;
    }

    public void l_() {
        this.playerInteractManager.a();
        --this.invulnerableTicks;
        this.activeContainer.b();

        while (!this.removeQueue.isEmpty()) {
            int i = Math.min(this.removeQueue.size(), 127);
            int[] aint = new int[i];
            Iterator iterator = this.removeQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            }

            this.playerConnection.sendPacket(new Packet29DestroyEntity(aint));
        }

        if (!this.chunkCoordIntPairQueue.isEmpty()) {
            ArrayList arraylist = new ArrayList();
            Iterator iterator1 = this.chunkCoordIntPairQueue.iterator();
            ArrayList arraylist1 = new ArrayList();

            while (iterator1.hasNext() && arraylist.size() < 5) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator1.next();

                iterator1.remove();
                if (chunkcoordintpair != null && this.world.isLoaded(chunkcoordintpair.x << 4, 0, chunkcoordintpair.z << 4)) {
                    arraylist.add(this.world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z));
                    arraylist1.addAll(((WorldServer) this.world).getTileEntities(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, chunkcoordintpair.x * 16 + 16, 256, chunkcoordintpair.z * 16 + 16));
                }
            }

            if (!arraylist.isEmpty()) {
                this.playerConnection.sendPacket(new Packet56MapChunkBulk(arraylist));
                Iterator iterator2 = arraylist1.iterator();

                while (iterator2.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator2.next();

                    this.b(tileentity);
                }

                iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    Chunk chunk = (Chunk) iterator2.next();

                    this.o().getTracker().a(this, chunk);
                }
            }
        }
    }

    public void setHealth(int i) {
        super.setHealth(i);
        Collection collection = this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.f);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

            this.getScoreboard().getPlayerScoreForObjective(this.getLocalizedName(), scoreboardobjective).updateForList(Arrays.asList(new EntityHuman[] { this}));
        }
    }

    public void g() {
        try {
            super.l_();

            for (int i = 0; i < this.inventory.getSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (itemstack != null && Item.byId[itemstack.id].f() && this.playerConnection.lowPriorityCount() <= 5) {
                    Packet packet = ((ItemWorldMapBase) Item.byId[itemstack.id]).c(itemstack, this.world, this);

                    if (packet != null) {
                        this.playerConnection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.cm || this.cn != this.foodData.a() || this.foodData.e() == 0.0F != this.co) {
                // CraftBukkit - this.getHealth() -> this.getScaledHealth()
                this.playerConnection.sendPacket(new Packet8UpdateHealth(this.getScaledHealth(), this.foodData.a(), this.foodData.e()));
                this.cm = this.getHealth();
                this.cn = this.foodData.a();
                this.co = this.foodData.e() == 0.0F;
            }

            if (this.expTotal != this.lastSentExp) {
                this.lastSentExp = this.expTotal;
                this.playerConnection.sendPacket(new Packet43SetExperience(this.exp, this.expTotal, this.expLevel));
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
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");

            this.a(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    public void die(DamageSource damagesource) {
        // CraftBukkit start
        if (this.dead) {
            return;
        }

        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (!keepInventory) {
            for (int i = 0; i < this.inventory.items.length; ++i) {
                if (this.inventory.items[i] != null) {
                    loot.add(CraftItemStack.asCraftMirror(this.inventory.items[i]));
                }
            }

            for (int i = 0; i < this.inventory.armor.length; ++i) {
                if (this.inventory.armor[i] != null) {
                    loot.add(CraftItemStack.asCraftMirror(this.inventory.armor[i]));
                }
            }
        }

        org.bukkit.event.entity.PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, this.bt.b());

        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && deathMessage.length() > 0) {
            this.server.getPlayerList().k(event.getDeathMessage());
        }

        // CraftBukkit - we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        if (!keepInventory) {
            for (int i = 0; i < this.inventory.items.length; ++i) {
                this.inventory.items[i] = null;
            }

            for (int i = 0; i < this.inventory.armor.length; ++i) {
                this.inventory.armor[i] = null;
            }
        }

        this.closeInventory();
        // CraftBukkit end

        Collection collection = this.world.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.c);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();
            ScoreboardScore scoreboardscore = this.getScoreboard().getPlayerScoreForObjective(this.getLocalizedName(), scoreboardobjective);

            scoreboardscore.incrementScore();
        }

        EntityLiving entityliving = this.bN();

        if (entityliving != null) {
            entityliving.c(this, this.aM);
        }
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
            boolean flag = this.server.T() && this.world.pvpMode && "fall".equals(damagesource.translationIndex);

            if (!flag && this.invulnerableTicks > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.getEntity();

                    if (entity instanceof EntityHuman && !this.a((EntityHuman) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shooter instanceof EntityHuman && !this.a((EntityHuman) entityarrow.shooter)) {
                            return false;
                        }
                    }
                }

                return super.damageEntity(damagesource, i);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
        return !this.world.pvpMode ? false : super.a(entityhuman);
    }

    public void c(int i) {
        if (this.dimension == 1 && i == 1) {
            this.a((Statistic) AchievementList.C);
            this.world.kill(this);
            this.viewingCredits = true;
            this.playerConnection.sendPacket(new Packet70Bed(4, 0));
        } else {
            if (this.dimension == 1 && i == 0) {
                this.a((Statistic) AchievementList.B);
                // CraftBukkit start - rely on custom portal management
                /*
                ChunkCoordinates chunkcoordinates = this.server.getWorldServer(i).getDimensionSpawn();

                if (chunkcoordinates != null) {
                    this.playerConnection.a((double) chunkcoordinates.x, (double) chunkcoordinates.y, (double) chunkcoordinates.z, 0.0F, 0.0F);
                }

                i = 1;
                */
                // CraftBukkit end
            } else {
                this.a((Statistic) AchievementList.x);
            }

            // CraftBukkit start
            TeleportCause cause = (this.dimension == 1 || i == 1) ? TeleportCause.END_PORTAL : TeleportCause.NETHER_PORTAL;
            this.server.getPlayerList().changeDimension(this, i, cause);
            // CraftBukkit end
            this.lastSentExp = -1;
            this.cm = -1;
            this.cn = -1;
        }
    }

    private void b(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.getUpdatePacket();

            if (packet != null) {
                this.playerConnection.sendPacket(packet);
            }
        }
    }

    public void receive(Entity entity, int i) {
        super.receive(entity, i);
        this.activeContainer.b();
    }

    public EnumBedResult a(int i, int j, int k) {
        EnumBedResult enumbedresult = super.a(i, j, k);

        if (enumbedresult == EnumBedResult.OK) {
            Packet17EntityLocationAction packet17entitylocationaction = new Packet17EntityLocationAction(this, 0, i, j, k);

            this.o().getTracker().a((Entity) this, (Packet) packet17entitylocationaction);
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.playerConnection.sendPacket(packet17entitylocationaction);
        }

        return enumbedresult;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.fauxSleeping && !this.sleeping) return; // CraftBukkit - Can't leave bed if not in one!

        if (this.isSleeping()) {
            this.o().getTracker().sendPacketToEntity(this, new Packet18ArmAnimation(this, 3));
        }

        super.a(flag, flag1, flag2);
        if (this.playerConnection != null) {
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
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

        this.playerConnection.sendPacket(new Packet39AttachEntity(this, this.vehicle));
        this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
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
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerWorkbench(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 1, "Crafting", 9, true));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void startEnchanting(int i, int j, int k, String s) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerEnchantTable(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 4, s == null ? "" : s, 9, s != null));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openAnvil(int i, int j, int k) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerAnvil(this.inventory, this.world, i, j, k, this));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 8, "Repairing", 9, true));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openContainer(IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerChest(this.inventory, iinventory));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 0, iinventory.getName(), iinventory.getSize(), iinventory.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openHopper(TileEntityHopper tileentityhopper) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHopper(this.inventory, tileentityhopper));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 9, tileentityhopper.getName(), tileentityhopper.getSize(), tileentityhopper.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openMinecartHopper(EntityMinecartHopper entityminecarthopper) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHopper(this.inventory, entityminecarthopper));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 9, entityminecarthopper.getName(), entityminecarthopper.getSize(), entityminecarthopper.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openFurnace(TileEntityFurnace tileentityfurnace) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerFurnace(this.inventory, tileentityfurnace));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 2, tileentityfurnace.getName(), tileentityfurnace.getSize(), tileentityfurnace.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openDispenser(TileEntityDispenser tileentitydispenser) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerDispenser(this.inventory, tileentitydispenser));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, tileentitydispenser instanceof TileEntityDropper ? 10 : 3, tileentitydispenser.getName(), tileentitydispenser.getSize(), tileentitydispenser.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerBrewingStand(this.inventory, tileentitybrewingstand));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 5, tileentitybrewingstand.getName(), tileentitybrewingstand.getSize(), tileentitybrewingstand.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBeacon(TileEntityBeacon tileentitybeacon) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerBeacon(this.inventory, tileentitybeacon));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 7, tileentitybeacon.getName(), tileentitybeacon.getSize(), tileentitybeacon.c()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openTrade(IMerchant imerchant, String s) {
        // CraftBukkit start - inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.inventory, imerchant, this.world));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.activeContainer).getMerchantInventory();

        this.playerConnection.sendPacket(new Packet100OpenWindow(this.containerCounter, 6, s == null ? "" : s, inventorymerchant.getSize(), s != null));
        MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);

        if (merchantrecipelist != null) {
            try {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

                dataoutputstream.writeInt(this.containerCounter);
                merchantrecipelist.a(dataoutputstream);
                this.playerConnection.sendPacket(new Packet250CustomPayload("MC|TrList", bytearrayoutputstream.toByteArray()));
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (!this.h) {
                this.playerConnection.sendPacket(new Packet103SetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.a());
    }

    public void a(Container container, List list) {
        this.playerConnection.sendPacket(new Packet104WindowItems(container.windowId, list));
        this.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getCarried()));
        // CraftBukkit start - send a Set Slot to update the crafting result slot
        if (java.util.EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
            this.playerConnection.sendPacket(new Packet103SetSlot(container.windowId, 0, container.getSlot(0).getItem()));
        }
        // CraftBukkit end
    }

    public void setContainerData(Container container, int i, int j) {
        this.playerConnection.sendPacket(new Packet105CraftProgressBar(container.windowId, i, j));
    }

    public void closeInventory() {
        this.playerConnection.sendPacket(new Packet101CloseWindow(this.activeContainer.windowId));
        this.j();
    }

    public void broadcastCarriedItem() {
        if (!this.h) {
            this.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void j() {
        this.activeContainer.b((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(Statistic statistic, int i) {
        if (statistic != null) {
            if (!statistic.f) {
                while (i > 100) {
                    this.playerConnection.sendPacket(new Packet200Statistic(statistic.e, 100));
                    i -= 100;
                }

                this.playerConnection.sendPacket(new Packet200Statistic(statistic.e, i));
            }
        }
    }

    public void k() {
        if (this.passenger != null) {
            this.passenger.mount(this);
        }

        if (this.sleeping) {
            this.a(true, false, false);
        }
    }

    public void triggerHealthUpdate() {
        this.cm = -99999999;
        this.lastSentExp = -1; // CraftBukkit - Added to reset
    }

    public void b(String s) {
        LocaleLanguage localelanguage = LocaleLanguage.a();
        String s1 = localelanguage.a(s);

        this.playerConnection.sendPacket(new Packet3Chat(s1));
    }

    protected void m() {
        this.playerConnection.sendPacket(new Packet38EntityStatus(this.id, (byte) 9));
        super.m();
    }

    public void a(ItemStack itemstack, int i) {
        super.a(itemstack, i);
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem().b_(itemstack) == EnumAnimation.EAT) {
            this.o().getTracker().sendPacketToEntity(this, new Packet18ArmAnimation(this, 5));
        }
    }

    public void copyTo(EntityHuman entityhuman, boolean flag) {
        super.copyTo(entityhuman, flag);
        this.lastSentExp = -1;
        this.cm = -1;
        this.cn = -1;
        this.removeQueue.addAll(((EntityPlayer) entityhuman).removeQueue);
    }

    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.playerConnection.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.playerConnection.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void c(MobEffect mobeffect) {
        super.c(mobeffect);
        this.playerConnection.sendPacket(new Packet42RemoveMobEffect(this.id, mobeffect));
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
    }

    public void b(Entity entity) {
        this.o().getTracker().sendPacketToEntity(this, new Packet18ArmAnimation(entity, 6));
    }

    public void c(Entity entity) {
        this.o().getTracker().sendPacketToEntity(this, new Packet18ArmAnimation(entity, 7));
    }

    public void updateAbilities() {
        if (this.playerConnection != null) {
            this.playerConnection.sendPacket(new Packet202Abilities(this.abilities));
        }
    }

    public WorldServer o() {
        return (WorldServer) this.world;
    }

    public void a(EnumGamemode enumgamemode) {
        this.playerInteractManager.setGameMode(enumgamemode);
        this.playerConnection.sendPacket(new Packet70Bed(3, enumgamemode.a()));
    }

    public void sendMessage(String s) {
        this.playerConnection.sendPacket(new Packet3Chat(s));
    }

    public boolean a(int i, String s) {
        return "seed".equals(s) && !this.server.T() ? true : (!"tell".equals(s) && !"help".equals(s) && !"me".equals(s) ? this.server.getPlayerList().isOp(this.name) : true);
    }

    public String p() {
        String s = this.playerConnection.networkManager.getSocketAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void a(Packet204LocaleAndViewDistance packet204localeandviewdistance) {
        if (this.locale.b().containsKey(packet204localeandviewdistance.d())) {
            this.locale.a(packet204localeandviewdistance.d(), false);
        }

        int i = 256 >> packet204localeandviewdistance.f();

        if (i > 3 && i < 15) {
            this.cr = i;
        }

        this.cs = packet204localeandviewdistance.g();
        this.ct = packet204localeandviewdistance.h();
        if (this.server.I() && this.server.H().equals(this.name)) {
            this.server.c(packet204localeandviewdistance.i());
        }

        this.b(1, !packet204localeandviewdistance.j());
    }

    public LocaleLanguage getLocale() {
        return this.locale;
    }

    public int getChatFlags() {
        return this.cs;
    }

    public void a(String s, int i) {
        String s1 = s + "\0" + i; // CraftBukkit - fix decompile error

        this.playerConnection.sendPacket(new Packet250CustomPayload("MC|TPack", s1.getBytes()));
    }

    public ChunkCoordinates b() {
        return new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY + 0.5D), MathHelper.floor(this.locZ));
    }

    // CraftBukkit start
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getDayTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getDayTime() - (this.world.getDayTime() % 24000) + this.timeOffset;
        }
    }

    public WeatherType weather = null;

    public WeatherType getPlayerWeather() {
        return this.weather;
    }
   
    public void setPlayerWeather(WeatherType type, boolean plugin) {
        if (!plugin && this.weather != null) {
            return;
        }

        if (plugin) {
            this.weather = type;
        }

        this.playerConnection.sendPacket(new Packet70Bed(type == WeatherType.DOWNFALL ? 1 : 2, 0));
    }
   
    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.o().getWorldData().hasStorm() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.name + " at " + this.locX + "," + this.locY + "," + this.locZ + ")";
    }

    public void reset() {
        float exp = 0;
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (this.keepLevel || keepInventory) {
            exp = this.exp;
            this.newTotalExp = this.expTotal;
            this.newLevel = this.expLevel;
        }

        this.health = this.maxHealth;
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
        if (this.keepLevel || keepInventory) {
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
