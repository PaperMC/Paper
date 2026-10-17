package io.papermc.generator.rewriter.types.simple;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.types.goal.MobGoalNames;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.preset.model.EnumValue;
import io.papermc.typewriter.util.ClassResolver;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;

import static io.papermc.generator.utils.Formatting.quoted;

public class EntityTypeRewriter extends EnumRegistryRewriter<EntityType<?>> {

    private static final Map<ResourceKey<EntityType<?>>, Class<? extends Entity>> ENTITY_GENERIC_TYPES =
        RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE).getFields(field -> {
            if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1) {
                return (Class<? extends Entity>) complexType.getActualTypeArguments()[0];
            }
            return null;
        });

    private static final Map<String, String> CLASS_RENAMES = ImmutableMap.<String, String>builder()
        .put("ExperienceBottle", "ThrownExpBottle")
        .put("EyeOfEnder", "EnderSignal")
        .put("EndCrystal", "EnderCrystal")
        .put("FireworkRocket", "Firework")
        .put("FishingBobber", "FishHook")
        .put("Fireball", "LargeFireball")
        .put("LeashKnot", "LeashHitch")
        .put("LightningBolt", "LightningStrike")
        .put("Tnt", "TNTPrimed")
        .put("Minecart", "RideableMinecart")
        .put("ChestMinecart", "StorageMinecart")
        .put("CommandBlockMinecart", "CommandMinecart")
        .put("TntMinecart", "ExplosiveMinecart")
        .put("FurnaceMinecart", "PoweredMinecart")
        .buildOrThrow();

    @Deprecated
    private static final Object2IntMap<EntityType<?>> LEGACY_ID = Util.make(new Object2IntOpenHashMap<>(), map -> {
        map.put(EntityTypes.ITEM, 1);
        map.put(EntityTypes.EXPERIENCE_ORB, 2);
        map.put(EntityTypes.AREA_EFFECT_CLOUD, 3);
        map.put(EntityTypes.ELDER_GUARDIAN, 4);
        map.put(EntityTypes.WITHER_SKELETON, 5);
        map.put(EntityTypes.STRAY, 6);
        map.put(EntityTypes.EGG, 7);
        map.put(EntityTypes.LEASH_KNOT, 8);
        map.put(EntityTypes.PAINTING, 9);
        map.put(EntityTypes.ARROW, 10);
        map.put(EntityTypes.SNOWBALL, 11);
        map.put(EntityTypes.FIREBALL, 12);
        map.put(EntityTypes.SMALL_FIREBALL, 13);
        map.put(EntityTypes.ENDER_PEARL, 14);
        map.put(EntityTypes.EYE_OF_ENDER, 15);
        map.put(EntityTypes.SPLASH_POTION, 16);
        map.put(EntityTypes.EXPERIENCE_BOTTLE, 17);
        map.put(EntityTypes.ITEM_FRAME, 18);
        map.put(EntityTypes.WITHER_SKULL, 19);
        map.put(EntityTypes.TNT, 20);
        map.put(EntityTypes.FALLING_BLOCK, 21);
        map.put(EntityTypes.FIREWORK_ROCKET, 22);
        map.put(EntityTypes.HUSK, 23);
        map.put(EntityTypes.SPECTRAL_ARROW, 24);
        map.put(EntityTypes.SHULKER_BULLET, 25);
        map.put(EntityTypes.DRAGON_FIREBALL, 26);
        map.put(EntityTypes.ZOMBIE_VILLAGER, 27);
        map.put(EntityTypes.SKELETON_HORSE, 28);
        map.put(EntityTypes.ZOMBIE_HORSE, 29);
        map.put(EntityTypes.ARMOR_STAND, 30);
        map.put(EntityTypes.DONKEY, 31);
        map.put(EntityTypes.MULE, 32);
        map.put(EntityTypes.EVOKER_FANGS, 33);
        map.put(EntityTypes.EVOKER, 34);
        map.put(EntityTypes.VEX, 35);
        map.put(EntityTypes.VINDICATOR, 36);
        map.put(EntityTypes.ILLUSIONER, 37);

        map.put(EntityTypes.COMMAND_BLOCK_MINECART, 40);
        map.put(EntityTypes.MINECART, 42);
        map.put(EntityTypes.CHEST_MINECART, 43);
        map.put(EntityTypes.FURNACE_MINECART, 44);
        map.put(EntityTypes.TNT_MINECART, 45);
        map.put(EntityTypes.HOPPER_MINECART, 46);
        map.put(EntityTypes.SPAWNER_MINECART, 47);

        map.put(EntityTypes.CREEPER, 50);
        map.put(EntityTypes.SKELETON, 51);
        map.put(EntityTypes.SPIDER, 52);
        map.put(EntityTypes.GIANT, 53);
        map.put(EntityTypes.ZOMBIE, 54);
        map.put(EntityTypes.SLIME, 55);
        map.put(EntityTypes.GHAST, 56);
        map.put(EntityTypes.ZOMBIFIED_PIGLIN, 57);
        map.put(EntityTypes.ENDERMAN, 58);
        map.put(EntityTypes.CAVE_SPIDER, 59);
        map.put(EntityTypes.SILVERFISH, 60);
        map.put(EntityTypes.BLAZE, 61);
        map.put(EntityTypes.MAGMA_CUBE, 62);
        map.put(EntityTypes.ENDER_DRAGON, 63);
        map.put(EntityTypes.WITHER, 64);
        map.put(EntityTypes.BAT, 65);
        map.put(EntityTypes.WITCH, 66);
        map.put(EntityTypes.ENDERMITE, 67);
        map.put(EntityTypes.GUARDIAN, 68);
        map.put(EntityTypes.SHULKER, 69);

        map.put(EntityTypes.PIG, 90);
        map.put(EntityTypes.SHEEP, 91);
        map.put(EntityTypes.COW, 92);
        map.put(EntityTypes.CHICKEN, 93);
        map.put(EntityTypes.SQUID, 94);
        map.put(EntityTypes.WOLF, 95);
        map.put(EntityTypes.MOOSHROOM, 96);
        map.put(EntityTypes.SNOW_GOLEM, 97);
        map.put(EntityTypes.OCELOT, 98);
        map.put(EntityTypes.IRON_GOLEM, 99);
        map.put(EntityTypes.HORSE, 100);
        map.put(EntityTypes.RABBIT, 101);
        map.put(EntityTypes.POLAR_BEAR, 102);
        map.put(EntityTypes.LLAMA, 103);
        map.put(EntityTypes.LLAMA_SPIT, 104);
        map.put(EntityTypes.PARROT, 105);

        map.put(EntityTypes.VILLAGER, 120);

        map.put(EntityTypes.END_CRYSTAL, 200);
    });

    private static final ClassResolver runtime = new ClassResolver(EntityTypeRewriter.class.getClassLoader());

    public EntityTypeRewriter() {
        super(Registries.ENTITY_TYPE, false);
    }

    @Override
    protected EnumValue.Builder rewriteEnumValue(Holder.Reference<EntityType<?>> reference) {
        String path = reference.key().identifier().getPath();
        List<String> arguments = new ArrayList<>(4);
        arguments.add(quoted(path));
        arguments.add(toBukkitClass(reference).concat(".class"));
        arguments.add(Integer.toString(LEGACY_ID.getOrDefault(reference.value(), -1)));

        if (!reference.value().canSummon()) {
            arguments.add(Boolean.FALSE.toString());
        }
        return super.rewriteEnumValue(reference).arguments(arguments);
    }

    private String toBukkitClass(Holder.Reference<EntityType<?>> reference) {
        Class<? extends Entity> internalClass = ENTITY_GENERIC_TYPES.get(reference.key());
        if (Mob.class.isAssignableFrom(internalClass)) {
            return this.importCollector.getShortName(MobGoalNames.BUKKIT_BRIDGE.get((Class<? extends Mob>) internalClass));
        }

        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, reference.key().identifier().getPath()); // use the key instead of the internal class name since name match a bit more
        ClassNamed resolvedClass = this.classNamedView.tryFindFirst(CLASS_RENAMES.getOrDefault(className, className))
            .orElseThrow(() -> new IllegalStateException("Could not find entity class for " + reference.key().identifier()))
            .resolve(runtime);
        Preconditions.checkArgument(org.bukkit.entity.Entity.class.isAssignableFrom(resolvedClass.knownClass()), "Generic type must be an entity");
        return this.importCollector.getShortName(resolvedClass);
    }
}
