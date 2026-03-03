package io.papermc.paper.entity;

import java.util.stream.Stream;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.entity.CraftEntityTypes;
import org.bukkit.entity.EntityType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
public class EntityTypesTest {

    public static Stream<EntityType<?>> data() {
        return Registry.ENTITY_TYPE.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEntityType(EntityType<?> type) {
        CraftEntityTypes.EntityTypeData<?, ?> entityTypeData = CraftEntityTypes.getEntityTypeData(type);
        assertNotNull(entityTypeData, String.format("Entity type %s does not have an entity type data, please add one to CraftEntityTypes.", type.key().asString()));
    }
}
