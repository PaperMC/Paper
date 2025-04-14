package org.bukkit.craftbukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractCow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.AbstractWindCharge;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Display;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.SizedFireball;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.WaterMob;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@AllFeatures
public class EntityTypesTest {

    private static final URI BUKKIT_CLASSES;
    // Entity classes, which do not have any entity type / entity type data
    private static final List<Class<? extends Entity>> EXCLUDE = Arrays.asList(
            AbstractArrow.class,
            AbstractCow.class,
            AbstractHorse.class,
            AbstractSkeleton.class,
            AbstractVillager.class,
            AbstractWindCharge.class,
            Ageable.class,
            Ambient.class,
            Animals.class,
            Boat.class,
            Boss.class,
            Breedable.class,
            ChestBoat.class,
            ChestedHorse.class,
            ComplexEntityPart.class,
            ComplexLivingEntity.class,
            Creature.class,
            Damageable.class,
            Display.class,
            EnderDragonPart.class,
            Enemy.class,
            Entity.class,
            Explosive.class,
            Fireball.class,
            Fish.class,
            Flying.class,
            Golem.class,
            Hanging.class,
            HumanEntity.class,
            Illager.class,
            LivingEntity.class,
            Minecart.class,
            Mob.class,
            Monster.class,
            NPC.class,
            PiglinAbstract.class,
            Projectile.class,
            Raider.class,
            Sittable.class,
            SizedFireball.class,
            Spellcaster.class,
            Steerable.class,
            Tameable.class,
            ThrowableProjectile.class,
            ThrownPotion.class,
            TippedArrow.class,
            Vehicle.class,
            WaterMob.class
    );

    static {
        try {
            BUKKIT_CLASSES = Entity.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static JarFile jarFile = null;

    public static Stream<Arguments> excludedData() {
        return EntityTypesTest.EXCLUDE.stream().map(Arguments::arguments);
    }

    public static Stream<Arguments> data() {
        return EntityTypesTest.jarFile
                .stream()
                .map(ZipEntry::getName)
                .filter(name -> name.endsWith(".class"))
                .filter(name -> name.startsWith("org/bukkit/entity"))
                .map(name -> name.substring(0, name.length() - ".class".length()))
                .map(name -> name.replace('/', '.'))
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Entity.class::isAssignableFrom)
                .filter(clazz -> !EntityTypesTest.EXCLUDE.contains(clazz))
                .map(Arguments::arguments);
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        EntityTypesTest.jarFile = new JarFile(new File(EntityTypesTest.BUKKIT_CLASSES));
    }

    @ParameterizedTest
    @MethodSource("excludedData")
    public <T extends Entity> void testExcludedClass(Class<T> clazz) {
        CraftEntityTypes.EntityTypeData<?, ?> entityTypeData = CraftEntityTypes.getEntityTypeData(clazz);
        assertNull(entityTypeData, String.format("Class %s is marked as excluded, because it does not have an entity type data, but we found one entity type data, something is not adding up.", clazz));
    }

    @ParameterizedTest
    @MethodSource("data")
    public <T extends Entity> void testEntityClass(Class<T> clazz) {
        CraftEntityTypes.EntityTypeData<?, ?> entityTypeData = CraftEntityTypes.getEntityTypeData(clazz);
        assertNotNull(entityTypeData, String.format("Class %s does not have an entity type data, please add on to CraftEntityTypes or mark the class as excluded in EntityTypesTest, if the class does not have an entity type.", clazz));
    }

    @ParameterizedTest
    @EnumSource(value = EntityType.class, names = "UNKNOWN", mode = EnumSource.Mode.EXCLUDE)
    public void testEntityType(EntityType entityType) {
        CraftEntityTypes.EntityTypeData<?, ?> entityTypeData = CraftEntityTypes.getEntityTypeData(entityType);
        assertNotNull(entityTypeData, String.format("Entity type %s does not have an entity type data, please add on to CraftEntityTypes.", entityType));
    }

    @AfterAll
    public static void clear() throws IOException {
        if (EntityTypesTest.jarFile != null) {
            EntityTypesTest.jarFile.close();
        }
    }
}
