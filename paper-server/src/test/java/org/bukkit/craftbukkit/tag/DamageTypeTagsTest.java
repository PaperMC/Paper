package org.bukkit.craftbukkit.tag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.VanillaFeature;
import org.bukkit.tag.DamageTypeTags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link DamageTypeTags} requires every tag to exist, so this fails when a Minecraft update drops one of the vanilla
 * tag files (like {@code bypasses_cooldown} in 1.21.2) instead of the class failing to initialize on a server.
 */
@VanillaFeature
class DamageTypeTagsTest {

    static Stream<String> tagFields() {
        return Arrays.stream(DamageTypeTags.class.getFields())
            .filter(field -> Modifier.isStatic(field.getModifiers()) && Tag.class.isAssignableFrom(field.getType()))
            .map(Field::getName);
    }

    @ParameterizedTest
    @MethodSource("tagFields")
    void testTagExistsInVanilla(final String fieldName) throws ReflectiveOperationException {
        final Tag<?> tag = (Tag<?>) DamageTypeTags.class.getField(fieldName).get(null);
        final TagKey<DamageType> tagKey = TagKey.create(Registries.DAMAGE_TYPE, CraftNamespacedKey.toMinecraft(tag.getKey()));
        assertTrue(
            RegistryHelper.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).get(tagKey).isPresent(),
            () -> "Missing vanilla damage type tag " + tagKey.location() + " for DamageTypeTags." + fieldName
        );
    }
}
