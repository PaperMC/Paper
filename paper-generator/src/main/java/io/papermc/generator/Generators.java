package io.papermc.generator;

import io.papermc.generator.registry.RegistryBootstrapper;
import io.papermc.generator.types.SimpleEnumGenerator;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.types.craftblockdata.CraftBlockDataBootstrapper;
import io.papermc.generator.types.goal.MobGoalGenerator;
import io.papermc.generator.utils.SimpleEnumBridgeGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.Util;
import net.minecraft.world.entity.animal.golem.CopperGolemState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Generators {

    List<SourceGenerator> API = Collections.unmodifiableList(Util.make(new ArrayList<>(), list -> {
        RegistryBootstrapper.bootstrap(list);
        list.add(new MobGoalGenerator("VanillaGoal", "com.destroystokyo.paper.entity.ai"));
        list.add(new SimpleEnumGenerator<>(CopperGolemState.class, "io.papermc.paper.entity"));
        // todo extract fields for registry based api
    }));

    List<SourceGenerator> SERVER = Collections.unmodifiableList(Util.make(new ArrayList<>(), list -> {
        list.add(new SimpleEnumBridgeGenerator<>(io.papermc.paper.entity.CopperGolemState.class, CopperGolemState.class, "io.papermc.paper.entity"));
        CraftBlockDataBootstrapper.bootstrap(list);
    }));
}
