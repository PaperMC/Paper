package com.destroystokyo.paper.entity.ai;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.util.set.OptimizedSmallEnumSet;

import net.minecraft.server.*; // intentional star import

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.*; // intentional star import

public class MobGoalHelper {

    private static final BiMap<String, String> deobfuscationMap = HashBiMap.create();
    private static final Map<Class<? extends PathfinderGoal>, Class<? extends Mob>> entityClassCache = new HashMap<>();
    private static final Map<Class<? extends EntityInsentient>, Class<? extends Mob>> bukkitMap = new HashMap<>();

    static final Set<String> ignored = new HashSet<>();

    static {
        // TODO these kinda should be checked on each release, in case obfuscation changes
        deobfuscationMap.put("bee_b", "bee_attack");
        deobfuscationMap.put("bee_c", "bee_become_angry");
        deobfuscationMap.put("bee_d", "bee_enter_hive");
        deobfuscationMap.put("bee_e", "bee_go_to_hive");
        deobfuscationMap.put("bee_f", "bee_go_to_known_flower");
        deobfuscationMap.put("bee_g", "bee_grow_crop");
        deobfuscationMap.put("bee_h", "bee_hurt_by_other");
        deobfuscationMap.put("bee_i", "bee_locate_hive");
        deobfuscationMap.put("bee_k", "bee_pollinate");
        deobfuscationMap.put("bee_l", "bee_wander");
        deobfuscationMap.put("cat_a", "cat_avoid_entity");
        deobfuscationMap.put("cat_b", "cat_relax_on_owner");
        deobfuscationMap.put("dolphin_b", "dolphin_swim_to_treasure");
        deobfuscationMap.put("dolphin_c", "dolphin_swim_with_player");
        deobfuscationMap.put("dolphin_d", "dolphin_play_with_items");
        deobfuscationMap.put("drowned_a", "drowned_attack");
        deobfuscationMap.put("drowned_b", "drowned_goto_beach");
        deobfuscationMap.put("drowned_c", "drowned_goto_water");
        deobfuscationMap.put("drowned_e", "drowned_swim_up");
        deobfuscationMap.put("drowned_f", "drowned_trident_attack");
        deobfuscationMap.put("enderman_a", "enderman_freeze_when_looked_at");
        deobfuscationMap.put("evoker_a", "evoker_attack_spell");
        deobfuscationMap.put("evoker_b", "evoker_cast_spell");
        deobfuscationMap.put("evoker_c", "evoker_summon_spell");
        deobfuscationMap.put("evoker_d", "evoker_wololo_spell");
        deobfuscationMap.put("fish_b", "fish_swim");
        deobfuscationMap.put("fox_a", "fox_defend_trusted");
        deobfuscationMap.put("fox_b", "fox_faceplant");
        deobfuscationMap.put("fox_e", "fox_breed");
        deobfuscationMap.put("fox_f", "fox_eat_berries");
        deobfuscationMap.put("fox_g", "fox_float");
        deobfuscationMap.put("fox_h", "fox_follow_parent");
        deobfuscationMap.put("fox_j", "fox_look_at_player");
        deobfuscationMap.put("fox_l", "fox_melee_attack");
        deobfuscationMap.put("fox_n", "fox_panic");
        deobfuscationMap.put("fox_o", "fox_pounce");
        deobfuscationMap.put("fox_p", "fox_search_for_items");
        deobfuscationMap.put("fox_q", "fox_stroll_through_village");
        deobfuscationMap.put("fox_r", "fox_perch_and_search");
        deobfuscationMap.put("fox_s", "fox_seek_shelter");
        deobfuscationMap.put("fox_t", "fox_sleep");
        deobfuscationMap.put("fox_u", "fox_stalk_prey");
        deobfuscationMap.put("illager_abstract_b", "raider_open_door");
        deobfuscationMap.put("illager_illusioner_a", "illusioner_blindness_spell");
        deobfuscationMap.put("illager_illusioner_b", "illusioner_mirror_spell");
        deobfuscationMap.put("illager_wizard_b", "spellcaster_cast_spell");
        deobfuscationMap.put("llama_a", "llama_attack_wolf");
        deobfuscationMap.put("llama_c", "llama_hurt_by");
        deobfuscationMap.put("llama_trader_a", "llamatrader_defended_wandering_trader");
        deobfuscationMap.put("monster_patrolling_a", "long_distance_patrol");
        deobfuscationMap.put("ocelot_a", "ocelot_avoid_entity");
        deobfuscationMap.put("ocelot_b", "ocelot_tempt");
        deobfuscationMap.put("panda_b", "panda_attack");
        deobfuscationMap.put("panda_c", "panda_avoid");
        deobfuscationMap.put("panda_d", "panda_breed");
        deobfuscationMap.put("panda_e", "panda_hurt_by_target");
        deobfuscationMap.put("panda_f", "panda_lie_on_back");
        deobfuscationMap.put("panda_g", "panda_look_at_player");
        deobfuscationMap.put("panda_i", "panda_panic");
        deobfuscationMap.put("panda_j", "panda_roll");
        deobfuscationMap.put("panda_k", "panda_sit");
        deobfuscationMap.put("panda_l", "panda_sneeze");
        deobfuscationMap.put("phantom_b", "phantom_attack_player");
        deobfuscationMap.put("phantom_c", "phantom_attack_strategy");
        deobfuscationMap.put("phantom_e", "phantom_circle_around_anchor");
        deobfuscationMap.put("phantom_i", "phantom_sweep_attack");
        deobfuscationMap.put("polar_bear_a", "polarbear_attack_players");
        deobfuscationMap.put("polar_bear_b", "polarbear_hurt_by");
        deobfuscationMap.put("polar_bear_c", "polarbear_melee");
        deobfuscationMap.put("polar_bear_d", "polarbear_panic");
        deobfuscationMap.put("puffer_fish_a", "pufferfish_puff");
        deobfuscationMap.put("raider_a", "raider_hold_ground");
        deobfuscationMap.put("raider_b", "raider_obtain_banner");
        deobfuscationMap.put("raider_c", "raider_celebration");
        deobfuscationMap.put("raider_d", "raider_move_through_village");
        deobfuscationMap.put("ravager_a", "ravager_melee_attack");
        deobfuscationMap.put("shulker_a", "shulker_attack");
        deobfuscationMap.put("shulker_c", "shulker_defense");
        deobfuscationMap.put("shulker_d", "shulker_nearest");
        deobfuscationMap.put("shulker_e", "shulker_peek");
        deobfuscationMap.put("squid_a", "squid_flee");
        deobfuscationMap.put("skeleton_abstract_1", "skeleton_melee");
        deobfuscationMap.put("strider_a", "strider_go_to_lava");
        deobfuscationMap.put("turtle_a", "turtle_breed");
        deobfuscationMap.put("turtle_b", "turtle_go_home");
        deobfuscationMap.put("turtle_c", "turtle_goto_water");
        deobfuscationMap.put("turtle_d", "turtle_lay_egg");
        deobfuscationMap.put("turtle_f", "turtle_panic");
        deobfuscationMap.put("turtle_h", "turtle_random_stroll");
        deobfuscationMap.put("turtle_i", "turtle_tempt");
        deobfuscationMap.put("turtle_j", "turtle_travel");
        deobfuscationMap.put("vex_a", "vex_charge_attack");
        deobfuscationMap.put("vex_b", "vex_copy_target_of_owner");
        deobfuscationMap.put("vex_d", "vex_random_move");
        deobfuscationMap.put("villager_trader_a", "villagertrader_wander_to_position");
        deobfuscationMap.put("vindicator_a", "vindicator_break_door");
        deobfuscationMap.put("vindicator_b", "vindicator_johnny_attack");
        deobfuscationMap.put("vindicator_c", "vindicator_melee_attack");
        deobfuscationMap.put("wither_a", "wither_do_nothing");
        deobfuscationMap.put("wolf_a", "wolf_avoid_entity");
        deobfuscationMap.put("zombie_a", "zombie_attack_turtle_egg");

        ignored.add("selector_1");
        ignored.add("selector_2");
        ignored.add("wrapped");

        bukkitMap.put(EntityInsentient.class, Mob.class);
        bukkitMap.put(EntityAgeable.class, Ageable.class);
        bukkitMap.put(EntityAmbient.class, Ambient.class);
        bukkitMap.put(EntityAnimal.class, Animals.class);
        bukkitMap.put(EntityBat.class, Bat.class);
        bukkitMap.put(EntityBee.class, Bee.class);
        bukkitMap.put(EntityBlaze.class, Blaze.class);
        bukkitMap.put(EntityCat.class, Cat.class);
        bukkitMap.put(EntityCaveSpider.class, CaveSpider.class);
        bukkitMap.put(EntityChicken.class, Chicken.class);
        bukkitMap.put(EntityCod.class, Cod.class);
        bukkitMap.put(EntityCow.class, Cow.class);
        bukkitMap.put(EntityCreature.class, Creature.class);
        bukkitMap.put(EntityCreeper.class, Creeper.class);
        bukkitMap.put(EntityDolphin.class, Dolphin.class);
        bukkitMap.put(EntityDrowned.class, Drowned.class);
        bukkitMap.put(EntityEnderDragon.class, EnderDragon.class);
        bukkitMap.put(EntityEnderman.class, Enderman.class);
        bukkitMap.put(EntityEndermite.class, Endermite.class);
        bukkitMap.put(EntityEvoker.class, Evoker.class);
        bukkitMap.put(EntityFish.class, Fish.class);
        bukkitMap.put(EntityFishSchool.class, Fish.class); // close enough
        bukkitMap.put(EntityFlying.class, Flying.class);
        bukkitMap.put(EntityFox.class, Fox.class);
        bukkitMap.put(EntityGhast.class, Ghast.class);
        bukkitMap.put(EntityGiantZombie.class, Giant.class);
        bukkitMap.put(EntityGolem.class, Golem.class);
        bukkitMap.put(EntityGuardian.class, Guardian.class);
        bukkitMap.put(EntityGuardianElder.class, ElderGuardian.class);
        bukkitMap.put(EntityHorse.class, Horse.class);
        bukkitMap.put(EntityHorseAbstract.class, AbstractHorse.class);
        bukkitMap.put(EntityHorseChestedAbstract.class, ChestedHorse.class);
        bukkitMap.put(EntityHorseDonkey.class, Donkey.class);
        bukkitMap.put(EntityHorseMule.class, Mule.class);
        bukkitMap.put(EntityHorseSkeleton.class, SkeletonHorse.class);
        bukkitMap.put(EntityHorseZombie.class, ZombieHorse.class);
        bukkitMap.put(EntityIllagerAbstract.class, Illager.class);
        bukkitMap.put(EntityIllagerIllusioner.class, Illusioner.class);
        bukkitMap.put(EntityIllagerWizard.class, Spellcaster.class);
        bukkitMap.put(EntityIronGolem.class, IronGolem.class);
        bukkitMap.put(EntityLlama.class, Llama.class);
        bukkitMap.put(EntityLlamaTrader.class, TraderLlama.class);
        bukkitMap.put(EntityMagmaCube.class, MagmaCube.class);
        bukkitMap.put(EntityMonster.class, Monster.class);
        bukkitMap.put(EntityMonsterPatrolling.class, Monster.class); // close enough
        bukkitMap.put(EntityMushroomCow.class, MushroomCow.class);
        bukkitMap.put(EntityOcelot.class, Ocelot.class);
        bukkitMap.put(EntityPanda.class, Panda.class);
        bukkitMap.put(EntityParrot.class, Parrot.class);
        bukkitMap.put(EntityPerchable.class, Parrot.class); // close enough
        bukkitMap.put(EntityPhantom.class, Phantom.class);
        bukkitMap.put(EntityPig.class, Pig.class);
        bukkitMap.put(EntityPigZombie.class, PigZombie.class);
        bukkitMap.put(EntityPillager.class, Pillager.class);
        bukkitMap.put(EntityPolarBear.class, PolarBear.class);
        bukkitMap.put(EntityPufferFish.class, PufferFish.class);
        bukkitMap.put(EntityRabbit.class, Rabbit.class);
        bukkitMap.put(EntityRaider.class, Raider.class);
        bukkitMap.put(EntityRavager.class, Ravager.class);
        bukkitMap.put(EntitySalmon.class, Salmon.class);
        bukkitMap.put(EntitySheep.class, Sheep.class);
        bukkitMap.put(EntityShulker.class, Shulker.class);
        bukkitMap.put(EntitySilverfish.class, Silverfish.class);
        bukkitMap.put(EntitySkeleton.class, Skeleton.class);
        bukkitMap.put(EntitySkeletonAbstract.class, Skeleton.class);
        bukkitMap.put(EntitySkeletonStray.class, Stray.class);
        bukkitMap.put(EntitySkeletonWither.class, WitherSkeleton.class);
        bukkitMap.put(EntitySlime.class, Slime.class);
        bukkitMap.put(EntitySnowman.class, Snowman.class);
        bukkitMap.put(EntitySpider.class, Spider.class);
        bukkitMap.put(EntitySquid.class, Squid.class);
        bukkitMap.put(EntityTameableAnimal.class, Tameable.class);
        bukkitMap.put(EntityTropicalFish.class, TropicalFish.class);
        bukkitMap.put(EntityTurtle.class, Turtle.class);
        bukkitMap.put(EntityVex.class, Vex.class);
        bukkitMap.put(EntityVillager.class, Villager.class);
        bukkitMap.put(EntityVillagerAbstract.class, AbstractVillager.class);
        bukkitMap.put(EntityVillagerTrader.class, WanderingTrader.class);
        bukkitMap.put(EntityVindicator.class, Vindicator.class);
        bukkitMap.put(EntityWaterAnimal.class, WaterMob.class);
        bukkitMap.put(EntityWitch.class, Witch.class);
        bukkitMap.put(EntityWither.class, Wither.class);
        bukkitMap.put(EntityWolf.class, Wolf.class);
        bukkitMap.put(EntityZombie.class, Zombie.class);
        bukkitMap.put(EntityZombieHusk.class, Husk.class);
        bukkitMap.put(EntityZombieVillager.class, ZombieVillager.class);
        bukkitMap.put(EntityHoglin.class, Hoglin.class);
        bukkitMap.put(EntityPiglin.class, Piglin.class);
        bukkitMap.put(EntityPiglinAbstract.class, PiglinAbstract.class);
        bukkitMap.put(EntityPiglinBrute.class, PiglinBrute.class);
        bukkitMap.put(EntityStrider.class, Strider.class);
        bukkitMap.put(EntityZoglin.class, Zoglin.class);
    }

    public static String getUsableName(Class<?> clazz) {
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        boolean flag = false;
        // inner classes
        if (name.contains("$")) {
            String cut = name.substring(name.indexOf("$") + 1);
            if (cut.length() <= 2) {
                name = name.replace("Entity", "");
                name = name.replace("$", "_");
                flag = true;
            } else {
                // mapped, wooo
                name = cut;
            }
        }
        name = name.replace("PathfinderGoal", "");
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        name = sb.toString();
        name = name.replaceFirst("_", "");

        if (flag && !deobfuscationMap.containsKey(name.toLowerCase()) && !ignored.contains(name)) {
            System.out.println("need to map " + clazz.getName() + " (" + name.toLowerCase() + ")");
        }

        // did we rename this key?
        return deobfuscationMap.getOrDefault(name, name);
    }

    public static EnumSet<GoalType> vanillaToPaper(OptimizedSmallEnumSet<PathfinderGoal.Type> types) {
        EnumSet<GoalType> goals = EnumSet.noneOf(GoalType.class);
        for (GoalType type : GoalType.values()) {
            if (types.hasElement(paperToVanilla(type))) {
                goals.add(type);
            }
        }
        return goals;
    }

    public static GoalType vanillaToPaper(PathfinderGoal.Type type) {
        switch (type) {
            case MOVE:
                return GoalType.MOVE;
            case LOOK:
                return GoalType.LOOK;
            case JUMP:
                return GoalType.JUMP;
            case UNKNOWN_BEHAVIOR:
                return GoalType.UNKNOWN_BEHAVIOR;
            case TARGET:
                return GoalType.TARGET;
            default:
                throw new IllegalArgumentException("Unknown vanilla mob goal type " + type.name());
        }
    }

    public static EnumSet<PathfinderGoal.Type> paperToVanilla(EnumSet<GoalType> types) {
        EnumSet<PathfinderGoal.Type> goals = EnumSet.noneOf(PathfinderGoal.Type.class);
        for (GoalType type : types) {
            goals.add(paperToVanilla(type));
        }
        return goals;
    }

    public static PathfinderGoal.Type paperToVanilla(GoalType type) {
        switch (type) {
            case MOVE:
                return PathfinderGoal.Type.MOVE;
            case LOOK:
                return PathfinderGoal.Type.LOOK;
            case JUMP:
                return PathfinderGoal.Type.JUMP;
            case UNKNOWN_BEHAVIOR:
                return PathfinderGoal.Type.UNKNOWN_BEHAVIOR;
            case TARGET:
                return PathfinderGoal.Type.TARGET;
            default:
                throw new IllegalArgumentException("Unknown paper mob goal type " + type.name());
        }
    }

    public static <T extends Mob> GoalKey<T> getKey(Class<? extends PathfinderGoal> goalClass) {
        String name = getUsableName(goalClass);
        if (ignored.contains(name)) {
            //noinspection unchecked
            return (GoalKey<T>) GoalKey.of(Mob.class, NamespacedKey.minecraft(name));
        }
        return GoalKey.of(getEntity(goalClass), NamespacedKey.minecraft(name));
    }

    public static <T extends Mob> Class<T> getEntity(Class<? extends PathfinderGoal> goalClass) {
        //noinspection unchecked
        return (Class<T>) entityClassCache.computeIfAbsent(goalClass, key -> {
            for (Constructor<?> ctor : key.getDeclaredConstructors()) {
                for (int i = 0; i < ctor.getParameterCount(); i++) {
                    Class<?> param = ctor.getParameterTypes()[i];
                    if (EntityInsentient.class.isAssignableFrom(param)) {
                        //noinspection unchecked
                        return toBukkitClass((Class<? extends EntityInsentient>) param);
                    } else if (IRangedEntity.class.isAssignableFrom(param)) {
                        return RangedEntity.class;
                    }
                }
            }
            throw new RuntimeException("Can't figure out applicable entity for mob goal " + goalClass); // maybe just return EntityInsentient?
        });
    }

    public static Class<? extends Mob> toBukkitClass(Class<? extends EntityInsentient> nmsClass) {
        Class<? extends Mob> bukkitClass = bukkitMap.get(nmsClass);
        if (bukkitClass == null) {
            throw new RuntimeException("Can't figure out applicable bukkit entity for nms entity " + nmsClass); // maybe just return Mob?
        }
        return bukkitClass;
    }
}
