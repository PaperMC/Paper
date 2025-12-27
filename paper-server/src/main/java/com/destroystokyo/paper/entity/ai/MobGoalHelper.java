package com.destroystokyo.paper.entity.ai;

import com.destroystokyo.paper.entity.RangedEntity;
import com.google.common.base.CaseFormat;
import io.papermc.paper.entity.SchoolableFish;
import io.papermc.paper.util.ObfHelper;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;

public class MobGoalHelper {

    private static final Map<Class<? extends Goal>, Class<? extends Mob>> GENERIC_TYPE_CACHE = new HashMap<>();
    public static final Map<Class<? extends net.minecraft.world.entity.Mob>, Class<? extends Mob>> BUKKIT_BRIDGE = Util.make(new LinkedHashMap<>(), map -> {
        //<editor-fold defaultstate="collapsed" desc="bukkitMap Entities">
        // Start generate - MobGoalHelper#BUKKIT_BRIDGE
        map.put(net.minecraft.world.entity.Mob.class, Mob.class);
        map.put(net.minecraft.world.entity.AgeableMob.class, Ageable.class);
        map.put(net.minecraft.world.entity.ambient.AmbientCreature.class, Ambient.class);
        map.put(net.minecraft.world.entity.animal.Animal.class, Animals.class);
        map.put(net.minecraft.world.entity.ambient.Bat.class, Bat.class);
        map.put(net.minecraft.world.entity.animal.bee.Bee.class, Bee.class);
        map.put(net.minecraft.world.entity.monster.Blaze.class, Blaze.class);
        map.put(net.minecraft.world.entity.animal.feline.Cat.class, Cat.class);
        map.put(net.minecraft.world.entity.monster.spider.CaveSpider.class, CaveSpider.class);
        map.put(net.minecraft.world.entity.animal.chicken.Chicken.class, Chicken.class);
        map.put(net.minecraft.world.entity.animal.fish.Cod.class, Cod.class);
        map.put(net.minecraft.world.entity.animal.cow.Cow.class, Cow.class);
        map.put(net.minecraft.world.entity.PathfinderMob.class, Creature.class);
        map.put(net.minecraft.world.entity.monster.Creeper.class, Creeper.class);
        map.put(net.minecraft.world.entity.animal.dolphin.Dolphin.class, Dolphin.class);
        map.put(net.minecraft.world.entity.monster.zombie.Drowned.class, Drowned.class);
        map.put(net.minecraft.world.entity.boss.enderdragon.EnderDragon.class, EnderDragon.class);
        map.put(net.minecraft.world.entity.monster.EnderMan.class, Enderman.class);
        map.put(net.minecraft.world.entity.monster.Endermite.class, Endermite.class);
        map.put(net.minecraft.world.entity.monster.illager.Evoker.class, Evoker.class);
        map.put(net.minecraft.world.entity.animal.fish.AbstractFish.class, Fish.class);
        map.put(net.minecraft.world.entity.animal.fish.AbstractSchoolingFish.class, SchoolableFish.class);
        map.put(net.minecraft.world.entity.animal.fox.Fox.class, Fox.class);
        map.put(net.minecraft.world.entity.monster.Ghast.class, Ghast.class);
        map.put(net.minecraft.world.entity.monster.Giant.class, Giant.class);
        map.put(net.minecraft.world.entity.animal.golem.AbstractGolem.class, Golem.class);
        map.put(net.minecraft.world.entity.monster.Guardian.class, Guardian.class);
        map.put(net.minecraft.world.entity.monster.ElderGuardian.class, ElderGuardian.class);
        map.put(net.minecraft.world.entity.animal.equine.Horse.class, Horse.class);
        map.put(net.minecraft.world.entity.animal.equine.AbstractHorse.class, AbstractHorse.class);
        map.put(net.minecraft.world.entity.animal.equine.AbstractChestedHorse.class, ChestedHorse.class);
        map.put(net.minecraft.world.entity.animal.equine.Donkey.class, Donkey.class);
        map.put(net.minecraft.world.entity.animal.equine.Mule.class, Mule.class);
        map.put(net.minecraft.world.entity.animal.equine.SkeletonHorse.class, SkeletonHorse.class);
        map.put(net.minecraft.world.entity.animal.equine.ZombieHorse.class, ZombieHorse.class);
        map.put(net.minecraft.world.entity.animal.camel.Camel.class, Camel.class);
        map.put(net.minecraft.world.entity.monster.illager.AbstractIllager.class, Illager.class);
        map.put(net.minecraft.world.entity.monster.illager.Illusioner.class, Illusioner.class);
        map.put(net.minecraft.world.entity.monster.illager.SpellcasterIllager.class, Spellcaster.class);
        map.put(net.minecraft.world.entity.animal.golem.IronGolem.class, IronGolem.class);
        map.put(net.minecraft.world.entity.animal.equine.Llama.class, Llama.class);
        map.put(net.minecraft.world.entity.animal.equine.TraderLlama.class, TraderLlama.class);
        map.put(net.minecraft.world.entity.monster.MagmaCube.class, MagmaCube.class);
        map.put(net.minecraft.world.entity.monster.Monster.class, Monster.class);
        map.put(net.minecraft.world.entity.monster.PatrollingMonster.class, Raider.class);
        map.put(net.minecraft.world.entity.animal.cow.MushroomCow.class, MushroomCow.class);
        map.put(net.minecraft.world.entity.animal.feline.Ocelot.class, Ocelot.class);
        map.put(net.minecraft.world.entity.animal.panda.Panda.class, Panda.class);
        map.put(net.minecraft.world.entity.animal.parrot.Parrot.class, Parrot.class);
        map.put(net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity.class, Parrot.class);
        map.put(net.minecraft.world.entity.monster.Phantom.class, Phantom.class);
        map.put(net.minecraft.world.entity.animal.pig.Pig.class, Pig.class);
        map.put(net.minecraft.world.entity.monster.zombie.ZombifiedPiglin.class, PigZombie.class);
        map.put(net.minecraft.world.entity.monster.illager.Pillager.class, Pillager.class);
        map.put(net.minecraft.world.entity.animal.polarbear.PolarBear.class, PolarBear.class);
        map.put(net.minecraft.world.entity.animal.fish.Pufferfish.class, PufferFish.class);
        map.put(net.minecraft.world.entity.animal.rabbit.Rabbit.class, Rabbit.class);
        map.put(net.minecraft.world.entity.raid.Raider.class, Raider.class);
        map.put(net.minecraft.world.entity.monster.Ravager.class, Ravager.class);
        map.put(net.minecraft.world.entity.animal.fish.Salmon.class, Salmon.class);
        map.put(net.minecraft.world.entity.animal.sheep.Sheep.class, Sheep.class);
        map.put(net.minecraft.world.entity.monster.Shulker.class, Shulker.class);
        map.put(net.minecraft.world.entity.monster.Silverfish.class, Silverfish.class);
        map.put(net.minecraft.world.entity.monster.skeleton.Skeleton.class, Skeleton.class);
        map.put(net.minecraft.world.entity.monster.skeleton.AbstractSkeleton.class, AbstractSkeleton.class);
        map.put(net.minecraft.world.entity.monster.skeleton.Stray.class, Stray.class);
        map.put(net.minecraft.world.entity.monster.skeleton.WitherSkeleton.class, WitherSkeleton.class);
        map.put(net.minecraft.world.entity.monster.Slime.class, Slime.class);
        map.put(net.minecraft.world.entity.animal.golem.SnowGolem.class, Snowman.class);
        map.put(net.minecraft.world.entity.monster.spider.Spider.class, Spider.class);
        map.put(net.minecraft.world.entity.animal.squid.Squid.class, Squid.class);
        map.put(net.minecraft.world.entity.TamableAnimal.class, Tameable.class);
        map.put(net.minecraft.world.entity.animal.fish.TropicalFish.class, TropicalFish.class);
        map.put(net.minecraft.world.entity.animal.turtle.Turtle.class, Turtle.class);
        map.put(net.minecraft.world.entity.monster.Vex.class, Vex.class);
        map.put(net.minecraft.world.entity.npc.villager.Villager.class, Villager.class);
        map.put(net.minecraft.world.entity.npc.villager.AbstractVillager.class, AbstractVillager.class);
        map.put(net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader.class, WanderingTrader.class);
        map.put(net.minecraft.world.entity.monster.illager.Vindicator.class, Vindicator.class);
        map.put(net.minecraft.world.entity.animal.fish.WaterAnimal.class, WaterMob.class);
        map.put(net.minecraft.world.entity.monster.Witch.class, Witch.class);
        map.put(net.minecraft.world.entity.boss.wither.WitherBoss.class, Wither.class);
        map.put(net.minecraft.world.entity.animal.wolf.Wolf.class, Wolf.class);
        map.put(net.minecraft.world.entity.monster.zombie.Zombie.class, Zombie.class);
        map.put(net.minecraft.world.entity.monster.zombie.Husk.class, Husk.class);
        map.put(net.minecraft.world.entity.monster.zombie.ZombieVillager.class, ZombieVillager.class);
        map.put(net.minecraft.world.entity.monster.hoglin.Hoglin.class, Hoglin.class);
        map.put(net.minecraft.world.entity.monster.piglin.Piglin.class, Piglin.class);
        map.put(net.minecraft.world.entity.monster.piglin.AbstractPiglin.class, PiglinAbstract.class);
        map.put(net.minecraft.world.entity.monster.piglin.PiglinBrute.class, PiglinBrute.class);
        map.put(net.minecraft.world.entity.monster.Strider.class, Strider.class);
        map.put(net.minecraft.world.entity.monster.Zoglin.class, Zoglin.class);
        map.put(net.minecraft.world.entity.animal.squid.GlowSquid.class, GlowSquid.class);
        map.put(net.minecraft.world.entity.animal.axolotl.Axolotl.class, Axolotl.class);
        map.put(net.minecraft.world.entity.animal.goat.Goat.class, Goat.class);
        map.put(net.minecraft.world.entity.animal.frog.Frog.class, Frog.class);
        map.put(net.minecraft.world.entity.animal.frog.Tadpole.class, Tadpole.class);
        map.put(net.minecraft.world.entity.monster.warden.Warden.class, Warden.class);
        map.put(net.minecraft.world.entity.animal.allay.Allay.class, Allay.class);
        map.put(net.minecraft.world.entity.animal.sniffer.Sniffer.class, Sniffer.class);
        map.put(net.minecraft.world.entity.monster.breeze.Breeze.class, Breeze.class);
        map.put(net.minecraft.world.entity.animal.armadillo.Armadillo.class, Armadillo.class);
        map.put(net.minecraft.world.entity.monster.skeleton.Bogged.class, Bogged.class);
        map.put(net.minecraft.world.entity.monster.creaking.Creaking.class, Creaking.class);
        map.put(net.minecraft.world.entity.animal.AgeableWaterCreature.class, Squid.class);
        map.put(net.minecraft.world.entity.animal.cow.AbstractCow.class, AbstractCow.class);
        map.put(net.minecraft.world.entity.animal.happyghast.HappyGhast.class, HappyGhast.class);
        map.put(net.minecraft.world.entity.animal.golem.CopperGolem.class, CopperGolem.class);
        map.put(net.minecraft.world.entity.animal.nautilus.AbstractNautilus.class, AbstractNautilus.class);
        map.put(net.minecraft.world.entity.animal.nautilus.Nautilus.class, Nautilus.class);
        map.put(net.minecraft.world.entity.animal.nautilus.ZombieNautilus.class, ZombieNautilus.class);
        map.put(net.minecraft.world.entity.animal.camel.CamelHusk.class, CamelHusk.class);
        map.put(net.minecraft.world.entity.monster.skeleton.Parched.class, Parched.class);
        // End generate - MobGoalHelper#BUKKIT_BRIDGE
        //</editor-fold>
    });

    // TODO these kinda should be checked on each release, in case nested classes changes
    private static final Map<String, String> RENAMES = Util.make(new HashMap<>(), map -> {
        map.put("AbstractSkeleton$1", "AbstractSkeletonMelee");

        // remove duplicate
        map.put("TraderLlama$TraderLlamaDefendWanderingTraderGoal", "TraderLlamaDefendWanderingTraderGoal");
        map.put("AbstractIllager$RaiderOpenDoorGoal", "RaiderOpenDoorGoal");
    });

    private static final Set<Class<? extends Mob>> NO_SPECIFIER = Set.of(
        Mob.class,
        Creature.class,
        Animals.class,
        RangedEntity.class,
        Tameable.class,
        Monster.class,
        PufferFish.class // weird case
    );

    private static String getPathName(Class<? extends Mob> type, Class<?> holderClass, String name) {
        String pathName = name.substring(name.lastIndexOf('.') + 1);
        boolean needRename = false;

        // inner classes
        int firstInnerDelimiter = pathName.indexOf('$');
        if (firstInnerDelimiter != -1) {
            String innerClassNames = pathName.substring(firstInnerDelimiter + 1);
            for (String innerClassName : innerClassNames.split("\\$")) {
                if (NumberUtils.isDigits(innerClassName)) {
                    needRename = true;
                    break;
                }
            }
            if (!needRename && !RENAMES.containsKey(pathName)) {
                pathName = innerClassNames;
            }
        }

        if (!RENAMES.containsKey(pathName)) {
            if (needRename) {
                throw new IllegalStateException("need to map " + name + " (" + pathName + ")");
            }
            String prefix = null;
            if (!NO_SPECIFIER.contains(type)) {
                prefix = type.getSimpleName();
            } else if (!net.minecraft.world.entity.Mob.class.isAssignableFrom(holderClass)) {
                prefix = holderClass.getSimpleName();
            }
            if (prefix != null && !pathName.startsWith(prefix)) {
                pathName = prefix + pathName;
            }
        } else {
            pathName = RENAMES.get(pathName);
        }

        pathName = pathName.replace("TargetGoal", ""); // replace last? reverse search?
        pathName = pathName.replace("Goal", "");
        pathName = pathName.replace("Abstract", "");
        pathName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pathName);

        return pathName;
    }

    public static EnumSet<GoalType> vanillaToPaper(Goal goal) {
        EnumSet<GoalType> goals = EnumSet.noneOf(GoalType.class);
        for (GoalType type : GoalType.values()) {
            if (goal.hasFlag(paperToVanilla(type))) {
                goals.add(type);
            }
        }
        return goals;
    }

    public static GoalType vanillaToPaper(Goal.Flag type) {
        return switch (type) {
            case MOVE -> GoalType.MOVE;
            case LOOK -> GoalType.LOOK;
            case JUMP -> GoalType.JUMP;
            case UNKNOWN_BEHAVIOR -> GoalType.UNKNOWN_BEHAVIOR;
            case TARGET -> GoalType.TARGET;
        };
    }

    public static EnumSet<Goal.Flag> paperToVanilla(EnumSet<GoalType> types) {
        EnumSet<Goal.Flag> goals = EnumSet.noneOf(Goal.Flag.class);
        for (GoalType type : types) {
            goals.add(paperToVanilla(type));
        }
        return goals;
    }

    public static Goal.Flag paperToVanilla(GoalType type) {
        return switch (type) {
            case MOVE -> Goal.Flag.MOVE;
            case LOOK -> Goal.Flag.LOOK;
            case JUMP -> Goal.Flag.JUMP;
            case UNKNOWN_BEHAVIOR -> Goal.Flag.UNKNOWN_BEHAVIOR;
            case TARGET -> Goal.Flag.TARGET;
        };
    }

    public static <T extends Mob> GoalKey<T> getKey(Class<? extends Goal> goalClass) {
        Class<T> type = getGenericType(goalClass);

        String name = goalClass.getName();
        if (io.papermc.paper.util.MappingEnvironment.reobf()) {
            name = ObfHelper.INSTANCE.deobfClassName(name);
        }

        Class<?> holderClass = getTopLevelClass(goalClass);
        name = getPathName(type, holderClass, name);
        return GoalKey.of(type, NamespacedKey.minecraft(name));
    }

    private static final Int2BooleanFunction[] VISIBILITY_SEARCH_STEP = {
        Modifier::isPublic,
        Modifier::isProtected,
        mod -> (mod & 0b111) == 0, // package-private
        Modifier::isPrivate,
    };

    private static final Comparator<Constructor<?>> VISIBILITY_ORDER = Comparator.comparingInt(constructor -> {
        int mod = constructor.getModifiers();
        for (int i = 0; i < VISIBILITY_SEARCH_STEP.length; i++) {
            Int2BooleanFunction visibility = VISIBILITY_SEARCH_STEP[i];
            if (visibility.test(mod)) {
                return i;
            }
        }
        throw new UnsupportedOperationException("Unknown visibility: " + mod);
    });

    private static <T extends Mob> Class<T> getGenericType(Class<? extends Goal> goalClass) {
        //noinspection unchecked
        return (Class<T>) GENERIC_TYPE_CACHE.computeIfAbsent(goalClass, key -> {
            Constructor<?>[] constructors = key.getDeclaredConstructors();
            Arrays.sort(constructors, VISIBILITY_ORDER);
            for (Constructor<?> constructor : constructors) {
                for (Class<?> paramType : constructor.getParameterTypes()) {
                    if (net.minecraft.world.entity.Mob.class.isAssignableFrom(paramType)) {
                        //noinspection unchecked
                        return toBukkitClass((Class<? extends net.minecraft.world.entity.Mob>) paramType);
                    } else if (RangedAttackMob.class.isAssignableFrom(paramType)) {
                        return RangedEntity.class;
                    }
                }
            }
            throw new RuntimeException("Can't figure out applicable entity for mob goal " + goalClass); // maybe just return Mob?
        });
    }

    private static Class<?> getTopLevelClass(Class<?> clazz) {
        Class<?> topLevelClass = clazz;
        Class<?> upperClass = clazz;

        while(true) {
            upperClass = upperClass.getEnclosingClass();
            if (upperClass == null) {
                return topLevelClass;
            }

            topLevelClass = upperClass;
        }
    }

    public static Class<? extends Mob> toBukkitClass(Class<? extends net.minecraft.world.entity.Mob> internalClass) {
        Class<? extends Mob> bukkitClass = BUKKIT_BRIDGE.get(internalClass);
        if (bukkitClass == null) {
            throw new RuntimeException("Can't figure out applicable bukkit entity for internal entity " + internalClass); // maybe just return Mob?
        }
        return bukkitClass;
    }
}
