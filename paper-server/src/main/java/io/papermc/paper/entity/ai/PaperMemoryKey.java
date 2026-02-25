package io.papermc.paper.entity.ai;

import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import io.papermc.paper.util.MCUtil;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftAgeable;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHoglin;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.craftbukkit.entity.CraftPiglinAbstract;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.Player;

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public class PaperMemoryKey<API, NMS> extends HolderableBase<MemoryModuleType<NMS>> implements MemoryKey {

    private static final PaperTypedDataAdapters<MemoryModuleType<?>> ADAPTERS = PaperTypedDataAdapters.create(
        BuiltInRegistries.MEMORY_MODULE_TYPE,
        PaperMemoryKeyCollector::new,
        collector -> {
            collector.registerUntyped(MemoryModuleType.DUMMY); // special case
            collector.register(MemoryModuleType.HOME, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.register(MemoryModuleType.JOB_SITE, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.register(MemoryModuleType.POTENTIAL_JOB_SITE, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.register(MemoryModuleType.MEETING_POINT, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.register(MemoryModuleType.SECONDARY_JOB_SITE,
                nms -> transformUnmodifiable(nms, CraftLocation::fromGlobalPos),
                api -> transformUnmodifiable(api, CraftLocation::toGlobalPos)
            );
            collector.register(MemoryModuleType.NEAREST_LIVING_ENTITIES,
                nms -> transformUnmodifiable(nms, entity -> (LivingEntity) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftLivingEntity) entity).getHandle())
            );
            //collector.register(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, fromApi, toApi);
            collector.register(MemoryModuleType.VISIBLE_VILLAGER_BABIES,
                nms -> transformUnmodifiable(nms, entity -> (LivingEntity) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftLivingEntity) entity).getHandle())
            );
            collector.register(MemoryModuleType.NEAREST_PLAYERS,
                nms -> transformUnmodifiable(nms, entity -> (Player) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftPlayer) entity).getHandle())
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                nms -> (Player) nms.getBukkitEntity(),
                api -> ((CraftPlayer) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                nms -> (Player) nms.getBukkitEntity(),
                api -> ((CraftPlayer) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS,
                nms -> transformUnmodifiable(nms, entity -> (Player) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftPlayer) entity).getHandle())
            );
            //collector.register(MemoryModuleType.WALK_TARGET, fromApi, toApi);
            //collector.register(MemoryModuleType.LOOK_TARGET, fromApi, toApi);
            collector.register(MemoryModuleType.ATTACK_TARGET,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.registerIdentity(MemoryModuleType.ATTACK_COOLING_DOWN);
            collector.register(MemoryModuleType.INTERACTION_TARGET,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.BREED_TARGET,
                nms -> (Ageable) nms.getBukkitEntity(),
                api -> ((CraftAgeable) api).getHandle()
            );
            collector.register(MemoryModuleType.RIDE_TARGET,
                nms -> (Entity) nms.getBukkitEntity(),
                api -> ((CraftEntity) api).getHandle()
            );
            //collector.register(MemoryModuleType.PATH, fromApi, toApi);
            collector.register(MemoryModuleType.INTERACTABLE_DOORS,
                nms -> transformUnmodifiable(nms, CraftLocation::fromGlobalPos),
                api -> transformUnmodifiable(api, CraftLocation::toGlobalPos)
            );
            collector.register(MemoryModuleType.DOORS_TO_CLOSE,
                nms -> nms.stream().map(CraftLocation::fromGlobalPos).collect(Collectors.toSet()),
                api -> api.stream().map(CraftLocation::toGlobalPos).collect(Collectors.toSet())
            );
            collector.register(MemoryModuleType.NEAREST_BED, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.register(MemoryModuleType.HURT_BY, CraftDamageSource::new, CraftDamageSource::getHandle);
            collector.register(MemoryModuleType.HURT_BY_ENTITY,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.AVOID_TARGET,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_HOSTILE,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_ATTACKABLE,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.HIDING_PLACE, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.registerIdentity(MemoryModuleType.HEARD_BELL_TIME);
            collector.registerIdentity(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            collector.registerIdentity(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
            collector.registerIdentity(MemoryModuleType.DANGER_DETECTED_RECENTLY);
            collector.registerIdentity(MemoryModuleType.LAST_SLEPT);
            collector.registerIdentity(MemoryModuleType.LAST_WOKEN);
            collector.registerIdentity(MemoryModuleType.LAST_WORKED_AT_POI);
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ADULT,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                nms -> (Item) nms.getBukkitEntity(),
                api -> ((CraftItem) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                nms -> (Mob) nms.getBukkitEntity(),
                api -> ((CraftMob) api).getHandle()
            );
            collector.registerIdentity(MemoryModuleType.PLAY_DEAD_TICKS);
            collector.register(MemoryModuleType.TEMPTING_PLAYER,
                nms -> (Player) nms.getBukkitEntity(),
                api -> ((CraftPlayer) api).getHandle()
            );
            collector.registerIdentity(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.GAZE_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.IS_TEMPTED);
            collector.registerIdentity(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.LONG_JUMP_MID_JUMP);
            collector.registerIdentity(MemoryModuleType.HAS_HUNTING_COOLDOWN);
            collector.registerIdentity(MemoryModuleType.RAM_COOLDOWN_TICKS);
            collector.register(MemoryModuleType.RAM_TARGET, MCUtil::toPosition, MCUtil::toVec3);
            collector.registerUntyped(MemoryModuleType.IS_IN_WATER);
            collector.registerUntyped(MemoryModuleType.IS_PREGNANT);
            collector.registerIdentity(MemoryModuleType.IS_PANICKING);
            collector.registerIdentity(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS); // todo unmodifiable?
            collector.register(MemoryModuleType.VISITED_BLOCK_POSITIONS,
                nms -> nms.stream().map(CraftLocation::fromGlobalPos).collect(Collectors.toSet()),
                api -> api.stream().map(CraftLocation::toGlobalPos).collect(Collectors.toSet())
            );
            collector.register(MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS,
                nms -> nms.stream().map(CraftLocation::fromGlobalPos).collect(Collectors.toSet()),
                api -> api.stream().map(CraftLocation::toGlobalPos).collect(Collectors.toSet())
            );
            collector.registerIdentity(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.CHARGE_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.ATTACK_TARGET_COOLDOWN);
            collector.registerIdentity(MemoryModuleType.SPEAR_FLEEING_TIME);
            collector.register(MemoryModuleType.SPEAR_FLEEING_POSITION, MCUtil::toPosition, MCUtil::toVec3);
            collector.register(MemoryModuleType.SPEAR_CHARGE_POSITION, MCUtil::toPosition, MCUtil::toVec3);
            collector.registerIdentity(MemoryModuleType.SPEAR_ENGAGE_TIME);
            //collector.register(MemoryModuleType.SPEAR_STATUS, fromApi, toApi);
            collector.registerIdentity(MemoryModuleType.ANGRY_AT);
            collector.registerIdentity(MemoryModuleType.UNIVERSAL_ANGER);
            collector.registerIdentity(MemoryModuleType.ADMIRING_ITEM);
            collector.registerIdentity(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            collector.registerIdentity(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM);
            collector.registerIdentity(MemoryModuleType.ADMIRING_DISABLED);
            collector.registerIdentity(MemoryModuleType.HUNTED_RECENTLY);
            collector.register(MemoryModuleType.CELEBRATE_LOCATION, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.registerIdentity(MemoryModuleType.DANCING);
            collector.register(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN,
                nms -> (Hoglin) nms.getBukkitEntity(),
                api -> ((CraftHoglin) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
                nms -> (Hoglin) nms.getBukkitEntity(),
                api -> ((CraftHoglin) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
                nms -> (Player) nms.getBukkitEntity(),
                api -> ((CraftPlayer) api).getHandle()
            );
            collector.register(MemoryModuleType.NEARBY_ADULT_PIGLINS,
                nms -> transformUnmodifiable(nms, entity -> (PiglinAbstract) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftPiglinAbstract) entity).getHandle())
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                nms -> transformUnmodifiable(nms, entity -> (PiglinAbstract) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftPiglinAbstract) entity).getHandle())
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS,
                nms -> transformUnmodifiable(nms, entity -> (Hoglin) entity.getBukkitEntity()),
                api -> transformUnmodifiable(api, entity -> ((CraftHoglin) entity).getHandle())
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN,
                nms -> (PiglinAbstract) nms.getBukkitEntity(),
                api -> ((CraftPiglinAbstract) api).getHandle()
            );
            collector.register(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.registerIdentity(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT);
            collector.registerIdentity(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT);
            collector.register(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
                nms -> (Player) nms.getBukkitEntity(),
                api -> ((CraftPlayer) api).getHandle()
            );
            collector.registerIdentity(MemoryModuleType.ATE_RECENTLY);
            collector.register(MemoryModuleType.NEAREST_REPELLENT, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.registerIdentity(MemoryModuleType.PACIFIED);
            collector.register(MemoryModuleType.ROAR_TARGET,
                nms -> (LivingEntity) nms.getBukkitEntity(),
                api -> ((CraftLivingEntity) api).getHandle()
            );
            collector.register(MemoryModuleType.DISTURBANCE_LOCATION, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.registerUntyped(MemoryModuleType.RECENT_PROJECTILE);
            collector.registerUntyped(MemoryModuleType.IS_SNIFFING);
            collector.registerUntyped(MemoryModuleType.IS_EMERGING);
            collector.registerUntyped(MemoryModuleType.ROAR_SOUND_DELAY);
            collector.registerUntyped(MemoryModuleType.DIG_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.ROAR_SOUND_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.SNIFF_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.TOUCH_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.VIBRATION_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.SONIC_BOOM_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.SONIC_BOOM_SOUND_DELAY);
            collector.registerIdentity(MemoryModuleType.LIKED_PLAYER);
            collector.register(MemoryModuleType.LIKED_NOTEBLOCK_POSITION, CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
            collector.registerIdentity(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
            collector.registerIdentity(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS);
            collector.register(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS,
                nms -> transformUnmodifiable(nms, CraftLocation::fromGlobalPos),
                api -> transformUnmodifiable(api, CraftLocation::toGlobalPos)
            );
            collector.register(MemoryModuleType.SNIFFER_SNIFFING_TARGET, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.registerIdentity(MemoryModuleType.SNIFFER_DIGGING);
            collector.registerIdentity(MemoryModuleType.SNIFFER_HAPPY);
            collector.registerUntyped(MemoryModuleType.BREEZE_JUMP_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.BREEZE_SHOOT);
            collector.registerUntyped(MemoryModuleType.BREEZE_SHOOT_CHARGING);
            collector.registerUntyped(MemoryModuleType.BREEZE_SHOOT_RECOVERING);
            collector.registerUntyped(MemoryModuleType.BREEZE_SHOOT_COOLDOWN);
            collector.registerUntyped(MemoryModuleType.BREEZE_JUMP_INHALING);
            collector.register(MemoryModuleType.BREEZE_JUMP_TARGET, MCUtil::toPosition, MCUtil::toBlockPos);
            collector.registerUntyped(MemoryModuleType.BREEZE_LEAVING_WATER);
        }
    );

    private final PaperTypedDataAdapter<API, NMS> adapter;

    private PaperMemoryKey(final Holder<MemoryModuleType<NMS>> holder, final PaperTypedDataAdapter<API, NMS> adapter) {
        super(holder);
        this.adapter = adapter;
    }

    public PaperTypedDataAdapter<API, NMS> getAdapter() {
        return this.adapter;
    }

    @SuppressWarnings("unchecked")
    public static <NMS> MemoryKey of(final Holder<?> holder) {
        final Holder.Reference<MemoryModuleType<NMS>> reference = (Holder.Reference<MemoryModuleType<NMS>>) holder;
        final PaperTypedDataAdapter<?, NMS> adapter = PaperMemoryKey.ADAPTERS.get(reference.key());
        if (adapter.isUnimplemented()) {
            return new Unimplemented<>(reference, adapter);
        } else if (adapter.isUntyped()) {
            return new NonValuedImpl<>(reference, adapter);
        } else {
            return new ValuedImpl<>(reference, adapter);
        }
    }

    public static final class NonValuedImpl<API, NMS> extends PaperMemoryKey<API, NMS> implements MemoryKey.NonValued {

        NonValuedImpl(
            final Holder<MemoryModuleType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class ValuedImpl<API, NMS> extends PaperMemoryKey<API, NMS> implements MemoryKey.Valued<API> {

        ValuedImpl(
            final Holder<MemoryModuleType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class Unimplemented<API, NMS> extends PaperMemoryKey<API, NMS> {

        public Unimplemented(
            final Holder<MemoryModuleType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }
}
