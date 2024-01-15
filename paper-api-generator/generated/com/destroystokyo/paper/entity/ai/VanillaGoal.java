package com.destroystokyo.paper.entity.ai;

import com.destroystokyo.paper.entity.RangedEntity;
import io.papermc.paper.entity.SchoolableFish;
import io.papermc.paper.generated.GeneratedFrom;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla keys for Mob Goals.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@GeneratedFrom("1.20.4")
public interface VanillaGoal<T extends Mob> extends Goal<T> {
    GoalKey<AbstractHorse> RANDOM_STAND = create("random_stand", AbstractHorse.class);

    GoalKey<AbstractHorse> RUN_AROUND_LIKE_CRAZY = create("run_around_like_crazy", AbstractHorse.class);

    GoalKey<AbstractSkeleton> ABSTRACT_SKELETON_MELEE = create("abstract_skeleton_melee", AbstractSkeleton.class);

    GoalKey<AbstractVillager> LOOK_AT_TRADING_PLAYER = create("look_at_trading_player", AbstractVillager.class);

    GoalKey<AbstractVillager> TRADE_WITH_PLAYER = create("trade_with_player", AbstractVillager.class);

    GoalKey<Animals> BREED = create("breed", Animals.class);

    GoalKey<Animals> FOLLOW_PARENT = create("follow_parent", Animals.class);

    GoalKey<Bee> BEE_ATTACK = create("bee_attack", Bee.class);

    GoalKey<Bee> BEE_BECOME_ANGRY = create("bee_become_angry", Bee.class);

    GoalKey<Bee> BEE_ENTER_HIVE = create("bee_enter_hive", Bee.class);

    GoalKey<Bee> BEE_GO_TO_HIVE = create("bee_go_to_hive", Bee.class);

    GoalKey<Bee> BEE_GO_TO_KNOWN_FLOWER = create("bee_go_to_known_flower", Bee.class);

    GoalKey<Bee> BEE_GROW_CROP = create("bee_grow_crop", Bee.class);

    GoalKey<Bee> BEE_HURT_BY_OTHER = create("bee_hurt_by_other", Bee.class);

    GoalKey<Bee> BEE_LOCATE_HIVE = create("bee_locate_hive", Bee.class);

    GoalKey<Bee> BEE_POLLINATE = create("bee_pollinate", Bee.class);

    GoalKey<Bee> BEE_WANDER = create("bee_wander", Bee.class);

    GoalKey<Blaze> BLAZE_ATTACK = create("blaze_attack", Blaze.class);

    GoalKey<Cat> CAT_AVOID_ENTITY = create("cat_avoid_entity", Cat.class);

    GoalKey<Cat> CAT_LIE_ON_BED = create("cat_lie_on_bed", Cat.class);

    GoalKey<Cat> CAT_RELAX_ON_OWNER = create("cat_relax_on_owner", Cat.class);

    GoalKey<Cat> CAT_SIT_ON_BLOCK = create("cat_sit_on_block", Cat.class);

    GoalKey<Cat> CAT_TEMPT = create("cat_tempt", Cat.class);

    GoalKey<Creature> AVOID_ENTITY = create("avoid_entity", Creature.class);

    GoalKey<Creature> BREATH_AIR = create("breath_air", Creature.class);

    GoalKey<Creature> DROWNED_GO_TO_WATER = create("drowned_go_to_water", Creature.class);

    GoalKey<Creature> FLEE_SUN = create("flee_sun", Creature.class);

    GoalKey<Creature> FOLLOW_BOAT = create("follow_boat", Creature.class);

    GoalKey<Creature> GOLEM_RANDOM_STROLL_IN_VILLAGE = create("golem_random_stroll_in_village", Creature.class);

    GoalKey<Creature> HURT_BY = create("hurt_by", Creature.class);

    GoalKey<Creature> MELEE_ATTACK = create("melee_attack", Creature.class);

    GoalKey<Creature> MOVE_BACK_TO_VILLAGE = create("move_back_to_village", Creature.class);

    GoalKey<Creature> MOVE_THROUGH_VILLAGE = create("move_through_village", Creature.class);

    GoalKey<Creature> MOVE_TOWARDS = create("move_towards", Creature.class);

    GoalKey<Creature> MOVE_TOWARDS_RESTRICTION = create("move_towards_restriction", Creature.class);

    GoalKey<Creature> PANIC = create("panic", Creature.class);

    GoalKey<Creature> PARROT_WANDER = create("parrot_wander", Creature.class);

    GoalKey<Creature> RANDOM_STROLL = create("random_stroll", Creature.class);

    GoalKey<Creature> RANDOM_SWIMMING = create("random_swimming", Creature.class);

    GoalKey<Creature> REMOVE_BLOCK = create("remove_block", Creature.class);

    GoalKey<Creature> RESTRICT_SUN = create("restrict_sun", Creature.class);

    GoalKey<Creature> STROLL_THROUGH_VILLAGE = create("stroll_through_village", Creature.class);

    GoalKey<Creature> TEMPT = create("tempt", Creature.class);

    GoalKey<Creature> TRY_FIND_WATER = create("try_find_water", Creature.class);

    GoalKey<Creature> WATER_AVOIDING_RANDOM_FLYING = create("water_avoiding_random_flying", Creature.class);

    GoalKey<Creature> WATER_AVOIDING_RANDOM_STROLL = create("water_avoiding_random_stroll", Creature.class);

    GoalKey<Creeper> SWELL = create("swell", Creeper.class);

    GoalKey<Dolphin> DOLPHIN_JUMP = create("dolphin_jump", Dolphin.class);

    GoalKey<Dolphin> DOLPHIN_SWIM_TO_TREASURE = create("dolphin_swim_to_treasure", Dolphin.class);

    GoalKey<Dolphin> DOLPHIN_SWIM_WITH_PLAYER = create("dolphin_swim_with_player", Dolphin.class);

    GoalKey<Dolphin> PLAY_WITH_ITEMS = create("play_with_items", Dolphin.class);

    GoalKey<Drowned> DROWNED_ATTACK = create("drowned_attack", Drowned.class);

    GoalKey<Drowned> DROWNED_GO_TO_BEACH = create("drowned_go_to_beach", Drowned.class);

    GoalKey<Drowned> DROWNED_SWIM_UP = create("drowned_swim_up", Drowned.class);

    GoalKey<Enderman> ENDERMAN_FREEZE_WHEN_LOOKED_AT = create("enderman_freeze_when_looked_at", Enderman.class);

    GoalKey<Enderman> ENDERMAN_LEAVE_BLOCK = create("enderman_leave_block", Enderman.class);

    GoalKey<Enderman> ENDERMAN_LOOK_FOR_PLAYER = create("enderman_look_for_player", Enderman.class);

    GoalKey<Enderman> ENDERMAN_TAKE_BLOCK = create("enderman_take_block", Enderman.class);

    GoalKey<Evoker> EVOKER_ATTACK_SPELL = create("evoker_attack_spell", Evoker.class);

    GoalKey<Evoker> EVOKER_CASTING_SPELL = create("evoker_casting_spell", Evoker.class);

    GoalKey<Evoker> EVOKER_SUMMON_SPELL = create("evoker_summon_spell", Evoker.class);

    GoalKey<Evoker> EVOKER_WOLOLO_SPELL = create("evoker_wololo_spell", Evoker.class);

    GoalKey<Fish> FISH_SWIM = create("fish_swim", Fish.class);

    GoalKey<Fox> DEFEND_TRUSTED = create("defend_trusted", Fox.class);

    GoalKey<Fox> FACEPLANT = create("faceplant", Fox.class);

    GoalKey<Fox> FOX_BREED = create("fox_breed", Fox.class);

    GoalKey<Fox> FOX_EAT_BERRIES = create("fox_eat_berries", Fox.class);

    GoalKey<Fox> FOX_FLOAT = create("fox_float", Fox.class);

    GoalKey<Fox> FOX_FOLLOW_PARENT = create("fox_follow_parent", Fox.class);

    GoalKey<Fox> FOX_LOOK_AT_PLAYER = create("fox_look_at_player", Fox.class);

    GoalKey<Fox> FOX_MELEE_ATTACK = create("fox_melee_attack", Fox.class);

    GoalKey<Fox> FOX_PANIC = create("fox_panic", Fox.class);

    GoalKey<Fox> FOX_POUNCE = create("fox_pounce", Fox.class);

    GoalKey<Fox> FOX_SEARCH_FOR_ITEMS = create("fox_search_for_items", Fox.class);

    GoalKey<Fox> FOX_STROLL_THROUGH_VILLAGE = create("fox_stroll_through_village", Fox.class);

    GoalKey<Fox> PERCH_AND_SEARCH = create("perch_and_search", Fox.class);

    GoalKey<Fox> SEEK_SHELTER = create("seek_shelter", Fox.class);

    GoalKey<Fox> SLEEP = create("sleep", Fox.class);

    GoalKey<Fox> STALK_PREY = create("stalk_prey", Fox.class);

    GoalKey<Ghast> GHAST_LOOK = create("ghast_look", Ghast.class);

    GoalKey<Ghast> GHAST_SHOOT_FIREBALL = create("ghast_shoot_fireball", Ghast.class);

    GoalKey<Ghast> RANDOM_FLOAT_AROUND = create("random_float_around", Ghast.class);

    GoalKey<Guardian> GUARDIAN_ATTACK = create("guardian_attack", Guardian.class);

    GoalKey<Illager> RAIDER_OPEN_DOOR = create("raider_open_door", Illager.class);

    GoalKey<Illusioner> ILLUSIONER_BLINDNESS_SPELL = create("illusioner_blindness_spell", Illusioner.class);

    GoalKey<Illusioner> ILLUSIONER_MIRROR_SPELL = create("illusioner_mirror_spell", Illusioner.class);

    GoalKey<IronGolem> DEFEND_VILLAGE = create("defend_village", IronGolem.class);

    GoalKey<IronGolem> OFFER_FLOWER = create("offer_flower", IronGolem.class);

    GoalKey<Llama> LLAMA_ATTACK_WOLF = create("llama_attack_wolf", Llama.class);

    GoalKey<Llama> LLAMA_FOLLOW_CARAVAN = create("llama_follow_caravan", Llama.class);

    GoalKey<Llama> LLAMA_HURT_BY = create("llama_hurt_by", Llama.class);

    GoalKey<Llama> TRADER_LLAMA_DEFEND_WANDERING_TRADER = create("trader_llama_defend_wandering_trader", Llama.class);

    GoalKey<Mob> BREAK_DOOR = create("break_door", Mob.class);

    GoalKey<Mob> CLIMB_ON_TOP_OF_POWDER_SNOW = create("climb_on_top_of_powder_snow", Mob.class);

    GoalKey<Mob> EAT_BLOCK = create("eat_block", Mob.class);

    GoalKey<Mob> FLOAT = create("float", Mob.class);

    GoalKey<Mob> FOLLOW_MOB = create("follow_mob", Mob.class);

    GoalKey<Mob> INTERACT = create("interact", Mob.class);

    GoalKey<Mob> LEAP_AT = create("leap_at", Mob.class);

    GoalKey<Mob> LOOK_AT_PLAYER = create("look_at_player", Mob.class);

    GoalKey<Mob> NEAREST_ATTACKABLE = create("nearest_attackable", Mob.class);

    GoalKey<Mob> OCELOT_ATTACK = create("ocelot_attack", Mob.class);

    GoalKey<Mob> OPEN_DOOR = create("open_door", Mob.class);

    GoalKey<Mob> RANDOM_LOOK_AROUND = create("random_look_around", Mob.class);

    GoalKey<Mob> RESET_UNIVERSAL_ANGER = create("reset_universal_anger", Mob.class);

    GoalKey<Mob> USE_ITEM = create("use_item", Mob.class);

    GoalKey<Mob> VINDICATOR_BREAK_DOOR = create("vindicator_break_door", Mob.class);

    GoalKey<Monster> RANGED_BOW_ATTACK = create("ranged_bow_attack", Monster.class);

    GoalKey<Monster> RANGED_CROSSBOW_ATTACK = create("ranged_crossbow_attack", Monster.class);

    GoalKey<Ocelot> OCELOT_AVOID_ENTITY = create("ocelot_avoid_entity", Ocelot.class);

    GoalKey<Ocelot> OCELOT_TEMPT = create("ocelot_tempt", Ocelot.class);

    GoalKey<Panda> PANDA_ATTACK = create("panda_attack", Panda.class);

    GoalKey<Panda> PANDA_AVOID = create("panda_avoid", Panda.class);

    GoalKey<Panda> PANDA_BREED = create("panda_breed", Panda.class);

    GoalKey<Panda> PANDA_HURT_BY = create("panda_hurt_by", Panda.class);

    GoalKey<Panda> PANDA_LIE_ON_BACK = create("panda_lie_on_back", Panda.class);

    GoalKey<Panda> PANDA_LOOK_AT_PLAYER = create("panda_look_at_player", Panda.class);

    GoalKey<Panda> PANDA_PANIC = create("panda_panic", Panda.class);

    GoalKey<Panda> PANDA_ROLL = create("panda_roll", Panda.class);

    GoalKey<Panda> PANDA_SIT = create("panda_sit", Panda.class);

    GoalKey<Panda> PANDA_SNEEZE = create("panda_sneeze", Panda.class);

    GoalKey<Parrot> LAND_ON_OWNERS_SHOULDER = create("land_on_owners_shoulder", Parrot.class);

    GoalKey<Phantom> PHANTOM_ATTACK_PLAYER = create("phantom_attack_player", Phantom.class);

    GoalKey<Phantom> PHANTOM_ATTACK_STRATEGY = create("phantom_attack_strategy", Phantom.class);

    GoalKey<Phantom> PHANTOM_CIRCLE_AROUND_ANCHOR = create("phantom_circle_around_anchor", Phantom.class);

    GoalKey<Phantom> PHANTOM_SWEEP_ATTACK = create("phantom_sweep_attack", Phantom.class);

    GoalKey<PolarBear> POLAR_BEAR_ATTACK_PLAYERS = create("polar_bear_attack_players", PolarBear.class);

    GoalKey<PolarBear> POLAR_BEAR_HURT_BY = create("polar_bear_hurt_by", PolarBear.class);

    GoalKey<PolarBear> POLAR_BEAR_MELEE_ATTACK = create("polar_bear_melee_attack", PolarBear.class);

    GoalKey<PolarBear> POLAR_BEAR_PANIC = create("polar_bear_panic", PolarBear.class);

    GoalKey<PufferFish> PUFFERFISH_PUFF = create("pufferfish_puff", PufferFish.class);

    GoalKey<Rabbit> RABBIT_AVOID_ENTITY = create("rabbit_avoid_entity", Rabbit.class);

    GoalKey<Rabbit> RABBIT_PANIC = create("rabbit_panic", Rabbit.class);

    GoalKey<Rabbit> RAID_GARDEN = create("raid_garden", Rabbit.class);

    GoalKey<Raider> HOLD_GROUND_ATTACK = create("hold_ground_attack", Raider.class);

    GoalKey<Raider> LONG_DISTANCE_PATROL = create("long_distance_patrol", Raider.class);

    GoalKey<Raider> NEAREST_ATTACKABLE_WITCH = create("nearest_attackable_witch", Raider.class);

    GoalKey<Raider> NEAREST_HEALABLE_RAIDER = create("nearest_healable_raider", Raider.class);

    GoalKey<Raider> OBTAIN_RAID_LEADER_BANNER = create("obtain_raid_leader_banner", Raider.class);

    GoalKey<Raider> PATHFIND_TO_RAID = create("pathfind_to_raid", Raider.class);

    GoalKey<Raider> RAIDER_CELEBRATION = create("raider_celebration", Raider.class);

    GoalKey<Raider> RAIDER_MOVE_THROUGH_VILLAGE = create("raider_move_through_village", Raider.class);

    GoalKey<RangedEntity> DROWNED_TRIDENT_ATTACK = create("drowned_trident_attack", RangedEntity.class);

    GoalKey<RangedEntity> RANGED_ATTACK = create("ranged_attack", RangedEntity.class);

    GoalKey<SchoolableFish> FOLLOW_FLOCK_LEADER = create("follow_flock_leader", SchoolableFish.class);

    GoalKey<Shulker> SHULKER_ATTACK = create("shulker_attack", Shulker.class);

    GoalKey<Shulker> SHULKER_DEFENSE_ATTACK = create("shulker_defense_attack", Shulker.class);

    GoalKey<Shulker> SHULKER_NEAREST_ATTACK = create("shulker_nearest_attack", Shulker.class);

    GoalKey<Shulker> SHULKER_PEEK = create("shulker_peek", Shulker.class);

    GoalKey<Silverfish> SILVERFISH_MERGE_WITH_STONE = create("silverfish_merge_with_stone", Silverfish.class);

    GoalKey<Silverfish> SILVERFISH_WAKE_UP_FRIENDS = create("silverfish_wake_up_friends", Silverfish.class);

    GoalKey<SkeletonHorse> SKELETON_TRAP = create("skeleton_trap", SkeletonHorse.class);

    GoalKey<Slime> SLIME_ATTACK = create("slime_attack", Slime.class);

    GoalKey<Slime> SLIME_FLOAT = create("slime_float", Slime.class);

    GoalKey<Slime> SLIME_KEEP_ON_JUMPING = create("slime_keep_on_jumping", Slime.class);

    GoalKey<Slime> SLIME_RANDOM_DIRECTION = create("slime_random_direction", Slime.class);

    GoalKey<Spellcaster> SPELLCASTER_CASTING_SPELL = create("spellcaster_casting_spell", Spellcaster.class);

    GoalKey<Spider> SPIDER = create("spider", Spider.class);

    GoalKey<Spider> SPIDER_ATTACK = create("spider_attack", Spider.class);

    GoalKey<Squid> SQUID_FLEE = create("squid_flee", Squid.class);

    GoalKey<Squid> SQUID_RANDOM_MOVEMENT = create("squid_random_movement", Squid.class);

    GoalKey<Strider> STRIDER_GO_TO_LAVA = create("strider_go_to_lava", Strider.class);

    GoalKey<Tameable> FOLLOW_OWNER = create("follow_owner", Tameable.class);

    GoalKey<Tameable> NON_TAME_RANDOM = create("non_tame_random", Tameable.class);

    GoalKey<Tameable> OWNER_HURT = create("owner_hurt", Tameable.class);

    GoalKey<Tameable> OWNER_HURT_BY = create("owner_hurt_by", Tameable.class);

    GoalKey<Tameable> SIT_WHEN_ORDERED_TO = create("sit_when_ordered_to", Tameable.class);

    GoalKey<Turtle> TURTLE_BREED = create("turtle_breed", Turtle.class);

    GoalKey<Turtle> TURTLE_GO_HOME = create("turtle_go_home", Turtle.class);

    GoalKey<Turtle> TURTLE_GO_TO_WATER = create("turtle_go_to_water", Turtle.class);

    GoalKey<Turtle> TURTLE_LAY_EGG = create("turtle_lay_egg", Turtle.class);

    GoalKey<Turtle> TURTLE_PANIC = create("turtle_panic", Turtle.class);

    GoalKey<Turtle> TURTLE_RANDOM_STROLL = create("turtle_random_stroll", Turtle.class);

    GoalKey<Turtle> TURTLE_TRAVEL = create("turtle_travel", Turtle.class);

    GoalKey<Vex> VEX_CHARGE_ATTACK = create("vex_charge_attack", Vex.class);

    GoalKey<Vex> VEX_COPY_OWNER = create("vex_copy_owner", Vex.class);

    GoalKey<Vex> VEX_RANDOM_MOVE = create("vex_random_move", Vex.class);

    GoalKey<Vindicator> VINDICATOR_JOHNNY_ATTACK = create("vindicator_johnny_attack", Vindicator.class);

    GoalKey<WanderingTrader> WANDER_TO_POSITION = create("wander_to_position", WanderingTrader.class);

    GoalKey<Wither> WITHER_DO_NOTHING = create("wither_do_nothing", Wither.class);

    GoalKey<Wolf> BEG = create("beg", Wolf.class);

    GoalKey<Wolf> WOLF_AVOID_ENTITY = create("wolf_avoid_entity", Wolf.class);

    GoalKey<Wolf> WOLF_PANIC = create("wolf_panic", Wolf.class);

    GoalKey<Zombie> ZOMBIE_ATTACK = create("zombie_attack", Zombie.class);

    GoalKey<Zombie> ZOMBIE_ATTACK_TURTLE_EGG = create("zombie_attack_turtle_egg", Zombie.class);

    /**
     * Removed in 1.20.2
     */
    @Deprecated(
            since = "1.20.2"
    )
    GoalKey<Vindicator> VINDICATOR_MELEE_ATTACK = create("vindicator_melee_attack", Vindicator.class);

    /**
     * Removed in 1.20.2
     */
    @Deprecated(
            since = "1.20.2"
    )
    GoalKey<Ravager> RAVAGER_MELEE_ATTACK = create("ravager_melee_attack", Ravager.class);

    /**
     * Removed in 1.20.2
     */
    @Deprecated(
            since = "1.20.2"
    )
    GoalKey<Rabbit> EVIL_RABBIT_ATTACK = create("evil_rabbit_attack", Rabbit.class);

    /**
     * Removed in 1.16
     */
    @Deprecated(
            forRemoval = true,
            since = "1.16"
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PigZombie> ANGER = create("anger", PigZombie.class);

    /**
     * Removed in 1.16
     */
    @Deprecated(
            forRemoval = true,
            since = "1.16"
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PigZombie> ANGER_OTHER = create("anger_other", PigZombie.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Blaze> BLAZE_FIREBALL = create("blaze_fireball", Blaze.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Cat> TEMPT_CHANCE = create("tempt_chance", Cat.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Dolphin> DOLPHIN_PLAY_WITH_ITEMS = create("dolphin_play_with_items", Dolphin.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Drowned> DROWNED_GOTO_BEACH = create("drowned_goto_beach", Drowned.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> DROWNED_GOTO_WATER = create("drowned_goto_water", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Enderman> ENDERMAN_PICKUP_BLOCK = create("enderman_pickup_block", Enderman.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Enderman> ENDERMAN_PLACE_BLOCK = create("enderman_place_block", Enderman.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Enderman> PLAYER_WHO_LOOKED_AT_TARGET = create("player_who_looked_at_target", Enderman.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Evoker> EVOKER_CAST_SPELL = create("evoker_cast_spell", Evoker.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_DEFEND_TRUSTED = create("fox_defend_trusted", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_FACEPLANT = create("fox_faceplant", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_PERCH_AND_SEARCH = create("fox_perch_and_search", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_SLEEP = create("fox_sleep", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_SEEK_SHELTER = create("fox_seek_shelter", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fox> FOX_STALK_PREY = create("fox_stalk_prey", Fox.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Ghast> GHAST_ATTACK_TARGET = create("ghast_attack_target", Ghast.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Ghast> GHAST_IDLE_MOVE = create("ghast_idle_move", Ghast.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Ghast> GHAST_MOVE_TOWARDS_TARGET = create("ghast_move_towards_target", Ghast.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Spellcaster> SPELLCASTER_CAST_SPELL = create("spellcaster_cast_spell", Spellcaster.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<TraderLlama> LLAMATRADER_DEFENDED_WANDERING_TRADER = create("llamatrader_defended_wandering_trader", TraderLlama.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Panda> PANDA_HURT_BY_TARGET = create("panda_hurt_by_target", Panda.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PolarBear> POLARBEAR_ATTACK_PLAYERS = create("polarbear_attack_players", PolarBear.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PolarBear> POLARBEAR_HURT_BY = create("polarbear_hurt_by", PolarBear.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PolarBear> POLARBEAR_MELEE = create("polarbear_melee", PolarBear.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<PolarBear> POLARBEAR_PANIC = create("polarbear_panic", PolarBear.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Rabbit> EAT_CARROTS = create("eat_carrots", Rabbit.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Rabbit> KILLER_RABBIT_MELEE_ATTACK = create("killer_rabbit_melee_attack", Rabbit.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Rabbit> RABBIT_AVOID_TARGET = create("rabbit_avoid_target", Rabbit.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Raider> RAIDER_HOLD_GROUND = create("raider_hold_ground", Raider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Raider> RAIDER_OBTAIN_BANNER = create("raider_obtain_banner", Raider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Shulker> SHULKER_DEFENSE = create("shulker_defense", Shulker.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Shulker> SHULKER_NEAREST = create("shulker_nearest", Shulker.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Silverfish> SILVERFISH_HIDE_IN_BLOCK = create("silverfish_hide_in_block", Silverfish.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Silverfish> SILVERFISH_WAKE_OTHERS = create("silverfish_wake_others", Silverfish.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Slime> SLIME_IDLE = create("slime_idle", Slime.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Slime> SLIME_NEAREST_PLAYER = create("slime_nearest_player", Slime.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Slime> SLIME_RANDOM_JUMP = create("slime_random_jump", Slime.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Spider> SPIDER_MELEE_ATTACK = create("spider_melee_attack", Spider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Spider> SPIDER_NEAREST_ATTACKABLE_TARGET = create("spider_nearest_attackable_target", Spider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Squid> SQUID = create("squid", Squid.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Turtle> TURTLE_GOTO_WATER = create("turtle_goto_water", Turtle.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Turtle> TURTLE_TEMPT = create("turtle_tempt", Turtle.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Vex> VEX_COPY_TARGET_OF_OWNER = create("vex_copy_target_of_owner", Vex.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<WanderingTrader> VILLAGERTRADER_WANDER_TO_POSITION = create("villagertrader_wander_to_position", WanderingTrader.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<RangedEntity> ARROW_ATTACK = create("arrow_attack", RangedEntity.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> AVOID_TARGET = create("avoid_target", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Monster> BOW_SHOOT = create("bow_shoot", Monster.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> BREATH = create("breath", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Cat> CAT_SIT_ON_BED = create("cat_sit_on_bed", Cat.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Monster> CROSSBOW_ATTACK = create("crossbow_attack", Monster.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> DOOR_OPEN = create("door_open", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> EAT_TILE = create("eat_tile", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Fish> FISH_SCHOOL = create("fish_school", Fish.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> FOLLOW_ENTITY = create("follow_entity", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<SkeletonHorse> HORSE_TRAP = create("horse_trap", SkeletonHorse.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> HURT_BY_TARGET = create("hurt_by_target", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Cat> JUMP_ON_BLOCK = create("jump_on_block", Cat.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> LEAP_AT_TARGET = create("leap_at_target", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Llama> LLAMA_FOLLOW = create("llama_follow", Llama.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> MOVE_TOWARDS_TARGET = create("move_towards_target", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> NEAREST_ATTACKABLE_TARGET = create("nearest_attackable_target", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Raider> NEAREST_ATTACKABLE_TARGET_WITCH = create("nearest_attackable_target_witch", Raider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> NEAREST_VILLAGE = create("nearest_village", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Tameable> OWNER_HURT_BY_TARGET = create("owner_hurt_by_target", Tameable.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Tameable> OWNER_HURT_TARGET = create("owner_hurt_target", Tameable.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Parrot> PERCH = create("perch", Parrot.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Raider> RAID = create("raid", Raider.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> RANDOM_FLY = create("random_fly", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> RANDOM_LOOKAROUND = create("random_lookaround", Mob.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> RANDOM_STROLL_LAND = create("random_stroll_land", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> RANDOM_SWIM = create("random_swim", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Tameable> RANDOM_TARGET_NON_TAMED = create("random_target_non_tamed", Tameable.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Tameable> SIT = create("sit", Tameable.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> STROLL_VILLAGE = create("stroll_village", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<AbstractHorse> TAME = create("tame", AbstractHorse.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> WATER = create("water", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Dolphin> WATER_JUMP = create("water_jump", Dolphin.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Creature> STROLL_VILLAGE_GOLEM = create("stroll_village_golem", Creature.class);

    @Deprecated(
            forRemoval = true
    )
    @ApiStatus.ScheduledForRemoval(
            inVersion = "1.21"
    )
    GoalKey<Mob> UNIVERSAL_ANGER_RESET = create("universal_anger_reset", Mob.class);

    private static @NotNull GoalKey create(final @NotNull String key, final @NotNull Class clazz) {
        return GoalKey.of(clazz, NamespacedKey.minecraft(key));
    }
}
