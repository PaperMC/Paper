package io.papermc.generator.types.goal;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.google.common.base.CaseFormat;
import io.papermc.generator.utils.Formatting;
import io.papermc.paper.entity.SchoolableFish;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MobGoalNames { // todo sync with MobGoalHelper ideally this should not be duplicated

    private static final Map<Class<? extends Goal>, Class<? extends Mob>> entityClassCache = new HashMap<>();
    public static final Map<Class<? extends net.minecraft.world.entity.Mob>, Class<? extends Mob>> bukkitMap = new LinkedHashMap<>();

    static {
        //<editor-fold defaultstate="collapsed" desc="bukkitMap Entities">
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
        bukkitMap.put(net.minecraft.world.entity.animal.camel.Camel.class, org.bukkit.entity.Camel.class);
        bukkitMap.put(net.minecraft.world.entity.monster.AbstractIllager.class, Illager.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Illusioner.class, Illusioner.class);
        bukkitMap.put(net.minecraft.world.entity.monster.SpellcasterIllager.class, Spellcaster.class);
        bukkitMap.put(net.minecraft.world.entity.animal.IronGolem.class, IronGolem.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.Llama.class, Llama.class);
        bukkitMap.put(net.minecraft.world.entity.animal.horse.TraderLlama.class, TraderLlama.class);
        bukkitMap.put(net.minecraft.world.entity.monster.MagmaCube.class, MagmaCube.class);
        bukkitMap.put(net.minecraft.world.entity.monster.Monster.class, Monster.class);
        bukkitMap.put(net.minecraft.world.entity.monster.PatrollingMonster.class, Raider.class); // close enough
        bukkitMap.put(net.minecraft.world.entity.animal.MushroomCow.class, MushroomCow.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Ocelot.class, Ocelot.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Panda.class, Panda.class);
        bukkitMap.put(net.minecraft.world.entity.animal.Parrot.class, Parrot.class);
        bukkitMap.put(net.minecraft.world.entity.animal.ShoulderRidingEntity.class, Parrot.class); // close enough
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
        bukkitMap.put(net.minecraft.world.entity.animal.AgeableWaterCreature.class, Squid.class); // close enough
        bukkitMap.put(net.minecraft.world.entity.animal.AbstractCow.class, AbstractCow.class);
        bukkitMap.put(net.minecraft.world.entity.animal.HappyGhast.class, HappyGhast.class);
        //</editor-fold>
    }

    private static final Map<String, String> deobfuscationMap = new HashMap<>();

    static {
        // TODO these kinda should be checked on each release, in case obfuscation changes
        deobfuscationMap.put("abstract_skeleton_1", "abstract_skeleton_melee");
    }

    private static String getPathName(String name) {
        String pathName = name.substring(name.lastIndexOf('.') + 1);
        boolean needDeobfMap = false;

        // inner classes
        int firstInnerDelimiter = pathName.indexOf('$');
        if (firstInnerDelimiter != -1) {
            String innerClassName = pathName.substring(firstInnerDelimiter + 1);
            for (String nestedClass : innerClassName.split("\\$")) {
                if (NumberUtils.isDigits(nestedClass)) {
                    needDeobfMap = true;
                    break;
                }
            }
            if (!needDeobfMap) {
                pathName = innerClassName;
            }
            pathName = pathName.replace('$', '_');
            // mapped, wooo!
        }

        pathName = Formatting.stripWordOfCamelCaseName(pathName, "TargetGoal", true); // replace last? reverse search?
        pathName = Formatting.stripWordOfCamelCaseName(pathName, "Goal", true);
        pathName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pathName);

        if (needDeobfMap && !deobfuscationMap.containsKey(pathName)) {
            System.err.println("need to map " + name + " (" + pathName + ")");
        }

        // did we rename this key?
        return deobfuscationMap.getOrDefault(pathName, pathName);
    }

    public static <T extends Mob> GoalKey<T> getKey(Class<? extends Goal> goalClass) {
        String name = getPathName(goalClass.getName());
        return GoalKey.of(getEntity(goalClass), NamespacedKey.minecraft(name));
    }

    private static <T extends Mob> Class<T> getEntity(Class<? extends Goal> goalClass) {
        //noinspection unchecked
        return (Class<T>) entityClassCache.computeIfAbsent(goalClass, key -> {
            for (Constructor<?> ctor : key.getDeclaredConstructors()) {
                for (Class<?> param : ctor.getParameterTypes()) {
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

    private static Class<? extends Mob> toBukkitClass(Class<? extends net.minecraft.world.entity.Mob> nmsClass) {
        Class<? extends Mob> bukkitClass = bukkitMap.get(nmsClass);
        if (bukkitClass == null) {
            throw new RuntimeException("Can't figure out applicable bukkit entity for nms entity " + nmsClass); // maybe just return Mob?
        }
        return bukkitClass;
    }
}
