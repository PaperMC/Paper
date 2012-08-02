package org.bukkit;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.WorldType;
import org.junit.Test;

public class WorldTypeTest {
    @Test
    public void testTypes() {
        List<WorldType> missingTypes = new ArrayList<WorldType>();

        for (WorldType type : WorldType.types) {
            if (type == null) continue;

            if (org.bukkit.WorldType.getByName(type.name()) == null) {
                missingTypes.add(type);
            }
        }

        if (!missingTypes.isEmpty()) {
            for (WorldType type : missingTypes) {
                System.out.println(type.name() + " is missing!");
            }
            fail("Missing (" + missingTypes.size() + ") WorldTypes!");
        }
    }
}
