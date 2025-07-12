package io.papermc.paper.registry;

import com.destroystokyo.paper.MaterialTags;
import io.papermc.paper.tag.BaseTag;
import io.papermc.paper.tag.EntityTags;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class CustomTagsTest {

    @BeforeAll
    public static void testInitialize() {
        assertDoesNotThrow(() -> Class.forName(MaterialTags.class.getName()));
        assertDoesNotThrow(() -> Class.forName(EntityTags.class.getName()));
    }

    @ParameterizedTest
    @MethodSource("tags")
    public void testLocked(BaseTag<?, ?> tag) {
        assertTrue(tag.isLocked(), "Tag " + tag.key() + " is not locked");
    }

    public static Set<BaseTag<?, ?>> tags() {
        Set<BaseTag<?, ?>> tags = new HashSet<>();
        collectTags(tags, MaterialTags.class);
        collectTags(tags, EntityTags.class);
        return tags;
    }

    private static void collectTags(Set<BaseTag<?, ?>> into, Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && BaseTag.class.isAssignableFrom(field.getType())) {
                    into.add((BaseTag<?, ?>) field.get(null));
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
