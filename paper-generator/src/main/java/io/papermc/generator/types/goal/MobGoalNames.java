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
import net.minecraft.util.Util;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractCow;
import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractNautilus;
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
import org.bukkit.entity.Camel;
import org.bukkit.entity.CamelHusk;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.CopperGolem;
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
import org.bukkit.entity.Nautilus;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parched;
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
import org.bukkit.entity.SulfurCube;
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
import org.bukkit.entity.ZombieNautilus;
import org.bukkit.entity.ZombieVillager;

public final class MobGoalNames { // todo sync with MobGoalHelper ideally this should not be duplicated

    public static final Map<Class<? extends net.minecraft.world.entity.Mob>, Class<? extends Mob>> BUKKIT_BRIDGE = Util.make(new LinkedHashMap<>(), map -> {
        //<editor-fold defaultstate="collapsed" desc="bukkitMap Entities">
        map.put(net.minecraft.world.entity.Mob.class, Mob.class);
        map.put(AgeableMob.class, Ageable.class);
        map.put(AmbientCreature.class, Ambient.class);
        map.put(Animal.class, Animals.class);
        map.put(net.minecraft.world.entity.ambient.Bat.class, Bat.class);
        map.put(net.minecraft.world.entity.animal.bee.Bee.class, Bee.class);
        map.put(net.minecraft.world.entity.monster.Blaze.class, Blaze.class);
        map.put(net.minecraft.world.entity.animal.feline.Cat.class, Cat.class);
        map.put(net.minecraft.world.entity.monster.spider.CaveSpider.class, CaveSpider.class);
        map.put(net.minecraft.world.entity.animal.chicken.Chicken.class, Chicken.class);
        map.put(net.minecraft.world.entity.animal.fish.Cod.class, Cod.class);
        map.put(net.minecraft.world.entity.animal.cow.Cow.class, Cow.class);
        map.put(PathfinderMob.class, Creature.class);
        map.put(net.minecraft.world.entity.monster.Creeper.class, Creeper.class);
        map.put(net.minecraft.world.entity.animal.dolphin.Dolphin.class, Dolphin.class);
        map.put(net.minecraft.world.entity.monster.zombie.Drowned.class, Drowned.class);
        map.put(net.minecraft.world.entity.boss.enderdragon.EnderDragon.class, EnderDragon.class);
        map.put(net.minecraft.world.entity.monster.Enderman.class, Enderman.class);
        map.put(net.minecraft.world.entity.monster.Endermite.class, Endermite.class);
        map.put(net.minecraft.world.entity.monster.illager.Evoker.class, Evoker.class);
        map.put(AbstractFish.class, Fish.class);
        map.put(AbstractSchoolingFish.class, SchoolableFish.class);
        map.put(net.minecraft.world.entity.animal.fox.Fox.class, Fox.class);
        map.put(net.minecraft.world.entity.monster.Ghast.class, Ghast.class);
        map.put(net.minecraft.world.entity.monster.Giant.class, Giant.class);
        map.put(AbstractGolem.class, Golem.class);
        map.put(net.minecraft.world.entity.monster.Guardian.class, Guardian.class);
        map.put(net.minecraft.world.entity.monster.ElderGuardian.class, ElderGuardian.class);
        map.put(net.minecraft.world.entity.animal.equine.Horse.class, Horse.class);
        map.put(net.minecraft.world.entity.animal.equine.AbstractHorse.class, AbstractHorse.class);
        map.put(AbstractChestedHorse.class, ChestedHorse.class);
        map.put(net.minecraft.world.entity.animal.equine.Donkey.class, Donkey.class);
        map.put(net.minecraft.world.entity.animal.equine.Mule.class, Mule.class);
        map.put(net.minecraft.world.entity.animal.equine.SkeletonHorse.class, SkeletonHorse.class);
        map.put(net.minecraft.world.entity.animal.equine.ZombieHorse.class, ZombieHorse.class);
        map.put(net.minecraft.world.entity.animal.camel.Camel.class, Camel.class);
        map.put(AbstractIllager.class, Illager.class);
        map.put(net.minecraft.world.entity.monster.illager.Illusioner.class, Illusioner.class);
        map.put(SpellcasterIllager.class, Spellcaster.class);
        map.put(net.minecraft.world.entity.animal.golem.IronGolem.class, IronGolem.class);
        map.put(net.minecraft.world.entity.animal.equine.Llama.class, Llama.class);
        map.put(net.minecraft.world.entity.animal.equine.TraderLlama.class, TraderLlama.class);
        map.put(net.minecraft.world.entity.monster.cubemob.MagmaCube.class, MagmaCube.class);
        map.put(net.minecraft.world.entity.monster.Monster.class, Monster.class);
        map.put(PatrollingMonster.class, Raider.class); // close enough
        map.put(net.minecraft.world.entity.animal.cow.MushroomCow.class, MushroomCow.class);
        map.put(net.minecraft.world.entity.animal.feline.Ocelot.class, Ocelot.class);
        map.put(net.minecraft.world.entity.animal.panda.Panda.class, Panda.class);
        map.put(net.minecraft.world.entity.animal.parrot.Parrot.class, Parrot.class);
        map.put(ShoulderRidingEntity.class, Parrot.class); // close enough
        map.put(net.minecraft.world.entity.monster.Phantom.class, Phantom.class);
        map.put(net.minecraft.world.entity.animal.pig.Pig.class, Pig.class);
        map.put(ZombifiedPiglin.class, PigZombie.class);
        map.put(net.minecraft.world.entity.monster.illager.Pillager.class, Pillager.class);
        map.put(net.minecraft.world.entity.animal.polarbear.PolarBear.class, PolarBear.class);
        map.put(Pufferfish.class, PufferFish.class);
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
        map.put(net.minecraft.world.entity.monster.cubemob.Slime.class, Slime.class);
        map.put(SnowGolem.class, Snowman.class);
        map.put(net.minecraft.world.entity.monster.spider.Spider.class, Spider.class);
        map.put(net.minecraft.world.entity.animal.squid.Squid.class, Squid.class);
        map.put(TamableAnimal.class, Tameable.class);
        map.put(net.minecraft.world.entity.animal.fish.TropicalFish.class, TropicalFish.class);
        map.put(net.minecraft.world.entity.animal.turtle.Turtle.class, Turtle.class);
        map.put(net.minecraft.world.entity.monster.Vex.class, Vex.class);
        map.put(net.minecraft.world.entity.npc.villager.Villager.class, Villager.class);
        map.put(net.minecraft.world.entity.npc.villager.AbstractVillager.class, AbstractVillager.class);
        map.put(net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader.class, WanderingTrader.class);
        map.put(net.minecraft.world.entity.monster.illager.Vindicator.class, Vindicator.class);
        map.put(WaterAnimal.class, WaterMob.class);
        map.put(net.minecraft.world.entity.monster.Witch.class, Witch.class);
        map.put(WitherBoss.class, Wither.class);
        map.put(net.minecraft.world.entity.animal.wolf.Wolf.class, Wolf.class);
        map.put(net.minecraft.world.entity.monster.zombie.Zombie.class, Zombie.class);
        map.put(net.minecraft.world.entity.monster.zombie.Husk.class, Husk.class);
        map.put(net.minecraft.world.entity.monster.zombie.ZombieVillager.class, ZombieVillager.class);
        map.put(net.minecraft.world.entity.monster.hoglin.Hoglin.class, Hoglin.class);
        map.put(net.minecraft.world.entity.monster.piglin.Piglin.class, Piglin.class);
        map.put(AbstractPiglin.class, PiglinAbstract.class);
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
        map.put(AgeableWaterCreature.class, Squid.class); // close enough
        map.put(net.minecraft.world.entity.animal.cow.AbstractCow.class, AbstractCow.class);
        map.put(net.minecraft.world.entity.animal.happyghast.HappyGhast.class, HappyGhast.class);
        map.put(net.minecraft.world.entity.animal.golem.CopperGolem.class, CopperGolem.class);
        map.put(net.minecraft.world.entity.animal.nautilus.AbstractNautilus.class, AbstractNautilus.class);
        map.put(net.minecraft.world.entity.animal.nautilus.Nautilus.class, Nautilus.class);
        map.put(net.minecraft.world.entity.animal.nautilus.ZombieNautilus.class, ZombieNautilus.class);
        map.put(net.minecraft.world.entity.animal.camel.CamelHusk.class, CamelHusk.class);
        map.put(net.minecraft.world.entity.monster.skeleton.Parched.class, Parched.class);
        map.put(net.minecraft.world.entity.monster.cubemob.SulfurCube.class, SulfurCube.class);
        map.put(net.minecraft.world.entity.monster.cubemob.AbstractCubeMob.class, AbstractCubeMob.class);
        //</editor-fold>
    });

    private static final Map<Class<? extends Goal>, Class<? extends Mob>> GENERIC_TYPE_CACHE = new HashMap<>();

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
        PufferFish.class, // weird case
        AbstractCubeMob.class
    );

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

    private MobGoalNames() {
    }
}
