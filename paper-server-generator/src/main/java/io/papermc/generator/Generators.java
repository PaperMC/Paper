package io.papermc.generator;

import io.papermc.generator.types.EntityMetaWatcherGenerator;
import io.papermc.generator.types.EntityTypeToEntityClassGenerator;
import io.papermc.generator.types.SourceGenerator;

public interface Generators {

    SourceGenerator[] SERVER = {
            new EntityMetaWatcherGenerator("EntityMetaWatcher", "io.papermc.paper.entity.meta"),
            new EntityTypeToEntityClassGenerator("EntityTypeToEntityClass", "io.papermc.paper.entity.meta"),
    };
}
