package io.papermc.paper.plugin.bytecode;

import com.destroystokyo.paper.event.player.*;
import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.RewriteRuleVisitorFactory;
import io.papermc.asm.rules.classes.ClassToInterfaceRule;
import io.papermc.paper.event.player.*;
import java.util.Set;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

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
                PlayerExpChangeEvent.class
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
