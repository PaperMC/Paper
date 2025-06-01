package com.destroystokyo.paper.entity.ai;

import com.destroystokyo.paper.entity.RangedEntity;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.papermc.paper.entity.SchoolableFish;
import io.papermc.paper.util.ObfHelper;
import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;

public class MobGoalHelper {

    private static final BiMap<String, String> deobfuscationMap = HashBiMap.create();
    private static final Map<Class<? extends Goal>, Class<? extends Mob>> entityClassCache = new HashMap<>();
    private static final Map<Class<? extends net.minecraft.world.entity.Mob>, Class<? extends Mob>> bukkitMap = new HashMap<>();

    static final Set<String> ignored = new HashSet<>();

    static {
        // TODO these kinda should be checked on each release, in case obfuscation changes
        deobfuscationMap.put("abstract_skeleton_1", "abstract_skeleton_melee");

        ignored.add("goal_selector_1");
        ignored.add("goal_selector_2");
        ignored.add("selector_1");
        ignored.add("selector_2");
        ignored.add("wrapped");

        //<editor-fold defaultstate="collapsed" desc="bukkitMap Entities">
        // Start generate - MobGoalHelper#bukkitMap
        // @GeneratedFrom 1.21.6-pre1
        bukkitMap.put(net.minecraft.world.entity.Mob.class, Mob.class);
        bukkitMap.put(net.minecraft.world.entity.AgeableMob.class, Ageable.class);
        bukkitMap.put(net.minecraft.world.entity.ambient.AmbientCreature.class, Ambient.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Animal.class, Animals.class);
        bukkitMap.put(net.minecraft.world.entity.ambient.Bat.class, Bat.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Bee.class, Bee.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Blaze.class, Blaze.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Cat.class, Cat.class);
        bukkitMap.put(net.minecraft.world.entity.monster.CaveSpider.class, CaveSpider.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Chicken.class, Chicken.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Cod.class, Cod.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Cow.class, Cow.class);
        bukkitMap.put(net.minecraft.world.entity.PathfinderMob.class, Creature.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Creeper.class, Creeper.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Dolphin.class, Dolphin.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Drowned.class, Drowned.class);
        bukkitMap.put(net.minecraft.world.entity.boss.enderdragon.EnderDragon.class, EnderDragon.class);
        bukkitMap.put(net.minecraft.world.entity.monster.EnderMan.class, Enderman.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Endermite.class, Endermite.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Evoker.class, Evoker.class);
        bukkitMap.put(net.minecraft.world.entity.animal.AbstractFish.class, Fish.class);
        bukkitMap.put(net.minecraft.world.entity.animal.AbstractSchoolingFish.class, SchoolableFish.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Fox.class, Fox.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Ghast.class, Ghast.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Giant.class, Giant.class);
        bukkitMap.put(net.minecraft.world.entity.animal.AbstractGolem.class, Golem.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Guardian.class, Guardian.class);
        bukkitMap.put(net.minecraft.world.entity.monster.ElderGuardian.class, ElderGuardian.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.Horse.class, Horse.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.AbstractHorse.class, AbstractHorse.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.AbstractChestedHorse.class, ChestedHorse.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.Donkey.class, Donkey.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.Mule.class, Mule.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.SkeletonHorse.class, SkeletonHorse.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.ZombieHorse.class, ZombieHorse.class);
        bukkitMap.put(net.minecraft.world.entity.animal.camel.Camel.class, Camel.class);
        bukkitMap.put(net.minecraft.world.entity.monster.AbstractIllager.class, Illager.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Illusioner.class, Illusioner.class);
        bukkitMap.put(net.minecraft.world.entity.monster.SpellcasterIllager.class, Spellcaster.class);
        bukkitMap.put(net.minecraft.world.entity.animal.IronGolem.class, IronGolem.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.Llama.class, Llama.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.TraderLlama.class, TraderLlama.class);
        bukkitMap.put(net.minecraft.world.entity.monster.MagmaCube.class, MagmaCube.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Monster.class, Monster.class);
        bukkitMap.put(net.minecraft.world.entity.monster.PatrollingMonster.class, Raider.class);
        bukkitMap.put(net.minecraft.world.entity.animal.MushroomCow.class, MushroomCow.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Ocelot.class, Ocelot.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Panda.class, Panda.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Parrot.class, Parrot.class);
        bukkitMap.put(net.minecraft.world.entity.animal.ShoulderRidingEntity.class, Parrot.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Phantom.class, Phantom.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Pig.class, Pig.class);
        bukkitMap.put(net.minecraft.world.entity.monster.ZombifiedPiglin.class, PigZombie.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Pillager.class, Pillager.class);
        bukkitMap.put(net.minecraft.world.entity.animal.PolarBear.class, PolarBear.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Pufferfish.class, PufferFish.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Rabbit.class, Rabbit.class);
        bukkitMap.put(net.minecraft.world.entity.raid.Raider.class, Raider.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Ravager.class, Ravager.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Salmon.class, Salmon.class);
        bukkitMap.put(net.minecraft.world.entity.animal.sheep.Sheep.class, Sheep.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Shulker.class, Shulker.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Silverfish.class, Silverfish.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Skeleton.class, Skeleton.class);
        bukkitMap.put(net.minecraft.world.entity.monster.AbstractSkeleton.class, AbstractSkeleton.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Stray.class, Stray.class);
        bukkitMap.put(net.minecraft.world.entity.monster.WitherSkeleton.class, WitherSkeleton.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Slime.class, Slime.class);
        bukkitMap.put(net.minecraft.world.entity.animal.SnowGolem.class, Snowman.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Spider.class, Spider.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Squid.class, Squid.class);
        bukkitMap.put(net.minecraft.world.entity.TamableAnimal.class, Tameable.class);
        bukkitMap.put(net.minecraft.world.entity.animal.TropicalFish.class, TropicalFish.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Turtle.class, Turtle.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Vex.class, Vex.class);
        bukkitMap.put(net.minecraft.world.entity.npc.Villager.class, Villager.class);
        bukkitMap.put(net.minecraft.world.entity.npc.AbstractVillager.class, AbstractVillager.class);
        bukkitMap.put(net.minecraft.world.entity.npc.WanderingTrader.class, WanderingTrader.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Vindicator.class, Vindicator.class);
        bukkitMap.put(net.minecraft.world.entity.animal.WaterAnimal.class, WaterMob.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Witch.class, Witch.class);
        bukkitMap.put(net.minecraft.world.entity.boss.wither.WitherBoss.class, Wither.class);
        bukkitMap.put(net.minecraft.world.entity.animal.wolf.Wolf.class, Wolf.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Zombie.class, Zombie.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Husk.class, Husk.class);
        bukkitMap.put(net.minecraft.world.entity.monster.ZombieVillager.class, ZombieVillager.class);
        bukkitMap.put(net.minecraft.world.entity.monster.hoglin.Hoglin.class, Hoglin.class);
        bukkitMap.put(net.minecraft.world.entity.monster.piglin.Piglin.class, Piglin.class);
        bukkitMap.put(net.minecraft.world.entity.monster.piglin.AbstractPiglin.class, PiglinAbstract.class);
        bukkitMap.put(net.minecraft.world.entity.monster.piglin.PiglinBrute.class, PiglinBrute.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Strider.class, Strider.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Zoglin.class, Zoglin.class);
        bukkitMap.put(net.minecraft.world.entity.GlowSquid.class, GlowSquid.class);
        bukkitMap.put(net.minecraft.world.entity.animal.axolotl.Axolotl.class, Axolotl.class);
        bukkitMap.put(net.minecraft.world.entity.animal.goat.Goat.class, Goat.class);
        bukkitMap.put(net.minecraft.world.entity.animal.frog.Frog.class, Frog.class);
        bukkitMap.put(net.minecraft.world.entity.animal.frog.Tadpole.class, Tadpole.class);
        bukkitMap.put(net.minecraft.world.entity.monster.warden.Warden.class, Warden.class);
        bukkitMap.put(net.minecraft.world.entity.animal.allay.Allay.class, Allay.class);
        bukkitMap.put(net.minecraft.world.entity.animal.sniffer.Sniffer.class, Sniffer.class);
        bukkitMap.put(net.minecraft.world.entity.monster.breeze.Breeze.class, Breeze.class);
        bukkitMap.put(net.minecraft.world.entity.animal.armadillo.Armadillo.class, Armadillo.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Bogged.class, Bogged.class);
        bukkitMap.put(net.minecraft.world.entity.monster.creaking.Creaking.class, Creaking.class);
        bukkitMap.put(net.minecraft.world.entity.animal.AgeableWaterCreature.class, Squid.class);
        bukkitMap.put(net.minecraft.world.entity.animal.AbstractCow.class, AbstractCow.class);
        bukkitMap.put(net.minecraft.world.entity.animal.HappyGhast.class, HappyGhast.class);
        // End generate - MobGoalHelper#bukkitMap
        //</editor-fold>
    }

    public static String getUsableName(Class<?> clazz) {
        String name = io.papermc.paper.util.MappingEnvironment.reobf() ? ObfHelper.INSTANCE.deobfClassName(clazz.getName()) : clazz.getName();
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
        name = name.replace("TargetGoal", "");
        name = name.replace("Goal", "");
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

        if (flag && !deobfuscationMap.containsKey(name.toLowerCase(java.util.Locale.ROOT)) && !ignored.contains(name)) {
            System.out.println("need to map " + clazz.getName() + " (" + name.toLowerCase(java.util.Locale.ROOT) + ")");
        }

        // did we rename this key?
        return deobfuscationMap.getOrDefault(name, name);
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

    public static EnumSet<Goal.Flag> paperToVanilla(EnumSet<GoalType> types) {
        EnumSet<Goal.Flag> goals = EnumSet.noneOf(Goal.Flag.class);
        for (GoalType type : types) {
            goals.add(paperToVanilla(type));
        }
        return goals;
    }

    public static Goal.Flag paperToVanilla(GoalType type) {
        switch (type) {
            case MOVE:
                return Goal.Flag.MOVE;
            case LOOK:
                return Goal.Flag.LOOK;
            case JUMP:
                return Goal.Flag.JUMP;
            case UNKNOWN_BEHAVIOR:
                return Goal.Flag.UNKNOWN_BEHAVIOR;
            case TARGET:
                return Goal.Flag.TARGET;
            default:
                throw new IllegalArgumentException("Unknown paper mob goal type " + type.name());
        }
    }

    public static <T extends Mob> GoalKey<T> getKey(Class<? extends Goal> goalClass) {
        String name = getUsableName(goalClass);
        if (ignored.contains(name)) {
            //noinspection unchecked
            return (GoalKey<T>) GoalKey.of(Mob.class, NamespacedKey.minecraft(name));
        }
        return GoalKey.of(getEntity(goalClass), NamespacedKey.minecraft(name));
    }

    public static <T extends Mob> Class<T> getEntity(Class<? extends Goal> goalClass) {
        //noinspection unchecked
        return (Class<T>) entityClassCache.computeIfAbsent(goalClass, key -> {
            for (Constructor<?> ctor : key.getDeclaredConstructors()) {
                for (int i = 0; i < ctor.getParameterCount(); i++) {
                    Class<?> param = ctor.getParameterTypes()[i];
                    if (net.minecraft.world.entity.Mob.class.isAssignableFrom(param)) {
                        //noinspection unchecked
                        return toBukkitClass((Class<? extends net.minecraft.world.entity.Mob>) param);
                    } else if (RangedAttackMob.class.isAssignableFrom(param)) {
                        return RangedEntity.class;
                    }
                }
            }
            throw new RuntimeException("Can't figure out applicable entity for mob goal " + goalClass); // maybe just return Mob?
        });
    }

    public static Class<? extends Mob> toBukkitClass(Class<? extends net.minecraft.world.entity.Mob> nmsClass) {
        Class<? extends Mob> bukkitClass = bukkitMap.get(nmsClass);
        if (bukkitClass == null) {
            throw new RuntimeException("Can't figure out applicable bukkit entity for nms entity " + nmsClass); // maybe just return Mob?
        }
        return bukkitClass;
    }
}
