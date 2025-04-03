package org.bukkit;

import com.google.common.base.Joiner;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.level.block.LevelEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class EffectTest {

    private static List<Integer> collectNmsLevelEvents() throws ReflectiveOperationException {
        final List<Integer> events = new ArrayList<>();
        for (final Field field : LevelEvent.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType() == int.class) {
                events.add((int) field.get(null));
            }
        }
        return events;
    }

    private static boolean isNotDeprecated(Effect effect) throws ReflectiveOperationException {
        return !Effect.class.getDeclaredField(effect.name()).isAnnotationPresent(Deprecated.class);
    }

    @Test
    public void checkAllApiExists() throws ReflectiveOperationException {
        Map<Integer, Effect> toId = new HashMap<>();
        for (final Effect effect : Effect.values()) {
            if (isNotDeprecated(effect)) {
                final Effect put = toId.put(effect.getId(), effect);
                assertNull(put, "duplicate API effect: " + put);
            }
        }

        final Set<Integer> missingEvents = new HashSet<>();
        for (final Integer event : collectNmsLevelEvents()) {
            if (toId.get(event) == null) {
                missingEvents.add(event);
            }
        }
        if (!missingEvents.isEmpty()) {
            fail("Missing API Effects:\n" + Joiner.on("\n").join(missingEvents));
        }
    }

    @Test
    public void checkNoExtraApi() throws ReflectiveOperationException {
        Map<Integer, Effect> toId = new HashMap<>();
        for (final Effect effect : Effect.values()) {
            if (isNotDeprecated(effect)) {
                final Effect put = toId.put(effect.getId(), effect);
                assertNull(put, "duplicate API effect: " + put);
            }
        }

        final List<Integer> nmsEvents = collectNmsLevelEvents();
        final Set<Effect> extraApiEffects = new HashSet<>();
        for (final Map.Entry<Integer, Effect> entry : toId.entrySet()) {
            if (!nmsEvents.contains(entry.getKey())) {
                extraApiEffects.add(entry.getValue());
            }
        }
        if (!extraApiEffects.isEmpty()) {
            fail("Extra API Effects:\n" + Joiner.on("\n").join(extraApiEffects));
        }
    }
}
