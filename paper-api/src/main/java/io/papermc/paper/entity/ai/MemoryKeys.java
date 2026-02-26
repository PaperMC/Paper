package io.papermc.paper.entity.ai;

import com.destroystokyo.paper.entity.Pathfinder;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.Player;

public final class MemoryKeys {

    public static final MemoryKey.NonValued DUMMY = unvalued("dummy");
    public static final MemoryKey.Valued<Location> HOME = valued("home");
    public static final MemoryKey.Valued<Location> JOB_SITE = valued("job_site");
    public static final MemoryKey.Valued<Location> POTENTIAL_JOB_SITE = valued("potential_job_site");
    public static final MemoryKey.Valued<Location> MEETING_POINT = valued("meeting_point");
    public static final MemoryKey.Valued<List<Location>> SECONDARY_JOB_SITE = valued("secondary_job_site");
    public static final MemoryKey.Valued<List<LivingEntity>> NEAREST_LIVING_ENTITIES = valued("mobs");
    //public static final MemoryKey.Valued<NearestVisibleLivingEntities> NEAREST_VISIBLE_LIVING_ENTITIES = valued("visible_mobs");
    public static final MemoryKey.Valued<List<LivingEntity>> VISIBLE_VILLAGER_BABIES = valued("visible_villager_babies");
    public static final MemoryKey.Valued<List<Player>> NEAREST_PLAYERS = valued("nearest_players");
    public static final MemoryKey.Valued<Player> NEAREST_VISIBLE_PLAYER = valued("nearest_visible_player");
    public static final MemoryKey.Valued<Player> NEAREST_VISIBLE_ATTACKABLE_PLAYER = valued("nearest_visible_targetable_player");
    public static final MemoryKey.Valued<List<Player>> NEAREST_VISIBLE_ATTACKABLE_PLAYERS = valued("nearest_visible_targetable_players");
    //public static final MemoryKey.Valued<WalkTarget> WALK_TARGET = valued("walk_target");
    //public static final MemoryKey.Valued<PositionTracker> LOOK_TARGET = valued("look_target");
    public static final MemoryKey.Valued<LivingEntity> ATTACK_TARGET = valued("attack_target");
    public static final MemoryKey.Valued<Boolean> ATTACK_COOLING_DOWN = valued("attack_cooling_down");
    public static final MemoryKey.Valued<LivingEntity> INTERACTION_TARGET = valued("interaction_target");
    public static final MemoryKey.Valued<Ageable> BREED_TARGET = valued("breed_target");
    public static final MemoryKey.Valued<Entity> RIDE_TARGET = valued("ride_target");
    public static final MemoryKey.Valued<Pathfinder.PathResult> PATH = valued("path");
    public static final MemoryKey.Valued<List<Location>> INTERACTABLE_DOORS = valued("interactable_doors");
    public static final MemoryKey.Valued<Set<Location>> DOORS_TO_CLOSE = valued("doors_to_close");
    public static final MemoryKey.Valued<BlockPosition> NEAREST_BED = valued("nearest_bed");
    public static final MemoryKey.Valued<DamageSource> HURT_BY = valued("hurt_by");
    public static final MemoryKey.Valued<LivingEntity> HURT_BY_ENTITY = valued("hurt_by_entity");
    public static final MemoryKey.Valued<LivingEntity> AVOID_TARGET = valued("avoid_target");
    public static final MemoryKey.Valued<LivingEntity> NEAREST_HOSTILE = valued("nearest_hostile");
    public static final MemoryKey.Valued<LivingEntity> NEAREST_ATTACKABLE = valued("nearest_attackable");
    public static final MemoryKey.Valued<Location> HIDING_PLACE = valued("hiding_place");
    public static final MemoryKey.Valued<Long> HEARD_BELL_TIME = valued("heard_bell_time");
    public static final MemoryKey.Valued<Long> CANT_REACH_WALK_TARGET_SINCE = valued("cant_reach_walk_target_since");
    public static final MemoryKey.Valued<Boolean> GOLEM_DETECTED_RECENTLY = valued("golem_detected_recently");
    public static final MemoryKey.Valued<Boolean> DANGER_DETECTED_RECENTLY = valued("danger_detected_recently");
    public static final MemoryKey.Valued<Long> LAST_SLEPT = valued("last_slept");
    public static final MemoryKey.Valued<Long> LAST_WOKEN = valued("last_woken");
    public static final MemoryKey.Valued<Long> LAST_WORKED_AT_POI = valued("last_worked_at_poi");
    public static final MemoryKey.Valued<LivingEntity> NEAREST_VISIBLE_ADULT = valued("nearest_visible_adult");
    public static final MemoryKey.Valued<Item> NEAREST_VISIBLE_WANTED_ITEM = valued("nearest_visible_wanted_item");
    public static final MemoryKey.Valued<Mob> NEAREST_VISIBLE_NEMESIS = valued("nearest_visible_nemesis");
    public static final MemoryKey.Valued<Integer> PLAY_DEAD_TICKS = valued("play_dead_ticks");
    public static final MemoryKey.Valued<Player> TEMPTING_PLAYER = valued("tempting_player");
    public static final MemoryKey.Valued<Integer> TEMPTATION_COOLDOWN_TICKS = valued("temptation_cooldown_ticks");
    public static final MemoryKey.Valued<Integer> GAZE_COOLDOWN_TICKS = valued("gaze_cooldown_ticks");
    public static final MemoryKey.Valued<Boolean> IS_TEMPTED = valued("is_tempted");
    public static final MemoryKey.Valued<Integer> LONG_JUMP_COOLDOWN_TICKS = valued("long_jump_cooling_down");
    public static final MemoryKey.Valued<Boolean> LONG_JUMP_MID_JUMP = valued("long_jump_mid_jump");
    public static final MemoryKey.Valued<Boolean> HAS_HUNTING_COOLDOWN = valued("has_hunting_cooldown");
    public static final MemoryKey.Valued<Integer> RAM_COOLDOWN_TICKS = valued("ram_cooldown_ticks");
    public static final MemoryKey.Valued<Position> RAM_TARGET = valued("ram_target");
    public static final MemoryKey.NonValued IS_IN_WATER = unvalued("is_in_water");
    public static final MemoryKey.NonValued IS_PREGNANT = unvalued("is_pregnant");
    public static final MemoryKey.Valued<Boolean> IS_PANICKING = valued("is_panicking");
    public static final MemoryKey.Valued<List<UUID>> UNREACHABLE_TONGUE_TARGETS = valued("unreachable_tongue_targets");
    public static final MemoryKey.Valued<Set<Location>> VISITED_BLOCK_POSITIONS = valued("visited_block_positions");
    public static final MemoryKey.Valued<Set<Location>> UNREACHABLE_TRANSPORT_BLOCK_POSITIONS = valued("unreachable_transport_block_positions");
    public static final MemoryKey.Valued<Integer> TRANSPORT_ITEMS_COOLDOWN_TICKS = valued("transport_items_cooldown_ticks");
    public static final MemoryKey.Valued<Integer> CHARGE_COOLDOWN_TICKS = valued("charge_cooldown_ticks");
    public static final MemoryKey.Valued<Integer> ATTACK_TARGET_COOLDOWN = valued("attack_target_cooldown");
    public static final MemoryKey.Valued<Integer> SPEAR_FLEEING_TIME = valued("spear_fleeing_time");
    public static final MemoryKey.Valued<Position> SPEAR_FLEEING_POSITION = valued("spear_fleeing_position");
    public static final MemoryKey.Valued<Position> SPEAR_CHARGE_POSITION = valued("spear_charge_position");
    public static final MemoryKey.Valued<Integer> SPEAR_ENGAGE_TIME = valued("spear_engage_time");
    //public static final MemoryKey.Valued<SpearAttack.SpearStatus> SPEAR_STATUS = valued("spear_status");
    public static final MemoryKey.Valued<UUID> ANGRY_AT = valued("angry_at");
    public static final MemoryKey.Valued<Boolean> UNIVERSAL_ANGER = valued("universal_anger");
    public static final MemoryKey.Valued<Boolean> ADMIRING_ITEM = valued("admiring_item");
    public static final MemoryKey.Valued<Integer> TIME_TRYING_TO_REACH_ADMIRE_ITEM = valued("time_trying_to_reach_admire_item");
    public static final MemoryKey.Valued<Boolean> DISABLE_WALK_TO_ADMIRE_ITEM = valued("disable_walk_to_admire_item");
    public static final MemoryKey.Valued<Boolean> ADMIRING_DISABLED = valued("admiring_disabled");
    public static final MemoryKey.Valued<Boolean> HUNTED_RECENTLY = valued("hunted_recently");
    public static final MemoryKey.Valued<Location> CELEBRATE_LOCATION = valued("celebrate_location");
    public static final MemoryKey.Valued<Boolean> DANCING = valued("dancing");
    public static final MemoryKey.Valued<Hoglin> NEAREST_VISIBLE_HUNTABLE_HOGLIN = valued("nearest_visible_huntable_hoglin");
    public static final MemoryKey.Valued<Hoglin> NEAREST_VISIBLE_BABY_HOGLIN = valued("nearest_visible_baby_hoglin");
    public static final MemoryKey.Valued<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD = valued("nearest_targetable_player_not_wearing_gold");
    public static final MemoryKey.Valued<List<PiglinAbstract>> NEARBY_ADULT_PIGLINS = valued("nearby_adult_piglins");
    public static final MemoryKey.Valued<List<PiglinAbstract>> NEAREST_VISIBLE_ADULT_PIGLINS = valued("nearest_visible_adult_piglins");
    public static final MemoryKey.Valued<List<Hoglin>> NEAREST_VISIBLE_ADULT_HOGLINS = valued("nearest_visible_adult_hoglins");
    public static final MemoryKey.Valued<PiglinAbstract> NEAREST_VISIBLE_ADULT_PIGLIN = valued("nearest_visible_adult_piglin");
    public static final MemoryKey.Valued<LivingEntity> NEAREST_VISIBLE_ZOMBIFIED = valued("nearest_visible_zombified");
    public static final MemoryKey.Valued<Integer> VISIBLE_ADULT_PIGLIN_COUNT = valued("visible_adult_piglin_count");
    public static final MemoryKey.Valued<Integer> VISIBLE_ADULT_HOGLIN_COUNT = valued("visible_adult_hoglin_count");
    public static final MemoryKey.Valued<Player> NEAREST_PLAYER_HOLDING_WANTED_ITEM = valued("nearest_player_holding_wanted_item");
    public static final MemoryKey.Valued<Boolean> ATE_RECENTLY = valued("ate_recently");
    public static final MemoryKey.Valued<BlockPosition> NEAREST_REPELLENT = valued("nearest_repellent");
    public static final MemoryKey.Valued<Boolean> PACIFIED = valued("pacified");
    public static final MemoryKey.Valued<LivingEntity> ROAR_TARGET = valued("roar_target");
    public static final MemoryKey.Valued<BlockPosition> DISTURBANCE_LOCATION = valued("disturbance_location");
    public static final MemoryKey.NonValued RECENT_PROJECTILE = unvalued("recent_projectile");
    public static final MemoryKey.NonValued IS_SNIFFING = unvalued("is_sniffing");
    public static final MemoryKey.NonValued IS_EMERGING = unvalued("is_emerging");
    public static final MemoryKey.NonValued ROAR_SOUND_DELAY = unvalued("roar_sound_delay");
    public static final MemoryKey.NonValued DIG_COOLDOWN = unvalued("dig_cooldown");
    public static final MemoryKey.NonValued ROAR_SOUND_COOLDOWN = unvalued("roar_sound_cooldown");
    public static final MemoryKey.NonValued SNIFF_COOLDOWN = unvalued("sniff_cooldown");
    public static final MemoryKey.NonValued TOUCH_COOLDOWN = unvalued("touch_cooldown");
    public static final MemoryKey.NonValued VIBRATION_COOLDOWN = unvalued("vibration_cooldown");
    public static final MemoryKey.NonValued SONIC_BOOM_COOLDOWN = unvalued("sonic_boom_cooldown");
    public static final MemoryKey.NonValued SONIC_BOOM_SOUND_COOLDOWN = unvalued("sonic_boom_sound_cooldown");
    public static final MemoryKey.NonValued SONIC_BOOM_SOUND_DELAY = unvalued("sonic_boom_sound_delay");
    public static final MemoryKey.Valued<UUID> LIKED_PLAYER = valued("liked_player");
    public static final MemoryKey.Valued<Location> LIKED_NOTEBLOCK_POSITION = valued("liked_noteblock");
    public static final MemoryKey.Valued<Integer> LIKED_NOTEBLOCK_COOLDOWN_TICKS = valued("liked_noteblock_cooldown_ticks");
    public static final MemoryKey.Valued<Integer> ITEM_PICKUP_COOLDOWN_TICKS = valued("item_pickup_cooldown_ticks");
    public static final MemoryKey.Valued<List<Location>> SNIFFER_EXPLORED_POSITIONS = valued("sniffer_explored_positions");
    public static final MemoryKey.Valued<BlockPosition> SNIFFER_SNIFFING_TARGET = valued("sniffer_sniffing_target");
    public static final MemoryKey.Valued<Boolean> SNIFFER_DIGGING = valued("sniffer_digging");
    public static final MemoryKey.Valued<Boolean> SNIFFER_HAPPY = valued("sniffer_happy");
    public static final MemoryKey.NonValued BREEZE_JUMP_COOLDOWN = unvalued("breeze_jump_cooldown");
    public static final MemoryKey.NonValued BREEZE_SHOOT = unvalued("breeze_shoot");
    public static final MemoryKey.NonValued BREEZE_SHOOT_CHARGING = unvalued("breeze_shoot_charging");
    public static final MemoryKey.NonValued BREEZE_SHOOT_RECOVERING = unvalued("breeze_shoot_recover");
    public static final MemoryKey.NonValued BREEZE_SHOOT_COOLDOWN = unvalued("breeze_shoot_cooldown");
    public static final MemoryKey.NonValued BREEZE_JUMP_INHALING = unvalued("breeze_jump_inhaling");
    public static final MemoryKey.Valued<BlockPosition> BREEZE_JUMP_TARGET = valued("breeze_jump_target");
    public static final MemoryKey.NonValued BREEZE_LEAVING_WATER = unvalued("breeze_leaving_water");

    private static MemoryKey.NonValued unvalued(final @KeyPattern.Value String name) {
        final MemoryKey memoryKey = Registry.MEMORY_KEY.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (memoryKey instanceof MemoryKey.NonValued) {
            return (MemoryKey.NonValued) memoryKey;
        }
        throw new IllegalStateException(name + " is not a valid unvalued type, it is a " + memoryKey.getClass().getTypeName());
    }

    @SuppressWarnings("unchecked")
    private static <T> MemoryKey.Valued<T> valued(final @KeyPattern.Value String name) {
        final MemoryKey memoryKey = Registry.MEMORY_KEY.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (memoryKey instanceof MemoryKey.Valued) {
            return (MemoryKey.Valued<T>) memoryKey;
        }
        throw new IllegalStateException(name + " is not a valid valued type, it is a " + memoryKey.getClass().getTypeName());
    }

    private MemoryKeys() {
    }
}
