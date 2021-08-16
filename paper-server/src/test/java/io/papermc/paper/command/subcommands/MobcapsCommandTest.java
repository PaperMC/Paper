package io.papermc.paper.command.subcommands;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.entity.MobCategory;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Normal
public class MobcapsCommandTest {
    @Test
    public void testMobCategoryColors() {
        final Set<String> missing = new HashSet<>();
        for (final MobCategory value : MobCategory.values()) {
            if (!MobcapsCommand.MOB_CATEGORY_COLORS.containsKey(value)) {
                missing.add(value.getName());
            }
        }
        Assertions.assertTrue(missing.isEmpty(), "MobcapsCommand.MOB_CATEGORY_COLORS map missing TextColors for [" + String.join(", ", missing + "]"));
    }
}
