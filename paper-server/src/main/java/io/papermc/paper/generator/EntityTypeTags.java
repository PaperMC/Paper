package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public interface EntityTypeTags {

    TagKey<EntityType<?>> HORSES = create("horses");
    TagKey<EntityType<?>> MINECART = create("minecart");
    TagKey<EntityType<?>> SPLITTING_MOB = create("splitting_mob");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name));
    }
}
