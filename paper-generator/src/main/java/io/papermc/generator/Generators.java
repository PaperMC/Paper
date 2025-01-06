package io.papermc.generator;

import io.papermc.generator.registry.RegistryBootstrapper;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.types.craftblockdata.CraftBlockDataBootstrapper;
import io.papermc.generator.types.goal.MobGoalGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.Util;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Generators {

    List<SourceGenerator> API = Collections.unmodifiableList(Util.make(new ArrayList<>(), list -> {
        RegistryBootstrapper.bootstrap(list);
        list.add(new MobGoalGenerator("VanillaGoal", "com.destroystokyo.paper.entity.ai"));
        // todo extract fields for registry based api
    }));

    List<SourceGenerator> SERVER = Collections.unmodifiableList(Util.make(new ArrayList<>(), CraftBlockDataBootstrapper::bootstrap));
}
