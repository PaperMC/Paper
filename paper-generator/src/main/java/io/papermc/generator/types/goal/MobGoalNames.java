package io.papermc.generator.types.goal;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.google.common.base.CaseFormat;
import io.papermc.generator.utils.Formatting;
import io.papermc.paper.entity.SchoolableFish;
import io.papermc.typewriter.util.ClassHelper;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractCow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creaking;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.HappyGhast;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Tadpole;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Warden;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MobGoalNames { // todo sync with MobGoalHelper ideally this should not be duplicated

    private static final Map<Class<? extends Goal>, Class<? extends Mob>> GENERIC_TYPE_CACHE = new HashMap<>();
    public static final Map<Class<? extends net.minecraft.world.entity.Mob>, Class<? extends Mob>> BUKKIT_BRIDGE = Util.make(new LinkedHashMap<>(), map -> {
        //<editor-fold defaultstate="collapsed" desc="bukkitMap Entities">
        map.put(net.minecraft.world.entity.Mob.class, Mob.class);
        map.put(net.minecraft.world.entity.AgeableMob.class, Ageable.class);
        map.put(net.minecraft.world.entity.ambient.AmbientCreature.class, Ambient.class);
        map.put(net.minecraft.world.entity.animal.Animal.class, Animals.class);
        map.put(net.minecraft.world.entity.ambient.Bat.class, Bat.class);
        map.put(net.minecraft.world.entity.animal.Bee.class, Bee.class);
        map.put(net.minecraft.world.entity.monster.Blaze.class, Blaze.class);
        map.put(net.minecraft.world.entity.animal.Cat.class, Cat.class);
        map.put(net.minecraft.world.entity.monster.CaveSpider.class, CaveSpider.class);
        map.put(net.minecraft.world.entity.animal.Chicken.class, Chicken.class);
        map.put(net.minecraft.world.entity.animal.Cod.class, Cod.class);
        map.put(net.minecraft.world.entity.animal.Cow.class, Cow.class);
        map.put(net.minecraft.world.entity.PathfinderMob.class, Creature.class);
        map.put(net.minecraft.world.entity.monster.Creeper.class, Creeper.class);
        map.put(net.minecraft.world.entity.animal.Dolphin.class, Dolphin.class);
        map.put(net.minecraft.world.entity.monster.Drowned.class, Drowned.class);
        map.put(net.minecraft.world.entity.boss.enderdragon.EnderDragon.class, EnderDragon.class);
        map.put(net.minecraft.world.entity.monster.EnderMan.class, Enderman.class);
        map.put(net.minecraft.world.entity.monster.Endermite.class, Endermite.class);
        map.put(net.minecraft.world.entity.monster.Evoker.class, Evoker.class);
        map.put(net.minecraft.world.entity.animal.AbstractFish.class, Fish.class);
        map.put(net.minecraft.world.entity.animal.AbstractSchoolingFish.class, SchoolableFish.class);
        map.put(net.minecraft.world.entity.animal.Fox.class, Fox.class);
        map.put(net.minecraft.world.entity.monster.Ghast.class, Ghast.class);
        map.put(net.minecraft.world.entity.monster.Giant.class, Giant.class);
        map.put(net.minecraft.world.entity.animal.AbstractGolem.class, Golem.class);
        map.put(net.minecraft.world.entity.monster.Guardian.class, Guardian.class);
        map.put(net.minecraft.world.entity.monster.ElderGuardian.class, ElderGuardian.class);
        map.put(net.minecraft.world.entity.animal.horse.Horse.class, Horse.class);
        map.put(net.minecraft.world.entity.animal.horse.AbstractHorse.class, AbstractHorse.class);
        map.put(net.minecraft.world.entity.animal.horse.AbstractChestedHorse.class, ChestedHorse.class);
        map.put(net.minecraft.world.entity.animal.horse.Donkey.class, Donkey.class);
        map.put(net.minecraft.world.entity.animal.horse.Mule.class, Mule.class);
        map.put(net.minecraft.world.entity.animal.horse.SkeletonHorse.class, SkeletonHorse.class);
        map.put(net.minecraft.world.entity.animal.horse.ZombieHorse.class, ZombieHorse.class);
        map.put(net.minecraft.world.entity.animal.camel.Camel.class, org.bukkit.entity.Camel.class);
        map.put(net.minecraft.world.entity.monster.AbstractIllager.class, Illager.class);
        map.put(net.minecraft.world.entity.monster.Illusioner.class, Illusioner.class);
        map.put(net.minecraft.world.entity.monster.SpellcasterIllager.class, Spellcaster.class);
        map.put(net.minecraft.world.entity.animal.IronGolem.class, IronGolem.class);
        map.put(net.minecraft.world.entity.animal.horse.Llama.class, Llama.class);
        map.put(net.minecraft.world.entity.animal.horse.TraderLlama.class, TraderLlama.class);
        map.put(net.minecraft.world.entity.monster.MagmaCube.class, MagmaCube.class);
        map.put(net.minecraft.world.entity.monster.Monster.class, Monster.class);
        map.put(net.minecraft.world.entity.monster.PatrollingMonster.class, Raider.class); // close enough
        map.put(net.minecraft.world.entity.animal.MushroomCow.class, MushroomCow.class);
        map.put(net.minecraft.world.entity.animal.Ocelot.class, Ocelot.class);
        map.put(net.minecraft.world.entity.animal.Panda.class, Panda.class);
        map.put(net.minecraft.world.entity.animal.Parrot.class, Parrot.class);
        map.put(net.minecraft.world.entity.animal.ShoulderRidingEntity.class, Parrot.class); // close enough
        map.put(net.minecraft.world.entity.monster.Phantom.class, Phantom.class);
        map.put(net.minecraft.world.entity.animal.Pig.class, Pig.class);
        map.put(net.minecraft.world.entity.monster.ZombifiedPiglin.class, PigZombie.class);
        map.put(net.minecraft.world.entity.monster.Pillager.class, Pillager.class);
        map.put(net.minecraft.world.entity.animal.PolarBear.class, PolarBear.class);
        map.put(net.minecraft.world.entity.animal.Pufferfish.class, PufferFish.class);
        map.put(net.minecraft.world.entity.animal.Rabbit.class, Rabbit.class);
        map.put(net.minecraft.world.entity.raid.Raider.class, Raider.class);
        map.put(net.minecraft.world.entity.monster.Ravager.class, Ravager.class);
        map.put(net.minecraft.world.entity.animal.Salmon.class, Salmon.class);
        map.put(net.minecraft.world.entity.animal.sheep.Sheep.class, Sheep.class);
        map.put(net.minecraft.world.entity.monster.Shulker.class, Shulker.class);
        map.put(net.minecraft.world.entity.monster.Silverfish.class, Silverfish.class);
        map.put(net.minecraft.world.entity.monster.Skeleton.class, Skeleton.class);
        map.put(net.minecraft.world.entity.monster.AbstractSkeleton.class, AbstractSkeleton.class);
        map.put(net.minecraft.world.entity.monster.Stray.class, Stray.class);
        map.put(net.minecraft.world.entity.monster.WitherSkeleton.class, WitherSkeleton.class);
        map.put(net.minecraft.world.entity.monster.Slime.class, Slime.class);
        map.put(net.minecraft.world.entity.animal.SnowGolem.class, Snowman.class);
        map.put(net.minecraft.world.entity.monster.Spider.class, Spider.class);
        map.put(net.minecraft.world.entity.animal.Squid.class, Squid.class);
        map.put(net.minecraft.world.entity.TamableAnimal.class, Tameable.class);
        map.put(net.minecraft.world.entity.animal.TropicalFish.class, TropicalFish.class);
        map.put(net.minecraft.world.entity.animal.Turtle.class, Turtle.class);
        map.put(net.minecraft.world.entity.monster.Vex.class, Vex.class);
        map.put(net.minecraft.world.entity.npc.Villager.class, Villager.class);
        map.put(net.minecraft.world.entity.npc.AbstractVillager.class, AbstractVillager.class);
        map.put(net.minecraft.world.entity.npc.WanderingTrader.class, WanderingTrader.class);
        map.put(net.minecraft.world.entity.monster.Vindicator.class, Vindicator.class);
        map.put(net.minecraft.world.entity.animal.WaterAnimal.class, WaterMob.class);
        map.put(net.minecraft.world.entity.monster.Witch.class, Witch.class);
        map.put(net.minecraft.world.entity.boss.wither.WitherBoss.class, Wither.class);
        map.put(net.minecraft.world.entity.animal.wolf.Wolf.class, Wolf.class);
        map.put(net.minecraft.world.entity.monster.Zombie.class, Zombie.class);
        map.put(net.minecraft.world.entity.monster.Husk.class, Husk.class);
        map.put(net.minecraft.world.entity.monster.ZombieVillager.class, ZombieVillager.class);
        map.put(net.minecraft.world.entity.monster.hoglin.Hoglin.class, Hoglin.class);
        map.put(net.minecraft.world.entity.monster.piglin.Piglin.class, Piglin.class);
        map.put(net.minecraft.world.entity.monster.piglin.AbstractPiglin.class, PiglinAbstract.class);
        map.put(net.minecraft.world.entity.monster.piglin.PiglinBrute.class, PiglinBrute.class);
        map.put(net.minecraft.world.entity.monster.Strider.class, Strider.class);
        map.put(net.minecraft.world.entity.monster.Zoglin.class, Zoglin.class);
        map.put(net.minecraft.world.entity.GlowSquid.class, GlowSquid.class);
        map.put(net.minecraft.world.entity.animal.axolotl.Axolotl.class, Axolotl.class);
        map.put(net.minecraft.world.entity.animal.goat.Goat.class, Goat.class);
        map.put(net.minecraft.world.entity.animal.frog.Frog.class, Frog.class);
        map.put(net.minecraft.world.entity.animal.frog.Tadpole.class, Tadpole.class);
        map.put(net.minecraft.world.entity.monster.warden.Warden.class, Warden.class);
        map.put(net.minecraft.world.entity.animal.allay.Allay.class, Allay.class);
        map.put(net.minecraft.world.entity.animal.sniffer.Sniffer.class, Sniffer.class);
        map.put(net.minecraft.world.entity.monster.breeze.Breeze.class, Breeze.class);
        map.put(net.minecraft.world.entity.animal.armadillo.Armadillo.class, Armadillo.class);
        map.put(net.minecraft.world.entity.monster.Bogged.class, Bogged.class);
        map.put(net.minecraft.world.entity.monster.creaking.Creaking.class, Creaking.class);
        map.put(net.minecraft.world.entity.animal.AgeableWaterCreature.class, Squid.class); // close enough
        map.put(net.minecraft.world.entity.animal.AbstractCow.class, AbstractCow.class);
        map.put(net.minecraft.world.entity.animal.HappyGhast.class, HappyGhast.class);
        //</editor-fold>
    });

    // TODO these kinda should be checked on each release, in case nested classes changes
    private static final Map<String, String> NESTED_CLASS_NAMES = Util.make(new HashMap<>(), map -> {
        map.put("AbstractSkeleton$1", "AbstractSkeletonMelee");

        // remove duplicate
        map.put("TraderLlama$TraderLlamaDefendWanderingTraderGoal", "TraderLlamaDefendWanderingTraderGoal");
        map.put("AbstractIllager$RaiderOpenDoorGoal", "RaiderOpenDoorGoal");

        // weird enderman case
        map.put("EnderMan.EndermanFreezeWhenLookedAt", "EndermanFreezeWhenLookedAt");
        map.put("EnderMan.EndermanLeaveBlockGoal", "EndermanLeaveBlockGoal");
        map.put("EnderMan.EndermanTakeBlockGoal", "EndermanTakeBlockGoal");
        map.put("EnderMan.EndermanLookForPlayerGoal", "EndermanLookForPlayerGoal");
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
            if (!needRename && !NESTED_CLASS_NAMES.containsKey(pathName)) {
                pathName = innerClassNames;
            }
        }

        if (!NESTED_CLASS_NAMES.containsKey(pathName)) {
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
            pathName = NESTED_CLASS_NAMES.get(pathName);
        }

        pathName = Formatting.stripWordOfCamelCaseName(pathName, "TargetGoal", true); // replace last? reverse search?
        pathName = Formatting.stripWordOfCamelCaseName(pathName, "Goal", true);
        pathName = Formatting.stripWordOfCamelCaseName(pathName, "Abstract", true);
        pathName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pathName);

        return pathName;
    }

    public static <T extends Mob> GoalKey<T> getKey(Class<? extends Goal> goalClass) {
        Class<T> type = getGenericType(goalClass);
        Class<?> holderClass = ClassHelper.getTopLevelClass(goalClass);
        String name = getPathName(type, holderClass, goalClass.getName());
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
            throw new IllegalStateException("Can't figure out applicable entity for mob goal " + goalClass); // maybe just return Mob?
        });
    }

    private static Class<? extends Mob> toBukkitClass(Class<? extends net.minecraft.world.entity.Mob> internalClass) {
        Class<? extends Mob> bukkitClass = BUKKIT_BRIDGE.get(internalClass);
        if (bukkitClass == null) {
            throw new IllegalStateException("Can't figure out applicable bukkit entity for internal entity " + internalClass); // maybe just return Mob?
        }
        return bukkitClass;
    }
}
