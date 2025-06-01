package org.bukkit.craftbukkit.event;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftExplosionResult;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftSpellcaster;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Animals;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Strider;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BellResonateEvent;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.block.VaultDisplayItemEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerBucketFishEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRecipeBookClickEvent;
import org.bukkit.event.player.PlayerRecipeBookSettingsChangeEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.bukkit.event.raid.RaidStopEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class CraftEventFactory {

    // helper methods
    private static boolean canBuild(Level world, Player player, int x, int z) {
        int spawnSize = Bukkit.getServer().getSpawnRadius();

        if (world.dimension() != Level.OVERWORLD) return true;
        if (spawnSize <= 0) return true;
        if (((CraftServer) Bukkit.getServer()).getHandle().getOps().isEmpty()) return true;
        if (player.isOp()) return true;

        BlockPos spawnPos = world.getSharedSpawnPos();

        int distanceFromSpawn = Math.max(Math.abs(x - spawnPos.getX()), Math.abs(z - spawnPos.getZ()));
        return distanceFromSpawn > spawnSize;
    }

    public static boolean callPlayerSignOpenEvent(net.minecraft.world.entity.player.Player player, SignBlockEntity signBlockEntity, boolean front, PlayerSignOpenEvent.Cause cause) {
        final Block block = CraftBlock.at(signBlockEntity.getLevel(), signBlockEntity.getBlockPos());
        final Sign sign = (Sign) CraftBlockStates.getBlockState(block);
        final Side side = (front) ? Side.FRONT : Side.BACK;
        return CraftEventFactory.callPlayerSignOpenEvent((Player) player.getBukkitEntity(), sign, side, cause);
    }

    public static boolean callPlayerSignOpenEvent(Player player, Sign sign, Side side, PlayerSignOpenEvent.Cause cause) {
        final PlayerSignOpenEvent event = new PlayerSignOpenEvent(player, sign, side, cause);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static Either<net.minecraft.world.entity.player.Player.BedSleepingProblem, Unit> callPlayerBedEnterEvent(net.minecraft.world.entity.player.Player player, BlockPos bed, Either<net.minecraft.world.entity.player.Player.BedSleepingProblem, Unit> nmsBedResult) {
        BedEnterResult bedEnterResult = nmsBedResult.mapBoth(sleepingProblem -> {
            return switch (sleepingProblem) {
                case NOT_POSSIBLE_HERE -> BedEnterResult.NOT_POSSIBLE_HERE;
                case NOT_POSSIBLE_NOW -> BedEnterResult.NOT_POSSIBLE_NOW;
                case TOO_FAR_AWAY -> BedEnterResult.TOO_FAR_AWAY;
                case NOT_SAFE -> BedEnterResult.NOT_SAFE;
                case OBSTRUCTED -> BedEnterResult.OBSTRUCTED;
                default -> BedEnterResult.OTHER_PROBLEM;
            };
        }, t -> BedEnterResult.OK).map(java.util.function.Function.identity(), java.util.function.Function.identity());

        PlayerBedEnterEvent event = new PlayerBedEnterEvent((Player) player.getBukkitEntity(), CraftBlock.at(player.level(), bed), bedEnterResult);
        Bukkit.getServer().getPluginManager().callEvent(event);

        Result result = event.useBed();
        if (result == Result.ALLOW) {
            return Either.right(Unit.INSTANCE);
        } else if (result == Result.DENY) {
            return Either.left(net.minecraft.world.entity.player.Player.BedSleepingProblem.OTHER_PROBLEM);
        }

        return nmsBedResult;
    }

    public static EntityEnterLoveModeEvent callEntityEnterLoveModeEvent(net.minecraft.world.entity.player.Player entityHuman, Animal entityAnimal, int loveTicks) {
        EntityEnterLoveModeEvent entityEnterLoveModeEvent = new EntityEnterLoveModeEvent((Animals) entityAnimal.getBukkitEntity(), entityHuman != null ? entityHuman.getBukkitEntity() : null, loveTicks);
        Bukkit.getPluginManager().callEvent(entityEnterLoveModeEvent);
        return entityEnterLoveModeEvent;
    }

    public static PlayerHarvestBlockEvent callPlayerHarvestBlockEvent(Level world, BlockPos pos, net.minecraft.world.entity.player.Player player, InteractionHand hand, List<ItemStack> itemsToHarvest) {
        List<org.bukkit.inventory.ItemStack> bukkitItemsToHarvest = new ArrayList<>(itemsToHarvest.stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList()));
        PlayerHarvestBlockEvent playerHarvestBlockEvent = new PlayerHarvestBlockEvent((Player) player.getBukkitEntity(), CraftBlock.at(world, pos), CraftEquipmentSlot.getHand(hand), bukkitItemsToHarvest);
        Bukkit.getPluginManager().callEvent(playerHarvestBlockEvent);
        return playerHarvestBlockEvent;
    }

    public static PlayerBucketEntityEvent callPlayerFishBucketEvent(net.minecraft.world.entity.LivingEntity fish, net.minecraft.world.entity.player.Player entityHuman, ItemStack originalBucket, ItemStack entityBucket, InteractionHand hand) {
        Player player = (Player) entityHuman.getBukkitEntity();
        EquipmentSlot handSlot = CraftEquipmentSlot.getHand(hand);

        PlayerBucketEntityEvent event;
        if (fish instanceof AbstractFish) {
            event = new PlayerBucketFishEvent(player, (Fish) fish.getBukkitEntity(), CraftItemStack.asBukkitCopy(originalBucket), CraftItemStack.asBukkitCopy(entityBucket), handSlot);
        } else {
            event = new PlayerBucketEntityEvent(player, fish.getBukkitEntity(), CraftItemStack.asBukkitCopy(originalBucket), CraftItemStack.asBukkitCopy(entityBucket), handSlot);
        }
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static TradeSelectEvent callTradeSelectEvent(int newIndex, MerchantMenu merchant) {
        TradeSelectEvent tradeSelectEvent = new TradeSelectEvent(merchant.getBukkitView(), newIndex);
        Bukkit.getPluginManager().callEvent(tradeSelectEvent);
        return tradeSelectEvent;
    }

    @SuppressWarnings("deprecation") // Paper use deprecated event to maintain compat (it extends modern event)
    public static boolean handleBellRingEvent(Level world, BlockPos position, Direction direction, Entity entity) {
        Block block = CraftBlock.at(world, position);
        BlockFace bukkitDirection = CraftBlock.notchToBlockFace(direction);
        BellRingEvent event = new io.papermc.paper.event.block.BellRingEvent(block, bukkitDirection, (entity != null) ? entity.getBukkitEntity() : null); // Paper - deprecated BellRingEvent
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static Stream<net.minecraft.world.entity.LivingEntity> handleBellResonateEvent(Level world, BlockPos position, List<LivingEntity> bukkitEntities) {
        Block block = CraftBlock.at(world, position);
        BellResonateEvent event = new BellResonateEvent(block, bukkitEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event.getResonatedEntities().stream().map((bukkitEntity) -> ((CraftLivingEntity) bukkitEntity).getHandle());
    }

    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(ServerLevel level, net.minecraft.world.entity.player.Player player, InteractionHand hand, List<BlockState> blockStates, BlockPos clickedPos) {
        Player cplayer = (Player) player.getBukkitEntity();
        Block clickedBlock = CraftBlock.at(level, clickedPos);

        boolean canBuild = true;
        for (BlockState blockState : blockStates) {
            if (!CraftEventFactory.canBuild(level, cplayer, blockState.getX(), blockState.getZ())) {
                canBuild = false;
                break;
            }
        }

        EquipmentSlot handSlot = CraftEquipmentSlot.getHand(hand);
        BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, clickedBlock, cplayer.getInventory().getItem(handSlot), cplayer, canBuild, handSlot);
        event.callEvent();

        return event;
    }

    public static BlockPlaceEvent callBlockPlaceEvent(ServerLevel level, net.minecraft.world.entity.player.Player player, InteractionHand hand, BlockState replacedSnapshot, BlockPos clickedPos) {
        Player cplayer = (Player) player.getBukkitEntity();

        Block clickedBlock = CraftBlock.at(level, clickedPos);
        Block placedBlock = replacedSnapshot.getBlock();

        boolean canBuild = CraftEventFactory.canBuild(level, cplayer, placedBlock.getX(), placedBlock.getZ());

        EquipmentSlot handSlot = CraftEquipmentSlot.getHand(hand);
        BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedSnapshot, clickedBlock, cplayer.getInventory().getItem(handSlot), cplayer, canBuild, handSlot);
        event.callEvent();

        return event;
    }

    public static void handleBlockDropItemEvent(Block block, BlockState state, ServerPlayer player, List<ItemEntity> items) {
        List<Item> list = new ArrayList<>();
        for (ItemEntity item : items) {
            list.add((Item) item.getBukkitEntity());
        }

        BlockDropItemEvent event = new BlockDropItemEvent(block, state, player.getBukkitEntity(), list);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            for (Item bukkit : list) {
                if (!bukkit.isValid()) {
                    Entity item = ((org.bukkit.craftbukkit.entity.CraftItem) bukkit).getHandle();
                    item.level().addFreshEntity(item);
                }
            }
        } else {
            for (Item bukkit : list) {
                if (bukkit.isValid()) {
                    bukkit.remove();
                }
            }
        }
    }

    public static EntityPlaceEvent callEntityPlaceEvent(UseOnContext context, Entity entity) {
        return CraftEventFactory.callEntityPlaceEvent(context.getLevel(), context.getClickedPos(), context.getClickedFace(), context.getPlayer(), entity, context.getHand());
    }

    public static EntityPlaceEvent callEntityPlaceEvent(Level world, BlockPos clickedPos, Direction clickedFace, net.minecraft.world.entity.player.Player player, Entity entity, InteractionHand hand) {
        Player cplayer = (player == null) ? null : (Player) player.getBukkitEntity();
        org.bukkit.block.Block clickedBlock = CraftBlock.at(world, clickedPos);
        org.bukkit.block.BlockFace blockFace = org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(clickedFace);

        EntityPlaceEvent event = new EntityPlaceEvent(entity.getBukkitEntity(), cplayer, clickedBlock, blockFace, CraftEquipmentSlot.getHand(hand));
        entity.level().getCraftServer().getPluginManager().callEvent(event);

        return event;
    }

    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(Level world, net.minecraft.world.entity.player.Player player, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack itemInHand, InteractionHand hand) {
        return (PlayerBucketEmptyEvent) CraftEventFactory.getPlayerBucketEvent(false, world, player, changed, clicked, clickedFace, itemInHand, Items.BUCKET, hand);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(Level world, net.minecraft.world.entity.player.Player player, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack itemInHand, net.minecraft.world.item.Item bucket, InteractionHand hand) {
        return (PlayerBucketFillEvent) CraftEventFactory.getPlayerBucketEvent(true, world, player, clicked, changed, clickedFace, itemInHand, bucket, hand);
    }

    private static PlayerEvent getPlayerBucketEvent(boolean isFilling, Level world, net.minecraft.world.entity.player.Player player, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack bucket, net.minecraft.world.item.Item item, InteractionHand hand) {
        Player cplayer = (Player) player.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        Material bucketItem = CraftItemType.minecraftToBukkit(bucket.getItem());

        Block block = CraftBlock.at(world, changed);
        Block clickedBlock = CraftBlock.at(world, clicked);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);
        EquipmentSlot handSlot = CraftEquipmentSlot.getHand(hand);

        PlayerEvent event;
        if (isFilling) {
            event = new PlayerBucketFillEvent(cplayer, block, clickedBlock, blockFace, bucketItem, itemInHand, handSlot);
            ((PlayerBucketFillEvent) event).setCancelled(!CraftEventFactory.canBuild(world, cplayer, changed.getX(), changed.getZ()));
        } else {
            event = new PlayerBucketEmptyEvent(cplayer, block, clickedBlock, blockFace, bucketItem, itemInHand, handSlot);
            ((PlayerBucketEmptyEvent) event).setCancelled(!CraftEventFactory.canBuild(world, cplayer, changed.getX(), changed.getZ()));
        }

        event.callEvent();

        return event;
    }

    public static PlayerInteractEvent callPlayerInteractEvent(net.minecraft.world.entity.player.Player player, Action action, ItemStack item, InteractionHand hand) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new AssertionError(String.format("%s performing %s with %s", player, action, item));
        }
        return CraftEventFactory.callPlayerInteractEvent(player, action, null, Direction.SOUTH, item, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(net.minecraft.world.entity.player.Player player, Action action, BlockPos pos, Direction direction, ItemStack item, InteractionHand hand) {
        return CraftEventFactory.callPlayerInteractEvent(player, action, pos, direction, item, false, hand, null);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(net.minecraft.world.entity.player.Player player, Action action, BlockPos pos, Direction direction, ItemStack item, boolean cancelledBlock, InteractionHand hand, Vec3 targetPos) {
        return CraftEventFactory.callPlayerInteractEvent(player, action, pos, direction, item, cancelledBlock, false, hand, targetPos);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(net.minecraft.world.entity.player.Player player, Action action, BlockPos pos, Direction direction, ItemStack item, boolean cancelledBlock, boolean cancelledItem, InteractionHand hand, Vec3 targetPos) {
        Player cplayer = (player == null) ? null : (Player) player.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(item);

        Vector clickedPos = null;
        if (pos != null && targetPos != null) {
            clickedPos = CraftVector.toBukkit(targetPos.subtract(Vec3.atLowerCornerOf(pos)));
        }

        CraftServer craftServer = (CraftServer) cplayer.getServer();
        Block clickedBlock = null;
        if (pos != null) {
            clickedBlock = org.bukkit.craftbukkit.block.CraftBlock.at(player.level(), pos);
        } else {
            switch (action) {
                case LEFT_CLICK_BLOCK:
                    action = Action.LEFT_CLICK_AIR;
                    break;
                case RIGHT_CLICK_BLOCK:
                    action = Action.RIGHT_CLICK_AIR;
                    break;
            }
        }
        BlockFace blockFace = CraftBlock.notchToBlockFace(direction);

        if (itemInHand.isEmpty()) {
            itemInHand = null;
        }

        PlayerInteractEvent event = new PlayerInteractEvent(cplayer, action, itemInHand, clickedBlock, blockFace, (hand == null) ? null : ((hand == InteractionHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND), clickedPos);
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        if (cancelledItem) {
            event.setUseItemInHand(Result.DENY);
        }
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    public static EntityTransformEvent callEntityTransformEvent(net.minecraft.world.entity.LivingEntity original, net.minecraft.world.entity.LivingEntity coverted, EntityTransformEvent.TransformReason transformReason) {
        return CraftEventFactory.callEntityTransformEvent(original, Collections.singletonList(coverted), transformReason);
    }

    public static EntityTransformEvent callEntityTransformEvent(net.minecraft.world.entity.LivingEntity original, List<net.minecraft.world.entity.LivingEntity> convertedList, EntityTransformEvent.TransformReason convertType) {
        List<org.bukkit.entity.Entity> list = new ArrayList<>();
        for (net.minecraft.world.entity.LivingEntity entityLiving : convertedList) {
            list.add(entityLiving.getBukkitEntity());
        }

        EntityTransformEvent event = new EntityTransformEvent(original.getBukkitEntity(), list, convertType);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static EntityShootBowEvent callEntityShootBowEvent(net.minecraft.world.entity.LivingEntity entity, ItemStack bow, ItemStack consumableItem, Entity entityArrow, InteractionHand hand, float force, boolean consumeItem) {
        LivingEntity shooter = (LivingEntity) entity.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(bow);
        CraftItemStack itemConsumable = CraftItemStack.asCraftMirror(consumableItem);
        org.bukkit.entity.Entity arrow = entityArrow.getBukkitEntity();

        if (itemInHand.isEmpty()) {
            itemInHand = null;
        }

        EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, itemConsumable, arrow, CraftEquipmentSlot.getHand(hand), force, consumeItem);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static VillagerCareerChangeEvent callVillagerCareerChangeEvent(net.minecraft.world.entity.npc.Villager villager, Profession future, VillagerCareerChangeEvent.ChangeReason reason) {
        VillagerCareerChangeEvent event = new VillagerCareerChangeEvent((Villager) villager.getBukkitEntity(), future, reason);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static BlockDamageEvent callBlockDamageEvent(ServerPlayer player, BlockPos pos, Direction direction, ItemStack item, boolean instaBreak) {
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(item);
        Block clickedBlock = CraftBlock.at(player.level(), pos);

        BlockDamageEvent event = new BlockDamageEvent(player.getBukkitEntity(), clickedBlock, CraftBlock.notchToBlockFace(direction), itemInHand, instaBreak);
        event.callEvent();

        return event;
    }

    public static BlockDamageAbortEvent callBlockDamageAbortEvent(ServerPlayer player, BlockPos pos, ItemStack item) {
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(item);
        Block clickedBlock = CraftBlock.at(player.level(), pos);

        BlockDamageAbortEvent event = new BlockDamageAbortEvent(player.getBukkitEntity(), clickedBlock, itemInHand);
        event.callEvent();

        return event;
    }

    public static boolean doEntityAddEventCalling(Level world, Entity entity, SpawnReason spawnReason) {
        if (entity == null) return false;

        org.bukkit.event.Cancellable event = null;
        if (entity instanceof net.minecraft.world.entity.LivingEntity && !(entity instanceof ServerPlayer)) {
            boolean isAnimal = entity instanceof Animal || entity instanceof WaterAnimal || entity instanceof AbstractGolem;
            boolean isMonster = entity instanceof Monster || entity instanceof Ghast || entity instanceof Slime;

            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !world.getWorld().getAllowAnimals() || isMonster && !world.getWorld().getAllowMonsters()) {
                    entity.discard(null); // Add Bukkit remove cause
                    return false;
                }
            }

            event = CraftEventFactory.callCreatureSpawnEvent((net.minecraft.world.entity.LivingEntity) entity, spawnReason);
        } else if (entity instanceof ItemEntity) {
            event = CraftEventFactory.callItemSpawnEvent((ItemEntity) entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Vehicle) {
            event = CraftEventFactory.callVehicleCreateEvent(entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.LightningStrike) {
            LightningStrikeEvent.Cause cause = switch (spawnReason) {
                case COMMAND -> LightningStrikeEvent.Cause.COMMAND;
                case CUSTOM -> LightningStrikeEvent.Cause.CUSTOM;
                case SPAWNER -> LightningStrikeEvent.Cause.SPAWNER;
                default -> LightningStrikeEvent.Cause.UNKNOWN;
            };
            // This event is called in nms-patches for common causes like Weather, Trap or Trident (SpawnReason.DEFAULT) then can ignore this cases for avoid two calls to this event
            if (cause == LightningStrikeEvent.Cause.UNKNOWN && spawnReason == SpawnReason.DEFAULT) {
                return true;
            }
            event = CraftEventFactory.callLightningStrikeEvent((LightningStrike) entity.getBukkitEntity(), cause);
        } else if (!(entity instanceof ServerPlayer)) {
            event = CraftEventFactory.callEntitySpawnEvent(entity);
        }

        if (event != null && (event.isCancelled() || entity.isRemoved())) {
            Entity vehicle = entity.getVehicle();
            if (vehicle != null) {
                vehicle.discard(null); // Add Bukkit remove cause
            }
            for (Entity passenger : entity.getIndirectPassengers()) {
                passenger.discard(null); // Add Bukkit remove cause
            }
            entity.discard(null); // Add Bukkit remove cause
            return false;
        }

        // Spigot start - SPIGOT-7523: Merge after spawn event and only merge if the event was not cancelled (gets checked above)
        if (entity instanceof net.minecraft.world.entity.ExperienceOrb xp) {
            double radius = world.spigotConfig.expMerge;
            event = CraftEventFactory.callEntitySpawnEvent(entity); // Call spawn event for ExperienceOrb entities
            if (radius > 0 && !event.isCancelled() && !entity.isRemoved()) {
                // Paper start - Maximum exp value when merging; Whole section has been tweaked, see comments for specifics
                final long maxValue = world.paperConfig().entities.behavior.experienceMergeMaxValue;
                final boolean mergeUnconditionally = maxValue <= 0;
                if (mergeUnconditionally || xp.getValue() < maxValue) { // Paper - Skip iteration if unnecessary

                List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().inflate(radius, radius, radius));
                for (Entity e : entities) {
                    if (e instanceof net.minecraft.world.entity.ExperienceOrb loopItem) {
                        // Paper start
                        if (!loopItem.isRemoved() && xp.count == loopItem.count && (mergeUnconditionally || loopItem.getValue() < maxValue) && new com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent((org.bukkit.entity.ExperienceOrb) entity.getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) loopItem.getBukkitEntity()).callEvent()) { // Paper - ExperienceOrbMergeEvent
                            long newTotal = (long)xp.getValue() + (long)loopItem.getValue();
                            if ((int) newTotal < 0) continue; // Overflow
                            if (!mergeUnconditionally && newTotal > maxValue) {
                                loopItem.setValue((int) (newTotal - maxValue));
                                xp.setValue((int) maxValue);
                            } else {
                            xp.setValue(xp.getValue() + loopItem.getValue());
                            loopItem.discard(null); // Add Bukkit remove cause
                            } // Paper end - Maximum exp value when merging
                        }
                    }
                }
                } // Paper end - End iteration skip check - All tweaking ends here
            }
        }
        // Spigot end

        return true;
    }

    public static EntitySpawnEvent callEntitySpawnEvent(Entity entity) {
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

        EntitySpawnEvent event = new EntitySpawnEvent(bukkitEntity);
        bukkitEntity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static CreatureSpawnEvent callCreatureSpawnEvent(net.minecraft.world.entity.LivingEntity entityliving, SpawnReason spawnReason) {
        LivingEntity entity = (LivingEntity) entityliving.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTameEvent callEntityTameEvent(Mob entity, net.minecraft.world.entity.player.Player tamer) {
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
        org.bukkit.entity.AnimalTamer bukkitTamer = (tamer != null ? tamer.getBukkitEntity() : null);
        CraftServer craftServer = (CraftServer) bukkitEntity.getServer();

        EntityTameEvent event = new EntityTameEvent((LivingEntity) bukkitEntity, bukkitTamer);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemSpawnEvent callItemSpawnEvent(ItemEntity entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        ItemSpawnEvent event = new ItemSpawnEvent(entity);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemDespawnEvent callItemDespawnEvent(ItemEntity entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();

        ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());

        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static boolean callItemMergeEvent(ItemEntity merging, ItemEntity mergingWith) {
        org.bukkit.entity.Item entityMerging = (org.bukkit.entity.Item) merging.getBukkitEntity();
        org.bukkit.entity.Item entityMergingWith = (org.bukkit.entity.Item) mergingWith.getBukkitEntity();

        ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);

        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static PotionSplashEvent callPotionSplashEvent(net.minecraft.world.entity.projectile.ThrownSplashPotion potion, @Nullable HitResult position, Map<LivingEntity, Double> affectedEntities) { // Paper - nullable hitResult
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();

        Block hitBlock = null;
        BlockFace hitFace = null;
        if (position != null && position.getType() == HitResult.Type.BLOCK) { // Paper - nullable hitResult
            BlockHitResult positionBlock = (BlockHitResult) position;
            hitBlock = CraftBlock.at(potion.level(), positionBlock.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        }

        org.bukkit.entity.Entity hitEntity = null;
        if (position != null && position.getType() == HitResult.Type.ENTITY) { // Paper - nullable hitResult
            hitEntity = ((EntityHitResult) position).getEntity().getBukkitEntity();
        }

        PotionSplashEvent event = new PotionSplashEvent(thrownPotion, hitEntity, hitBlock, hitFace, affectedEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LingeringPotionSplashEvent callLingeringPotionSplashEvent(net.minecraft.world.entity.projectile.ThrownLingeringPotion potion, @Nullable HitResult position, net.minecraft.world.entity.AreaEffectCloud cloud) { // Paper - nullable hitResult
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();
        AreaEffectCloud effectCloud = (AreaEffectCloud) cloud.getBukkitEntity();

        Block hitBlock = null;
        BlockFace hitFace = null;
        if (position != null && position.getType() == HitResult.Type.BLOCK) { // Paper
            BlockHitResult positionBlock = (BlockHitResult) position;
            hitBlock = CraftBlock.at(potion.level(), positionBlock.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        }

        org.bukkit.entity.Entity hitEntity = null;
        if (position != null && position.getType() == HitResult.Type.ENTITY) { // Paper
            hitEntity = ((EntityHitResult) position).getEntity().getBukkitEntity();
        }

        LingeringPotionSplashEvent event = new LingeringPotionSplashEvent(thrownPotion, hitEntity, hitBlock, hitFace, effectCloud);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    // Paper start - Fix potions splash events
    public static io.papermc.paper.event.entity.WaterBottleSplashEvent callWaterBottleSplashEvent(net.minecraft.world.entity.projectile.AbstractThrownPotion potion, @Nullable HitResult hitResult, Map<LivingEntity, Double> affectedEntities, java.util.Set<LivingEntity> rehydrate, java.util.Set<LivingEntity> extinguish) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();

        Block hitBlock = null;
        BlockFace hitFace = null;
        org.bukkit.entity.Entity hitEntity = null;

        if (hitResult != null) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                hitBlock = CraftBlock.at(potion.level(), blockHitResult.getBlockPos());
                hitFace = CraftBlock.notchToBlockFace(blockHitResult.getDirection());
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                hitEntity = ((EntityHitResult) hitResult).getEntity().getBukkitEntity();
            }
        }

        io.papermc.paper.event.entity.WaterBottleSplashEvent event = new io.papermc.paper.event.entity.WaterBottleSplashEvent(
            thrownPotion, hitEntity, hitBlock, hitFace, affectedEntities, rehydrate, extinguish
        );
        event.callEvent();
        return event;
    }
    // Paper end - Fix potions splash events

    public static BlockFadeEvent callBlockFadeEvent(LevelAccessor world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        CraftBlockState snapshot = CraftBlockStates.getBlockState(world, pos);
        snapshot.setData(state);

        BlockFadeEvent event = new BlockFadeEvent(snapshot.getBlock(), snapshot);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleMoistureChangeEvent(Level world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags) {
        CraftBlockState snapshot = CraftBlockStates.getBlockState(world, pos);
        snapshot.setData(state);

        MoistureChangeEvent event = new MoistureChangeEvent(snapshot.getBlock(), snapshot);
        if (event.callEvent()) {
            snapshot.place(flags);
            return true;
        }
        return false;
    }

    public static BlockPos sourceBlockOverride = null; // SPIGOT-7068: Add source block override, not the most elegant way but better than passing down a BlockPos up to five methods deep.

    public static boolean handleBlockSpreadEvent(LevelAccessor world, BlockPos source, BlockPos target, net.minecraft.world.level.block.state.BlockState state, int flags) {
        return handleBlockSpreadEvent(world, source, target, state, flags, false);
    }

    public static boolean handleBlockSpreadEvent(LevelAccessor world, BlockPos source, BlockPos target, net.minecraft.world.level.block.state.BlockState state, int flags, boolean checkSetResult) {
        // Suppress during worldgen
        if (!(world instanceof Level)) {
            boolean result = world.setBlock(target, state, flags);
            return !checkSetResult || result;
        }

        CraftBlockState snapshot = CraftBlockStates.getBlockState(world, target);
        snapshot.setData(state);

        BlockSpreadEvent event = new BlockSpreadEvent(snapshot.getBlock(), CraftBlock.at(world, CraftEventFactory.sourceBlockOverride != null ? CraftEventFactory.sourceBlockOverride : source), snapshot);
        if (event.callEvent()) {
            boolean result = snapshot.place(flags);
            return !checkSetResult || result;
        }
        return false;
    }

    public static EntityDeathEvent callEntityDeathEvent(net.minecraft.world.entity.LivingEntity victim, DamageSource damageSource) {
        return CraftEventFactory.callEntityDeathEvent(victim, damageSource, new ArrayList<>(0)); // Paper - Restore vanilla drops behavior
    }

    public static EntityDeathEvent callEntityDeathEvent(net.minecraft.world.entity.LivingEntity victim, DamageSource damageSource, List<Entity.DefaultDrop> drops) { // Paper - Restore vanilla drops behavior
        // Paper start
        return CraftEventFactory.callEntityDeathEvent(victim, damageSource, drops, com.google.common.util.concurrent.Runnables.doNothing());
    }

    private static final java.util.function.Function<org.bukkit.inventory.ItemStack, Entity.DefaultDrop> FROM_FUNCTION = stack -> {
        if (stack == null) return null;
        return new Entity.DefaultDrop(CraftItemType.bukkitToMinecraft(stack.getType()), stack, null);
    };

    public static EntityDeathEvent callEntityDeathEvent(net.minecraft.world.entity.LivingEntity victim, DamageSource damageSource, List<Entity.DefaultDrop> drops, Runnable lootCheck) { // Paper - Restore vanilla drops behavior
        // Paper end
        CraftLivingEntity entity = (CraftLivingEntity) victim.getBukkitEntity();
        CraftDamageSource bukkitDamageSource = new CraftDamageSource(damageSource);
        CraftWorld world = (CraftWorld) entity.getWorld();
        EntityDeathEvent event = new EntityDeathEvent(entity, bukkitDamageSource, new io.papermc.paper.util.TransformingRandomAccessList<>(drops, Entity.DefaultDrop::stack, FROM_FUNCTION), victim.getExpReward(world.getHandle(), damageSource.getEntity())); // Paper - Restore vanilla drops behavior
        populateFields(victim, event); // Paper - make cancellable
        Bukkit.getServer().getPluginManager().callEvent(event);

        // Paper start - make cancellable
        if (event.isCancelled()) {
            return event;
        }
        playDeathSound(victim, event, damageSource);
        // Paper end
        victim.expToDrop = event.getDroppedExp();
        lootCheck.run(); // Paper - advancement triggers before destroying items

        // Paper start - Restore vanilla drops behavior
        for (Entity.DefaultDrop drop : drops) {
            if (drop == null) continue;
            final org.bukkit.inventory.ItemStack stack = drop.stack();
        // Paper end - Restore vanilla drops behavior
            if (stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0) continue;

            drop.runConsumer(s -> world.dropItem(entity.getLocation(), s)); // Paper - Restore vanilla drops behavior
            if (stack instanceof CraftItemStack) stack.setAmount(0); // Paper - destroy this item - if this ever leaks due to game bugs, ensure it doesn't dupe, but don't nuke bukkit stacks of manually added items
        }

        return event;
    }

    public static PlayerDeathEvent callPlayerDeathEvent(ServerPlayer victim, DamageSource damageSource, List<Entity.DefaultDrop> drops, net.kyori.adventure.text.Component deathMessage, boolean showDeathMessages, boolean keepInventory) {
        CraftPlayer entity = victim.getBukkitEntity();
        CraftDamageSource bukkitDamageSource = new CraftDamageSource(damageSource);
        PlayerDeathEvent event = new PlayerDeathEvent(entity, bukkitDamageSource, new io.papermc.paper.util.TransformingRandomAccessList<>(drops, Entity.DefaultDrop::stack, FROM_FUNCTION), victim.getExpReward(victim.level(), damageSource.getEntity()), 0, deathMessage, showDeathMessages);
        event.setKeepInventory(keepInventory);
        event.setKeepLevel(victim.keepLevel); // SPIGOT-2222: pre-set keepLevel
        populateFields(victim, event); // Paper - make cancellable
        Bukkit.getServer().getPluginManager().callEvent(event);
        // Paper start - make cancellable
        if (event.isCancelled()) {
            return event;
        }
        playDeathSound(victim, event, damageSource);
        // Paper end

        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();

        // Paper start - Restore vanilla drops behavior
        for (Entity.DefaultDrop drop : drops) {
            if (drop == null) continue;
            final org.bukkit.inventory.ItemStack stack = drop.stack();
        // Paper end - Restore vanilla drops behavior
            if (stack == null || stack.getType() == Material.AIR) continue;

            drop.runConsumer(s -> victim.drop(CraftItemStack.unwrap(s), true, false)); // Paper - Restore vanilla drops behavior
        }

        return event;
    }

    // Paper start - helper methods for making death event cancellable
    // Add information to death event
    private static void populateFields(net.minecraft.world.entity.LivingEntity victim, EntityDeathEvent event) {
        event.setReviveHealth(event.getEntity().getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue());
        event.setShouldPlayDeathSound(!victim.silentDeath && !victim.isSilent());
        net.minecraft.sounds.SoundEvent soundEffect = victim.getDeathSound();
        event.setDeathSound(soundEffect != null ? org.bukkit.craftbukkit.CraftSound.minecraftToBukkit(soundEffect) : null);
        event.setDeathSoundCategory(org.bukkit.SoundCategory.valueOf(victim.getSoundSource().name()));
        event.setDeathSoundVolume(victim.getSoundVolume());
        event.setDeathSoundPitch(victim.getVoicePitch());
    }

    // Play death sound manually
    private static void playDeathSound(net.minecraft.world.entity.LivingEntity victim, EntityDeathEvent event, DamageSource damageSource) {
        if (event.shouldPlayDeathSound() && event.getDeathSound() != null && event.getDeathSoundCategory() != null) {
            net.minecraft.world.entity.player.Player source = victim instanceof net.minecraft.world.entity.player.Player ? (net.minecraft.world.entity.player.Player) victim : null;
            double x = event.getEntity().getLocation().getX();
            double y = event.getEntity().getLocation().getY();
            double z = event.getEntity().getLocation().getZ();
            net.minecraft.sounds.SoundEvent soundEffect = org.bukkit.craftbukkit.CraftSound.bukkitToMinecraft(event.getDeathSound());
            net.minecraft.sounds.SoundSource soundCategory = net.minecraft.sounds.SoundSource.valueOf(event.getDeathSoundCategory().name());
            victim.level().playSound(source, x, y, z, soundEffect, soundCategory, event.getDeathSoundVolume(), event.getDeathSoundPitch());
            victim.playSecondaryHurtSound(damageSource);
        }
    }
    // Paper end

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        return CraftEventFactory.handleEntityDamageEvent(entity, source, modifiers, modifierFunctions, false);
    }

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions, boolean cancelled) {
        CraftDamageSource bukkitDamageSource = new CraftDamageSource(source);
        final Entity damager = source.eventEntityDamager() != null ? source.eventEntityDamager() : source.getDirectEntity(); // Paper - fix DamageSource API
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            if (damager == null) {
                return CraftEventFactory.callEntityDamageEvent(source.eventBlockDamager(), source.causingBlockSnapshot(), entity, DamageCause.BLOCK_EXPLOSION, bukkitDamageSource, modifiers, modifierFunctions, cancelled);
            }
            DamageCause damageCause = (damager.getBukkitEntity() instanceof org.bukkit.entity.TNTPrimed) ? DamageCause.BLOCK_EXPLOSION : DamageCause.ENTITY_EXPLOSION;
            return CraftEventFactory.callEntityDamageEvent(damager, entity, damageCause, bukkitDamageSource, modifiers, modifierFunctions, cancelled, source.isCritical()); // Paper - add critical damage API
        } else if (damager != null || source.getDirectEntity() != null) {
            DamageCause cause = DamageCause.ENTITY_ATTACK;

            if (source.knownCause() != null) {
                cause = source.knownCause();
            } else if (damager instanceof net.minecraft.world.entity.projectile.Projectile) {
                if (damager.getBukkitEntity() instanceof ThrownPotion) {
                    cause = DamageCause.MAGIC;
                } else if (damager.getBukkitEntity() instanceof Projectile) {
                    cause = DamageCause.PROJECTILE;
                }
            } else if (source.is(DamageTypes.THORNS)) {
                cause = DamageCause.THORNS;
            } else if (source.is(DamageTypes.SONIC_BOOM)) {
                cause = DamageCause.SONIC_BOOM;
            } else if (source.is(DamageTypes.FALLING_STALACTITE) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.FALLING_ANVIL)) {
                cause = DamageCause.FALLING_BLOCK;
            } else if (source.is(DamageTypes.LIGHTNING_BOLT)) {
                cause = DamageCause.LIGHTNING;
            } else if (source.is(DamageTypes.FALL)) {
                cause = DamageCause.FALL;
            } else if (source.is(DamageTypes.DRAGON_BREATH)) {
                cause = DamageCause.DRAGON_BREATH;
            } else if (source.is(DamageTypes.MAGIC)) {
                cause = DamageCause.MAGIC;
            }

            return CraftEventFactory.callEntityDamageEvent(damager, entity, cause, bukkitDamageSource, modifiers, modifierFunctions, cancelled, source.isCritical()); // Paper - add critical damage API
        } else if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return CraftEventFactory.callEntityDamageEvent(source.eventBlockDamager(), source.causingBlockSnapshot(), entity, DamageCause.VOID, bukkitDamageSource, modifiers, modifierFunctions, cancelled);
        } else if (source.is(DamageTypes.LAVA)) {
            return CraftEventFactory.callEntityDamageEvent(source.eventBlockDamager(), source.causingBlockSnapshot(), entity, DamageCause.LAVA, bukkitDamageSource, modifiers, modifierFunctions, cancelled);
        } else if (source.eventBlockDamager() != null) {
            DamageCause cause;
            if (source.knownCause() != null) {
                cause = source.knownCause();
            } else if (source.is(DamageTypes.CACTUS) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.STALAGMITE) || source.is(DamageTypes.FALLING_STALACTITE) || source.is(DamageTypes.FALLING_ANVIL)) {
                cause = DamageCause.CONTACT;
            } else if (source.is(DamageTypes.HOT_FLOOR)) {
                cause = DamageCause.HOT_FLOOR;
            } else if (source.is(DamageTypes.MAGIC)) {
                cause = DamageCause.MAGIC;
            } else if (source.is(DamageTypes.IN_FIRE)) {
                cause = DamageCause.FIRE;
            } else if (source.is(DamageTypes.CAMPFIRE)) {
                cause = DamageCause.CAMPFIRE;
            } else {
                cause = DamageCause.CUSTOM;
            }
            return CraftEventFactory.callEntityDamageEvent(source.eventBlockDamager(), source.causingBlockSnapshot(), entity, cause, bukkitDamageSource, modifiers, modifierFunctions, cancelled);
        }

        DamageCause cause;
        if (source.knownCause() != null) {
            cause = source.knownCause();
        } else if (source.is(DamageTypes.IN_FIRE)) {
            cause = DamageCause.FIRE;
        } else if (source.is(DamageTypes.STARVE)) {
            cause = DamageCause.STARVATION;
        } else if (source.is(DamageTypes.WITHER)) {
            cause = DamageCause.WITHER;
        } else if (source.is(DamageTypes.IN_WALL)) {
            cause = DamageCause.SUFFOCATION;
        } else if (source.is(DamageTypes.DROWN)) {
            cause = DamageCause.DROWNING;
        } else if (source.is(DamageTypes.ON_FIRE)) {
            cause = DamageCause.FIRE_TICK;
        } else if (source.is(DamageTypes.MAGIC)) {
            cause = DamageCause.MAGIC;
        } else if (source.is(DamageTypes.FALL)) {
            cause = DamageCause.FALL;
        } else if (source.is(DamageTypes.FLY_INTO_WALL)) {
            cause = DamageCause.FLY_INTO_WALL;
        } else if (source.is(DamageTypes.CRAMMING)) {
            cause = DamageCause.CRAMMING;
        } else if (source.is(DamageTypes.DRY_OUT)) {
            cause = DamageCause.DRYOUT;
        } else if (source.is(DamageTypes.FREEZE)) {
            cause = DamageCause.FREEZE;
        } else if (source.is(DamageTypes.GENERIC_KILL)) {
            cause = DamageCause.KILL;
        } else if (source.is(DamageTypes.OUTSIDE_BORDER)) {
            cause = DamageCause.WORLD_BORDER;
        } else {
            cause = DamageCause.CUSTOM;
        }

        return CraftEventFactory.callEntityDamageEvent(null, entity, cause, bukkitDamageSource, modifiers, modifierFunctions, cancelled, source.isCritical()); // Paper - add critical damage API
    }

    private static EntityDamageEvent callEntityDamageEvent(Entity damager, Entity damagee, DamageCause cause, org.bukkit.damage.DamageSource bukkitDamageSource, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions, boolean cancelled, boolean critical) { // Paper - add critical damage API
        EntityDamageEvent event;
        if (damager != null) {
            event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, bukkitDamageSource, modifiers, modifierFunctions, critical);
        } else {
            event = new EntityDamageEvent(damagee.getBukkitEntity(), cause, bukkitDamageSource, modifiers, modifierFunctions);
        }
        return CraftEventFactory.callEntityDamageEvent(event, damagee, cancelled);
    }

    private static EntityDamageEvent callEntityDamageEvent(Block damager, BlockState damagerState, Entity damagee, DamageCause cause, org.bukkit.damage.DamageSource bukkitDamageSource, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions, boolean cancelled) {
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagerState, damagee.getBukkitEntity(), cause, bukkitDamageSource, modifiers, modifierFunctions);
        return CraftEventFactory.callEntityDamageEvent(event, damagee, cancelled);
    }

    private static EntityDamageEvent callEntityDamageEvent(EntityDamageEvent event, Entity damagee, boolean cancelled) {
        event.setCancelled(cancelled);
        event.callEvent();

        if (!event.isCancelled()) {
            event.getEntity().setLastDamageCause(event);
        } else {
            damagee.lastDamageCancelled = true; // SPIGOT-5339, SPIGOT-6252, SPIGOT-6777: Keep track if the event was canceled
        }

        return event;
    }

    private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);

    public static EntityDamageEvent handleLivingEntityDamageEvent(Entity damagee, DamageSource source, double rawDamage, double freezingModifier, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function<Double, Double> freezing, Function<Double, Double> hardHat, Function<Double, Double> blocking, Function<Double, Double> armor, Function<Double, Double> resistance, Function<Double, Double> magic, Function<Double, Double> absorption) {
    // Paper start - fix invulnerability reduction in EntityDamageEvent
        return handleLivingEntityDamageEvent(damagee, source, rawDamage, freezingModifier, hardHatModifier, blockingModifier, armorModifier, resistanceModifier, magicModifier, absorptionModifier, freezing, hardHat, blocking, armor, resistance, magic, absorption, null);
    }
    public static EntityDamageEvent handleLivingEntityDamageEvent(Entity damagee, DamageSource source, double rawDamage, double freezingModifier, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function<Double, Double> freezing, Function<Double, Double> hardHat, Function<Double, Double> blocking, Function<Double, Double> armor, Function<Double, Double> resistance, Function<Double, Double> magic, Function<Double, Double> absorption, java.util.function.BiConsumer<Map<DamageModifier, Double>, Map<DamageModifier, Function<? super Double, Double>>> callback) {
    // Paper end - fix invulnerability reduction in EntityDamageEvent
        Map<DamageModifier, Double> modifiers = new EnumMap<>(DamageModifier.class);
        Map<DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap<>(DamageModifier.class);
        modifiers.put(DamageModifier.BASE, rawDamage);
        modifierFunctions.put(DamageModifier.BASE, CraftEventFactory.ZERO);
        if (source.is(DamageTypeTags.IS_FREEZING)) { // Paper
            modifiers.put(DamageModifier.FREEZING, freezingModifier);
            modifierFunctions.put(DamageModifier.FREEZING, freezing);
        }
        if (source.is(DamageTypeTags.DAMAGES_HELMET)) { // Paper
            modifiers.put(DamageModifier.HARD_HAT, hardHatModifier);
            modifierFunctions.put(DamageModifier.HARD_HAT, hardHat);
        }
        if (damagee instanceof net.minecraft.world.entity.player.Player) {
            modifiers.put(DamageModifier.BLOCKING, blockingModifier);
            modifierFunctions.put(DamageModifier.BLOCKING, blocking);
        }
        modifiers.put(DamageModifier.ARMOR, armorModifier);
        modifierFunctions.put(DamageModifier.ARMOR, armor);
        modifiers.put(DamageModifier.RESISTANCE, resistanceModifier);
        modifierFunctions.put(DamageModifier.RESISTANCE, resistance);
        modifiers.put(DamageModifier.MAGIC, magicModifier);
        modifierFunctions.put(DamageModifier.MAGIC, magic);
        modifiers.put(DamageModifier.ABSORPTION, absorptionModifier);
        modifierFunctions.put(DamageModifier.ABSORPTION, absorption);
        if (callback != null) callback.accept(modifiers, modifierFunctions); // Paper - fix invulnerability reduction in EntityDamageEvent
        return CraftEventFactory.handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }

    // Non-Living Entities such as EntityEnderCrystal and EntityFireball need to call this
    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage) {
        return CraftEventFactory.handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage) {
        return CraftEventFactory.handleNonLivingEntityDamageEvent(entity, source, damage, cancelOnZeroDamage, false);
    }

    public static EntityDamageEvent callNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelled) {
        final EnumMap<DamageModifier, Double> modifiers = new EnumMap<>(DamageModifier.class);
        final EnumMap<DamageModifier, Function<? super Double, Double>> functions = new EnumMap(DamageModifier.class);

        modifiers.put(DamageModifier.BASE, damage);
        functions.put(DamageModifier.BASE, CraftEventFactory.ZERO);

        return CraftEventFactory.handleEntityDamageEvent(entity, source, modifiers, functions, cancelled);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage, boolean cancelled) {
        final EntityDamageEvent event = CraftEventFactory.callNonLivingEntityDamageEvent(entity, source, damage, cancelled);

        if (event == null) {
            return false;
        }
        return event.isCancelled() || (cancelOnZeroDamage && event.getDamage() == 0);
    }

    public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerExpCooldownChangeEvent callPlayerXpCooldownEvent(net.minecraft.world.entity.player.Player entity, int newCooldown, PlayerExpCooldownChangeEvent.ChangeReason changeReason) {
        Player player = (Player) entity.getBukkitEntity();
        PlayerExpCooldownChangeEvent event = new PlayerExpCooldownChangeEvent(player, newCooldown, changeReason);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerItemMendEvent callPlayerItemMendEvent(net.minecraft.world.entity.player.Player entity, net.minecraft.world.entity.ExperienceOrb orb, net.minecraft.world.item.ItemStack nmsMendedItem, net.minecraft.world.entity.EquipmentSlot slot, int repairAmount, int consumedExperience) { // Paper - Expand PlayerItemMendEvent
        Player player = (Player) entity.getBukkitEntity();
        org.bukkit.inventory.ItemStack bukkitStack = CraftItemStack.asCraftMirror(nmsMendedItem);
        PlayerItemMendEvent event = new PlayerItemMendEvent(player, bukkitStack, CraftEquipmentSlot.getSlot(slot), (ExperienceOrb) orb.getBukkitEntity(), repairAmount, consumedExperience); // Paper - Expand PlayerItemMendEvent
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    // Paper start - Add orb
    public static PlayerExpChangeEvent callPlayerExpChangeEvent(net.minecraft.world.entity.player.Player entity, net.minecraft.world.entity.ExperienceOrb entityOrb) {
        Player player = (Player) entity.getBukkitEntity();
        ExperienceOrb source = (ExperienceOrb) entityOrb.getBukkitEntity();
        int expAmount = source.getExperience();
        PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, source, expAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    // Paper end

    public static boolean handleBlockGrowEvent(Level world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags) {
        CraftBlockState snapshot = CraftBlockStates.getBlockState(world, pos);
        snapshot.setData(state);

        BlockGrowEvent event = new BlockGrowEvent(snapshot.getBlock(), snapshot);
        if (event.callEvent()) {
            snapshot.place(flags);
            return true;
        }

        return false;
    }

    public static FluidLevelChangeEvent callFluidLevelChangeEvent(Level world, BlockPos block, net.minecraft.world.level.block.state.BlockState newData) {
        FluidLevelChangeEvent event = new FluidLevelChangeEvent(CraftBlock.at(world, block), CraftBlockData.fromData(newData));
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(net.minecraft.world.entity.player.Player entity, int level) {
        return CraftEventFactory.callFoodLevelChangeEvent(entity, level, null);
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(net.minecraft.world.entity.player.Player entity, int level, ItemStack item) {
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level, (item == null) ? null : CraftItemStack.asBukkitCopy(item));
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PigZapEvent callPigZapEvent(Entity pig, Entity lightning, Entity pigzombie) {
        PigZapEvent event = new PigZapEvent((Pig) pig.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), (PigZombie) pigzombie.getBukkitEntity());
        pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static boolean callHorseJumpEvent(Entity horse, float power) {
        HorseJumpEvent event = new HorseJumpEvent((AbstractHorse) horse.getBukkitEntity(), power);
        horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    // Paper start
    public static com.destroystokyo.paper.event.entity.EntityZapEvent callEntityZapEvent(Entity entity, Entity lightning, Entity changedEntity) {
        com.destroystokyo.paper.event.entity.EntityZapEvent event = new com.destroystokyo.paper.event.entity.EntityZapEvent(entity.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), changedEntity.getBukkitEntity());
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    // Paper end

    public static boolean callEntityChangeBlockEvent(Entity entity, BlockPos pos, net.minecraft.world.level.block.state.BlockState newState) {
        return CraftEventFactory.callEntityChangeBlockEvent(entity, pos, newState, false);
    }

    public static boolean callEntityChangeBlockEvent(Entity entity, BlockPos pos, net.minecraft.world.level.block.state.BlockState newState, boolean cancelled) {
        Block block = CraftBlock.at(entity.level(), pos);

        EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity.getBukkitEntity(), block, CraftBlockData.fromData(newState));
        event.setCancelled(cancelled);
        event.getEntity().getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static CreeperPowerEvent callCreeperPowerEvent(Entity creeper, Entity lightning, CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) creeper.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), cause);
        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetEvent callEntityTargetEvent(Entity entity, Entity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), (target == null) ? null : target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(Entity entity, net.minecraft.world.entity.LivingEntity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (target == null) ? null : (LivingEntity) target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreakDoorEvent callEntityBreakDoorEvent(Entity entity, BlockPos pos, net.minecraft.world.level.block.state.BlockState newState) { // Paper
        org.bukkit.entity.Entity entity1 = entity.getBukkitEntity();
        Block block = CraftBlock.at(entity.level(), pos);

        EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity) entity1, block, newState.createCraftBlockData()); // Paper
        entity1.getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static AbstractContainerMenu callInventoryOpenEvent(ServerPlayer player, AbstractContainerMenu container) {
        // Paper start - Add titleOverride to InventoryOpenEvent
        return callInventoryOpenEventWithTitle(player, container).getSecond();
    }

    public static com.mojang.datafixers.util.Pair<net.kyori.adventure.text.@org.jetbrains.annotations.Nullable Component, @org.jetbrains.annotations.Nullable AbstractContainerMenu> callInventoryOpenEventWithTitle(ServerPlayer player, AbstractContainerMenu container) {
        return CraftEventFactory.callInventoryOpenEventWithTitle(player, container, false);
        // Paper end - Add titleOverride to InventoryOpenEvent
    }

    public static com.mojang.datafixers.util.Pair<net.kyori.adventure.text.@org.jetbrains.annotations.Nullable Component, @org.jetbrains.annotations.Nullable AbstractContainerMenu> callInventoryOpenEventWithTitle(ServerPlayer player, AbstractContainerMenu container, boolean cancelled) {
        container.startOpen(); // delegate start open logic to before InventoryOpenEvent is fired
        if (player.containerMenu != player.inventoryMenu) { // fire INVENTORY_CLOSE if one already open
            player.connection.handleContainerClose(new ServerboundContainerClosePacket(player.containerMenu.containerId), InventoryCloseEvent.Reason.OPEN_NEW); // Paper - Inventory close reason
        }

        CraftServer server = player.level().getCraftServer();
        CraftPlayer craftPlayer = player.getBukkitEntity();
        player.containerMenu.transferTo(container, craftPlayer);

        InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
        event.setCancelled(cancelled);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            container.transferTo(player.containerMenu, craftPlayer);
            return com.mojang.datafixers.util.Pair.of(null, null); // Paper - Add titleOverride to InventoryOpenEvent
        }

        return com.mojang.datafixers.util.Pair.of(event.titleOverride(), container); // Paper - Add titleOverride to InventoryOpenEvent
    }

    public static ItemStack callPreCraftEvent(CraftingContainer matrix, Container resultInventory, ItemStack result, InventoryView lastCraftView, boolean isRepair) {
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, resultInventory);
        inventory.setResult(CraftItemStack.asCraftMirror(result));

        PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);
        Bukkit.getPluginManager().callEvent(event);

        return CraftItemStack.asNMSCopy(event.getInventory().getResult());
    }

    public static CrafterCraftEvent callCrafterCraftEvent(BlockPos pos, Level world, ItemStack result, RecipeHolder<net.minecraft.world.item.crafting.CraftingRecipe> holder) {
        CraftBlock block = CraftBlock.at(world, pos);
        CraftItemStack itemStack = CraftItemStack.asCraftMirror(result);
        CraftingRecipe craftingRecipe = (CraftingRecipe) holder.toBukkitRecipe();

        CrafterCraftEvent crafterCraftEvent = new CrafterCraftEvent(block, craftingRecipe, itemStack);
        Bukkit.getPluginManager().callEvent(crafterCraftEvent);
        return crafterCraftEvent;
    }

    // Paper start
    @Deprecated
    public static com.destroystokyo.paper.event.entity.ProjectileCollideEvent callProjectileCollideEvent(Entity entity, EntityHitResult position) {
        Projectile projectile = (Projectile) entity.getBukkitEntity();
        org.bukkit.entity.Entity collided = position.getEntity().getBukkitEntity();
        com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = new com.destroystokyo.paper.event.entity.ProjectileCollideEvent(projectile, collided);

        if (projectile.getShooter() instanceof Player && collided instanceof Player) {
            if (!((Player) projectile.getShooter()).canSee((Player) collided)) {
                event.setCancelled(true);
                return event;
            }
        }

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    // Paper end

    public static ProjectileLaunchEvent callProjectileLaunchEvent(Entity entity) {
        Projectile bukkitEntity = (Projectile) entity.getBukkitEntity();
        ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ProjectileHitEvent callProjectileHitEvent(Entity entity, HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.MISS) {
            return null;
        }

        Block hitBlock = null;
        BlockFace hitFace = null;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult positionBlock = (BlockHitResult) hitResult;
            hitBlock = CraftBlock.at(entity.level(), positionBlock.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        }

        org.bukkit.entity.Entity hitEntity = null;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            hitEntity = ((EntityHitResult) hitResult).getEntity().getBukkitEntity();
        }
        // Paper start - legacy event
        boolean cancelled = false;
        if (hitEntity != null && hitResult instanceof EntityHitResult entityHitResult) {
            cancelled = callProjectileCollideEvent(entity, entityHitResult).isCancelled();
        }
        // Paper end

        ProjectileHitEvent event = new ProjectileHitEvent((Projectile) entity.getBukkitEntity(), hitEntity, hitBlock, hitFace);
        event.setCancelled(cancelled); // Paper - propagate legacy event cancellation to modern event
        entity.level().getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ExpBottleEvent callExpBottleEvent(Entity entity, HitResult hitResult, int exp) {
        ThrownExpBottle bottle = (ThrownExpBottle) entity.getBukkitEntity();

        Block hitBlock = null;
        BlockFace hitFace = null;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult positionBlock = (BlockHitResult) hitResult;
            hitBlock = CraftBlock.at(entity.level(), positionBlock.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        }

        org.bukkit.entity.Entity hitEntity = null;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            hitEntity = ((EntityHitResult) hitResult).getEntity().getBukkitEntity();
        }

        ExpBottleEvent event = new ExpBottleEvent(bottle, hitEntity, hitBlock, hitFace, exp);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockRedstoneEvent callRedstoneChange(Level world, BlockPos pos, int oldCurrent, int newCurrent) {
        BlockRedstoneEvent event = new BlockRedstoneEvent(CraftBlock.at(world, pos), oldCurrent, newCurrent);
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static NotePlayEvent callNotePlayEvent(Level world, BlockPos pos, NoteBlockInstrument instrument, int note) {
        NotePlayEvent event = new NotePlayEvent(CraftBlock.at(world, pos), org.bukkit.Instrument.getByType((byte) instrument.ordinal()), new org.bukkit.Note(note));
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerItemBreakEvent(ServerPlayer human, ItemStack brokenItem) {
        CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        PlayerItemBreakEvent event = new PlayerItemBreakEvent(human.getBukkitEntity(), item);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static BlockIgniteEvent callBlockIgniteEvent(Level world, BlockPos pos, BlockPos sourcePos) {
        Block igniter = CraftBlock.at(world, sourcePos);
        final IgniteCause cause;
        switch (igniter.getType()) {
            case LAVA:
                cause = IgniteCause.LAVA;
                break;
            case DISPENSER:
                cause = IgniteCause.FLINT_AND_STEEL;
                break;
            case FIRE: // Fire or any other unknown block counts as SPREAD.
            default:
                cause = IgniteCause.SPREAD;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(CraftBlock.at(world, pos), cause, igniter);
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(Level world, BlockPos pos, Entity igniter) {
        org.bukkit.entity.Entity bukkitIgniter = igniter.getBukkitEntity();
        IgniteCause cause = switch (bukkitIgniter.getType()) {
            case END_CRYSTAL -> IgniteCause.ENDER_CRYSTAL;
            case LIGHTNING_BOLT -> IgniteCause.LIGHTNING;
            case SMALL_FIREBALL, FIREBALL -> IgniteCause.FIREBALL;
            case ARROW -> IgniteCause.ARROW;
            default -> IgniteCause.FLINT_AND_STEEL;
        };

        if (igniter instanceof net.minecraft.world.entity.projectile.Projectile) {
            Entity shooter = ((net.minecraft.world.entity.projectile.Projectile) igniter).getOwner();
            if (shooter != null) {
                bukkitIgniter = shooter.getBukkitEntity();
            }
        }

        BlockIgniteEvent event = new BlockIgniteEvent(CraftBlock.at(world, pos), cause, bukkitIgniter);
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(Level world, BlockPos pos, Explosion explosion) {
        org.bukkit.entity.Entity igniter = explosion.getDirectSourceEntity() == null ? null : explosion.getDirectSourceEntity().getBukkitEntity();

        BlockIgniteEvent event = new BlockIgniteEvent(CraftBlock.at(world, pos), IgniteCause.EXPLOSION, igniter);
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(Level world, BlockPos pos, IgniteCause cause, Entity igniter) {
        BlockIgniteEvent event = new BlockIgniteEvent(CraftBlock.at(world, pos), cause, igniter.getBukkitEntity());
        world.getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void handleInventoryCloseEvent(net.minecraft.world.entity.player.Player human, org.bukkit.event.inventory.InventoryCloseEvent.Reason reason) {
        InventoryCloseEvent event = new InventoryCloseEvent(human.containerMenu.getBukkitView(), reason); // Paper
        human.level().getCraftServer().getPluginManager().callEvent(event);
        human.containerMenu.transferTo(human.inventoryMenu, human.getBukkitEntity());
    }

    public static ItemStack handleEditBookEvent(ServerPlayer player, int itemInHandIndex, ItemStack itemInHand, ItemStack newBookItem) {
        PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), (itemInHandIndex >= 0 && itemInHandIndex <= 8) ? itemInHandIndex : -1, (BookMeta) CraftItemStack.getItemMeta(itemInHand), (BookMeta) CraftItemStack.getItemMeta(newBookItem), newBookItem.is(Items.WRITTEN_BOOK));
        player.level().getCraftServer().getPluginManager().callEvent(editBookEvent);

        // If they've got the same item in their hand, it'll need to be updated.
        if (itemInHand != null && itemInHand.is(Items.WRITABLE_BOOK)) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }
                BookMeta meta = editBookEvent.getNewBookMeta();
                CraftItemStack.setItemMeta(itemInHand, meta);
            } else {
                player.containerMenu.sendAllDataToRemote(); // SPIGOT-7484
            }
        }

        return itemInHand;
    }

    public static void callRecipeBookSettingsEvent(ServerPlayer player, RecipeBookType type, boolean open, boolean filter) {
        PlayerRecipeBookSettingsChangeEvent.RecipeBookType bukkitType = PlayerRecipeBookSettingsChangeEvent.RecipeBookType.values()[type.ordinal()];
        Bukkit.getPluginManager().callEvent(new PlayerRecipeBookSettingsChangeEvent(player.getBukkitEntity(), bukkitType, open, filter));
    }

    public static boolean handlePlayerUnleashEntityEvent(Leashable leashable, net.minecraft.world.entity.player.@Nullable Player player, @Nullable InteractionHand hand, boolean dropLeash) {
        if (!(leashable instanceof final Entity entity)) return true;
        return handlePlayerUnleashEntityEvent(entity, player, hand, dropLeash);
    }

    public static boolean handlePlayerUnleashEntityEvent(Entity entity, net.minecraft.world.entity.player.@Nullable Player player, @Nullable InteractionHand hand, boolean dropLeash) {
        if (player == null || hand == null) return true;

        PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player) player.getBukkitEntity(), CraftEquipmentSlot.getHand(hand), dropLeash);
        entity.level().getCraftServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        if (entity instanceof final Leashable leashable) {
            if (event.isDropLeash()) leashable.dropLeash();
            else leashable.removeLeash();
        }
        return true;
    }

    public static boolean handlePlayerLeashEntityEvent(Leashable leashed, Entity leashHolder, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        if (!(leashed instanceof final Entity leashedEntity)) return false;
        return callPlayerLeashEntityEvent(leashedEntity, leashHolder, player, hand).callEvent();
    }

    public static @Nullable PlayerLeashEntityEvent callPlayerLeashEntityEvent(Leashable leashed, Entity leashHolder, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        if (!(leashed instanceof final Entity leashedEntity)) return null;
        return callPlayerLeashEntityEvent(leashedEntity, leashHolder, player, hand);
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(Entity entity, Entity leashHolder, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player) player.getBukkitEntity(), CraftEquipmentSlot.getHand(hand));
        entity.level().getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerRiptideEvent(net.minecraft.world.entity.player.Player player, ItemStack tridentItemStack, float velocityX, float velocityY, float velocityZ) {
        PlayerRiptideEvent event = new PlayerRiptideEvent((Player) player.getBukkitEntity(), CraftItemStack.asCraftMirror(tridentItemStack), new Vector(velocityX, velocityY, velocityZ));
        player.level().getCraftServer().getPluginManager().callEvent(event);
    }

    public static BlockShearEntityEvent callBlockShearEntityEvent(Entity animal, org.bukkit.block.Block dispenser, CraftItemStack is, List<ItemStack> drops) { // Paper - custom shear drops
        BlockShearEntityEvent bse = new BlockShearEntityEvent(dispenser, animal.getBukkitEntity(), is, Lists.transform(drops, CraftItemStack::asCraftMirror)); // Paper - custom shear drops
        Bukkit.getPluginManager().callEvent(bse);
        return bse;
    }

    public static PlayerShearEntityEvent handlePlayerShearEntityEvent(net.minecraft.world.entity.player.Player player, Entity sheared, ItemStack shears, InteractionHand hand, List<ItemStack> drops) { // Paper - custom shear drops
        if (!(player instanceof ServerPlayer)) {
            return null; // Paper - custom shear drops
        }

        PlayerShearEntityEvent event = new PlayerShearEntityEvent((Player) player.getBukkitEntity(), sheared.getBukkitEntity(), CraftItemStack.asCraftMirror(shears), (hand == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND), Lists.transform(drops, CraftItemStack::asCraftMirror)); // Paper - custom shear drops
        Bukkit.getPluginManager().callEvent(event);
        return event; // Paper - custom shear drops
    }

    public static Cancellable handleStatisticsIncrease(net.minecraft.world.entity.player.Player entityHuman, net.minecraft.stats.Stat<?> statistic, int current, int newValue) {
        Player player = ((ServerPlayer) entityHuman).getBukkitEntity();
        org.bukkit.Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
        if (stat == null) {
            System.err.println("Unhandled statistic: " + statistic);
            return null;
        }
        switch (stat) {
            case FALL_ONE_CM:
            case BOAT_ONE_CM:
            case CLIMB_ONE_CM:
            case WALK_ON_WATER_ONE_CM:
            case WALK_UNDER_WATER_ONE_CM:
            case FLY_ONE_CM:
            case HORSE_ONE_CM:
            case MINECART_ONE_CM:
            case PIG_ONE_CM:
            case PLAY_ONE_MINUTE:
            case SWIM_ONE_CM:
            case WALK_ONE_CM:
            case SPRINT_ONE_CM:
            case CROUCH_ONE_CM:
            case TIME_SINCE_DEATH:
            case SNEAK_TIME:
            case TOTAL_WORLD_TIME:
            case TIME_SINCE_REST:
            case AVIATE_ONE_CM:
            case STRIDER_ONE_CM:
                // Do not process event for these - too spammy
                return null;
            default:
        }

        final Event event;
        if (stat.getType() == Type.UNTYPED) {
            event = new PlayerStatisticIncrementEvent(player, stat, current, newValue);
        } else if (stat.getType() == Type.ENTITY) {
            EntityType entityType = CraftStatistic.getEntityTypeFromStatistic((net.minecraft.stats.Stat<net.minecraft.world.entity.EntityType<?>>) statistic);
            event = new PlayerStatisticIncrementEvent(player, stat, current, newValue, entityType);
        } else {
            Material material = CraftStatistic.getMaterialFromStatistic(statistic);
            event = new PlayerStatisticIncrementEvent(player, stat, current, newValue, material);
        }
        event.callEvent();
        return (Cancellable) event;
    }

    public static boolean callFireworkExplodeEvent(FireworkRocketEntity firework) {
        FireworkExplodeEvent event = new FireworkExplodeEvent((Firework) firework.getBukkitEntity());
        if (!event.callEvent()) {
            firework.discard(null);
            return false;
        }
        return true;
    }

    public static PrepareAnvilEvent callPrepareAnvilEvent(AnvilView view, ItemStack item) {
        // Paper start - Add PrepareResultEvent
        view.getTopInventory().setItem(net.minecraft.world.inventory.AnvilMenu.RESULT_SLOT, CraftItemStack.asCraftMirror(item));
        return null; // verify nothing uses return - disable event: handled below in PrepareResult
        // Paper end - Add PrepareResultEvent
    }

    public static PrepareGrindstoneEvent callPrepareGrindstoneEvent(InventoryView view, ItemStack item) {
        // Paper start - Add PrepareResultEvent
        view.getTopInventory().setItem(net.minecraft.world.inventory.GrindstoneMenu.RESULT_SLOT, CraftItemStack.asCraftMirror(item));
        return null; // verify nothing uses return - disable event: handled below in PrepareResult
        // Paper end - Add PrepareResultEvent
    }

    public static PrepareSmithingEvent callPrepareSmithingEvent(InventoryView view, ItemStack item) {
        // Paper start - Add PrepareResultEvent
        view.getTopInventory().setItem(net.minecraft.world.inventory.SmithingMenu.RESULT_SLOT, CraftItemStack.asCraftMirror(item));
        return null; // verify nothing uses return - disable event: handled below in PrepareResult
        // Paper end - Add PrepareResultEvent
    }

    public static void callPrepareResultEvent(AbstractContainerMenu container, int resultSlot) {
        final com.destroystokyo.paper.event.inventory.PrepareResultEvent event;
        InventoryView view = container.getBukkitView();
        org.bukkit.inventory.ItemStack origItem = view.getTopInventory().getItem(resultSlot);
        CraftItemStack result = origItem != null ? CraftItemStack.asCraftCopy(origItem) : null;
        if (view.getTopInventory() instanceof org.bukkit.inventory.AnvilInventory && view instanceof AnvilView anvilView) {
            event = new PrepareAnvilEvent(anvilView, result);
        } else if (view.getTopInventory() instanceof org.bukkit.inventory.GrindstoneInventory) {
            event = new PrepareGrindstoneEvent(view, result);
        } else if (view.getTopInventory() instanceof org.bukkit.inventory.SmithingInventory) {
            event = new PrepareSmithingEvent(view, result);
        } else {
            event = new com.destroystokyo.paper.event.inventory.PrepareResultEvent(view, result);
        }
        event.callEvent();
        event.getInventory().setItem(resultSlot, event.getResult());
        container.broadcastChanges();;
    }

    public static SpawnerSpawnEvent callSpawnerSpawnEvent(Entity spawnee, BlockPos pos) {
        org.bukkit.craftbukkit.entity.CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = CraftBlock.at(spawnee.level(), pos).getState();
        if (!(state instanceof org.bukkit.block.CreatureSpawner)) {
            state = null;
        }

        SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (org.bukkit.block.CreatureSpawner) state);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static TrialSpawnerSpawnEvent callTrialSpawnerSpawnEvent(Entity spawnee, BlockPos pos) {
        org.bukkit.craftbukkit.entity.CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = CraftBlock.at(spawnee.level(), pos).getState();
        if (!(state instanceof org.bukkit.block.TrialSpawner)) {
            state = null;
        }

        TrialSpawnerSpawnEvent event = new TrialSpawnerSpawnEvent(entity, (org.bukkit.block.TrialSpawner) state);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockDispenseLootEvent callBlockDispenseLootEvent(ServerLevel level, BlockPos pos, net.minecraft.world.entity.player.Player player, List<ItemStack> rewardLoot) {
        List<org.bukkit.inventory.ItemStack> craftItemStacks = rewardLoot.stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList());

        BlockDispenseLootEvent event = new BlockDispenseLootEvent((player == null) ? null : (Player) player.getBukkitEntity(), CraftBlock.at(level, pos), craftItemStacks);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static VaultDisplayItemEvent callVaultDisplayItemEvent(ServerLevel level, BlockPos pos, ItemStack displayitemStack) {
        VaultDisplayItemEvent event = new VaultDisplayItemEvent(CraftBlock.at(level, pos), CraftItemStack.asBukkitCopy(displayitemStack));
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleGlideEvent callToggleGlideEvent(net.minecraft.world.entity.LivingEntity entity, boolean gliding) {
        EntityToggleGlideEvent event = new EntityToggleGlideEvent((LivingEntity) entity.getBukkitEntity(), gliding);
        entity.level().getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleSwimEvent callToggleSwimEvent(net.minecraft.world.entity.LivingEntity entity, boolean swimming) {
        EntityToggleSwimEvent event = new EntityToggleSwimEvent((LivingEntity) entity.getBukkitEntity(), swimming);
        entity.level().getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static AreaEffectCloudApplyEvent callAreaEffectCloudApplyEvent(net.minecraft.world.entity.AreaEffectCloud cloud, List<LivingEntity> entities) {
        AreaEffectCloudApplyEvent event = new AreaEffectCloudApplyEvent((AreaEffectCloud) cloud.getBukkitEntity(), entities);
        cloud.level().getCraftServer().getPluginManager().callEvent(event);
        return event;
    }

    public static VehicleCreateEvent callVehicleCreateEvent(Entity entity) {
        Vehicle bukkitEntity = (Vehicle) entity.getBukkitEntity();
        VehicleCreateEvent event = new VehicleCreateEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreedEvent callEntityBreedEvent(net.minecraft.world.entity.LivingEntity child, net.minecraft.world.entity.LivingEntity mother, net.minecraft.world.entity.LivingEntity father, net.minecraft.world.entity.LivingEntity breeder, ItemStack bredWith, int experience) {
        LivingEntity breederEntity = breeder == null ? null : (LivingEntity) breeder.getBukkitEntity();
        CraftItemStack bredWithStack = bredWith == null ? null : CraftItemStack.asCraftMirror(bredWith).clone();

        EntityBreedEvent event = new EntityBreedEvent((LivingEntity) child.getBukkitEntity(), (LivingEntity) mother.getBukkitEntity(), (LivingEntity) father.getBukkitEntity(), breederEntity, bredWithStack, experience);
        event.callEvent();
        return event;
    }

    public static BlockPhysicsEvent callBlockPhysicsEvent(LevelAccessor world, BlockPos pos) {
        org.bukkit.block.Block block = CraftBlock.at(world, pos);
        BlockPhysicsEvent event = new BlockPhysicsEvent(block, block.getBlockData());
        // Suppress during worldgen
        if (world instanceof Level) {
            event.callEvent();
        }
        return event;
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(net.minecraft.world.entity.LivingEntity entity, @Nullable MobEffectInstance oldEffect, @Nullable MobEffectInstance newEffect, EntityPotionEffectEvent.Cause cause) {
        return CraftEventFactory.callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, true);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(net.minecraft.world.entity.LivingEntity entity, @Nullable MobEffectInstance oldEffect, @Nullable MobEffectInstance newEffect, EntityPotionEffectEvent.Cause cause, EntityPotionEffectEvent.Action action) {
        return CraftEventFactory.callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, action, true);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(net.minecraft.world.entity.LivingEntity entity, @Nullable MobEffectInstance oldEffect, @Nullable MobEffectInstance newEffect, EntityPotionEffectEvent.Cause cause, boolean willOverride) {
        EntityPotionEffectEvent.Action action = EntityPotionEffectEvent.Action.CHANGED;
        if (oldEffect == null) {
            action = EntityPotionEffectEvent.Action.ADDED;
        } else if (newEffect == null) {
            action = EntityPotionEffectEvent.Action.REMOVED;
        }

        return CraftEventFactory.callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, action, willOverride);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(net.minecraft.world.entity.LivingEntity entity, @Nullable MobEffectInstance oldEffect, @Nullable MobEffectInstance newEffect, EntityPotionEffectEvent.Cause cause, EntityPotionEffectEvent.Action action, boolean willOverride) {
        PotionEffect bukkitOldEffect = (oldEffect == null) ? null : CraftPotionUtil.toBukkit(oldEffect);
        PotionEffect bukkitNewEffect = (newEffect == null) ? null : CraftPotionUtil.toBukkit(newEffect);

        Preconditions.checkState(bukkitOldEffect != null || bukkitNewEffect != null, "Old and new potion effect are both null");

        EntityPotionEffectEvent event = new EntityPotionEffectEvent((LivingEntity) entity.getBukkitEntity(), bukkitOldEffect, bukkitNewEffect, cause, action, willOverride);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static boolean handleBlockFormEvent(Level world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags) {
        return CraftEventFactory.handleBlockFormEvent(world, pos, state, flags, null);
    }

    public static boolean handleBlockFormEvent(Level world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags, @Nullable Entity entity) {
        return CraftEventFactory.handleBlockFormEvent(world, pos, state, flags, entity, false);
    }

    public static boolean handleBlockFormEvent(Level world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags, @Nullable Entity entity, boolean checkSetResult) {
        CraftBlockState snapshot = CraftBlockStates.getBlockState(world, pos);
        snapshot.setData(state);

        BlockFormEvent event = (entity == null) ? new BlockFormEvent(snapshot.getBlock(), snapshot) : new EntityBlockFormEvent(entity.getBukkitEntity(), snapshot.getBlock(), snapshot);
        if (event.callEvent()) {
            boolean result = snapshot.place(flags);
            return !checkSetResult || result;
        }

        return false;
    }

    public static boolean handleBatToggleSleepEvent(Entity bat, boolean awake) {
        BatToggleSleepEvent event = new BatToggleSleepEvent((Bat) bat.getBukkitEntity(), awake);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static boolean handlePlayerRecipeListUpdateEvent(net.minecraft.world.entity.player.Player player, ResourceLocation recipe) {
        PlayerRecipeDiscoverEvent event = new PlayerRecipeDiscoverEvent((Player) player.getBukkitEntity(), CraftNamespacedKey.fromMinecraft(recipe));
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static EntityPickupItemEvent callEntityPickupItemEvent(Entity entity, ItemEntity item, int remaining, boolean cancelled) {
        EntityPickupItemEvent event = new EntityPickupItemEvent((LivingEntity) entity.getBukkitEntity(), (Item) item.getBukkitEntity(), remaining);
        event.setCancelled(cancelled);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LightningStrikeEvent callLightningStrikeEvent(LightningStrike entity, LightningStrikeEvent.Cause cause) {
        LightningStrikeEvent event = new LightningStrikeEvent(entity.getWorld(), entity, cause);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean callRaidTriggerEvent(Level level, Raid raid, ServerPlayer player) {
        RaidTriggerEvent event = new RaidTriggerEvent(new CraftRaid(raid, level), level.getWorld(), player.getBukkitEntity());
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static void callRaidFinishEvent(Level level, Raid raid, List<Player> players) {
        RaidFinishEvent event = new RaidFinishEvent(new CraftRaid(raid, level), level.getWorld(), players);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void callRaidStopEvent(Level level, Raid raid, RaidStopEvent.Reason reason) {
        RaidStopEvent event = new RaidStopEvent(new CraftRaid(raid, level), level.getWorld(), reason);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void callRaidSpawnWaveEvent(Level level, Raid raid, net.minecraft.world.entity.raid.Raider leader, Set<net.minecraft.world.entity.raid.Raider> raiders) {
        Raider bukkitLeader = (Raider) leader.getBukkitEntity();
        List<Raider> bukkitRaiders = new ArrayList<>(raiders.size());
        for (net.minecraft.world.entity.raid.Raider raider : raiders) {
            bukkitRaiders.add((Raider) raider.getBukkitEntity());
        }
        RaidSpawnWaveEvent event = new RaidSpawnWaveEvent(new CraftRaid(raid, level), level.getWorld(), bukkitLeader, bukkitRaiders);
        event.callEvent();
    }

    public static LootGenerateEvent callLootGenerateEvent(Container inventory, LootTable lootTable, LootContext lootInfo, List<ItemStack> loot, boolean plugin) {
        CraftWorld world = lootInfo.getLevel().getWorld();
        Entity entity = lootInfo.getOptionalParameter(LootContextParams.THIS_ENTITY);
        List<org.bukkit.inventory.ItemStack> bukkitLoot = loot.stream().map(CraftItemStack::asCraftMirror).collect(Collectors.toCollection(ArrayList::new));

        LootGenerateEvent event = new LootGenerateEvent(world, (entity != null ? entity.getBukkitEntity() : null), inventory.getOwner(), lootTable.craftLootTable, CraftLootTable.convertContext(lootInfo), bukkitLoot, plugin);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean callStriderTemperatureChangeEvent(net.minecraft.world.entity.monster.Strider strider, boolean shivering) {
        StriderTemperatureChangeEvent event = new StriderTemperatureChangeEvent((Strider) strider.getBukkitEntity(), shivering);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static boolean handleEntitySpellCastEvent(SpellcasterIllager caster, SpellcasterIllager.IllagerSpell spell) {
        EntitySpellCastEvent event = new EntitySpellCastEvent((Spellcaster) caster.getBukkitEntity(), CraftSpellcaster.toBukkitSpell(spell));
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static ArrowBodyCountChangeEvent callArrowBodyCountChangeEvent(net.minecraft.world.entity.LivingEntity entity, int oldAmount, int newAmount, boolean isReset) {
        org.bukkit.entity.LivingEntity bukkitEntity = (LivingEntity) entity.getBukkitEntity();

        ArrowBodyCountChangeEvent event = new ArrowBodyCountChangeEvent(bukkitEntity, oldAmount, newAmount, isReset);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static EntityExhaustionEvent callPlayerExhaustionEvent(net.minecraft.world.entity.player.Player humanEntity, EntityExhaustionEvent.ExhaustionReason exhaustionReason, float exhaustion) {
        EntityExhaustionEvent event = new EntityExhaustionEvent(humanEntity.getBukkitEntity(), exhaustionReason, exhaustion);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static PiglinBarterEvent callPiglinBarterEvent(net.minecraft.world.entity.monster.piglin.Piglin piglin, List<ItemStack> outcome, ItemStack input) {
        PiglinBarterEvent event = new PiglinBarterEvent((Piglin) piglin.getBukkitEntity(), CraftItemStack.asBukkitCopy(input), outcome.stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList()));
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void callEntitiesLoadEvent(Level world, ChunkPos coords, List<Entity> entities) {
        List<org.bukkit.entity.Entity> bukkitEntities = Collections.unmodifiableList(entities.stream().map(Entity::getBukkitEntity).collect(Collectors.toList()));
        EntitiesLoadEvent event = new EntitiesLoadEvent(new CraftChunk((ServerLevel) world, coords.x, coords.z), bukkitEntities);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void callEntitiesUnloadEvent(Level world, ChunkPos coords, List<Entity> entities) {
        List<org.bukkit.entity.Entity> bukkitEntities = Collections.unmodifiableList(entities.stream().map(Entity::getBukkitEntity).collect(Collectors.toList()));
        EntitiesUnloadEvent event = new EntitiesUnloadEvent(new CraftChunk((ServerLevel) world, coords.x, coords.z), bukkitEntities);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static boolean callTNTPrimeEvent(Level world, BlockPos pos, TNTPrimeEvent.PrimeCause cause, Entity causingEntity, BlockPos causePosition) {
        org.bukkit.entity.Entity bukkitEntity = (causingEntity == null) ? null : causingEntity.getBukkitEntity();
        org.bukkit.block.Block bukkitBlock = (causePosition == null) ? null : CraftBlock.at(world, causePosition);

        TNTPrimeEvent event = new TNTPrimeEvent(CraftBlock.at(world, pos), cause, bukkitEntity, bukkitBlock);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    public static PlayerRecipeBookClickEvent callRecipeBookClickEvent(ServerPlayer player, Recipe recipe, boolean shiftClick) {
        PlayerRecipeBookClickEvent event = new PlayerRecipeBookClickEvent(player.getBukkitEntity(), recipe, shiftClick);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTeleportEvent callEntityTeleportEvent(Entity nmsEntity, double x, double y, double z) {
        CraftEntity entity = nmsEntity.getBukkitEntity();
        Location to = new Location(entity.getWorld(), x, y, z, nmsEntity.getYRot(), nmsEntity.getXRot());
        return CraftEventFactory.callEntityTeleportEvent(nmsEntity, to);
    }

    public static EntityTeleportEvent callEntityTeleportEvent(Entity nmsEntity, Location to) {
        CraftEntity entity = nmsEntity.getBukkitEntity();
        EntityTeleportEvent event = new org.bukkit.event.entity.EntityTeleportEvent(entity, entity.getLocation(), to);

        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static boolean callEntityInteractEvent(Entity nmsEntity, Block block) {
        EntityInteractEvent event = new EntityInteractEvent(nmsEntity.getBukkitEntity(), block);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    public static EntityExplodeEvent callEntityExplodeEvent(Entity entity, List<Block> blocks, float yield, Explosion.BlockInteraction effect) {
        EntityExplodeEvent event = new EntityExplodeEvent(entity.getBukkitEntity(), entity.getBukkitEntity().getLocation(), blocks, yield, CraftExplosionResult.toExplosionResult(effect));
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockExplodeEvent callBlockExplodeEvent(Block block, BlockState state, List<Block> blocks, float yield, Explosion.BlockInteraction effect) {
        BlockExplodeEvent event = new BlockExplodeEvent(block, state, blocks, yield, CraftExplosionResult.toExplosionResult(effect));
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ExplosionPrimeEvent callExplosionPrimeEvent(Explosive explosive) {
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(explosive);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ExplosionPrimeEvent callExplosionPrimeEvent(Entity nmsEntity, float size, boolean fire) {
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(nmsEntity.getBukkitEntity(), size, fire);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static io.papermc.paper.event.entity.EntityKnockbackEvent callEntityKnockbackEvent(CraftLivingEntity entity, Entity pusher, Entity attacker, io.papermc.paper.event.entity.EntityKnockbackEvent.Cause cause, double force, Vec3 knockback) {
        Vector apiKnockback = CraftVector.toBukkit(knockback);

        final Vector currentVelocity = entity.getVelocity();
        final Vector legacyFinalKnockback = currentVelocity.clone().add(apiKnockback);
        final org.bukkit.event.entity.EntityKnockbackEvent.KnockbackCause legacyCause = org.bukkit.event.entity.EntityKnockbackEvent.KnockbackCause.valueOf(cause.name());
        EntityKnockbackEvent legacyEvent;
        if (pusher != null) {
            legacyEvent = new EntityKnockbackByEntityEvent(entity, pusher.getBukkitEntity(), legacyCause, force, apiKnockback, legacyFinalKnockback);
        } else {
            legacyEvent = new EntityKnockbackEvent(entity, legacyCause, force, apiKnockback, legacyFinalKnockback);
        }
        legacyEvent.callEvent();

        final io.papermc.paper.event.entity.EntityKnockbackEvent event;
        apiKnockback = legacyEvent.getFinalKnockback().subtract(currentVelocity);
        if (attacker != null) {
            event = new com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent(entity, attacker.getBukkitEntity(), cause, (float) force, apiKnockback);
        } else {
            event = new io.papermc.paper.event.entity.EntityKnockbackEvent(entity, cause, apiKnockback);
        }
        event.setCancelled(legacyEvent.isCancelled());
        event.callEvent();
        return event;
    }

    public static void callEntityRemoveEvent(Entity entity, EntityRemoveEvent.Cause cause) {
        if (entity instanceof ServerPlayer) {
            return; // Don't call for player
        }

        if (cause == null) {
            // Don't call if cause is null
            // This can happen when an entity changes dimension,
            // the entity gets removed during world gen or
            // the entity is removed before it is even spawned (when the spawn event is cancelled for example)
            return;
        }

        // Do not call during generation.
        if (entity.generation) return;

        Bukkit.getPluginManager().callEvent(new EntityRemoveEvent(entity.getBukkitEntity(), cause));
    }

    public static void callPlayerUseUnknownEntityEvent(net.minecraft.world.entity.player.Player player, net.minecraft.network.protocol.game.ServerboundInteractPacket packet, InteractionHand hand, @Nullable net.minecraft.world.phys.Vec3 vector) {
        new com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent(
            (Player) player.getBukkitEntity(), packet.getEntityId(), packet.isAttack(),
            CraftEquipmentSlot.getHand(hand),
            vector != null ? CraftVector.toBukkit(vector) : null
        ).callEvent();
    }

    public static ItemStack handleWitchReadyPotionEvent(net.minecraft.world.entity.monster.Witch witch, @Nullable ItemStack potion) {
        com.destroystokyo.paper.event.entity.WitchReadyPotionEvent event = new com.destroystokyo.paper.event.entity.WitchReadyPotionEvent((org.bukkit.entity.Witch) witch.getBukkitEntity(), CraftItemStack.asCraftMirror(potion));
        if (!event.callEvent() || event.getPotion() == null) {
            return ItemStack.EMPTY;
        }
        return org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(event.getPotion());
    }

    public static boolean handleBlockFailedDispenseEvent(ServerLevel serverLevel, BlockPos pos) {
        org.bukkit.block.Block block = CraftBlock.at(serverLevel, pos);
        io.papermc.paper.event.block.BlockFailedDispenseEvent event = new io.papermc.paper.event.block.BlockFailedDispenseEvent(block);
        return event.callEvent();
    }

    public static boolean handleBlockPreDispenseEvent(ServerLevel serverLevel, BlockPos pos, ItemStack itemStack, int slot) {
        org.bukkit.block.Block block = CraftBlock.at(serverLevel, pos);
        io.papermc.paper.event.block.BlockPreDispenseEvent event = new io.papermc.paper.event.block.BlockPreDispenseEvent(block, org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemStack), slot);
        return event.callEvent();
    }

    @Nullable
    public static ItemStack handleBlockDispenseEvent(net.minecraft.core.dispenser.BlockSource pointer, BlockPos to, ItemStack itemStack, net.minecraft.core.dispenser.DispenseItemBehavior instance) {
        org.bukkit.block.Block bukkitBlock = CraftBlock.at(pointer.level(), pointer.pos());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemStack.isDamageableItem() ? itemStack : itemStack.copyWithCount(1));

        org.bukkit.event.block.BlockDispenseEvent event = new org.bukkit.event.block.BlockDispenseEvent(bukkitBlock, craftItem.clone(), CraftVector.toBukkit(to));
        if (!event.callEvent()) {
            return itemStack;
        }

        if (!event.getItem().equals(craftItem)) {
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            net.minecraft.core.dispenser.DispenseItemBehavior itemBehavior = net.minecraft.world.level.block.DispenserBlock.getDispenseBehavior(pointer, eventStack);
            if (itemBehavior != net.minecraft.core.dispenser.DispenseItemBehavior.NOOP && itemBehavior != instance) {
                itemBehavior.dispense(pointer, eventStack);
                return itemStack;
            }
        }
        return null;
    }

    /**
     * Calls the {@link io.papermc.paper.event.entity.EntityFertilizeEggEvent}.
     * If the event is cancelled, this method also resets the love on both the {@code breeding} and {@code other} entity.
     *
     * @param breeding the entity on which #spawnChildFromBreeding was called.
     * @param other    the partner of the entity.
     * @return the event after it was called. The instance may be used to retrieve the experience of the event.
     */
    public static io.papermc.paper.event.entity.EntityFertilizeEggEvent callEntityFertilizeEggEvent(Animal breeding, Animal other) {
        ServerPlayer serverPlayer = breeding.getLoveCause();
        if (serverPlayer == null) serverPlayer = other.getLoveCause();
        final int experience = breeding.getRandom().nextInt(7) + 1; // From Animal#spawnChildFromBreeding(ServerLevel, Animal)

        final io.papermc.paper.event.entity.EntityFertilizeEggEvent event = new io.papermc.paper.event.entity.EntityFertilizeEggEvent((LivingEntity) breeding.getBukkitEntity(), (LivingEntity) other.getBukkitEntity(), serverPlayer == null ? null : serverPlayer.getBukkitEntity(), breeding.breedItem == null ? null : CraftItemStack.asCraftMirror(breeding.breedItem).clone(), experience);
        if (!event.callEvent()) {
            breeding.resetLove();
            other.resetLove(); // stop the pathfinding to avoid infinite loop
        }

        return event;
    }
}
