package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryMapper;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryAbstractHorse;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.inventory.CraftInventoryLectern;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMerchantCustom;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final Player entity) {
        super(server, entity);
        this.mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.getInventory());
        this.enderChest = new CraftInventory(entity.getEnderChestInventory());
    }

    @Override
    public PlayerInventory getInventory() {
        return this.inventory;
    }

    @Override
    public EntityEquipment getEquipment() {
        return this.inventory;
    }

    @Override
    public Inventory getEnderChest() {
        return this.enderChest;
    }

    @Override
    public MainHand getMainHand() {
        return this.getHandle().getMainArm() == HumanoidArm.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        this.getInventory().setItemInHand(item);
    }

    @Override
    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(this.getHandle().containerMenu.getCarried());
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        this.getHandle().containerMenu.setCarried(stack);
        if (this instanceof CraftPlayer) {
            this.getHandle().containerMenu.broadcastCarriedItem(); // Send set slot for cursor
        }
    }

    // Paper start
    @Override
    public void setHurtDirection(float hurtDirection) {
        this.getHandle().hurtDir = hurtDirection;
    }
    // Paper end

    // Paper start
    @Override
    public boolean isDeeplySleeping() {
        return getHandle().isSleepingLongEnough();
    }
    // Paper end

    @Override
    public int getSleepTicks() {
        return this.getHandle().sleepCounter;
    }

    // Paper start - Potential bed api
    @Override
    public Location getPotentialBedLocation() {
        ServerPlayer handle = (ServerPlayer) getHandle();
        BlockPos bed = handle.getRespawnPosition();
        if (bed == null) {
            return null;
        }

        net.minecraft.server.level.ServerLevel worldServer = handle.server.getLevel(handle.getRespawnDimension());
        if (worldServer == null) {
            return null;
        }
        return new Location(worldServer.getWorld(), bed.getX(), bed.getY(), bed.getZ());
    }
    // Paper end
    // Paper start
    @Override
    public org.bukkit.entity.FishHook getFishHook() {
        if (getHandle().fishing == null) {
            return null;
        }
        return (org.bukkit.entity.FishHook) getHandle().fishing.getBukkitEntity();
    }
    // Paper end
    @Override
    public boolean sleep(Location location, boolean force) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(this.getWorld()), "Cannot sleep across worlds");

        BlockPos blockposition = CraftLocation.toBlockPosition(location);
        BlockState iblockdata = this.getHandle().level().getBlockState(blockposition);
        if (!(iblockdata.getBlock() instanceof BedBlock)) {
            return false;
        }

        if (this.getHandle().startSleepInBed(blockposition, force).left().isPresent()) {
            return false;
        }

        // From BlockBed
        iblockdata = iblockdata.setValue(BedBlock.OCCUPIED, true);
        this.getHandle().level().setBlock(blockposition, iblockdata, 4);

        return true;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        Preconditions.checkState(this.isSleeping(), "Cannot wakeup if not sleeping");

        this.getHandle().stopSleepInBed(true, setSpawnLocation);
    }

    @Override
    public void startRiptideAttack(int duration, float damage, ItemStack attackItem) {
        Preconditions.checkArgument(duration > 0, "Duration must be greater than 0");
        Preconditions.checkArgument(damage >= 0, "Damage must not be negative");

        this.getHandle().startAutoSpinAttack(duration, damage, CraftItemStack.asNMSCopy(attackItem));
    }

    @Override
    public Location getBedLocation() {
        Preconditions.checkState(this.isSleeping(), "Not sleeping");

        BlockPos bed = this.getHandle().getSleepingPos().get();
        return CraftLocation.toBukkit(bed, this.getWorld());
    }

    @Override
    public String getName() {
        return this.getHandle().getScoreboardName();
    }

    @Override
    public boolean isOp() {
        return this.op;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean value) {
        this.op = value;
        this.perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public GameMode getGameMode() {
        return this.mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        Preconditions.checkArgument(mode != null, "GameMode cannot be null");

        this.mode = mode;
    }

    @Override
    public Player getHandle() {
        return (Player) this.entity;
    }

    public void setHandle(final Player entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.getInventory());
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + this.getEntityId() + "name=" + this.getName() + '}';
    }

    @Override
    public InventoryView getOpenInventory() {
        return this.getHandle().containerMenu.getBukkitView();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        if (!(this.getHandle() instanceof ServerPlayer)) return null;
        ServerPlayer player = (ServerPlayer) this.getHandle();
        AbstractContainerMenu formerContainer = this.getHandle().containerMenu;

        MenuProvider tileInventory = null;
        if (inventory instanceof CraftInventoryDoubleChest) {
            tileInventory = ((CraftInventoryDoubleChest) inventory).tile;
        } else if (inventory instanceof CraftInventoryLectern) {
            tileInventory = ((CraftInventoryLectern) inventory).tile;
        } else if (inventory instanceof CraftInventory) {
            CraftInventory craft = (CraftInventory) inventory;
            if (craft.getInventory() instanceof MenuProvider) {
                tileInventory = (MenuProvider) craft.getInventory();
            }
        }

        if (tileInventory instanceof MenuProvider) {
            if (tileInventory instanceof BlockEntity) {
                BlockEntity te = (BlockEntity) tileInventory;
                if (!te.hasLevel()) {
                    te.setLevel(this.getHandle().level());
                }
            }
        }

        if (tileInventory instanceof MenuProvider) {
            this.getHandle().openMenu(tileInventory);
        } else if (inventory instanceof CraftInventoryAbstractHorse craft && craft.getInventory().getOwner() instanceof CraftAbstractHorse horse) {
            this.getHandle().openHorseInventory(horse.getHandle(), craft.getInventory());
        } else {
            MenuType<?> container = CraftContainer.getNotchInventoryType(inventory);
            CraftHumanEntity.openCustomInventory(inventory, player, container);
        }

        if (this.getHandle().containerMenu == formerContainer) {
            return null;
        }
        this.getHandle().containerMenu.checkReachable = false;
        return this.getHandle().containerMenu.getBukkitView();
    }

    private static void openCustomInventory(Inventory inventory, ServerPlayer player, MenuType<?> windowType) {
        if (player.connection == null) return;
        Preconditions.checkArgument(windowType != null, "Unknown windowType");
        AbstractContainerMenu container = new CraftContainer(inventory, player, player.nextContainerCounter());

        // Paper start - Add titleOverride to InventoryOpenEvent
        final com.mojang.datafixers.util.Pair<net.kyori.adventure.text.Component, AbstractContainerMenu> result = CraftEventFactory.callInventoryOpenEventWithTitle(player, container);
        container = result.getSecond();
        // Paper end - Add titleOverride to InventoryOpenEvent
        if (container == null) return;

        //String title = container.getBukkitView().getTitle(); // Paper - comment
        net.kyori.adventure.text.Component adventure$title = container.getBukkitView().title(); // Paper
        if (adventure$title == null) adventure$title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(container.getBukkitView().getTitle()); // Paper
        if (result.getFirst() != null) adventure$title = result.getFirst(); // Paper - Add titleOverride to InventoryOpenEvent

        //player.connection.send(new ClientboundOpenScreenPacket(container.containerId, windowType, CraftChatMessage.fromString(title)[0])); // Paper - comment
        if (!player.isImmobile()) player.connection.send(new ClientboundOpenScreenPacket(container.containerId, windowType, io.papermc.paper.adventure.PaperAdventure.asVanilla(adventure$title))); // Paper - Prevent opening inventories when frozen
        player.containerMenu = container;
        player.initMenu(container);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        if (location == null) {
            location = this.getLocation();
        }
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.CRAFTING_TABLE) {
                return null;
            }
        }
        this.getHandle().openMenu(Blocks.CRAFTING_TABLE.defaultBlockState().getMenuProvider(this.getHandle().level(), CraftLocation.toBlockPosition(location)));
        if (force) {
            this.getHandle().containerMenu.checkReachable = false;
        }
        return this.getHandle().containerMenu.getBukkitView();
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        if (location == null) {
            location = this.getLocation();
        }
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTING_TABLE) {
                return null;
            }
        }

        // If there isn't an enchant table we can force create one, won't be very useful though.
        BlockPos pos = CraftLocation.toBlockPosition(location);
        // Paper start
        MenuProvider menuProvider = Blocks.ENCHANTING_TABLE.defaultBlockState().getMenuProvider(this.getHandle().level(), pos);
        if (menuProvider == null) {
            if (!force) {
                return null;
            }
            menuProvider = new net.minecraft.world.SimpleMenuProvider((syncId, inventory, player) -> {
                return new net.minecraft.world.inventory.EnchantmentMenu(syncId, inventory, net.minecraft.world.inventory.ContainerLevelAccess.create(this.getHandle().level(), pos));
            }, Component.translatable("container.enchant"));
        }
        this.getHandle().openMenu(menuProvider);
        // Paper end

        if (force) {
            this.getHandle().containerMenu.checkReachable = false;
        }
        return this.getHandle().containerMenu.getBukkitView();
    }

    @Override
    public void openInventory(InventoryView inventory) {
        Preconditions.checkArgument(this.equals(inventory.getPlayer()), "InventoryView must belong to the opening player");
        if (!(this.getHandle() instanceof ServerPlayer)) return; // TODO: NPC support?
        if (((ServerPlayer) this.getHandle()).connection == null) return;
        if (this.getHandle().containerMenu != this.getHandle().inventoryMenu) {
            // fire INVENTORY_CLOSE if one already open
            ((ServerPlayer) this.getHandle()).connection.handleContainerClose(new ServerboundContainerClosePacket(this.getHandle().containerMenu.containerId), org.bukkit.event.inventory.InventoryCloseEvent.Reason.OPEN_NEW); // Paper - Inventory close reason
        }
        ServerPlayer player = (ServerPlayer) this.getHandle();
        AbstractContainerMenu container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, this.getHandle(), player.nextContainerCounter());
        }

        // Trigger an INVENTORY_OPEN event
        // Paper start - Add titleOverride to InventoryOpenEvent
        final com.mojang.datafixers.util.Pair<net.kyori.adventure.text.Component, AbstractContainerMenu> result = CraftEventFactory.callInventoryOpenEventWithTitle(player, container);
        container = result.getSecond();
        // Paper end - Add titleOverride to InventoryOpenEvent
        if (container == null) {
            return;
        }

        // Now open the window
        MenuType<?> windowType = CraftContainer.getNotchInventoryType(inventory.getTopInventory());

        //String title = inventory.getTitle(); // Paper - comment
        net.kyori.adventure.text.Component adventure$title = inventory.title(); // Paper
        if (adventure$title == null) adventure$title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(inventory.getTitle()); // Paper
        if (result.getFirst() != null) adventure$title = result.getFirst(); // Paper - Add titleOverride to InventoryOpenEvent
        //player.connection.send(new ClientboundOpenScreenPacket(container.containerId, windowType, CraftChatMessage.fromString(title)[0])); // Paper - comment
        if (!player.isImmobile()) player.connection.send(new ClientboundOpenScreenPacket(container.containerId, windowType, io.papermc.paper.adventure.PaperAdventure.asVanilla(adventure$title))); // Paper - Prevent opening inventories when frozen
        player.containerMenu = container;
        player.initMenu(container);
    }

    @Override
    public InventoryView openMerchant(Villager villager, boolean force) {
        Preconditions.checkNotNull(villager, "villager cannot be null");

        return this.openMerchant((Merchant) villager, force);
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        Preconditions.checkNotNull(merchant, "merchant cannot be null");

        if (!force && merchant.isTrading()) {
            return null;
        } else if (merchant.isTrading()) {
            // we're not supposed to have multiple people using the same merchant, so we have to close it.
            merchant.getTrader().closeInventory();
        }

        net.minecraft.world.item.trading.Merchant mcMerchant;
        Component name;
        int level = 1; // note: using level 0 with active 'is-regular-villager'-flag allows hiding the name suffix
        if (merchant instanceof CraftAbstractVillager) {
            mcMerchant = ((CraftAbstractVillager) merchant).getHandle();
            name = ((CraftAbstractVillager) merchant).getHandle().getDisplayName();
            if (merchant instanceof CraftVillager) {
                level = ((CraftVillager) merchant).getHandle().getVillagerData().getLevel();
            }
        } else if (merchant instanceof CraftMerchantCustom) {
            mcMerchant = ((CraftMerchantCustom) merchant).getMerchant();
            name = ((CraftMerchantCustom) merchant).getMerchant().getScoreboardDisplayName();
        } else {
            throw new IllegalArgumentException("Can't open merchant " + merchant.toString());
        }

        mcMerchant.setTradingPlayer(this.getHandle());
        mcMerchant.openTradingScreen(this.getHandle(), name, level);

        return this.getHandle().containerMenu.getBukkitView();
    }

    // Paper start - Add additional containers
    @Override
    public InventoryView openAnvil(Location location, boolean force) {
        return this.openInventory(location, force, Material.ANVIL);
    }

    @Override
    public InventoryView openCartographyTable(Location location, boolean force) {
        return this.openInventory(location, force, Material.CARTOGRAPHY_TABLE);
    }

    @Override
    public InventoryView openGrindstone(Location location, boolean force) {
        return this.openInventory(location, force, Material.GRINDSTONE);
    }

    @Override
    public InventoryView openLoom(Location location, boolean force) {
        return this.openInventory(location, force, Material.LOOM);
    }

    @Override
    public InventoryView openSmithingTable(Location location, boolean force) {
        return this.openInventory(location, force, Material.SMITHING_TABLE);
    }

    @Override
    public InventoryView openStonecutter(Location location, boolean force) {
        return this.openInventory(location, force, Material.STONECUTTER);
    }

    private InventoryView openInventory(Location location, boolean force, Material material) {
        org.spigotmc.AsyncCatcher.catchOp("open" + material);
        if (location == null) {
            location = this.getLocation();
        }
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != material) {
                return null;
            }
        }
        net.minecraft.world.level.block.Block block;
        if (material == Material.ANVIL) {
            block = Blocks.ANVIL;
        } else if (material == Material.CARTOGRAPHY_TABLE) {
            block = Blocks.CARTOGRAPHY_TABLE;
        } else if (material == Material.GRINDSTONE) {
            block = Blocks.GRINDSTONE;
        } else if (material == Material.LOOM) {
            block = Blocks.LOOM;
        } else if (material == Material.SMITHING_TABLE) {
            block = Blocks.SMITHING_TABLE;
        } else if (material == Material.STONECUTTER) {
            block = Blocks.STONECUTTER;
        } else {
            throw new IllegalArgumentException("Unsupported inventory type: " + material);
        }
        this.getHandle().openMenu(block.getMenuProvider(null, this.getHandle().level(), new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        this.getHandle().containerMenu.checkReachable = !force;
        return this.getHandle().containerMenu.getBukkitView();
    }
    // Paper end

    @Override
    public void closeInventory() {
        // Paper start - Inventory close reason
        this.getHandle().closeContainer(org.bukkit.event.inventory.InventoryCloseEvent.Reason.PLUGIN);
    }
    @Override
    public void closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason reason) {
        getHandle().closeContainer(reason);
    }
    // Paper end - Inventory close reason

    @Override
    public boolean isBlocking() {
        return this.getHandle().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return this.getHandle().isUsingItem();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    @Override
    public int getEnchantmentSeed() {
        return this.getHandle().enchantmentSeed;
    }

    @Override
    public void setEnchantmentSeed(int i) {
        this.getHandle().enchantmentSeed = i;
    }

    @Override
    public int getExpToLevel() {
        return this.getHandle().getXpNeededForNextLevel();
    }

    @Override
    public float getAttackCooldown() {
        return this.getHandle().getAttackStrengthScale(0.5f);
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isItem(), "Material %s is not an item", material);

        return this.hasCooldown(new ItemStack(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isItem(), "Material %s is not an item", material);

        return this.getCooldown(new ItemStack(material));
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        this.setCooldown(new ItemStack(material), ticks);
    }

    @Override
    public boolean hasCooldown(ItemStack item) {
        Preconditions.checkArgument(item != null, "Material cannot be null");

        return this.getHandle().getCooldowns().isOnCooldown(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public int getCooldown(ItemStack item) {
        Preconditions.checkArgument(item != null, "Material cannot be null");

        ResourceLocation group = this.getHandle().getCooldowns().getCooldownGroup(CraftItemStack.asNMSCopy(item));
        if (group == null) {
            return 0;
        }

        ItemCooldowns.CooldownInstance cooldown = this.getHandle().getCooldowns().cooldowns.get(group);
        return (cooldown == null) ? 0 : Math.max(0, cooldown.endTime() - this.getHandle().getCooldowns().tickCount);
    }

    @Override
    public void setCooldown(ItemStack item, int ticks) {
        Preconditions.checkArgument(item != null, "Material cannot be null");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        this.getHandle().getCooldowns().addCooldown(CraftItemStack.asNMSCopy(item), ticks);
    }

    // Paper start
    @Override
    public org.bukkit.entity.Entity releaseLeftShoulderEntity() {
        if (!getHandle().getShoulderEntityLeft().isEmpty()) {
            Entity entity = getHandle().releaseLeftShoulderEntity();
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }

    @Override
    public org.bukkit.entity.Entity releaseRightShoulderEntity() {
        if (!getHandle().getShoulderEntityRight().isEmpty()) {
            Entity entity = getHandle().releaseRightShoulderEntity();
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }
    // Paper end

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        return this.discoverRecipes(Arrays.asList(recipe)) != 0;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return this.getHandle().awardRecipes(this.bukkitKeysToMinecraftRecipes(recipes));
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return this.undiscoverRecipes(Arrays.asList(recipe)) != 0;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return this.getHandle().resetRecipes(this.bukkitKeysToMinecraftRecipes(recipes));
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        return false;
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        return ImmutableSet.of();
    }

    private Collection<RecipeHolder<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        Collection<RecipeHolder<?>> recipes = new ArrayList<>();
        RecipeManager manager = this.getHandle().level().getServer().getRecipeManager();

        for (NamespacedKey recipeKey : recipeKeys) {
            Optional<? extends RecipeHolder<?>> recipe = manager.byKey(CraftRecipe.toMinecraft(recipeKey));
            if (!recipe.isPresent()) {
                continue;
            }

            recipes.add(recipe.get());
        }

        return recipes;
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
        if (!this.getHandle().getShoulderEntityLeft().isEmpty()) {
            Optional<Entity> shoulder = EntityType.create(this.getHandle().getShoulderEntityLeft(), this.getHandle().level(), EntitySpawnReason.LOAD);

            return (!shoulder.isPresent()) ? null : shoulder.get().getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityLeft(org.bukkit.entity.Entity entity) {
        this.getHandle().setShoulderEntityLeft(entity == null ? new CompoundTag() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
        if (!this.getHandle().getShoulderEntityRight().isEmpty()) {
            Optional<Entity> shoulder = EntityType.create(this.getHandle().getShoulderEntityRight(), this.getHandle().level(), EntitySpawnReason.LOAD);

            return (!shoulder.isPresent()) ? null : shoulder.get().getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityRight(org.bukkit.entity.Entity entity) {
        this.getHandle().setShoulderEntityRight(entity == null ? new CompoundTag() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    // Paper start - move open sign method to HumanEntity
    @Override
    public void openSign(final org.bukkit.block.Sign sign, final org.bukkit.block.sign.Side side) {
        org.bukkit.craftbukkit.block.CraftSign.openSign(sign, (CraftPlayer) this, side);
    }
    // Paper end
    @Override
    public boolean dropItem(boolean dropAll) {
        // Paper start - Fix HumanEntity#drop not updating the client inv
        if (!(this.getHandle() instanceof ServerPlayer player)) return false;
        boolean success = player.drop(dropAll);
        if (!success) return false;
        final net.minecraft.world.entity.player.Inventory inv = player.getInventory();
        final java.util.OptionalInt optionalSlot = player.containerMenu.findSlot(inv, inv.selected);
        optionalSlot.ifPresent(slot -> player.containerSynchronizer.sendSlotChange(player.containerMenu, slot, inv.getSelected()));
        return true;
        // Paper end - Fix HumanEntity#drop not updating the client inv
    }

    @Override
    @Nullable
    public Item dropItem(final int slot, final int amount, final boolean throwRandomly, final @Nullable Consumer<Item> entityOperation) {
        Preconditions.checkArgument(slot >= 0 && slot < this.inventory.getSize(), "Slot %s is not a valid inventory slot.", slot);

        return internalDropItemFromInventory(this.inventory.getItem(slot), amount, throwRandomly, entityOperation);
    }

    @Override
    @Nullable
    public Item dropItem(final @NotNull EquipmentSlot slot, final int amount, final boolean throwRandomly, final @Nullable Consumer<Item> entityOperation) {
        return internalDropItemFromInventory(this.inventory.getItem(slot), amount, throwRandomly, entityOperation);
    }

    @Nullable
    private Item internalDropItemFromInventory(final ItemStack originalItemStack, final int amount, final boolean throwRandomly, final @Nullable Consumer<Item> entityOperation) {
        if (originalItemStack == null || originalItemStack.isEmpty() || amount <= 0) return null;

        final net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.unwrap(originalItemStack);
        final net.minecraft.world.item.ItemStack dropContent = nmsItemStack.split(amount);

        // This will return the itemstack back to its original amount in case events fail
        final ItemEntity droppedEntity = this.getHandle().drop(dropContent, throwRandomly, true, true, entityOperation);
        return droppedEntity == null ? null : (Item) droppedEntity.getBukkitEntity();
    }

    @Override
    @Nullable
    public Item dropItem(final ItemStack itemStack, final boolean throwRandomly, final @Nullable Consumer<Item> entityOperation) {
        // This method implementation differs from the previous dropItem implementations, as it does not source
        // its itemstack from the players inventory. As such, we cannot reuse #internalDropItemFromInventory.
        Preconditions.checkArgument(itemStack != null, "Cannot drop a null itemstack");
        if (itemStack.isEmpty()) return null;

        final net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        // Do *not* call the event here, the item is not in the player inventory, they are not dropping it / do not need recovering logic (which would be a dupe).
        final ItemEntity droppedEntity = this.getHandle().drop(nmsItemStack, throwRandomly, true, false, entityOperation);
        return droppedEntity == null ? null : (Item) droppedEntity.getBukkitEntity();
    }

    @Override
    public float getExhaustion() {
        return this.getHandle().getFoodData().exhaustionLevel;
    }

    @Override
    public void setExhaustion(float value) {
        this.getHandle().getFoodData().exhaustionLevel = value;
    }

    @Override
    public float getSaturation() {
        return this.getHandle().getFoodData().saturationLevel;
    }

    @Override
    public void setSaturation(float value) {
        this.getHandle().getFoodData().saturationLevel = value;
    }

    @Override
    public int getFoodLevel() {
        return this.getHandle().getFoodData().foodLevel;
    }

    @Override
    public void setFoodLevel(int value) {
        this.getHandle().getFoodData().foodLevel = value;
    }

    @Override
    public int getSaturatedRegenRate() {
        return this.getHandle().getFoodData().saturatedRegenRate;
    }

    @Override
    public void setSaturatedRegenRate(int i) {
        this.getHandle().getFoodData().saturatedRegenRate = i;
    }

    @Override
    public int getUnsaturatedRegenRate() {
        return this.getHandle().getFoodData().unsaturatedRegenRate;
    }

    @Override
    public void setUnsaturatedRegenRate(int i) {
        this.getHandle().getFoodData().unsaturatedRegenRate = i;
    }

    @Override
    public int getStarvationRate() {
        return this.getHandle().getFoodData().starvationRate;
    }

    @Override
    public void setStarvationRate(int i) {
        this.getHandle().getFoodData().starvationRate = i;
    }

    @Override
    public Location getLastDeathLocation() {
        return this.getHandle().getLastDeathLocation().map(CraftMemoryMapper::fromNms).orElse(null);
    }

    @Override
    public void setLastDeathLocation(Location location) {
        if (location == null) {
            this.getHandle().setLastDeathLocation(Optional.empty());
        } else {
            this.getHandle().setLastDeathLocation(Optional.of(CraftMemoryMapper.toNms(location)));
        }
    }

    @Override
    public Firework fireworkBoost(ItemStack fireworkItemStack) {
        Preconditions.checkArgument(fireworkItemStack != null, "fireworkItemStack must not be null");
        Preconditions.checkArgument(fireworkItemStack.getType() == Material.FIREWORK_ROCKET, "fireworkItemStack must be of type %s", Material.FIREWORK_ROCKET);

        FireworkRocketEntity fireworks = new FireworkRocketEntity(this.getHandle().level(), CraftItemStack.asNMSCopy(fireworkItemStack), this.getHandle());
        boolean success = this.getHandle().level().addFreshEntity(fireworks, SpawnReason.CUSTOM);
        return success ? (Firework) fireworks.getBukkitEntity() : null;
    }

    @Override
    public org.bukkit.entity.Entity copy() {
        throw new UnsupportedOperationException("Cannot copy human entities");
    }

    @Override
    public org.bukkit.entity.Entity copy(Location location) {
        throw new UnsupportedOperationException("Cannot copy human entities");
    }
}
