package io.papermc.paper.entity;

import com.destroystokyo.paper.entity.ai.MobGoalHelper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Normal
class ShearableTest {

    static List<Class<Shearable>> nmsShearables() {
        try (final ScanResult result = new ClassGraph().enableClassInfo().whitelistPackages("net.minecraft.world.entity").scan()) {
            return result.getClassesImplementing(Shearable.class.getName()).loadClasses(Shearable.class);
        }
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("nmsShearables")
    void ensureImplementsShearable(final Class<? extends Shearable> shearableNmsClass) {
        final Class<? extends org.bukkit.entity.Mob> bukkitClass = MobGoalHelper.toBukkitClass((Class<? extends Mob>) shearableNmsClass);
        Assertions.assertTrue(io.papermc.paper.entity.Shearable.class.isAssignableFrom(bukkitClass), bukkitClass.getName() + " does not implement Shearable");
    }
}
