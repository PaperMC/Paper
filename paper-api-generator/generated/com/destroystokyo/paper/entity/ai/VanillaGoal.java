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
    /**
     * net.minecraft.world.entity.ai.goal.RangedAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<RangedEntity> RANGED_ATTACK = of("ranged_attack", RangedEntity.class);

    /**
     * net.minecraft.world.entity.monster.Drowned$DrownedTridentAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<RangedEntity> DROWNED_TRIDENT_ATTACK = of("drowned_trident_attack", RangedEntity.class);

    /**
     * net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<SchoolableFish> FOLLOW_FLOCK_LEADER = of("follow_flock_leader", SchoolableFish.class);

    /**
     * net.minecraft.world.entity.ai.goal.RandomStandGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<AbstractHorse> RANDOM_STAND = of("random_stand", AbstractHorse.class);

    /**
     * net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<AbstractHorse> RUN_AROUND_LIKE_CRAZY = of("run_around_like_crazy", AbstractHorse.class);

    /**
     * net.minecraft.world.entity.monster.AbstractSkeleton$1
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<AbstractSkeleton> ABSTRACT_SKELETON_MELEE = of("abstract_skeleton_melee", AbstractSkeleton.class);

    /**
     * net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<AbstractVillager> LOOK_AT_TRADING_PLAYER = of("look_at_trading_player", AbstractVillager.class);

    /**
     * net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<AbstractVillager> TRADE_WITH_PLAYER = of("trade_with_player", AbstractVillager.class);

    /**
     * net.minecraft.world.entity.ai.goal.BreedGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Animals> BREED = of("breed", Animals.class);

    /**
     * net.minecraft.world.entity.ai.goal.FollowParentGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Animals> FOLLOW_PARENT = of("follow_parent", Animals.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_ATTACK = of("bee_attack", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeBecomeAngryTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_BECOME_ANGRY = of("bee_become_angry", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeEnterHiveGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_ENTER_HIVE = of("bee_enter_hive", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeGoToHiveGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_GO_TO_HIVE = of("bee_go_to_hive", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeGoToKnownFlowerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_GO_TO_KNOWN_FLOWER = of("bee_go_to_known_flower", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_GROW_CROP = of("bee_grow_crop", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeHurtByOtherGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_HURT_BY_OTHER = of("bee_hurt_by_other", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeLocateHiveGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_LOCATE_HIVE = of("bee_locate_hive", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeePollinateGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_POLLINATE = of("bee_pollinate", Bee.class);

    /**
     * net.minecraft.world.entity.animal.Bee$BeeWanderGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Bee> BEE_WANDER = of("bee_wander", Bee.class);

    /**
     * net.minecraft.world.entity.monster.Blaze$BlazeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Blaze> BLAZE_ATTACK = of("blaze_attack", Blaze.class);

    /**
     * net.minecraft.world.entity.ai.goal.CatLieOnBedGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Cat> CAT_LIE_ON_BED = of("cat_lie_on_bed", Cat.class);

    /**
     * net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Cat> CAT_SIT_ON_BLOCK = of("cat_sit_on_block", Cat.class);

    /**
     * net.minecraft.world.entity.animal.Cat$CatAvoidEntityGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Cat> CAT_AVOID_ENTITY = of("cat_avoid_entity", Cat.class);

    /**
     * net.minecraft.world.entity.animal.Cat$CatRelaxOnOwnerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Cat> CAT_RELAX_ON_OWNER = of("cat_relax_on_owner", Cat.class);

    /**
     * net.minecraft.world.entity.animal.Cat$CatTemptGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Cat> CAT_TEMPT = of("cat_tempt", Cat.class);

    /**
     * net.minecraft.world.entity.ai.goal.AvoidEntityGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> AVOID_ENTITY = of("avoid_entity", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.BreathAirGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> BREATH_AIR = of("breath_air", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.FleeSunGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> FLEE_SUN = of("flee_sun", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.FollowBoatGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> FOLLOW_BOAT = of("follow_boat", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> GOLEM_RANDOM_STROLL_IN_VILLAGE = of("golem_random_stroll_in_village", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.MeleeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> MELEE_ATTACK = of("melee_attack", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> MOVE_BACK_TO_VILLAGE = of("move_back_to_village", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> MOVE_THROUGH_VILLAGE = of("move_through_village", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> MOVE_TOWARDS_RESTRICTION = of("move_towards_restriction", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> MOVE_TOWARDS = of("move_towards", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.PanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> PANIC = of("panic", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.RandomStrollGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> RANDOM_STROLL = of("random_stroll", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> RANDOM_SWIMMING = of("random_swimming", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.RemoveBlockGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> REMOVE_BLOCK = of("remove_block", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.RestrictSunGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> RESTRICT_SUN = of("restrict_sun", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> STROLL_THROUGH_VILLAGE = of("stroll_through_village", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.TemptGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> TEMPT = of("tempt", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.TryFindWaterGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> TRY_FIND_WATER = of("try_find_water", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> WATER_AVOIDING_RANDOM_FLYING = of("water_avoiding_random_flying", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> WATER_AVOIDING_RANDOM_STROLL = of("water_avoiding_random_stroll", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> HURT_BY = of("hurt_by", Creature.class);

    /**
     * net.minecraft.world.entity.animal.Parrot$ParrotWanderGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> PARROT_WANDER = of("parrot_wander", Creature.class);

    /**
     * net.minecraft.world.entity.monster.Drowned$DrownedGoToWaterGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creature> DROWNED_GO_TO_WATER = of("drowned_go_to_water", Creature.class);

    /**
     * net.minecraft.world.entity.ai.goal.SwellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Creeper> SWELL = of("swell", Creeper.class);

    /**
     * net.minecraft.world.entity.ai.goal.DolphinJumpGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Dolphin> DOLPHIN_JUMP = of("dolphin_jump", Dolphin.class);

    /**
     * net.minecraft.world.entity.animal.Dolphin$DolphinSwimToTreasureGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Dolphin> DOLPHIN_SWIM_TO_TREASURE = of("dolphin_swim_to_treasure", Dolphin.class);

    /**
     * net.minecraft.world.entity.animal.Dolphin$DolphinSwimWithPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Dolphin> DOLPHIN_SWIM_WITH_PLAYER = of("dolphin_swim_with_player", Dolphin.class);

    /**
     * net.minecraft.world.entity.animal.Dolphin$PlayWithItemsGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Dolphin> PLAY_WITH_ITEMS = of("play_with_items", Dolphin.class);

    /**
     * net.minecraft.world.entity.monster.Drowned$DrownedAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Drowned> DROWNED_ATTACK = of("drowned_attack", Drowned.class);

    /**
     * net.minecraft.world.entity.monster.Drowned$DrownedGoToBeachGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Drowned> DROWNED_GO_TO_BEACH = of("drowned_go_to_beach", Drowned.class);

    /**
     * net.minecraft.world.entity.monster.Drowned$DrownedSwimUpGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Drowned> DROWNED_SWIM_UP = of("drowned_swim_up", Drowned.class);

    /**
     * net.minecraft.world.entity.monster.EnderMan$EndermanFreezeWhenLookedAt
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Enderman> ENDERMAN_FREEZE_WHEN_LOOKED_AT = of("enderman_freeze_when_looked_at", Enderman.class);

    /**
     * net.minecraft.world.entity.monster.EnderMan$EndermanLeaveBlockGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Enderman> ENDERMAN_LEAVE_BLOCK = of("enderman_leave_block", Enderman.class);

    /**
     * net.minecraft.world.entity.monster.EnderMan$EndermanLookForPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Enderman> ENDERMAN_LOOK_FOR_PLAYER = of("enderman_look_for_player", Enderman.class);

    /**
     * net.minecraft.world.entity.monster.EnderMan$EndermanTakeBlockGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Enderman> ENDERMAN_TAKE_BLOCK = of("enderman_take_block", Enderman.class);

    /**
     * net.minecraft.world.entity.monster.Evoker$EvokerAttackSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Evoker> EVOKER_ATTACK_SPELL = of("evoker_attack_spell", Evoker.class);

    /**
     * net.minecraft.world.entity.monster.Evoker$EvokerCastingSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Evoker> EVOKER_CASTING_SPELL = of("evoker_casting_spell", Evoker.class);

    /**
     * net.minecraft.world.entity.monster.Evoker$EvokerSummonSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Evoker> EVOKER_SUMMON_SPELL = of("evoker_summon_spell", Evoker.class);

    /**
     * net.minecraft.world.entity.monster.Evoker$EvokerWololoSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Evoker> EVOKER_WOLOLO_SPELL = of("evoker_wololo_spell", Evoker.class);

    /**
     * net.minecraft.world.entity.animal.AbstractFish$FishSwimGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fish> FISH_SWIM = of("fish_swim", Fish.class);

    /**
     * net.minecraft.world.entity.animal.Fox$DefendTrustedTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> DEFEND_TRUSTED = of("defend_trusted", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FaceplantGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FACEPLANT = of("faceplant", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxBreedGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_BREED = of("fox_breed", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxEatBerriesGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_EAT_BERRIES = of("fox_eat_berries", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxFloatGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_FLOAT = of("fox_float", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxFollowParentGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_FOLLOW_PARENT = of("fox_follow_parent", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxLookAtPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_LOOK_AT_PLAYER = of("fox_look_at_player", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxMeleeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_MELEE_ATTACK = of("fox_melee_attack", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxPanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_PANIC = of("fox_panic", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxPounceGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_POUNCE = of("fox_pounce", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxSearchForItemsGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_SEARCH_FOR_ITEMS = of("fox_search_for_items", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$FoxStrollThroughVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> FOX_STROLL_THROUGH_VILLAGE = of("fox_stroll_through_village", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$PerchAndSearchGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> PERCH_AND_SEARCH = of("perch_and_search", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$SeekShelterGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> SEEK_SHELTER = of("seek_shelter", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$SleepGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> SLEEP = of("sleep", Fox.class);

    /**
     * net.minecraft.world.entity.animal.Fox$StalkPreyGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Fox> STALK_PREY = of("stalk_prey", Fox.class);

    /**
     * net.minecraft.world.entity.monster.Ghast$GhastLookGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Ghast> GHAST_LOOK = of("ghast_look", Ghast.class);

    /**
     * net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Ghast> GHAST_SHOOT_FIREBALL = of("ghast_shoot_fireball", Ghast.class);

    /**
     * net.minecraft.world.entity.monster.Ghast$RandomFloatAroundGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Ghast> RANDOM_FLOAT_AROUND = of("random_float_around", Ghast.class);

    /**
     * net.minecraft.world.entity.monster.Guardian$GuardianAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Guardian> GUARDIAN_ATTACK = of("guardian_attack", Guardian.class);

    /**
     * net.minecraft.world.entity.monster.AbstractIllager$RaiderOpenDoorGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Illager> RAIDER_OPEN_DOOR = of("raider_open_door", Illager.class);

    /**
     * net.minecraft.world.entity.monster.Illusioner$IllusionerBlindnessSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Illusioner> ILLUSIONER_BLINDNESS_SPELL = of("illusioner_blindness_spell", Illusioner.class);

    /**
     * net.minecraft.world.entity.monster.Illusioner$IllusionerMirrorSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Illusioner> ILLUSIONER_MIRROR_SPELL = of("illusioner_mirror_spell", Illusioner.class);

    /**
     * net.minecraft.world.entity.ai.goal.OfferFlowerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<IronGolem> OFFER_FLOWER = of("offer_flower", IronGolem.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<IronGolem> DEFEND_VILLAGE = of("defend_village", IronGolem.class);

    /**
     * net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Llama> LLAMA_FOLLOW_CARAVAN = of("llama_follow_caravan", Llama.class);

    /**
     * net.minecraft.world.entity.animal.horse.Llama$LlamaAttackWolfGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Llama> LLAMA_ATTACK_WOLF = of("llama_attack_wolf", Llama.class);

    /**
     * net.minecraft.world.entity.animal.horse.Llama$LlamaHurtByTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Llama> LLAMA_HURT_BY = of("llama_hurt_by", Llama.class);

    /**
     * net.minecraft.world.entity.animal.horse.TraderLlama$TraderLlamaDefendWanderingTraderGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Llama> TRADER_LLAMA_DEFEND_WANDERING_TRADER = of("trader_llama_defend_wandering_trader", Llama.class);

    /**
     * net.minecraft.world.entity.ai.goal.BreakDoorGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> BREAK_DOOR = of("break_door", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> CLIMB_ON_TOP_OF_POWDER_SNOW = of("climb_on_top_of_powder_snow", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.EatBlockGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> EAT_BLOCK = of("eat_block", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.FloatGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> FLOAT = of("float", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.FollowMobGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> FOLLOW_MOB = of("follow_mob", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.InteractGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> INTERACT = of("interact", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.LeapAtTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> LEAP_AT = of("leap_at", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> LOOK_AT_PLAYER = of("look_at_player", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.OcelotAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> OCELOT_ATTACK = of("ocelot_attack", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.OpenDoorGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> OPEN_DOOR = of("open_door", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> RANDOM_LOOK_AROUND = of("random_look_around", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.UseItemGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> USE_ITEM = of("use_item", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> NEAREST_ATTACKABLE = of("nearest_attackable", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> RESET_UNIVERSAL_ANGER = of("reset_universal_anger", Mob.class);

    /**
     * net.minecraft.world.entity.monster.Vindicator$VindicatorBreakDoorGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Mob> VINDICATOR_BREAK_DOOR = of("vindicator_break_door", Mob.class);

    /**
     * net.minecraft.world.entity.ai.goal.RangedBowAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Monster> RANGED_BOW_ATTACK = of("ranged_bow_attack", Monster.class);

    /**
     * net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Monster> RANGED_CROSSBOW_ATTACK = of("ranged_crossbow_attack", Monster.class);

    /**
     * net.minecraft.world.entity.animal.Ocelot$OcelotAvoidEntityGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Ocelot> OCELOT_AVOID_ENTITY = of("ocelot_avoid_entity", Ocelot.class);

    /**
     * net.minecraft.world.entity.animal.Ocelot$OcelotTemptGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Ocelot> OCELOT_TEMPT = of("ocelot_tempt", Ocelot.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_ATTACK = of("panda_attack", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaAvoidGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_AVOID = of("panda_avoid", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaBreedGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_BREED = of("panda_breed", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaHurtByTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_HURT_BY = of("panda_hurt_by", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaLieOnBackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_LIE_ON_BACK = of("panda_lie_on_back", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaLookAtPlayerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_LOOK_AT_PLAYER = of("panda_look_at_player", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaPanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_PANIC = of("panda_panic", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaRollGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_ROLL = of("panda_roll", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaSitGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_SIT = of("panda_sit", Panda.class);

    /**
     * net.minecraft.world.entity.animal.Panda$PandaSneezeGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Panda> PANDA_SNEEZE = of("panda_sneeze", Panda.class);

    /**
     * net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Parrot> LAND_ON_OWNERS_SHOULDER = of("land_on_owners_shoulder", Parrot.class);

    /**
     * net.minecraft.world.entity.monster.Phantom$PhantomAttackPlayerTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Phantom> PHANTOM_ATTACK_PLAYER = of("phantom_attack_player", Phantom.class);

    /**
     * net.minecraft.world.entity.monster.Phantom$PhantomAttackStrategyGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Phantom> PHANTOM_ATTACK_STRATEGY = of("phantom_attack_strategy", Phantom.class);

    /**
     * net.minecraft.world.entity.monster.Phantom$PhantomCircleAroundAnchorGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Phantom> PHANTOM_CIRCLE_AROUND_ANCHOR = of("phantom_circle_around_anchor", Phantom.class);

    /**
     * net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Phantom> PHANTOM_SWEEP_ATTACK = of("phantom_sweep_attack", Phantom.class);

    /**
     * net.minecraft.world.entity.animal.PolarBear$PolarBearAttackPlayersGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<PolarBear> POLAR_BEAR_ATTACK_PLAYERS = of("polar_bear_attack_players", PolarBear.class);

    /**
     * net.minecraft.world.entity.animal.PolarBear$PolarBearHurtByTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<PolarBear> POLAR_BEAR_HURT_BY = of("polar_bear_hurt_by", PolarBear.class);

    /**
     * net.minecraft.world.entity.animal.PolarBear$PolarBearMeleeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<PolarBear> POLAR_BEAR_MELEE_ATTACK = of("polar_bear_melee_attack", PolarBear.class);

    /**
     * net.minecraft.world.entity.animal.PolarBear$PolarBearPanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<PolarBear> POLAR_BEAR_PANIC = of("polar_bear_panic", PolarBear.class);

    /**
     * net.minecraft.world.entity.animal.Pufferfish$PufferfishPuffGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<PufferFish> PUFFERFISH_PUFF = of("pufferfish_puff", PufferFish.class);

    /**
     * net.minecraft.world.entity.animal.Rabbit$RabbitAvoidEntityGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Rabbit> RABBIT_AVOID_ENTITY = of("rabbit_avoid_entity", Rabbit.class);

    /**
     * net.minecraft.world.entity.animal.Rabbit$RabbitPanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Rabbit> RABBIT_PANIC = of("rabbit_panic", Rabbit.class);

    /**
     * net.minecraft.world.entity.animal.Rabbit$RaidGardenGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Rabbit> RAID_GARDEN = of("raid_garden", Rabbit.class);

    /**
     * net.minecraft.world.entity.ai.goal.PathfindToRaidGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> PATHFIND_TO_RAID = of("pathfind_to_raid", Raider.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> NEAREST_ATTACKABLE_WITCH = of("nearest_attackable_witch", Raider.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> NEAREST_HEALABLE_RAIDER = of("nearest_healable_raider", Raider.class);

    /**
     * net.minecraft.world.entity.monster.PatrollingMonster$LongDistancePatrolGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> LONG_DISTANCE_PATROL = of("long_distance_patrol", Raider.class);

    /**
     * net.minecraft.world.entity.raid.Raider$HoldGroundAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> HOLD_GROUND_ATTACK = of("hold_ground_attack", Raider.class);

    /**
     * net.minecraft.world.entity.raid.Raider$ObtainRaidLeaderBannerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> OBTAIN_RAID_LEADER_BANNER = of("obtain_raid_leader_banner", Raider.class);

    /**
     * net.minecraft.world.entity.raid.Raider$RaiderCelebration
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> RAIDER_CELEBRATION = of("raider_celebration", Raider.class);

    /**
     * net.minecraft.world.entity.raid.Raider$RaiderMoveThroughVillageGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Raider> RAIDER_MOVE_THROUGH_VILLAGE = of("raider_move_through_village", Raider.class);

    /**
     * net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Shulker> SHULKER_ATTACK = of("shulker_attack", Shulker.class);

    /**
     * net.minecraft.world.entity.monster.Shulker$ShulkerDefenseAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Shulker> SHULKER_DEFENSE_ATTACK = of("shulker_defense_attack", Shulker.class);

    /**
     * net.minecraft.world.entity.monster.Shulker$ShulkerNearestAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Shulker> SHULKER_NEAREST_ATTACK = of("shulker_nearest_attack", Shulker.class);

    /**
     * net.minecraft.world.entity.monster.Shulker$ShulkerPeekGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Shulker> SHULKER_PEEK = of("shulker_peek", Shulker.class);

    /**
     * net.minecraft.world.entity.monster.Silverfish$SilverfishMergeWithStoneGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Silverfish> SILVERFISH_MERGE_WITH_STONE = of("silverfish_merge_with_stone", Silverfish.class);

    /**
     * net.minecraft.world.entity.monster.Silverfish$SilverfishWakeUpFriendsGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Silverfish> SILVERFISH_WAKE_UP_FRIENDS = of("silverfish_wake_up_friends", Silverfish.class);

    /**
     * net.minecraft.world.entity.animal.horse.SkeletonTrapGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<SkeletonHorse> SKELETON_TRAP = of("skeleton_trap", SkeletonHorse.class);

    /**
     * net.minecraft.world.entity.monster.Slime$SlimeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Slime> SLIME_ATTACK = of("slime_attack", Slime.class);

    /**
     * net.minecraft.world.entity.monster.Slime$SlimeFloatGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Slime> SLIME_FLOAT = of("slime_float", Slime.class);

    /**
     * net.minecraft.world.entity.monster.Slime$SlimeKeepOnJumpingGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Slime> SLIME_KEEP_ON_JUMPING = of("slime_keep_on_jumping", Slime.class);

    /**
     * net.minecraft.world.entity.monster.Slime$SlimeRandomDirectionGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Slime> SLIME_RANDOM_DIRECTION = of("slime_random_direction", Slime.class);

    /**
     * net.minecraft.world.entity.monster.SpellcasterIllager$SpellcasterCastingSpellGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Spellcaster> SPELLCASTER_CASTING_SPELL = of("spellcaster_casting_spell", Spellcaster.class);

    /**
     * net.minecraft.world.entity.monster.Spider$SpiderAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Spider> SPIDER_ATTACK = of("spider_attack", Spider.class);

    /**
     * net.minecraft.world.entity.monster.Spider$SpiderTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Spider> SPIDER = of("spider", Spider.class);

    /**
     * net.minecraft.world.entity.animal.Squid$SquidFleeGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Squid> SQUID_FLEE = of("squid_flee", Squid.class);

    /**
     * net.minecraft.world.entity.animal.Squid$SquidRandomMovementGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Squid> SQUID_RANDOM_MOVEMENT = of("squid_random_movement", Squid.class);

    /**
     * net.minecraft.world.entity.monster.Strider$StriderGoToLavaGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Strider> STRIDER_GO_TO_LAVA = of("strider_go_to_lava", Strider.class);

    /**
     * net.minecraft.world.entity.ai.goal.FollowOwnerGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Tameable> FOLLOW_OWNER = of("follow_owner", Tameable.class);

    /**
     * net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Tameable> SIT_WHEN_ORDERED_TO = of("sit_when_ordered_to", Tameable.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Tameable> NON_TAME_RANDOM = of("non_tame_random", Tameable.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Tameable> OWNER_HURT_BY = of("owner_hurt_by", Tameable.class);

    /**
     * net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Tameable> OWNER_HURT = of("owner_hurt", Tameable.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleBreedGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_BREED = of("turtle_breed", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleGoHomeGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_GO_HOME = of("turtle_go_home", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleGoToWaterGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_GO_TO_WATER = of("turtle_go_to_water", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleLayEggGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_LAY_EGG = of("turtle_lay_egg", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtlePanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_PANIC = of("turtle_panic", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleRandomStrollGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_RANDOM_STROLL = of("turtle_random_stroll", Turtle.class);

    /**
     * net.minecraft.world.entity.animal.Turtle$TurtleTravelGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Turtle> TURTLE_TRAVEL = of("turtle_travel", Turtle.class);

    /**
     * net.minecraft.world.entity.monster.Vex$VexChargeAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Vex> VEX_CHARGE_ATTACK = of("vex_charge_attack", Vex.class);

    /**
     * net.minecraft.world.entity.monster.Vex$VexCopyOwnerTargetGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Vex> VEX_COPY_OWNER = of("vex_copy_owner", Vex.class);

    /**
     * net.minecraft.world.entity.monster.Vex$VexRandomMoveGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Vex> VEX_RANDOM_MOVE = of("vex_random_move", Vex.class);

    /**
     * net.minecraft.world.entity.monster.Vindicator$VindicatorJohnnyAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Vindicator> VINDICATOR_JOHNNY_ATTACK = of("vindicator_johnny_attack", Vindicator.class);

    /**
     * net.minecraft.world.entity.npc.WanderingTrader$WanderToPositionGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<WanderingTrader> WANDER_TO_POSITION = of("wander_to_position", WanderingTrader.class);

    /**
     * net.minecraft.world.entity.boss.wither.WitherBoss$WitherDoNothingGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Wither> WITHER_DO_NOTHING = of("wither_do_nothing", Wither.class);

    /**
     * net.minecraft.world.entity.ai.goal.BegGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Wolf> BEG = of("beg", Wolf.class);

    /**
     * net.minecraft.world.entity.animal.Wolf$WolfAvoidEntityGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Wolf> WOLF_AVOID_ENTITY = of("wolf_avoid_entity", Wolf.class);

    /**
     * net.minecraft.world.entity.animal.Wolf$WolfPanicGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Wolf> WOLF_PANIC = of("wolf_panic", Wolf.class);

    /**
     * net.minecraft.world.entity.ai.goal.ZombieAttackGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Zombie> ZOMBIE_ATTACK = of("zombie_attack", Zombie.class);

    /**
     * net.minecraft.world.entity.monster.Zombie$ZombieAttackTurtleEggGoal
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    GoalKey<Zombie> ZOMBIE_ATTACK_TURTLE_EGG = of("zombie_attack_turtle_egg", Zombie.class);

    private static @NotNull GoalKey of(final @NotNull String key, final @NotNull Class clazz) {
        return GoalKey.of(clazz, NamespacedKey.minecraft(key));
    }
    /**
     * @deprecated removed in 1.20.2
     */
    @Deprecated GoalKey<Vindicator> VINDICATOR_MELEE_ATTACK = GoalKey.of(Vindicator.class, NamespacedKey.minecraft("vindicator_melee_attack"));
    /**
     * @deprecated removed in 1.20.2
     */
    @Deprecated GoalKey<Ravager> RAVAGER_MELEE_ATTACK = GoalKey.of(Ravager.class, NamespacedKey.minecraft("ravager_melee_attack"));
    /**
     * @deprecated removed in 1.20.2
     */
    @Deprecated GoalKey<Rabbit> EVIL_RABBIT_ATTACK = GoalKey.of(Rabbit.class, NamespacedKey.minecraft("evil_rabbit_attack"));

    /**
     * @deprecated removed in 1.16
     */
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PigZombie> ANGER = GoalKey.of(PigZombie.class, NamespacedKey.minecraft("anger"));
    /**
     * @deprecated removed in 1.16
     */
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PigZombie> ANGER_OTHER = GoalKey.of(PigZombie.class, NamespacedKey.minecraft("anger_other"));

    // the constants below use spigot names, they no longer work
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Blaze> BLAZE_FIREBALL = GoalKey.of(Blaze.class, NamespacedKey.minecraft("blaze_fireball"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Cat> TEMPT_CHANCE = GoalKey.of(Cat.class, NamespacedKey.minecraft("tempt_chance"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Dolphin> DOLPHIN_PLAY_WITH_ITEMS = GoalKey.of(Dolphin.class, NamespacedKey.minecraft("dolphin_play_with_items"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Drowned> DROWNED_GOTO_BEACH = GoalKey.of(Drowned.class, NamespacedKey.minecraft("drowned_goto_beach"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> DROWNED_GOTO_WATER = GoalKey.of(Creature.class, NamespacedKey.minecraft("drowned_goto_water"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Enderman> ENDERMAN_PICKUP_BLOCK = GoalKey.of(Enderman.class, NamespacedKey.minecraft("enderman_pickup_block"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Enderman> ENDERMAN_PLACE_BLOCK = GoalKey.of(Enderman.class, NamespacedKey.minecraft("enderman_place_block"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Enderman> PLAYER_WHO_LOOKED_AT_TARGET = GoalKey.of(Enderman.class, NamespacedKey.minecraft("player_who_looked_at_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Evoker> EVOKER_CAST_SPELL = GoalKey.of(Evoker.class, NamespacedKey.minecraft("evoker_cast_spell"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_DEFEND_TRUSTED = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_defend_trusted"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_FACEPLANT = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_faceplant"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_PERCH_AND_SEARCH = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_perch_and_search"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_SLEEP = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_sleep"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_SEEK_SHELTER = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_seek_shelter"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fox> FOX_STALK_PREY = GoalKey.of(Fox.class, NamespacedKey.minecraft("fox_stalk_prey"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Ghast> GHAST_ATTACK_TARGET = GoalKey.of(Ghast.class, NamespacedKey.minecraft("ghast_attack_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Ghast> GHAST_IDLE_MOVE = GoalKey.of(Ghast.class, NamespacedKey.minecraft("ghast_idle_move"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Ghast> GHAST_MOVE_TOWARDS_TARGET = GoalKey.of(Ghast.class, NamespacedKey.minecraft("ghast_move_towards_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Spellcaster> SPELLCASTER_CAST_SPELL = GoalKey.of(Spellcaster.class, NamespacedKey.minecraft("spellcaster_cast_spell"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<TraderLlama> LLAMATRADER_DEFENDED_WANDERING_TRADER = GoalKey.of(TraderLlama.class, NamespacedKey.minecraft("llamatrader_defended_wandering_trader"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Panda> PANDA_HURT_BY_TARGET = GoalKey.of(Panda.class, NamespacedKey.minecraft("panda_hurt_by_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PolarBear> POLARBEAR_ATTACK_PLAYERS = GoalKey.of(PolarBear.class, NamespacedKey.minecraft("polarbear_attack_players"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PolarBear> POLARBEAR_HURT_BY = GoalKey.of(PolarBear.class, NamespacedKey.minecraft("polarbear_hurt_by"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PolarBear> POLARBEAR_MELEE = GoalKey.of(PolarBear.class, NamespacedKey.minecraft("polarbear_melee"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<PolarBear> POLARBEAR_PANIC = GoalKey.of(PolarBear.class, NamespacedKey.minecraft("polarbear_panic"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Rabbit> EAT_CARROTS = GoalKey.of(Rabbit.class, NamespacedKey.minecraft("eat_carrots"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Rabbit> KILLER_RABBIT_MELEE_ATTACK = GoalKey.of(Rabbit.class, NamespacedKey.minecraft("killer_rabbit_melee_attack"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Rabbit> RABBIT_AVOID_TARGET = GoalKey.of(Rabbit.class, NamespacedKey.minecraft("rabbit_avoid_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Raider> RAIDER_HOLD_GROUND = GoalKey.of(Raider.class, NamespacedKey.minecraft("raider_hold_ground"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Raider> RAIDER_OBTAIN_BANNER = GoalKey.of(Raider.class, NamespacedKey.minecraft("raider_obtain_banner"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Shulker> SHULKER_DEFENSE = GoalKey.of(Shulker.class, NamespacedKey.minecraft("shulker_defense"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Shulker> SHULKER_NEAREST = GoalKey.of(Shulker.class, NamespacedKey.minecraft("shulker_nearest"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Silverfish> SILVERFISH_HIDE_IN_BLOCK = GoalKey.of(Silverfish.class, NamespacedKey.minecraft("silverfish_hide_in_block"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Silverfish> SILVERFISH_WAKE_OTHERS = GoalKey.of(Silverfish.class, NamespacedKey.minecraft("silverfish_wake_others"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Slime> SLIME_IDLE = GoalKey.of(Slime.class, NamespacedKey.minecraft("slime_idle"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Slime> SLIME_NEAREST_PLAYER = GoalKey.of(Slime.class, NamespacedKey.minecraft("slime_nearest_player"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Slime> SLIME_RANDOM_JUMP = GoalKey.of(Slime.class, NamespacedKey.minecraft("slime_random_jump"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Spider> SPIDER_MELEE_ATTACK = GoalKey.of(Spider.class, NamespacedKey.minecraft("spider_melee_attack"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Spider> SPIDER_NEAREST_ATTACKABLE_TARGET = GoalKey.of(Spider.class, NamespacedKey.minecraft("spider_nearest_attackable_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Squid> SQUID = GoalKey.of(Squid.class, NamespacedKey.minecraft("squid"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Turtle> TURTLE_GOTO_WATER = GoalKey.of(Turtle.class, NamespacedKey.minecraft("turtle_goto_water"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Turtle> TURTLE_TEMPT = GoalKey.of(Turtle.class, NamespacedKey.minecraft("turtle_tempt"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Vex> VEX_COPY_TARGET_OF_OWNER = GoalKey.of(Vex.class, NamespacedKey.minecraft("vex_copy_target_of_owner"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<WanderingTrader> VILLAGERTRADER_WANDER_TO_POSITION = GoalKey.of(WanderingTrader.class, NamespacedKey.minecraft("villagertrader_wander_to_position"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<RangedEntity> ARROW_ATTACK = GoalKey.of(RangedEntity.class, NamespacedKey.minecraft("arrow_attack"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> AVOID_TARGET = GoalKey.of(Creature.class, NamespacedKey.minecraft("avoid_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Monster> BOW_SHOOT = GoalKey.of(Monster.class, NamespacedKey.minecraft("bow_shoot"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> BREATH = GoalKey.of(Creature.class, NamespacedKey.minecraft("breath"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Cat> CAT_SIT_ON_BED = GoalKey.of(Cat.class, NamespacedKey.minecraft("cat_sit_on_bed"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Monster> CROSSBOW_ATTACK = GoalKey.of(Monster.class, NamespacedKey.minecraft("crossbow_attack"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> DOOR_OPEN = GoalKey.of(Mob.class, NamespacedKey.minecraft("door_open"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> EAT_TILE = GoalKey.of(Mob.class, NamespacedKey.minecraft("eat_tile"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Fish> FISH_SCHOOL = GoalKey.of(Fish.class, NamespacedKey.minecraft("fish_school"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> FOLLOW_ENTITY = GoalKey.of(Mob.class, NamespacedKey.minecraft("follow_entity"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<SkeletonHorse> HORSE_TRAP = GoalKey.of(SkeletonHorse.class, NamespacedKey.minecraft("horse_trap"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> HURT_BY_TARGET = GoalKey.of(Creature.class, NamespacedKey.minecraft("hurt_by_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Cat> JUMP_ON_BLOCK = GoalKey.of(Cat.class, NamespacedKey.minecraft("jump_on_block"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> LEAP_AT_TARGET = GoalKey.of(Mob.class, NamespacedKey.minecraft("leap_at_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Llama> LLAMA_FOLLOW = GoalKey.of(Llama.class, NamespacedKey.minecraft("llama_follow"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> MOVE_TOWARDS_TARGET = GoalKey.of(Creature.class, NamespacedKey.minecraft("move_towards_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> NEAREST_ATTACKABLE_TARGET = GoalKey.of(Mob.class, NamespacedKey.minecraft("nearest_attackable_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Raider> NEAREST_ATTACKABLE_TARGET_WITCH = GoalKey.of(Raider.class, NamespacedKey.minecraft("nearest_attackable_target_witch"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> NEAREST_VILLAGE = GoalKey.of(Creature.class, NamespacedKey.minecraft("nearest_village"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Tameable> OWNER_HURT_BY_TARGET = GoalKey.of(Tameable.class, NamespacedKey.minecraft("owner_hurt_by_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Tameable> OWNER_HURT_TARGET = GoalKey.of(Tameable.class, NamespacedKey.minecraft("owner_hurt_target"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Parrot> PERCH = GoalKey.of(Parrot.class, NamespacedKey.minecraft("perch"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Raider> RAID = GoalKey.of(Raider.class, NamespacedKey.minecraft("raid"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> RANDOM_FLY = GoalKey.of(Creature.class, NamespacedKey.minecraft("random_fly"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> RANDOM_LOOKAROUND = GoalKey.of(Mob.class, NamespacedKey.minecraft("random_lookaround"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> RANDOM_STROLL_LAND = GoalKey.of(Creature.class, NamespacedKey.minecraft("random_stroll_land"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> RANDOM_SWIM = GoalKey.of(Creature.class, NamespacedKey.minecraft("random_swim"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Tameable> RANDOM_TARGET_NON_TAMED = GoalKey.of(Tameable.class, NamespacedKey.minecraft("random_target_non_tamed"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Tameable> SIT = GoalKey.of(Tameable.class, NamespacedKey.minecraft("sit"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> STROLL_VILLAGE = GoalKey.of(Creature.class, NamespacedKey.minecraft("stroll_village"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<AbstractHorse> TAME = GoalKey.of(AbstractHorse.class, NamespacedKey.minecraft("tame"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> WATER = GoalKey.of(Creature.class, NamespacedKey.minecraft("water"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Dolphin> WATER_JUMP = GoalKey.of(Dolphin.class, NamespacedKey.minecraft("water_jump"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Creature> STROLL_VILLAGE_GOLEM = GoalKey.of(Creature.class, NamespacedKey.minecraft("stroll_village_golem"));
    @Deprecated(forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.21") GoalKey<Mob> UNIVERSAL_ANGER_RESET = GoalKey.of(Mob.class, NamespacedKey.minecraft("universal_anger_reset"));
}
