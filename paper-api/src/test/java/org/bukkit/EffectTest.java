package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class EffectTest {
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(); // Paper

    @Test
    public void getById() {
        for (Effect effect : Effect.values()) {
            if (!isDeprecated(effect)) // Paper
            assertThat(Effect.getById(effect.getId()), is(effect));
        }
    }

    // Paper start
    private static boolean isDeprecated(Effect effect) {
        try {
            return Effect.class.getDeclaredField(effect.name()).isAnnotationPresent(Deprecated.class);
        } catch (NoSuchFieldException e) {
            LOGGER.error("Error getting effect enum field {}", effect.name(), e);
            return false;
        }
    }
    // Paper end
}
