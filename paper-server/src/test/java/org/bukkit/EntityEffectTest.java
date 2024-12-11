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
import net.minecraft.world.entity.EntityEvent;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

@Normal
public class EntityEffectTest {

    private static List<Byte> collectNmsLevelEvents() throws ReflectiveOperationException {
        final List<Byte> events = new ArrayList<>();
        for (final Field field : EntityEvent.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType() == byte.class) {
                events.add((byte) field.get(null));
            }
        }
        for (int i = 22; i <= 28; i++) {
            events.remove(Byte.valueOf((byte) i)); // all have existing API (debug info and op level)
        }
        events.remove(Byte.valueOf(EntityEvent.STOP_ATTACKING)); // not used on client anywhere
        events.remove(Byte.valueOf(EntityEvent.USE_ITEM_COMPLETE)); // not suitable for API (complete using item on Player)
        events.remove(Byte.valueOf(EntityEvent.FISHING_ROD_REEL_IN)); // not suitable for API (fishing rod reel in on FishingHook)
        events.add((byte) 0); // handled on Arrow (for some reason it's not in the EntityEvent nms file as a constant)
        return events;
    }

    private static boolean isNotDeprecated(EntityEffect effect) throws ReflectiveOperationException {
        return !EntityEffect.class.getDeclaredField(effect.name()).isAnnotationPresent(Deprecated.class);
    }

    @Test
    public void checkAllApiExists() throws ReflectiveOperationException {
        Map<Byte, EntityEffect> toId = new HashMap<>();
        for (final EntityEffect effect : EntityEffect.values()) {
            if (isNotDeprecated(effect)) {
                toId.put(effect.getData(), effect);
            }
        }

        final Set<Byte> missingEvents = new HashSet<>();
        for (final Byte event : collectNmsLevelEvents()) {
            if (toId.get(event) == null) {
                missingEvents.add(event);
            }
        }
        if (!missingEvents.isEmpty()) {
            fail("Missing API EntityEffects:\n" + Joiner.on("\n").join(missingEvents));
        }
    }

    @Test
    public void checkNoExtraApi() throws ReflectiveOperationException {
        Map<Byte, EntityEffect> toId = new HashMap<>();
        for (final EntityEffect effect : EntityEffect.values()) {
            if (isNotDeprecated(effect)) {
                toId.put(effect.getData(), effect);
            }
        }

        final List<Byte> nmsEvents = collectNmsLevelEvents();
        final Set<EntityEffect> extraApiEffects = new HashSet<>();
        for (final Map.Entry<Byte, EntityEffect> entry : toId.entrySet()) {
            if (!nmsEvents.contains(entry.getKey())) {
                extraApiEffects.add(entry.getValue());
            }
        }
        if (!extraApiEffects.isEmpty()) {
            fail("Extra API EntityEffects:\n" + Joiner.on("\n").join(extraApiEffects));
        }
    }
}
