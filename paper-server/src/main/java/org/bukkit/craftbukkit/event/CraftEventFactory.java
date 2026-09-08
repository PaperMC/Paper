package org.bukkit.craftbukkit.event;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.destroystokyo.paper.event.entity.WitchReadyPotionEvent;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import com.destroystokyo.paper.exception.ServerInternalException;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.block.bed.BedEnterAction;
import io.papermc.paper.block.bed.BedEnterActionImpl;
import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.connection.HorriblePlayerLoginEventHack;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import io.papermc.paper.event.block.BlockLockCheckEvent;
import io.papermc.paper.event.block.PaperBlockFailedDispenseEvent;
import io.papermc.paper.event.block.PaperBlockLockCheckEvent;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import io.papermc.paper.event.entity.EntityFertilizeEggEvent;
import io.papermc.paper.event.entity.EntityIgniteEvent;
import io.papermc.paper.event.entity.ItemTransportingEntityValidateTargetEvent;
import io.papermc.paper.event.entity.PaperEntityFertilizeEggEvent;
import io.papermc.paper.event.entity.PaperEntityIgniteEvent;
import io.papermc.paper.event.entity.PaperEntityKnockbackByEntityEvent;
import io.papermc.paper.event.entity.PaperEntityKnockbackEvent;
import io.papermc.paper.event.entity.PaperExperienceOrbMergeEvent;
import io.papermc.paper.event.entity.PaperItemTransportingEntityValidateTargetEvent;
import io.papermc.paper.event.entity.PaperProjectileCollideEvent;
import io.papermc.paper.event.entity.PaperWitchReadyPotionEvent;
import io.papermc.paper.event.inventory.PaperPrepareResultEvent;
import io.papermc.paper.event.network.PaperServerListPingEventImpl;
import io.papermc.paper.event.network.connection.PaperPlayerConnectionValidateLoginEvent;
import io.papermc.paper.event.player.PaperPlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PaperPlayerToggleEntityAgeLockEvent;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import io.papermc.paper.event.player.PlayerToggleEntityAgeLockEvent;
import io.papermc.paper.event.server.PaperServerExceptionEvent;
import io.papermc.paper.event.world.PaperWorldGameRuleChangeEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Optionull;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerLinks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.Stat;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.LockCode;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServerLinks;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.block.CraftBlockDispenseEvent;
import org.bukkit.craftbukkit.event.block.CraftBlockDropItemEvent;
import org.bukkit.craftbukkit.event.block.CraftBlockFormEvent;
import org.bukkit.craftbukkit.event.block.CraftBlockGrowEvent;
import org.bukkit.craftbukkit.event.block.CraftBlockRedstoneEvent;
import org.bukkit.craftbukkit.event.block.CraftBlockSpreadEvent;
import org.bukkit.craftbukkit.event.block.CraftCauldronLevelChangeEvent;
import org.bukkit.craftbukkit.event.block.CraftEntityBlockFormEvent;
import org.bukkit.craftbukkit.event.block.CraftHopperInventorySearchEvent;
import org.bukkit.craftbukkit.event.block.CraftInventoryCloseEvent;
import org.bukkit.craftbukkit.event.block.CraftMoistureChangeEvent;
import org.bukkit.craftbukkit.event.data.PortalEventResult;
import org.bukkit.craftbukkit.event.entity.CraftCreatureSpawnEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityChangeBlockEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityDamageByBlockEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityDamageByEntityEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityDamageEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityDeathEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityKnockbackByEntityEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityKnockbackEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityPickupItemEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityPortalEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityRemoveEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntitySpawnEvent;
import org.bukkit.craftbukkit.event.entity.CraftFireworkExplodeEvent;
import org.bukkit.craftbukkit.event.entity.CraftItemSpawnEvent;
import org.bukkit.craftbukkit.event.entity.CraftPlayerDeathEvent;
import org.bukkit.craftbukkit.event.entity.CraftPlayerUnleashEntityEvent;
import org.bukkit.craftbukkit.event.entity.CraftProjectileHitEvent;
import org.bukkit.craftbukkit.event.entity.CraftProjectileLaunchEvent;
import org.bukkit.craftbukkit.event.inventory.CraftInventoryOpenEvent;
import org.bukkit.craftbukkit.event.inventory.CraftPrepareAnvilEvent;
import org.bukkit.craftbukkit.event.inventory.CraftPrepareGrindstoneEvent;
import org.bukkit.craftbukkit.event.inventory.CraftPrepareItemCraftEvent;
import org.bukkit.craftbukkit.event.inventory.CraftPrepareSmithingEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBedEnterEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBucketEmptyEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBucketEntityEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBucketEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBucketFillEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerBucketFishEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEditBookEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerExpChangeEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerExpCooldownChangeEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerInteractEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerLeashEntityEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerLinksSendEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerPortalEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerStatisticIncrementEvent;
import org.bukkit.craftbukkit.event.vehicle.CraftVehicleCreateEvent;
import org.bukkit.craftbukkit.event.world.CraftClockTimeSkipEvent;
import org.bukkit.craftbukkit.event.world.CraftTimeSkipEvent;
import org.bukkit.craftbukkit.event.world.weather.CraftLightningStrikeEvent;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.ClockTimeSkipEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Only use it for more complex construct otherwise use flexible constructors.
 */
@ApiStatus.Obsolete
@NullMarked
public class CraftEventFactory {

    private record LegacyBedReasons(PlayerBedFailEnterEvent.FailReason failReason, PlayerBedEnterEvent.BedEnterResult bedEnterResult) {

        public static LegacyBedReasons from(final BedRule bedRule, final Player.BedSleepingProblem sleepingProblem) {
            final PlayerBedFailEnterEvent.FailReason failReason;
            if (sleepingProblem == Player.BedSleepingProblem.OTHER_PROBLEM) {
                failReason = PlayerBedFailEnterEvent.FailReason.OTHER_PROBLEM;
            } else if (sleepingProblem == Player.BedSleepingProblem.NOT_SAFE) {
                failReason = PlayerBedFailEnterEvent.FailReason.NOT_SAFE;
            } else if (sleepingProblem == Player.BedSleepingProblem.OBSTRUCTED) {
                failReason = PlayerBedFailEnterEvent.FailReason.OBSTRUCTED;
            } else if (sleepingProblem == Player.BedSleepingProblem.TOO_FAR_AWAY) {
                failReason = PlayerBedFailEnterEvent.FailReason.TOO_FAR_AWAY;
            } else if (sleepingProblem == Player.BedSleepingProblem.EXPLOSION) {
                failReason = PlayerBedFailEnterEvent.FailReason.EXPLOSION;
            } else if (bedRule.canSleep() == BedRule.Rule.NEVER) {
                failReason = PlayerBedFailEnterEvent.FailReason.NOT_POSSIBLE_HERE;
            } else if (bedRule.canSleep() == BedRule.Rule.WHEN_DARK) {
                failReason = PlayerBedFailEnterEvent.FailReason.NOT_POSSIBLE_NOW;
            } else {
                // Don't know what the reason is, defaulting to OTHER to prevent server crashes
                failReason = PlayerBedFailEnterEvent.FailReason.OTHER_PROBLEM;
            }
            return new LegacyBedReasons(failReason, PlayerBedEnterEvent.BedEnterResult.valueOf(failReason.name()));
        }
    }

    public static PlayerBedFailEnterEvent callPlayerBedFailEnterEvent(final Player player, final BlockPos pos, final Player.BedSleepingProblem sleepingProblem) {
        final BedRule bedRule = player.level().environmentAttributes().getDimensionValue(EnvironmentAttributes.BED_RULE);
        final PlayerBedFailEnterEvent event = new PaperPlayerBedFailEnterEvent(
            player, LegacyBedReasons.from(bedRule, sleepingProblem).failReason(), pos, new BedEnterActionImpl(bedRule, player.level(), sleepingProblem)
        );
        event.callEvent();
        return event;
    }

    public static Either<Player.BedSleepingProblem, Unit> callPlayerBedEnterEvent(final ServerPlayer player, final BlockPos pos, final Either<Player.BedSleepingProblem, Unit> bedResult) {
        final BedRule bedRule = player.level().environmentAttributes().getDimensionValue(EnvironmentAttributes.BED_RULE);
        final PlayerBedEnterEvent.BedEnterResult enterResult = bedResult.left()
            .map(sleepingProblem -> LegacyBedReasons.from(bedRule, sleepingProblem).bedEnterResult()).orElse(PlayerBedEnterEvent.BedEnterResult.OK);
        final BedEnterAction enterAction = BedEnterActionImpl.from(bedRule, player.level(), bedResult.left().orElse(null));

        final PlayerBedEnterEvent event = new CraftPlayerBedEnterEvent(player, pos, enterResult, enterAction);
        event.callEvent();

        final Event.Result result = event.useBed();
        if (result == Event.Result.ALLOW) {
            return Either.right(Unit.INSTANCE);
        } else if (result == Event.Result.DENY) {
            return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
        }

        return bedResult;
    }

    public static PlayerBucketEntityEvent callPlayerBucketEntityEvent(final LivingEntity entity, final Player player, final ItemStack originalBucket, final ItemStack entityBucket, final InteractionHand hand) {
        final PlayerBucketEntityEvent event;
        if (entity instanceof final AbstractFish fish) {
            event = new CraftPlayerBucketFishEvent(player, fish, originalBucket, entityBucket, hand);
        } else {
            event = new CraftPlayerBucketEntityEvent(player, entity, originalBucket, entityBucket, hand);
        }
        event.callEvent();
        return event;
    }

    public static void handleBlockDropItemEvent(
        final org.bukkit.block.Block block, final org.bukkit.block.BlockState state, final ServerPlayer player, final List<ItemEntity> items
    ) {
        final BlockDropItemEvent event = new CraftBlockDropItemEvent(block, state, player, items);
        final boolean cancelled = !event.callEvent();
        items.forEach(item -> {
            final boolean valid = item.isAlive() && item.valid;
            if (cancelled && valid) {
                item.getBukkitEntity().remove();
            } else if (!cancelled && !valid) {
                item.level().addFreshEntity(item);
            }
        });
    }

    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(final Level level, final Player player, final BlockPos changedPos, final BlockPos clickedPos, final Direction clickedFace, final ItemStack itemInHand, final InteractionHand hand) {
        return callPlayerBucketEvent(CraftPlayerBucketEmptyEvent::new, level, player, changedPos, clickedPos, clickedFace, itemInHand, Items.BUCKET, hand);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(final Level level, final Player player, final BlockPos clickedPos, final Direction clickedFace, final ItemStack itemInHand, final net.minecraft.world.item.Item bucket, final InteractionHand hand) {
        return callPlayerBucketEvent(CraftPlayerBucketFillEvent::new, level, player, clickedPos, clickedPos, clickedFace, itemInHand, bucket, hand);
    }

    private static <EVENT extends PlayerBucketEvent> EVENT callPlayerBucketEvent(final CraftPlayerBucketEvent.Factory<? extends EVENT> factory, final Level level, final Player player, final BlockPos changedPos, final BlockPos clickedPos, final Direction clickedFace, final ItemStack bucketItem, final net.minecraft.world.item.Item itemInHand, final InteractionHand hand) {
        final EVENT event = CraftPlayerBucketEvent.create(factory, level, player, changedPos, clickedPos, clickedFace, bucketItem, itemInHand, hand);
        event.callEvent();
        return event;
    }

    public static PlayerInteractEvent callPlayerInteractEvent(final Player player, final Action action, final ItemStack item, final InteractionHand hand) {
        assert action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR;
        return callPlayerInteractEvent(player, action, null, Direction.SOUTH, item, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(final Player player, final Action action, final @Nullable BlockPos pos, final @Nullable Direction direction, final @Nullable ItemStack item, final @Nullable InteractionHand hand) {
        return callPlayerInteractEvent(player, action, pos, direction, item, false, hand, null);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(final Player player, final Action action, final @Nullable BlockPos pos, final @Nullable Direction direction, final @Nullable ItemStack item, final boolean cancelledBlock, final @Nullable InteractionHand hand, final @Nullable Vec3 targetPos) {
        return callPlayerInteractEvent(player, action, pos, direction, item, cancelledBlock, false, hand, targetPos);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(final Player player, Action action, final @Nullable BlockPos pos, final @Nullable Direction direction, final @Nullable ItemStack item, final boolean cancelledBlock, final boolean cancelledItem, final @Nullable InteractionHand hand, final @Nullable Vec3 targetPos) {
        Vec3 clickedPos = null;
        if (pos != null && targetPos != null) {
            clickedPos = targetPos.subtract(Vec3.atLowerCornerOf(pos));
        }

        if (pos == null) {
            action = switch (action) {
                case LEFT_CLICK_BLOCK -> Action.LEFT_CLICK_AIR;
                case RIGHT_CLICK_BLOCK -> Action.RIGHT_CLICK_AIR;
                default -> action;
            };
        }

        final PlayerInteractEvent event = new CraftPlayerInteractEvent(player, action, item == null ? ItemStack.EMPTY : item, pos, direction, hand, clickedPos);
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        if (cancelledItem) {
            event.setUseItemInHand(Event.Result.DENY);
        }
        event.callEvent();

        return event;
    }

    public static boolean doEntityAddEventCalling(final Level level, final Entity entity, final CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entity instanceof ServerPlayer) return true;

        final EntitySpawnEvent event;
        if (entity instanceof final LivingEntity livingEntity) {
            event = new CraftCreatureSpawnEvent(livingEntity, spawnReason);
        } else if (entity instanceof final ItemEntity item) {
            event = new CraftItemSpawnEvent(item);
        } else if (entity.getBukkitEntity() instanceof final Projectile projectile) {
            // Not all projectiles extend Projectile, so check for Bukkit interface instead
            event = new CraftProjectileLaunchEvent(projectile);
        } else if (entity.getBukkitEntity() instanceof final Vehicle vehicle) {
            event = new CraftVehicleCreateEvent(vehicle);
        } else if (entity instanceof final LightningBolt bolt) {
            final LightningStrikeEvent.Cause cause = switch (spawnReason) {
                case COMMAND -> LightningStrikeEvent.Cause.COMMAND;
                case CUSTOM -> LightningStrikeEvent.Cause.CUSTOM;
                case SPAWNER -> LightningStrikeEvent.Cause.SPAWNER;
                default -> LightningStrikeEvent.Cause.UNKNOWN;
            };
            // This event is called in nms-patches for common causes like Weather, Trap or Trident (SpawnReason.DEFAULT) then can ignore this cases for avoid two calls to this event
            if (cause == LightningStrikeEvent.Cause.UNKNOWN && spawnReason == CreatureSpawnEvent.SpawnReason.DEFAULT) {
                return true;
            }
            event = new CraftLightningStrikeEvent(bolt, cause);
        } else {
            event = new CraftEntitySpawnEvent(entity);
        }

        if (!event.callEvent() || entity.isRemoved()) {
            final Entity vehicle = entity.getVehicle();
            if (vehicle != null) {
                vehicle.discard(null);
            }
            for (final Entity passenger : entity.getIndirectPassengers()) {
                passenger.discard(null);
            }
            entity.discard(null);
            return false;
        }

        if (entity instanceof final ExperienceOrb orb) {
            mergeNearbyOrbs(level, orb);
        }

        return true;
    }

    // spigot custom stuff that could just be implemented by a plugin
    private static void mergeNearbyOrbs(final Level level, final ExperienceOrb into) {
        final double radius = level.spigotConfig.expMerge;
        if (radius <= 0) {
            return;
        }

        final long maxValue = level.paperConfig().entities.behavior.experienceMergeMaxValue;
        final boolean mergeUnconditionally = maxValue <= 0;
        if (!mergeUnconditionally && into.getValue() >= maxValue) {
            return;
        }

        final List<ExperienceOrb> orbs = level.getEntitiesOfClass(ExperienceOrb.class, into.getBoundingBox().inflate(radius), o -> o != into);
        for (final ExperienceOrb orb : orbs) {
            if (!orb.isRemoved() && into.count == orb.count && (mergeUnconditionally || orb.getValue() < maxValue) &&
                new PaperExperienceOrbMergeEvent(into, orb).callEvent()) {
                final long newTotal = (long) into.getValue() + (long) orb.getValue();
                if ((int) newTotal < 0) continue; // Overflow
                if (!mergeUnconditionally && newTotal > maxValue) {
                    orb.setValue((int) (newTotal - maxValue));
                    into.setValue((int) maxValue);
                } else {
                    into.setValue(into.getValue() + orb.getValue());
                    orb.discard(null);
                }
            }
        }
    }

    public static boolean handleMoistureChangeEvent(final Level level, final BlockPos pos, final BlockState state, final @Block.UpdateFlags int flags) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, state);

        final MoistureChangeEvent event = new CraftMoistureChangeEvent(block, snapshot);
        if (event.callEvent()) {
            snapshot.place(flags);
            return true;
        }
        return false;
    }

    public static @Nullable BlockPos sourceBlockOverride = null; // SPIGOT-7068: Add source block override, not the most elegant way but better than passing down a BlockPos up to five methods deep.

    public static boolean handleBlockSpreadEvent(
        final LevelAccessor level, final BlockPos source, final BlockPos target, final BlockState state, final @Block.UpdateFlags int flags
    ) {
        return handleBlockSpreadEvent(level, source, target, state, flags, false);
    }

    public static boolean handleBlockSpreadEvent(
        final LevelAccessor level, final BlockPos source, final BlockPos target, final BlockState state, final @Block.UpdateFlags int flags, final boolean checkSetResult
    ) {
        // Suppress during world generation
        if (!(level instanceof Level)) {
            final boolean result = level.setBlock(target, state, flags);
            return !checkSetResult || result;
        }

        final org.bukkit.block.Block block = CraftBlock.at(level, target);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, state);

        final BlockSpreadEvent event = new CraftBlockSpreadEvent(block, CraftBlock.at(level, sourceBlockOverride != null ? sourceBlockOverride : source), snapshot);
        if (event.callEvent()) {
            final boolean result = snapshot.place(flags);
            return !checkSetResult || result;
        }
        return false;
    }

    public static boolean callEntityDeathEvent(final ServerLevel level, final LivingEntity victim, final DamageSource damageSource) {
        final EntityDeathEvent event = callEntityDeathEvent(level, victim, damageSource, new ArrayList<>(), false);
        return !event.isCancelled();
    }

    public static EntityDeathEvent callEntityDeathEvent(final ServerLevel level, final LivingEntity victim, final DamageSource damageSource, final List<Entity.DefaultDrop> drops, final boolean delayedDrops) {
        final CraftEntityDeathEvent event = new CraftEntityDeathEvent(
            victim,
            damageSource,
            drops,
            victim.getExpReward(level, damageSource.getEntity()),
            victim.getAttributeValue(Attributes.MAX_HEALTH),
            !victim.silentDeath && !victim.isSilent(),
            victim.getDeathSound(),
            victim.getSoundSource(),
            victim.getSoundVolume(),
            victim.getVoicePitch()
        );
        if (!event.callEvent()) {
            return event;
        }

        event.playDeathSound(victim, damageSource);
        victim.expToDrop = event.getDroppedExp();

        final Runnable dropAll = () -> {
            dropAllItems(drops, item -> victim.spawnAtLocation(level, item));
        };

        if (delayedDrops) {
            victim.postDeathEventTasks.add(dropAll);
        } else {
            dropAll.run();
        }

        return event;
    }

    private static void dropAllItems(final List<Entity.@Nullable DefaultDrop> drops, final Consumer<ItemStack> fallback) {
        for (final Entity.DefaultDrop drop : drops) {
            if (drop == null) continue;
            final org.bukkit.inventory.ItemStack stack = drop.stack();
            if (stack.isEmpty()) continue;

            drop.runConsumer(item -> fallback.accept(CraftItemStack.unwrap(item)));
        }
    }

    public static PlayerDeathEvent callPlayerDeathEvent(
        final ServerPlayer victim, final DamageSource damageSource, final List<Entity.DefaultDrop> drops, final Component deathMessage, final boolean showDeathMessages, final boolean keepInventory
    ) {
        final CraftPlayerDeathEvent event = new CraftPlayerDeathEvent(
            victim,
            damageSource,
            drops,
            victim.getExpReward(victim.level(), damageSource.getEntity()),
            victim.getAttributeValue(Attributes.MAX_HEALTH),
            !victim.silentDeath && !victim.isSilent(),
            victim.getDeathSound(),
            victim.getSoundSource(),
            victim.getSoundVolume(),
            victim.getVoicePitch(),
            deathMessage,
            showDeathMessages,
            keepInventory,
            victim.keepLevel
        );
        if (!event.callEvent()) {
            return event;
        }
        event.playDeathSound(victim, damageSource);

        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();

        dropAllItems(drops, item -> victim.drop(item, true, false));

        return event;
    }

    private static EntityDamageEvent handleEntityDamageEvent(
        final Entity entity,
        final DamageSource source,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, Function<? super Double, Double>> modifierFunctions
    ) {
        return handleEntityDamageEvent(entity, source, modifiers, modifierFunctions, false);
    }

    private static EntityDamageEvent handleEntityDamageEvent(
        final Entity entity,
        final DamageSource source,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, Function<? super Double, Double>> modifierFunctions,
        final boolean cancelled
    ) {
        final Entity damager = source.eventEntityDamager() != null ? source.eventEntityDamager() : source.getDirectEntity();
        if (damager != null) {
            DamageCause cause = DamageCause.ENTITY_ATTACK;

            if (source.knownCause() != null) {
                cause = source.knownCause();
            } else if (source.is(DamageTypes.FIREWORKS) || source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) { // look at relevant items in #is_explosion tag
                cause = damager instanceof PrimedTnt ? DamageCause.BLOCK_EXPLOSION : DamageCause.ENTITY_EXPLOSION;
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
            } else if (source.is(DamageTypes.DRAGON_BREATH)) {
                cause = DamageCause.DRAGON_BREATH; // never used
            } else if (source.is(DamageTypes.MAGIC)) {
                cause = DamageCause.MAGIC;
            }

            return callEntityDamageEvent(damager, entity, cause, source, modifiers, modifierFunctions, cancelled);
        } else if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) { // why this is a block event, damager is always null and no snapshot either
            return callEntityDamageEvent(
                source.eventBlockDamager(), source.causingBlockSnapshot(), entity, DamageCause.VOID, source, modifiers, modifierFunctions, cancelled
            );
        } else if (source.is(DamageTypes.LAVA)) { // todo move below once lastLavaContact is defined
            return callEntityDamageEvent(
                source.eventBlockDamager(), source.causingBlockSnapshot(), entity, DamageCause.LAVA, source, modifiers, modifierFunctions, cancelled
            );
        } else if (source.eventBlockDamager() != null || source.causingBlockSnapshot() != null) {
            final DamageCause cause;
            if (source.knownCause() != null) {
                cause = source.knownCause();
            } else if (source.is(DamageTypes.CACTUS) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.STALAGMITE) ||
                source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.CAMPFIRE)) {
                cause = DamageCause.CONTACT;
            } else if (source.is(DamageTypes.MAGIC)) {
                cause = DamageCause.MAGIC;
            } else if (source.is(DamageTypes.IN_FIRE)) {
                cause = DamageCause.FIRE;
            } else if (source.is(DamageTypes.BAD_RESPAWN_POINT)) {
                cause = DamageCause.BLOCK_EXPLOSION;
            } else {
                cause = DamageCause.CUSTOM;
            }
            return callEntityDamageEvent(
                source.eventBlockDamager(), source.causingBlockSnapshot(), entity, cause, source, modifiers, modifierFunctions, cancelled
            );
        }

        final DamageCause cause;
        if (source.knownCause() != null) {
            cause = source.knownCause();
        } else if (source.is(DamageTypes.IN_FIRE)) { // todo could be called above
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

        return callEntityDamageEvent(null, entity, cause, source, modifiers, modifierFunctions, cancelled);
    }

    private static EntityDamageEvent callEntityDamageEvent(
        final @Nullable Entity damager,
        final Entity damagee,
        final DamageCause cause,
        final DamageSource source,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, Function<? super Double, Double>> modifierFunctions,
        final boolean cancelled
    ) {
        final EntityDamageEvent event;
        if (damager != null) {
            event = new CraftEntityDamageByEntityEvent(damager, damagee, cause, source, modifiers, modifierFunctions);
        } else {
            event = new CraftEntityDamageEvent(damagee, cause, source, modifiers, modifierFunctions);
        }
        return callEntityDamageEvent(event, damagee, cancelled);
    }

    private static EntityDamageEvent callEntityDamageEvent(
        final org.bukkit.block.@Nullable Block damager,
        final org.bukkit.block.@Nullable BlockState damagerState,
        final Entity damagee,
        final DamageCause cause,
        final DamageSource source,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, Function<? super Double, Double>> modifierFunctions,
        final boolean cancelled
    ) {
        final EntityDamageByBlockEvent event = new CraftEntityDamageByBlockEvent(damager, damagerState, damagee, cause, source, modifiers, modifierFunctions);
        return callEntityDamageEvent(event, damagee, cancelled);
    }

    private static EntityDamageEvent callEntityDamageEvent(final EntityDamageEvent event, final Entity damagee, final boolean cancelled) {
        event.setCancelled(cancelled);
        event.callEvent();

        if (!event.isCancelled()) {
            event.getEntity().setLastDamageCause(event);
        } else {
            damagee.lastDamageCancelled = true; // SPIGOT-5339, SPIGOT-6252, SPIGOT-6777: Keep track if the event was canceled
        }

        return event;
    }

    private static final Function<? super Double, Double> ZERO = _ -> -0.0;

    public static EntityDamageEvent handleLivingEntityDamageEvent(
        final Entity damagee,
        final DamageSource source,
        final double rawDamage,
        final double freezingModifier,
        final double hardHatModifier,
        final double blockingModifier,
        final double armorModifier,
        final double resistanceModifier,
        final double magicModifier,
        final double absorptionModifier,
        final Function<Double, Double> freezing,
        final Function<Double, Double> hardHat,
        final Function<Double, Double> blocking,
        final Function<Double, Double> armor,
        final Function<Double, Double> resistance,
        final Function<Double, Double> magic,
        final Function<Double, Double> absorption,
        final @Nullable BiConsumer<Map<DamageModifier, Double>, Map<DamageModifier, Function<? super Double, Double>>> callback
    ) {
        final Map<DamageModifier, Double> modifiers = new EnumMap<>(DamageModifier.class);
        final Map<DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap<>(DamageModifier.class);
        modifiers.put(DamageModifier.BASE, rawDamage);
        modifierFunctions.put(DamageModifier.BASE, ZERO);
        if (source.is(DamageTypeTags.IS_FREEZING)) {
            modifiers.put(DamageModifier.FREEZING, freezingModifier);
            modifierFunctions.put(DamageModifier.FREEZING, freezing);
        }
        if (source.is(DamageTypeTags.DAMAGES_HELMET)) {
            modifiers.put(DamageModifier.HARD_HAT, hardHatModifier);
            modifierFunctions.put(DamageModifier.HARD_HAT, hardHat);
        }
        if (damagee instanceof Player) {
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
        if (callback != null) callback.accept(modifiers, modifierFunctions);
        return handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }

    // Non-Living Entities such as EntityEnderCrystal and EntityFireball need to call this
    public static boolean handleNonLivingEntityDamageEvent(final Entity entity, final DamageSource source, final double damage) {
        return handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }

    public static boolean handleNonLivingEntityDamageEvent(final Entity entity, final DamageSource source, final double damage, final boolean cancelOnZeroDamage) {
        return handleNonLivingEntityDamageEvent(entity, source, damage, cancelOnZeroDamage, false);
    }

    public static EntityDamageEvent callNonLivingEntityDamageEvent(final Entity entity, final DamageSource source, final double damage, final boolean cancelled) {
        final EnumMap<DamageModifier, Double> modifiers = new EnumMap<>(DamageModifier.class);
        final EnumMap<DamageModifier, Function<? super Double, Double>> functions = new EnumMap<>(DamageModifier.class);

        modifiers.put(DamageModifier.BASE, damage);
        functions.put(DamageModifier.BASE, ZERO);

        return handleEntityDamageEvent(entity, source, modifiers, functions, cancelled);
    }

    public static boolean handleNonLivingEntityDamageEvent(
        final Entity entity, final DamageSource source, final double damage, final boolean cancelOnZeroDamage, final boolean cancelled
    ) {
        final EntityDamageEvent event = callNonLivingEntityDamageEvent(entity, source, damage, cancelled);
        return event.isCancelled() || (cancelOnZeroDamage && event.getDamage() == 0);
    }

    public static PlayerExpCooldownChangeEvent callPlayerXpCooldownEvent(final ServerPlayer player, final int newCooldown, final PlayerExpCooldownChangeEvent.ChangeReason changeReason) {
        final PlayerExpCooldownChangeEvent event = new CraftPlayerExpCooldownChangeEvent(player, newCooldown, changeReason);
        event.callEvent();
        return event;
    }

    public static PlayerExpChangeEvent callPlayerExpChangeEvent(final ServerPlayer player, final ExperienceOrb source, final int amount) {
        final PlayerExpChangeEvent event = new CraftPlayerExpChangeEvent(player, source, amount);
        event.callEvent();
        return event;
    }

    public static boolean handleBlockGrowEvent(final Level level, final BlockPos pos, final BlockState state, final @Block.UpdateFlags int flags) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, state);

        final BlockGrowEvent event = new CraftBlockGrowEvent(block, snapshot);
        if (event.callEvent()) {
            snapshot.place(flags);
            return true;
        }

        return false;
    }

    public static boolean handleCauldronLevelChangeEvent(final Level level, final BlockPos pos, final BlockState newState, final @Nullable Entity entity, final CauldronLevelChangeEvent.ChangeReason reason) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, newState);

        final CauldronLevelChangeEvent event = new CraftCauldronLevelChangeEvent(block, entity, reason, snapshot);
        if (event.callEvent()) {
            snapshot.place(Block.UPDATE_ALL);
            return true;
        }

        return false;
    }

    public static boolean callEntityChangeBlockEvent(final Entity entity, final BlockPos pos, final BlockState newState) {
        return callEntityChangeBlockEvent(entity, pos, newState, false);
    }

    public static boolean callEntityChangeBlockEvent(final Entity entity, final BlockPos pos, final BlockState newState, final boolean cancelled) {
        final EntityChangeBlockEvent event = new CraftEntityChangeBlockEvent(entity, pos, newState);
        event.setCancelled(cancelled);
        return event.callEvent();
    }

    public static EntityTargetEvent.TargetReason getForgotTargetReason(final Mob body, final @Nullable LivingEntity previousTarget, final boolean wasInvalid) {
        if (previousTarget != null && !previousTarget.isAlive()) {
            return EntityTargetEvent.TargetReason.TARGET_DIED;
        } else if (wasInvalid || (previousTarget != null && !body.canAttack(previousTarget))) {
            return EntityTargetEvent.TargetReason.TARGET_INVALID;
        } else {
            return EntityTargetEvent.TargetReason.FORGOT_TARGET;
        }
    }

    public static @Nullable AbstractContainerMenu callInventoryOpenEvent(final ServerPlayer player, final AbstractContainerMenu container) {
        return callInventoryOpenEventWithTitle(player, container).getSecond();
    }

    public static Pair<net.kyori.adventure.text.@Nullable Component, @Nullable AbstractContainerMenu> callInventoryOpenEventWithTitle(final ServerPlayer player, final AbstractContainerMenu container) {
        return callInventoryOpenEventWithTitle(player, container, false);
    }

    public static Pair<net.kyori.adventure.text.@Nullable Component, @Nullable AbstractContainerMenu> callInventoryOpenEventWithTitle(final ServerPlayer player, final AbstractContainerMenu menu, final boolean cancelled) {
        menu.startOpen(); // delegate start open logic to before InventoryOpenEvent is fired
        if (player.containerMenu != player.inventoryMenu) { // fire INVENTORY_CLOSE if one already open
            player.connection.handleContainerClose(new ServerboundContainerClosePacket(player.containerMenu.containerId), InventoryCloseEvent.Reason.OPEN_NEW);
        }

        final CraftPlayer craftPlayer = player.getBukkitEntity();
        player.containerMenu.transferTo(menu, craftPlayer);

        final InventoryOpenEvent event = new CraftInventoryOpenEvent(menu);
        event.setCancelled(cancelled);

        if (!event.callEvent()) {
            menu.transferTo(player.containerMenu, craftPlayer);
            return Pair.of(null, null);
        }

        return Pair.of(event.titleOverride(), menu);
    }

    public static ItemStack callPreCraftEvent(
        final CraftingContainer container, final Container resultSlots, final ItemStack result, final AbstractContainerMenu menu, final Optional<RecipeHolder<CraftingRecipe>> recipe
    ) {
        final PrepareItemCraftEvent event = new CraftPrepareItemCraftEvent(container, resultSlots, result, menu, recipe);
        event.callEvent();
        return CraftItemStack.asNMSCopy(event.getInventory().getResult());
    }

    @Deprecated
    public static ProjectileCollideEvent callProjectileCollideEvent(final net.minecraft.world.entity.projectile.Projectile entity, final EntityHitResult hitResult) {
        final ProjectileCollideEvent event = new PaperProjectileCollideEvent(entity, hitResult.getEntity());

        if (event.getEntity().getShooter() instanceof final org.bukkit.entity.Player shooter && event.getCollidedWith() instanceof final org.bukkit.entity.Player collided) {
            if (!shooter.canSee(collided)) {
                event.setCancelled(true);
                return event;
            }
        }

        event.callEvent();
        return event;
    }

    public static ProjectileHitEvent callProjectileHitEvent(final net.minecraft.world.entity.projectile.Projectile entity, final HitResult hitResult) {
        boolean cancelled = false;
        if (hitResult instanceof final EntityHitResult entityHitResult) {
            cancelled = callProjectileCollideEvent(entity, entityHitResult).isCancelled();
        }

        final ProjectileHitEvent event = new CraftProjectileHitEvent(entity, hitResult);
        event.setCancelled(cancelled);
        event.callEvent();
        return event;
    }

    public static boolean callBinaryRedstoneChange(final LevelAccessor level, final BlockPos pos, final boolean willBePowered) {
        final int oldCurrent = willBePowered ? Redstone.SIGNAL_MIN : Redstone.SIGNAL_MAX;
        final int newCurrent = willBePowered ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_MIN;
        return callRedstoneChange(level, pos, oldCurrent, newCurrent).getNewCurrent() == newCurrent;
    }

    public static BlockRedstoneEvent callRedstoneChange(final LevelAccessor level, final BlockPos pos, final int oldCurrent, final int newCurrent) {
        final BlockRedstoneEvent event = new CraftBlockRedstoneEvent(level, pos, oldCurrent, newCurrent); // todo normalize newCurrent change outcome
        event.callEvent();
        return event;
    }

    public static void handleInventoryCloseEvent(final Player player, final InventoryCloseEvent.Reason reason) {
        final InventoryCloseEvent event = new CraftInventoryCloseEvent(player.containerMenu, reason);
        event.callEvent();
        player.containerMenu.transferTo(player.inventoryMenu, player.getBukkitEntity());
    }

    public static ItemStack handleEditBookEvent(final ServerPlayer player, final int itemInHandIndex, final ItemStack itemInHand, final ItemStack newBookItem) {
        final PlayerEditBookEvent event = new CraftPlayerEditBookEvent(
            player,
            itemInHandIndex >= 0 && itemInHandIndex < Inventory.SELECTION_SIZE ? itemInHandIndex : -1,
            itemInHand,
            newBookItem,
            newBookItem.is(Items.WRITTEN_BOOK)
        );
        event.callEvent();

        // If they've got the same item in their hand, it'll need to be updated.
        if (itemInHand.is(Items.WRITABLE_BOOK)) {
            if (!event.isCancelled()) {
                if (event.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }
                final BookMeta meta = event.getNewBookMeta();
                CraftItemStack.setItemMeta(itemInHand, meta);
            } else {
                player.containerMenu.forceSlot(player.getInventory(), itemInHandIndex); // SPIGOT-7484
            }
        }

        return itemInHand;
    }

    public static boolean handlePlayerUnleashEntityEvent(
        final Leashable leashable, final @Nullable Player player, final @Nullable InteractionHand hand, final boolean dropLeash, final boolean resendState
    ) {
        if (!(leashable instanceof final Entity entity)) return true;
        return handlePlayerUnleashEntityEvent(entity, player, hand, dropLeash, resendState);
    }

    public static boolean handlePlayerUnleashEntityEvent(
        final Entity entity, final @Nullable Player player, final @Nullable InteractionHand hand, final boolean dropLeash, final boolean resendState
    ) {
        if (player == null || hand == null) {
            if (entity instanceof final Leashable leashable) {
                if (dropLeash) leashable.dropLeash();
                else leashable.removeLeash();
            }
            return true;
        }

        final PlayerUnleashEntityEvent event = new CraftPlayerUnleashEntityEvent(entity, player, hand, dropLeash);
        if (!event.callEvent()) {
            if (resendState && entity instanceof final Leashable leashable) {
                ((ServerPlayer) player).connection.send(new ClientboundSetEntityLinkPacket(entity, leashable.getLeashHolder()));
            }
            return false;
        }

        if (entity instanceof final Leashable leashable) {
            if (event.isDropLeash()) leashable.dropLeash();
            else leashable.removeLeash();
        }
        return true;
    }

    public static boolean handlePlayerLeashEntityEvent(final Leashable leashed, final Entity leashHolder, final Player player, final InteractionHand hand) {
        if (!(leashed instanceof final Entity leashedEntity)) return true;
        return new CraftPlayerLeashEntityEvent(leashedEntity, leashHolder, player, hand).callEvent();
    }

    public static @Nullable PlayerLeashEntityEvent callPlayerLeashEntityEvent(final Leashable leashed, final Entity leashHolder, final Player player, final InteractionHand hand) {
        if (!(leashed instanceof final Entity leashedEntity)) return null;
        return callPlayerLeashEntityEvent(leashedEntity, leashHolder, player, hand);
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(final Entity entity, final Entity leashHolder, final Player player, final InteractionHand hand) {
        final PlayerLeashEntityEvent event = new CraftPlayerLeashEntityEvent(entity, leashHolder, player, hand);
        event.callEvent();
        return event;
    }

    public static boolean handleStatisticsIncrease(final Player player, final Stat<?> statistic, final int current, final int newValue) {
        final Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
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
            case HAPPY_GHAST_ONE_CM:
            case NAUTILUS_ONE_CM:
                // Do not process event for these - too spammy
                return true;
            default:
        }

        final PlayerStatisticIncrementEvent event;
        if (stat.getType() == Statistic.Type.UNTYPED) {
            event = new CraftPlayerStatisticIncrementEvent(player, stat, current, newValue, null);
        } else if (stat.getType() == Statistic.Type.ENTITY) {
            final EntityType entityType = CraftStatistic.getEntityTypeFromStatistic((Stat<net.minecraft.world.entity.EntityType<?>>) statistic);
            event = new CraftPlayerStatisticIncrementEvent(player, stat, current, newValue, entityType);
        } else {
            final Material material = CraftStatistic.getMaterialFromStatistic(statistic);
            event = new CraftPlayerStatisticIncrementEvent(player, stat, current, newValue, material);
        }

        return event.callEvent();
    }

    public static boolean callFireworkExplodeEvent(final FireworkRocketEntity firework) {
        final FireworkExplodeEvent event = new CraftFireworkExplodeEvent(firework);
        if (!event.callEvent()) {
            firework.discard(null);
            return false;
        }
        return true;
    }

    public static void callPrepareResultEvent(final AbstractContainerMenu container, final int resultSlot) {
        final InventoryView view = container.getBukkitView();
        final org.bukkit.inventory.ItemStack result = Optionull.map(view.getTopInventory().getItem(resultSlot), org.bukkit.inventory.ItemStack::clone);
        final PrepareResultEvent event = switch (container) {
            case AnvilMenu _ when view instanceof final AnvilView anvilView -> new CraftPrepareAnvilEvent(anvilView, result);
            case GrindstoneMenu _ -> new CraftPrepareGrindstoneEvent(view, result);
            case SmithingMenu _ -> new CraftPrepareSmithingEvent(view, result);
            default -> new PaperPrepareResultEvent(view, result);
        };
        event.callEvent();

        event.getInventory().setItem(resultSlot, event.getResult());
        container.broadcastChanges();
    }

    public static boolean handleBlockFormEvent(final Level level, final BlockPos pos, final BlockState state, final @Block.UpdateFlags int flags) {
        return handleBlockFormEvent(level, pos, state, flags, null);
    }

    public static boolean handleBlockFormEvent(
        final Level level, final BlockPos pos, final BlockState state, final @Block.UpdateFlags int flags, final @Nullable Entity entity
    ) {
        return handleBlockFormEvent(level, pos, state, flags, entity, false);
    }

    public static boolean handleBlockFormEvent(
        final Level level, final BlockPos pos, final BlockState state, final @Block.UpdateFlags int flags, final @Nullable Entity entity, final boolean checkSetResult
    ) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, state);

        final BlockFormEvent event = entity == null ? new CraftBlockFormEvent(block, snapshot) : new CraftEntityBlockFormEvent(entity, block, snapshot);
        if (event.callEvent()) {
            final boolean result = snapshot.place(flags);
            return !checkSetResult || result;
        }

        return false;
    }

    public static @Nullable BlockState handleBlockFormEvent(final Level level, final BlockPos pos, final BlockState newState) {
        final org.bukkit.block.Block block = CraftBlock.at(level, pos);
        final CraftBlockState snapshot = CraftBlockStates.snapshotOfSimpleBlock(block, newState);

        final BlockFormEvent event = new CraftBlockFormEvent(block, snapshot);
        return event.callEvent() ? ((CraftBlockState) event.getNewState()).getHandle() : null;
    }

    public static EntityPickupItemEvent callEntityPickupItemEvent(final LivingEntity entity, final ItemEntity item, final int remaining, final boolean cancelled) {
        final EntityPickupItemEvent event = new CraftEntityPickupItemEvent(entity, item, remaining);
        event.setCancelled(cancelled);
        event.callEvent();
        return event;
    }

    public static @Nullable PortalEventResult handlePortalEvents(final Entity entity, final Location to, final PortalType type, final int searchRadius, final int createRadius) {
        final PortalEventResult result;
        if (entity instanceof final ServerPlayer player) {
            final PlayerTeleportEvent.TeleportCause cause = switch (type) {
                case PortalType.ENDER -> PlayerTeleportEvent.TeleportCause.END_PORTAL;
                case PortalType.NETHER -> PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
                case PortalType.END_GATEWAY -> PlayerTeleportEvent.TeleportCause.END_GATEWAY; // not actually used yet, the Player/EntityTeleportEndGatewayEvent is called instead
                case PortalType.CUSTOM -> PlayerTeleportEvent.TeleportCause.PLUGIN;
            };
            result = callPlayerPortalEvent(player, to, cause, searchRadius, createRadius);
        } else {
            result = callEntityPortalEvent(entity, to, type, searchRadius, createRadius);
        }
        return result;
    }

    public static @Nullable PortalEventResult callPlayerPortalEvent(
        final ServerPlayer player, final Location to, final PlayerTeleportEvent.TeleportCause cause, final int searchRadius, final int createRadius
    ) {
        final org.bukkit.entity.Player bukkitPlayer = player.getBukkitEntity();
        final Location from = bukkitPlayer.getLocation();
        final PlayerPortalEvent event = new CraftPlayerPortalEvent(bukkitPlayer, from, to, cause, searchRadius, true, createRadius);
        event.callEvent();
        if (event.isCancelled() || event.getTo() == null || event.getTo().getWorld() == null || !player.isAlive()) {
            return null;
        }

        return new PortalEventResult(event.getTo(), event.getSearchRadius(), event.getCreationRadius(), event.canCreatePortal());
    }

    public static @Nullable PortalEventResult callEntityPortalEvent(final Entity entity, final Location to, final PortalType type, final int searchRadius, final int createRadius) {
        final org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
        final Location from = bukkitEntity.getLocation();
        final EntityPortalEvent event = new CraftEntityPortalEvent(bukkitEntity, from, to, searchRadius, true, createRadius, type);
        event.callEvent();
        if (event.isCancelled() || event.getTo() == null || event.getTo().getWorld() == null || !entity.isAlive()) {
            return null;
        }

        return new PortalEventResult(event.getTo(), event.getSearchRadius(), event.getCreationRadius(), event.canCreatePortal());
    }

    public static io.papermc.paper.event.entity.EntityKnockbackEvent callEntityKnockbackEvent(
        final LivingEntity entity, final Entity pusher, final Entity attacker, final io.papermc.paper.event.entity.EntityKnockbackEvent.Cause cause, final double force, final Vec3 knockback
    ) {
        Vector apiKnockback = CraftVector.toBukkit(knockback);
        final org.bukkit.entity.LivingEntity apiEntity = entity.getBukkitEntity();

        final Vector currentVelocity = CraftVector.toBukkit(entity.getDeltaMovement());
        final Vector legacyFinalKnockback = currentVelocity.clone().add(apiKnockback);
        final EntityKnockbackEvent.KnockbackCause legacyCause = EntityKnockbackEvent.KnockbackCause.valueOf(cause.name());
        final EntityKnockbackEvent legacyEvent;
        if (pusher != null) {
            legacyEvent = new CraftEntityKnockbackByEntityEvent(apiEntity, pusher.getBukkitEntity(), legacyCause, force, apiKnockback, legacyFinalKnockback);
        } else {
            legacyEvent = new CraftEntityKnockbackEvent(apiEntity, legacyCause, force, apiKnockback, legacyFinalKnockback);
        }
        legacyEvent.callEvent();

        final io.papermc.paper.event.entity.EntityKnockbackEvent event;
        apiKnockback = legacyEvent.getFinalKnockback().subtract(currentVelocity);
        if (attacker != null) {
            event = new PaperEntityKnockbackByEntityEvent(apiEntity, attacker.getBukkitEntity(), cause, (float) force, apiKnockback);
        } else {
            event = new PaperEntityKnockbackEvent(apiEntity, cause, apiKnockback);
        }
        event.setCancelled(legacyEvent.isCancelled());
        event.callEvent();
        return event;
    }

    public static void callEntityRemoveEvent(final Entity entity, final EntityRemoveEvent.Cause cause) {
        if (entity instanceof ServerPlayer) {
            return; // Don't call for players
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

        new CraftEntityRemoveEvent(entity, cause).callEvent();
    }

    public static ItemStack handleWitchReadyPotionEvent(final Witch witch, final ItemStack potion) {
        final WitchReadyPotionEvent event = new PaperWitchReadyPotionEvent(witch, potion);
        if (!event.callEvent()) {
            return ItemStack.EMPTY;
        }
        return CraftItemStack.asNMSCopy(event.getPotion());
    }

    public static boolean handleBlockFailedDispenseEvent(final Level level, final BlockPos pos) {
        final BlockFailedDispenseEvent event = new PaperBlockFailedDispenseEvent(level, pos);
        return event.callEvent() && event.shouldPlayEffect();
    }

    public static @Nullable ItemStack handleBlockDispenseEvent(final BlockSource pointer, final BlockPos to, final ItemStack dispensed, final DispenseItemBehavior instance) {
        final BlockDispenseEvent event = new CraftBlockDispenseEvent(pointer, dispensed, to);
        final org.bukkit.inventory.ItemStack originalItem = event.getItem();
        if (!event.callEvent()) {
            return dispensed;
        }

        if (!event.getItem().equals(originalItem)) {
            // Chain to handler for new item
            final ItemStack newItem = CraftItemStack.asNMSCopy(event.getItem());
            final DispenseItemBehavior itemBehavior = DispenserBlock.getDispenseBehavior(pointer, newItem);
            if (itemBehavior != DispenseItemBehavior.NOOP && itemBehavior != instance) {
                itemBehavior.dispense(pointer, newItem);
                return dispensed;
            }
        }
        return null;
    }

    // @formatter:off
    /// Calls the [EntityFertilizeEggEvent].
    /// If the event is cancelled, this method also resets the love on both the `breeding` and `other` entity.
    ///
    /// @param breeding the entity on which #spawnChildFromBreeding was called.
    /// @param partner  the partner of the entity.
    /// @return the event after it was called. The instance may be used to retrieve the experience of the event.
    // @formatter:on
    public static EntityFertilizeEggEvent callEntityFertilizeEggEvent(final Animal breeding, final Animal partner) {
        ServerPlayer serverPlayer = breeding.getLoveCause();
        if (serverPlayer == null) serverPlayer = partner.getLoveCause();
        final int experience = breeding.getRandom().nextInt(7) + 1; // From Animal#spawnChildFromBreeding(ServerLevel, Animal)

        final EntityFertilizeEggEvent event = new PaperEntityFertilizeEggEvent(breeding, partner, serverPlayer, breeding.breedItem, experience);
        if (!event.callEvent()) {
            breeding.resetLove();
            partner.resetLove(); // stop the pathfinding to avoid infinite loop
        }

        return event;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public static @Nullable Component handleLoginResult(
        final PlayerList.LoginResult result, final ServerPacketListener packetListener, final Connection connection, final GameProfile profile
    ) {
        final PlayerConnectionValidateLoginEvent event = new PaperPlayerConnectionValidateLoginEvent(packetListener, result);
        event.callEvent();

        Component disconnectReason = PaperAdventure.asVanilla(event.getKickMessage());

        // For the login event it normally was never fired during configuration phase. In order to make this deprecation less
        // breaky we will cache result and use it next time.
        if (packetListener instanceof ServerLoginPacketListener) {
            disconnectReason = HorriblePlayerLoginEventHack.execute(
                connection, profile, disconnectReason == null ? PlayerList.LoginResult.ALLOW : new PlayerList.LoginResult(disconnectReason, result.result())
            );
        } else if (connection.legacySavedLoginEventResultOverride != null) {
            // If the override is set, use it.
            disconnectReason = connection.legacySavedLoginEventResultOverride.orElse(null);
        }

        return disconnectReason;
    }

    public static boolean callTransporterValidateTarget(final PathfinderMob mob, final Level level, final BlockPos pos) {
        if (ItemTransportingEntityValidateTargetEvent.getHandlerList().getRegisteredListeners().length == 0) {
            return true; // No listeners, skip event creation
        }
        final ItemTransportingEntityValidateTargetEvent event = new PaperItemTransportingEntityValidateTargetEvent(mob, level, pos);
        event.callEvent();
        return event.isAllowed();
    }

    public static <T> PaperWorldGameRuleChangeEvent<T> handleGameRuleSet(final GameRule<T> rule, final T value, final ServerLevel level, final @Nullable CommandSender sender) {
        final PaperWorldGameRuleChangeEvent<T> event = new PaperWorldGameRuleChangeEvent<>(level, sender, rule, value);
        if (event.callEvent()) {
            level.getGameRules().set(rule, event.newValue(), level);
        }
        return event;
    }

    public static boolean callBlockLockCheckEvent(final BlockEntity blockEntity, final LockCode code, final Component displayName, final Player player) {
        if (!(player instanceof final ServerPlayer serverPlayer) ||
            blockEntity.getLevel() == null || blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) != blockEntity) {
            return true;
        }

        final BlockLockCheckEvent event = new PaperBlockLockCheckEvent(blockEntity, serverPlayer, displayName);
        event.callEvent();
        final boolean unlocked = event.getResult() == Event.Result.ALLOW ||
            (event.getResult() == Event.Result.DEFAULT && code.canUnlock(serverPlayer, event.isUsingCustomKeyItemStack() ? CraftItemStack.asNMSCopy(event.getKeyItem()) : serverPlayer.getMainHandItem()));
        if (!unlocked) {
            PaperBlockLockCheckEvent.LAST_LOCKED_EVENT = event;
        }
        return unlocked;
    }

    public static boolean callPlayerToggleEntityAgeLockEvent(
        final Player player, final Mob target, final ItemStack itemUsed, final InteractionHand hand, final boolean ageLocked, final @Nullable EntityDataAccessor<?> accessorToResync
    ) {
        final PlayerToggleEntityAgeLockEvent event = new PaperPlayerToggleEntityAgeLockEvent(player, target, itemUsed, hand, ageLocked);
        if (!event.callEvent()) {
            if (accessorToResync != null && player instanceof final ServerPlayer serverPlayer) {
                target.resendPossiblyDesyncedDataValues(List.of(accessorToResync), serverPlayer);
            }
            if (!player.hasInfiniteMaterials()) {
                player.inventoryMenu.forceHeldSlot(hand);
            }
            return false;
        }
        return true;
    }

    public static ClockTimeSkipEvent createTimeSkipEvent(final CommandSourceStack source, final long skipAmount) {
        if (GlobalConfiguration.get().time.affectsAllWorlds) {
            return new CraftClockTimeSkipEvent(ClockTimeSkipEvent.SkipReason.COMMAND, skipAmount);
        }
        return new CraftTimeSkipEvent(source.getLevel(), ClockTimeSkipEvent.SkipReason.COMMAND, skipAmount);
    }

    public static int callEntityIgniteEvent(final Entity entity, final int fuseTime) {
        if (EntityIgniteEvent.getHandlerList().getRegisteredListeners().length == 0) {
            return fuseTime; // No listeners, skip event creation
        }

        final EntityIgniteEvent event = new PaperEntityIgniteEvent(entity, fuseTime);
        if (!event.callEvent()) {
            return PrimedTnt.NO_FUSE;
        }
        return event.getFuseTime();
    }

    public static void handleModernServerListPingEvent(final MinecraftServer server, final Connection connection) {
        final PaperServerListPingEventImpl.Standard event = PaperServerListPingEventImpl.modern(server, connection, server.getStatus());

        // Close connection immediately if event is cancelled
        if (!event.callEvent()) {
            connection.disconnect(CommonComponents.EMPTY);
            return;
        }

        // Send response
        connection.send(new ClientboundStatusResponsePacket(event.packStatus()));
    }

    public static @Nullable Container callHopperInventorySearchEvent(
        final Level level,
        final BlockPos hopperPos,
        final @Nullable Container container,
        final HopperInventorySearchEvent.ContainerType containerType,
        final BlockPos searchPos
    ) {
        if (HopperInventorySearchEvent.getHandlerList().getRegisteredListeners().length == 0) return container; // No listeners, skip event creation
        final HopperInventorySearchEvent event = new CraftHopperInventorySearchEvent(level, hopperPos, container, containerType, searchPos);
        event.callEvent();
        return event.getInventory() != null ? ((CraftInventory) event.getInventory()).getInventory() : null;
    }

    public static ServerLinks handlePlayerLinksSendEvent(final ServerConfigurationPacketListener packetListener, final ServerLinks links) {
        final CraftPlayerLinksSendEvent event = new CraftPlayerLinksSendEvent(packetListener, links);
        event.callEvent();
        return ((CraftServerLinks) event.getLinks()).getServerLinks();
    }

    public static void reportInternalException(final Throwable cause) {
        try {
            new PaperServerExceptionEvent(new ServerInternalException(cause)).callEvent();
        } catch (final Throwable t) {
            Bukkit.getLogger().log(java.util.logging.Level.WARNING, "Exception posting PaperServerExceptionEvent", t); // Don't want to rethrow!
        }
    }
}
