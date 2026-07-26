package io.papermc.generator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.papermc.generator.types.goal.MobGoalNames;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MobGoalConverterTest {

    @Test
    public void testBukkitMap() {
        final List<Class<Mob>> classes;
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().whitelistPackages(Entity.class.getPackageName()).scan()) {
            classes = scanResult.getSubclasses(Mob.class.getName()).loadClasses(Mob.class);
        }

        assertFalse(classes.isEmpty(), "There are supposed to be more than 0 mob classes!");

        List<String> missingClasses = new ArrayList<>();
        for (Class<Mob> nmsClass : classes) {
            if (!MobGoalNames.BUKKIT_BRIDGE.containsKey(nmsClass)) {
                missingClasses.add(nmsClass.getCanonicalName());
            }
        }

        assertTrue(missingClasses.isEmpty(), () -> "Missing some entity classes in the bukkit map: " + String.join(", ", missingClasses));
    }
}
