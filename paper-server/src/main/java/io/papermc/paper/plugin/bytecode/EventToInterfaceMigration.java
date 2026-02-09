package io.papermc.paper.plugin.bytecode;

import com.destroystokyo.paper.event.block.*;
import com.destroystokyo.paper.event.brigadier.*;
import com.destroystokyo.paper.event.entity.*;
import com.destroystokyo.paper.event.player.*;
import com.destroystokyo.paper.loottable.*;
import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.RewriteRuleVisitorFactory;
import io.papermc.asm.rules.classes.ClassToInterfaceRule;
import io.papermc.paper.event.block.*;
import io.papermc.paper.event.entity.*;
import io.papermc.paper.event.packet.*;
import io.papermc.paper.event.player.*;
import java.util.Set;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public final class EventToInterfaceMigration {

    private static final RewriteRuleVisitorFactory VISITOR_FACTORY = RewriteRuleVisitorFactory.create(
        Opcodes.ASM9,
        chain -> {
            final Set<Class<? extends Event>> eventClasses = Set.of(
                //<editor-fold desc="event classes" defaultstate="collapsed">
                PlayerClientLoadedWorldEvent.class,
                PlayerCustomClickEvent.class,
                PlayerDeepSleepEvent.class,
                PlayerFailMoveEvent.class,
                PlayerFlowerPotManipulateEvent.class,
                PlayerInsertLecternBookEvent.class,
                PlayerInventorySlotChangeEvent.class,
                PlayerChangeBeaconEffectEvent.class,
                PlayerBedFailEnterEvent.class,
                PlayerItemFrameChangeEvent.class,
                PlayerLecternPageChangeEvent.class,
                PlayerLoomPatternSelectEvent.class,
                PlayerMapFilledEvent.class,
                PlayerNameEntityEvent.class,
                PlayerOpenSignEvent.class,
                PlayerServerFullCheckEvent.class,
                PlayerShieldDisableEvent.class,
                PlayerStonecutterRecipeSelectEvent.class,
                PlayerPickItemEvent.class,
                PlayerPickBlockEvent.class,
                PlayerPickEntityEvent.class,
                PlayerStopUsingItemEvent.class,
                PlayerTrackEntityEvent.class,
                PlayerUntrackEntityEvent.class,
                PrePlayerAttackEntityEvent.class,
                PlayerPurchaseEvent.class,
                PlayerTradeEvent.class,
                PlayerItemGroupCooldownEvent.class,
                PlayerItemCooldownEvent.class,
                AsyncPlayerSpawnLocationEvent.class,
                AbstractChatEvent.class,
                AsyncChatEvent.class,
                ChatEvent.class,
                PlayerCommandPreprocessEvent.class,
                PlayerSignCommandPreprocessEvent.class,
                AbstractRespawnEvent.class,
                PlayerRespawnEvent.class,
                PlayerPostRespawnEvent.class,
                PlayerAnimationEvent.class,
                PlayerArmSwingEvent.class,
                PlayerAdvancementDoneEvent.class,
                PlayerInteractEntityEvent.class,
                PlayerInteractAtEntityEvent.class,
                PlayerArmorStandManipulateEvent.class,
                PlayerAttemptPickupItemEvent.class,
                PlayerBedEnterEvent.class,
                PlayerBedLeaveEvent.class,
                PlayerBucketEvent.class,
                PlayerBucketFillEvent.class,
                PlayerBucketEmptyEvent.class,
                PlayerBucketEntityEvent.class,
                PlayerBucketFishEvent.class,
                PlayerChangedMainHandEvent.class,
                PlayerChangedWorldEvent.class,
                PlayerChannelEvent.class,
                PlayerRegisterChannelEvent.class,
                PlayerUnregisterChannelEvent.class,
                PlayerChatEvent.class,
                PlayerChatTabCompleteEvent.class,
                PlayerCommandSendEvent.class,
                PlayerDropItemEvent.class,
                PlayerEditBookEvent.class,
                PlayerEggThrowEvent.class,
                PlayerExpChangeEvent.class,
                PlayerExpCooldownChangeEvent.class,
                PlayerFishEvent.class,
                PlayerGameModeChangeEvent.class,
                PlayerHarvestBlockEvent.class,
                PlayerHideEntityEvent.class,
                PlayerInputEvent.class,
                PlayerInteractEvent.class,
                PlayerItemBreakEvent.class,
                PlayerItemConsumeEvent.class,
                PlayerItemDamageEvent.class,
                PlayerItemHeldEvent.class,
                PlayerItemMendEvent.class,
                PlayerJoinEvent.class,
                PlayerKickEvent.class,
                PlayerLevelChangeEvent.class,
                PlayerLinksSendEvent.class,
                PlayerLocaleChangeEvent.class,
                PlayerLoginEvent.class,
                PlayerMoveEvent.class,
                PlayerTeleportEvent.class,
                PlayerTeleportEndGatewayEvent.class,
                PlayerPortalEvent.class,
                PlayerPickupItemEvent.class,
                PlayerPickupArrowEvent.class,
                PlayerPreLoginEvent.class,
                PlayerQuitEvent.class,
                org.bukkit.event.player.PlayerRecipeBookClickEvent.class,
                PlayerRecipeBookSettingsChangeEvent.class,
                PlayerRecipeDiscoverEvent.class,
                PlayerResourcePackStatusEvent.class,
                PlayerRiptideEvent.class,
                PlayerShearEntityEvent.class,
                PlayerShowEntityEvent.class,
                PlayerSignOpenEvent.class,
                PlayerSpawnChangeEvent.class,
                PlayerStatisticIncrementEvent.class,
                PlayerSwapHandItemsEvent.class,
                PlayerTakeLecternBookEvent.class,
                PlayerToggleFlightEvent.class,
                PlayerToggleSneakEvent.class,
                PlayerToggleSprintEvent.class,
                PlayerVelocityEvent.class,
                AsyncPlayerChatEvent.class,
                AsyncPlayerChatPreviewEvent.class,
                IllegalPacketEvent.class,
                PlayerAdvancementCriterionGrantEvent.class,
                PlayerArmorChangeEvent.class,
                PlayerAttackEntityCooldownResetEvent.class,
                PlayerClientOptionsChangeEvent.class,
                PlayerConnectionCloseEvent.class,
                PlayerElytraBoostEvent.class,
                PlayerHandshakeEvent.class,
                PlayerJumpEvent.class,
                PlayerLaunchProjectileEvent.class,
                PlayerPickupExperienceEvent.class,
                PlayerReadyArrowEvent.class,
                com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent.class,
                PlayerSetSpawnEvent.class,
                PlayerStartSpectatingEntityEvent.class,
                PlayerStopSpectatingEntityEvent.class,
                PlayerUseUnknownEntityEvent.class,
                AsyncPlayerSendCommandsEvent.class,
                AsyncPlayerSendSuggestionsEvent.class,
                ClientTickEndEvent.class,
                PlayerShearBlockEvent.class,
                PlayerNaturallySpawnCreaturesEvent.class,
                LootableInventoryReplenishEvent.class,
                UncheckedSignChangeEvent.class,
                PlayerSpawnLocationEvent.class,
                PlayerEvent.class,
                BeaconActivatedEvent.class,
                BeaconDeactivatedEvent.class,
                BellRevealRaiderEvent.class,
                io.papermc.paper.event.block.BellRingEvent.class,
                org.bukkit.event.block.BellRingEvent.class,
                BlockBreakProgressUpdateEvent.class,
                BlockFailedDispenseEvent.class,
                BlockLockCheckEvent.class,
                BlockPreDispenseEvent.class,
                VaultChangeStateEvent.class,
                CompostItemEvent.class,
                EntityCompostItemEvent.class,
                BlockExpEvent.class,
                BlockBreakBlockEvent.class,
                BlockBreakEvent.class,
                FurnaceExtractEvent.class,
                BlockDestroyEvent.class,
                BlockGrowEvent.class,
                BlockFormEvent.class,
                DragonEggFormEvent.class,
                EntityBlockFormEvent.class,
                BlockSpreadEvent.class,
                BellResonateEvent.class,
                BlockBurnEvent.class,
                BlockCanBuildEvent.class,
                BlockCookEvent.class,
                FurnaceSmeltEvent.class,
                BlockDamageAbortEvent.class,
                BlockDamageEvent.class,
                BlockDispenseEvent.class,
                BlockDispenseArmorEvent.class,
                BlockDispenseLootEvent.class,
                BlockDropItemEvent.class,
                BlockExplodeEvent.class,
                BlockFadeEvent.class,
                BlockFertilizeEvent.class,
                BlockFromToEvent.class,
                BlockPlaceEvent.class,
                BlockMultiPlaceEvent.class
                //</editor-fold>
            );

            for (final Class<? extends Event> eventClass : eventClasses) {
                chain.then(new ClassToInterfaceRule(eventClass.describeConstable().orElseThrow(), null));
            }
        },
        ClassInfoProvider.basic()
    );

    private EventToInterfaceMigration() {
    }

    public static ClassVisitor visitor(final ClassVisitor parent) {
        return VISITOR_FACTORY.createVisitor(parent);
    }

    public static byte[] processClass(final byte[] bytes) {
        final ClassReader classReader = new ClassReader(bytes);
        final ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(visitor(classWriter), 0);
        return classWriter.toByteArray();
    }

}
